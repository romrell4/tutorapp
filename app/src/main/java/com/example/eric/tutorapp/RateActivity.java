package com.example.eric.tutorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eric.tutorapp.model.Review;
import com.example.eric.tutorapp.model.Tutor;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class RateActivity extends AppCompatActivity {
    private static final String TAG = "RateActivity";

    private String tutorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_rate);

        Intent intent = getIntent();
        tutorId = intent.getStringExtra(AvailableTutorsActivity.TUTOR_ID);
        String tutorUsername = intent.getStringExtra(AvailableTutorsActivity.TUTOR_USERNAME);
        TextView toolbarText = (TextView) findViewById(R.id.toolbarText);
        toolbarText.setText(tutorUsername);

        TextView ratingBarLabel = (TextView) findViewById(R.id.ratingBarLabel);
        ratingBarLabel.setText(getResources().getString(R.string.ratingBarLabelFormat, tutorUsername));

        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addRatingIfValid()) {
                    Intent intent = new Intent(RateActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean addRatingIfValid() {
        final int stars = (int) ((RatingBar) findViewById(R.id.ratingBar)).getRating();
        final String reviewText = ((EditText) findViewById(R.id.reviewText)).getText().toString();

        if (stars < 1 || stars > 5) {
            Toast.makeText(this, "Please select a valid rating for this tutor", Toast.LENGTH_SHORT).show();
            return false;
        }

        final Firebase tutorRef = new Firebase(HomeActivity.BASE_URL + "tutors/" + tutorId);
        tutorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Tutor tutor = dataSnapshot.getValue(Tutor.class);

                tutor.addReview(new Review(stars, reviewText, "3/26/2016", "Eric"));

                tutorRef.child("reviews").setValue(tutor.getReviews());
                tutorRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        return true;
    }
}
