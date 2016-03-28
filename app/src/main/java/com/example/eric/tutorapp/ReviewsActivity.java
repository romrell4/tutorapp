package com.example.eric.tutorapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eric.tutorapp.model.Review;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity {
    private static final String TAG = "ReviewsActivity";
    private ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_reviews);

        Intent intent = getIntent();
        final String tutorId = intent.getStringExtra(AvailableTutorsActivity.TUTOR_ID);
        final String tutorUsername = intent.getStringExtra(AvailableTutorsActivity.TUTOR_USERNAME);
        final String tutorRequestId = intent.getStringExtra(AvailableTutorsActivity.TUTOR_REQUEST_ID);

        TextView toolbarText = (TextView) findViewById(R.id.toolbarText);
        toolbarText.setText(tutorUsername);

        ListView reviewList = (ListView) findViewById(R.id.reviews);
        adapter = new ReviewAdapter(this, R.layout.review);
        reviewList.setAdapter(adapter);

        final ProgressDialog dialog = ProgressDialog.show(this, "Loading Reviews", "Please wait...");

        Firebase reviewsRef = new Firebase(HomeActivity.BASE_URL + "tutors/" + tutorId + "/reviews");
        reviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    adapter.add(child.getValue(Review.class));
                }
                dialog.hide();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.list_view_header_image, reviewList, false);
        reviewList.addHeaderView(linearLayout);

        Button contactButton = (Button) findViewById(R.id.contactButton);
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase tutorRequestRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + tutorRequestId);
                tutorRequestRef.child("activeTutorId").setValue(tutorId);

                Intent intent = new Intent(ReviewsActivity.this, MessagingActivity.class);
                intent.putExtra(AvailableTutorsActivity.TUTOR_ID, tutorId);
                intent.putExtra(AvailableTutorsActivity.TUTOR_REQUEST_ID, tutorRequestId);
                intent.putExtra(AvailableTutorsActivity.TUTOR_USERNAME, tutorUsername);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        final String tutorRequestId = getIntent().getStringExtra(AvailableTutorsActivity.TUTOR_REQUEST_ID);
        Firebase tutorRequestRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + tutorRequestId);
        tutorRequestRef.child("activeTutorId").removeValue();
        tutorRequestRef.child("tutorAccepted").removeValue();
        tutorRequestRef.child("studentAccepted").removeValue();
        tutorRequestRef.child("messages").removeValue();
        super.onBackPressed();
    }

    private class ReviewAdapter extends ArrayAdapter<Review> {
        private List<Review> reviews = new ArrayList<>();
        private int layout;

        public ReviewAdapter(Context context, int resource) {
            super(context, resource);
            this.layout = resource;
        }

        @Override
        public void add(Review object) {
            super.add(object);
            reviews.add(object);
        }

        @Override
        public int getCount() {
            return reviews.size();
        }

        @Override
        public void clear() {
            super.clear();
            reviews.clear();
        }

        @Override
        public Review getItem(int position) {
            return reviews.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(layout, parent, false);
            }

            Review review = getItem(position);

            TextView author = (TextView) convertView.findViewById(R.id.author);
            author.setText(getResources().getString(R.string.authorFormat, review.getAuthor()));
            TextView message = (TextView) convertView.findViewById(R.id.message);
            message.setText(review.getMessage());
            ImageView stars = (ImageView) convertView.findViewById(R.id.stars);
            stars.setImageResource(Utils.getRatingImage(review.getStars()));

            return convertView;
        }
    }
}