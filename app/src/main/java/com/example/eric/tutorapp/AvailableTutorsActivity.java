package com.example.eric.tutorapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eric.tutorapp.model.Course;
import com.example.eric.tutorapp.model.Review;
import com.example.eric.tutorapp.model.Tutor;
import com.example.eric.tutorapp.model.TutorRequest;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AvailableTutorsActivity extends AppCompatActivity {
    private static final String TAG = "AvailableTutorsActivity";
    public static final String TUTOR_ID = "com.tutorapp.tutorInfo";
    public static final String TUTOR_REQUEST_ID = "com.tutorapp.tutorRequestId";
    public static final String TUTOR_USERNAME = "com.tutorapp.tutorUsername";
    private TutorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_available_tutors);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Intent intent = getIntent();
        final String tutorRequestId = intent.getStringExtra(StudentSearchActivity.TUTOR_REQUEST_ID);
        final String studentName = intent.getStringExtra(StudentSearchActivity.STUDENT_NAME);

        adapter = new TutorAdapter(this, R.layout.tutor);
        final ListView availableTutors = (ListView) findViewById(R.id.availableTutors);
        availableTutors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tutor tutor = adapter.getItem(position);
                Intent intent = new Intent(AvailableTutorsActivity.this, ReviewsActivity.class);
                intent.putExtra(TUTOR_ID, tutor.getId());
                intent.putExtra(TUTOR_REQUEST_ID, tutorRequestId);
                intent.putExtra(TUTOR_USERNAME, tutor.getUsername());
                Log.d(TAG, "onClick: Student=" + studentName);
                intent.putExtra(StudentSearchActivity.STUDENT_NAME, studentName);

                startActivity(intent);
            }
        });
        availableTutors.setAdapter(adapter);


        final Firebase tutorRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + tutorRequestId + "/interestedTutors");
        tutorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Tutor tutor = child.getValue(Tutor.class);
                    adapter.add(tutor);
                }

                findViewById(R.id.loadingText).setVisibility(adapter.getCount() > 0 ? View.INVISIBLE : View.VISIBLE);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private static class TutorAdapter extends ArrayAdapter<Tutor> {
        private List<Tutor> tutors = new ArrayList<>();
        private int layout;


        public TutorAdapter(Context context, int resource) {
            super(context, resource);
            this.layout = resource;
        }

        @Override
        public void add(Tutor object) {
            tutors.add(object);
            super.add(object);
        }

        @Override
        public void clear() {
            tutors.clear();
            super.clear();
        }

        @Override
        public int getCount() {
            return tutors.size();
        }

        @Override
        public Tutor getItem(int position) {
            return tutors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(layout, parent, false);
            }

            Tutor tutor = getItem(position);

            int stars = 0;
            for (Review review : tutor.getReviews()) {
                stars += review.getStars();
            }
            stars /= tutor.getReviews().size();

            ((TextView) view.findViewById(R.id.username)).setText(tutor.getUsername());
            ((TextView) view.findViewById(R.id.ratings)).setText(view.getResources().getString(R.string.reviewAmountFormat, tutor.getReviews().size()));
            ((TextView) view.findViewById(R.id.ratings)).setCompoundDrawablesRelativeWithIntrinsicBounds(Utils.getRatingImage(stars), 0, 0, 0);
            ((TextView) view.findViewById(R.id.major)).setText(tutor.getMajor());

            return view;
        }
    }
}
