package com.example.eric.tutorapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.eric.tutorapp.model.ChatMessage;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {
    private static final String TAG = "MessagingActivity";
    private List<ChatMessage> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        String tutorRequestId = getIntent().getStringExtra(AvailableTutorsActivity.TUTOR_REQUEST_ID);

        final ProgressDialog dialog = ProgressDialog.show(this, "Loading Messages", "Please wait...");

        final ChatArrayAdapter chatArrayAdapter = new ChatArrayAdapter(this, R.layout.chat_message);

        final Firebase messagesRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + tutorRequestId + "/messages");
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                chatArrayAdapter.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: Child=" + child);
                    messages.add(child.getValue(ChatMessage.class));
                    chatArrayAdapter.add(child.getValue(ChatMessage.class));
                }
                dialog.hide();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });

        Button buttonSend = (Button) findViewById(R.id.sendButton);
        final ListView listView = (ListView) findViewById(R.id.messages);

        final EditText chatText = (EditText) findViewById(R.id.chatText);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages.add(new ChatMessage(chatText.getText().toString(), false, false));
                messagesRef.setValue(messages);
                chatText.setText("");
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
        public void clear() {
            chatMessageList.clear();
            super.clear();
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
            ChatMessage chatMessage = getItem(position);

            TextView chatText = (TextView) row.findViewById(R.id.singleMessage);
            chatText.setText(chatMessage.getText());
            chatText.setTextColor(chatMessage.getLeft() ? ContextCompat.getColor(getContext(), R.color.black) : ContextCompat.getColor(getContext(), R.color.white));

            RelativeLayout singleMessageContainer = (RelativeLayout) row.findViewById(R.id.singleMessageContainer);
            singleMessageContainer.setBackgroundResource(chatMessage.getLeft() ? R.drawable.bubble_left : R.drawable.bubble_right);

            LinearLayout rowContainer = (LinearLayout) row.findViewById(R.id.rowContainer);
            rowContainer.setGravity(chatMessage.getLeft() ? Gravity.START : Gravity.END);

            LinearLayout acceptAndRejectLayout = (LinearLayout) row.findViewById(R.id.acceptAndRejectLayout);
            acceptAndRejectLayout.setVisibility(chatMessage.getRequest() ? View.VISIBLE : View.GONE);
            return row;
        }
    }
}
