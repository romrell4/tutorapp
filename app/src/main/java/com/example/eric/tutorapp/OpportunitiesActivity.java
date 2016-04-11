package com.example.eric.tutorapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eric.tutorapp.model.ChatMessage;
import com.example.eric.tutorapp.model.Tutor;
import com.example.eric.tutorapp.model.TutorRequest;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OpportunitiesActivity extends AppCompatActivity {
    private static final String TAG = "OpportunitiesActivity";
    private Tutor loggedInTutor;
    private OpportunityAdapter adapter;
    private Firebase tutorRequestsRef;
    private ValueEventListener tutorRequestRefListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_opportunities);
    }

    @Override
    public void onBackPressed() {
        if (tutorRequestsRef != null && tutorRequestRefListener != null) {
            tutorRequestsRef.removeEventListener(tutorRequestRefListener);
        }
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final ProgressDialog dialog = ProgressDialog.show(this, "Loading Opportunities", "Please wait...");

        adapter = new OpportunityAdapter(this, R.layout.opportunity);

        String tutorId = getIntent().getStringExtra(HomeActivity.TUTOR_ID);

        Firebase tutorRef = new Firebase(HomeActivity.BASE_URL + "tutors/" + tutorId);
        tutorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Result=" + dataSnapshot);
                loggedInTutor = dataSnapshot.getValue(Tutor.class);
                loggedInTutor.setId(dataSnapshot.getKey());
                Log.d(TAG, "onDataChange: User: " + loggedInTutor);

                tutorRequestsRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests");
                tutorRequestRefListener = tutorRequestsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adapter.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            final TutorRequest request = child.getValue(TutorRequest.class);
                            request.setId(child.getKey());

                            ChatMessage newMessage = adapter.newMessage(request);
                            if (newMessage != null) {
                                Toast.makeText(OpportunitiesActivity.this, request.getName() + " says: " + newMessage.getText(), Toast.LENGTH_LONG).show();
                            }

                            adapter.add(request);
                        }
                        Log.d(TAG, "Completed Opportunity Fetch");
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        final ListView opportunities = (ListView) findViewById(R.id.opportunities);
        opportunities.setOnItemClickListener(new OpportunityOnItemClickListener());
        opportunities.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OpportunitiesActivity.this);
                dialogBuilder.setMessage("Are you sure you would like to delete this request?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + adapter.getItem(position).getId()).setValue(null);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
        opportunities.setAdapter(adapter);
    }

    private class OpportunityAdapter extends ArrayAdapter<TutorRequest> {
        private List<TutorRequest> opportunities = new ArrayList<>();
        private List<TutorRequest> oldOpportunities;
        private int layout;

        public OpportunityAdapter(Context context, int resource) {
            super(context, resource);
            this.layout = resource;
        }

        @Override
        public void clear() {
            oldOpportunities = new ArrayList<>(opportunities);
            opportunities.clear();
        }

        public ChatMessage newMessage(TutorRequest object) {
            for (TutorRequest opportunity : oldOpportunities) {
                if (object.getId().equals(opportunity.getId())) {
                    if (object.getMessages() != null) {
                        if (opportunity.getMessages() != null) {
                            if (object.getMessages().size() != opportunity.getMessages().size()) {
                                return object.getMessages().get(object.getMessages().size() - 1);
                            }
                        }
                    }
                }
            }
            return null;
        }

        @Override
        public void add(TutorRequest object) {
            super.add(object);
            opportunities.add(object);
        }

        @Override
        public int getCount() {
            return opportunities.size();
        }

        @Override
        public TutorRequest getItem(int position) {
            return opportunities.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(layout, parent, false);
            }

            final TutorRequest request = getItem(position);

            ((TextView) view.findViewById(R.id.nameText)).setText(request.getName());
            ((TextView) view.findViewById(R.id.timeText)).setText(getResources().getString(R.string.timeFormat, "a few moments ago"));
            ((TextView) view.findViewById(R.id.priceText)).setText(new DecimalFormat("'$'0.00").format(request.getPrice()));
            ((TextView) view.findViewById(R.id.courseText)).setText(request.getCourse().toSimpleString());
            ((TextView) view.findViewById(R.id.buildingText)).setText(request.getBuilding().getShortName());

            if (loggedInTutor.getId().equals(request.getActiveTutorId())) {
                if (request.getStudentAccepted() != null && request.getStudentAccepted()) {
                    view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                } else {
                    view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_grey));
                }
            } else {
                view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            }

            if (request.getInterestedTutors() != null && request.getInterestedTutors().contains(loggedInTutor)) {
                ((CheckBox) view.findViewById(R.id.interestedCheckbox)).setChecked(true);
            } else {
                ((CheckBox) view.findViewById(R.id.interestedCheckbox)).setChecked(false);
            }

            final CheckBox checkbox = (CheckBox) view.findViewById(R.id.interestedCheckbox);
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Clicked!");

                    Firebase tutorRequestRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + request.getId());
                    if (checkbox.isChecked()) {
                        request.addInterestedTutor(loggedInTutor);
                    } else {
                        if (loggedInTutor.getId().equals(request.getActiveTutorId())) {
                            request.setActiveTutorId(null);
                        }
                        request.removeInterestedTutor(loggedInTutor);
                    }
                    tutorRequestRef.setValue(request);
                }
            });

            return view;
        }
    }

    private class OpportunityOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            TutorRequest tutorRequest = adapter.getItem(position);
            if (loggedInTutor.getId().equals(tutorRequest.getActiveTutorId())) {
                final Dialog dialog = new Dialog(OpportunitiesActivity.this);
                dialog.setContentView(R.layout.tutor_message_popup);
                dialog.setTitle("Enter your message:");

                Button sendButton = (Button) dialog.findViewById(R.id.sendButton);
                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText messageText = (EditText) dialog.findViewById(R.id.messageText);
                        if (!messageText.getText().toString().isEmpty()) {
                            final TutorRequest tutorRequest = adapter.getItem(position);
                            final Firebase messagesRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + tutorRequest.getId() + "/messages");
                            List<ChatMessage> messages = tutorRequest.getMessages() == null ? new ArrayList<ChatMessage>() : tutorRequest.getMessages();

                            messages.add(new ChatMessage(messageText.getText().toString(), true, false));
                            messagesRef.setValue(messages);
                            dialog.dismiss();
                        }
                    }
                });

                Button inviteButton = (Button) dialog.findViewById(R.id.inviteButton);
                inviteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final TutorRequest tutorRequest = adapter.getItem(position);
                        final Firebase messagesRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + tutorRequest.getId() + "/messages");
                        List<ChatMessage> messages = tutorRequest.getMessages() == null ? new ArrayList<ChatMessage>() : tutorRequest.getMessages();

                        messages.add(new ChatMessage("This tutor has offered to help you. Click accept below to hire this tutor", true, true));
                        messagesRef.setValue(messages);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }
    }
}
