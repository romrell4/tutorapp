package com.example.eric.tutorapp;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eric.tutorapp.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 3/8/16.
 */
public class MessageFragment extends Fragment {

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private Intent intent;
    private boolean side = false;

    public MessageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean sendChatMessage(){
        chatArrayAdapter.add(new ChatMessage(chatText.getText().toString(), side));
        chatText.setText("");
        side = !side;
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        buttonSend = (Button) view.findViewById(R.id.sendButton);
        listView = (ListView) view.findViewById(R.id.messages);
        chatArrayAdapter = new ChatArrayAdapter(getActivity(), R.layout.chat_message);

        chatText = (EditText) view.findViewById(R.id.chatText);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return event.getAction() == KeyEvent.ACTION_DOWN && sendChatMessage();
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendChatMessage();
            }
        });
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
        return view;
    }

    private class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {
        private List<ChatMessage> chatMessageList = new ArrayList<>();

        public ChatArrayAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public void add(ChatMessage chatMessage) {
            chatMessageList.add(chatMessage);
            super.add(chatMessage);
        }

        @Override
        public int getCount() {
            return chatMessageList.size();
        }

        @Override
        public ChatMessage getItem(int position) {
            return chatMessageList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.chat_message, parent, false);
            }
            LinearLayout singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
            ChatMessage chatMessage = getItem(position);
            TextView chatText = (TextView) row.findViewById(R.id.singleMessage);
            chatText.setText(chatMessage.getText());
            chatText.setBackgroundResource(chatMessage.isLeft() ? R.drawable.bubble_left : R.drawable.bubble_right);
            chatText.setTextColor(chatMessage.isLeft() ? ContextCompat.getColor(getContext(), R.color.black) :ContextCompat.getColor(getContext(), R.color.white));
            singleMessageContainer.setGravity(chatMessage.isLeft() ? Gravity.START : Gravity.END);
            return row;
        }
    }
}
