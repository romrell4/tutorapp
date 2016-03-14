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
import android.view.Window;
import android.widget.AdapterView;
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
    public static final String TUTOR_INFO_ID = "com.tutorapp.tutorInfo";
    private TutorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_available_tutors);

        final ProgressDialog dialog = ProgressDialog.show(this, "Loading Available Tutors", "Please wait...");

        adapter = new TutorAdapter(this, new ArrayList<Tutor>());
        final ListView availableTutors = (ListView) findViewById(R.id.availableTutors);
        availableTutors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AvailableTutorsActivity.this, AvailableTutorActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable(TUTOR_INFO_ID, (Tutor) adapter.getItem(position));
                intent.putExtras(extras);
                startActivity(intent);
            }
        });


        final Firebase tutorRef = new Firebase(HomeActivity.BASE_URL + "tutors");
        tutorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Tutor> tutors = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    tutors.add(child.getValue(Tutor.class));
                }
                adapter = new TutorAdapter(AvailableTutorsActivity.this, tutors);
                availableTutors.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
//        createTests();
    }

    private void createTests() {
        final Firebase tutorRef = new Firebase(HomeActivity.BASE_URL + "tutors");
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("CHEM", "111", "Intro to Chemistry"));
        courses.add(new Course("CHEM", "112", "Intro to Chemistry (2)"));

        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(2, "Not great...", "1/6/2016", "Mike"));
        reviews.add(new Review(1, "Sucks", "3/4/2016", "Ray"));

        Tutor tutor = new Tutor("zjoanne", "testpass", "CHEM", courses, reviews);
        tutorRef.push().setValue(tutor);

        final Firebase tutorRequestRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests");
        tutorRequestRef.push().setValue(new TutorRequest("testId", "Eric", new Course("C S", "235", "Data Structures"), new BigDecimal(25), "TMCB", "Help!"));
    }

    private static class TutorAdapter extends BaseAdapter {
        private LayoutInflater inflater = null;
        private List<Tutor> data;

        public TutorAdapter(Context context, List<Tutor> data) {
            this.data = data;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.tutor, null);
            }


            Tutor tutor = data.get(position);

            int stars = 0;
            for (Review review : tutor.getReviews()) {
                stars += review.getStars();
            }
            stars /= tutor.getReviews().size();

            ((TextView) view.findViewById(R.id.username)).setText(tutor.getUsername());
            ((ImageView) view.findViewById(R.id.ratings)).setImageResource(Utils.getRatingImage(stars));
            ((TextView) view.findViewById(R.id.reviewAmount)).setText(view.getResources().getString(R.string.reviewAmountFormat, tutor.getReviews().size()));
            ((TextView) view.findViewById(R.id.major)).setText(tutor.getMajor());

            return view;
        }
    }
}
