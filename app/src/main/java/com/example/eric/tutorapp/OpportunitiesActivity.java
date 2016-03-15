package com.example.eric.tutorapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eric.tutorapp.model.Tutor;
import com.example.eric.tutorapp.model.TutorRequest;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class OpportunitiesActivity extends AppCompatActivity {
    private static final String TAG = "OpportunitiesActivity";
    private Tutor loggedInTutor;

    private ProgressDialog dialog;
    private final AtomicInteger dialogCountdown = new AtomicInteger(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_opportunities);

        dialog = ProgressDialog.show(this, "Loading Opportunities", "Please wait...");

        Firebase tutorRef = new Firebase(HomeActivity.BASE_URL + "tutors");
        tutorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot firstTutor = dataSnapshot.getChildren().iterator().next();
                loggedInTutor = firstTutor.getValue(Tutor.class);
                loggedInTutor.setId(firstTutor.getKey());
                Log.d(TAG, "onDataChange: User: " + loggedInTutor);
                decrementDialogCountdown();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });

        final ListView opportunities = (ListView) findViewById(R.id.opportunities);
        final OpportunityAdapter adapter = new OpportunityAdapter(this, R.layout.opportunity);
        opportunities.setAdapter(adapter);

        final Firebase requestRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests");
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    TutorRequest request = child.getValue(TutorRequest.class);
                    request.setId(child.getKey());
                    adapter.add(request);
                }
                Log.d(TAG, "Completed Opportunity Fetch");
                decrementDialogCountdown();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    private void decrementDialogCountdown() {
        if (dialogCountdown.decrementAndGet() == 0) {
            dialog.dismiss();
        }
    }

    private class OpportunityAdapter extends ArrayAdapter<TutorRequest> {
        private List<TutorRequest> opportunities = new ArrayList<>();
        private int layout;

        public OpportunityAdapter(Context context, int resource) {
            super(context, resource);
            this.layout = resource;
        }

        @Override
        public void clear() {
            opportunities.clear();
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
            ((TextView) view.findViewById(R.id.buildingText)).setText(request.getBuilding());

            final CheckBox checkbox = (CheckBox) view.findViewById(R.id.interestedCheckbox);
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Clicked!");

                    Firebase tutorRequestRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + request.getId());
                    if (checkbox.isChecked()) {
                        request.addInterestedTutor(loggedInTutor);
                    } else {
                        request.removeInterestedTutor(loggedInTutor);
                    }
                    tutorRequestRef.setValue(request);
                }
            });

            return view;
        }
    }
}
