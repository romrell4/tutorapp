package com.example.eric.tutorapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentSearchActivity extends AppCompatActivity {
    private static final String TAG = "StudentSearchActivity";

    private Firebase myCoursesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_student_search);

        final ProgressDialog dialog = ProgressDialog.show(StudentSearchActivity.this, "Loading Courses", "Please wait...");

        myCoursesRef = new Firebase("https://romrell4-tutorapp.firebaseio.com/courses");
        myCoursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> courseList = new ArrayList<>();
                Iterable<DataSnapshot> courses = dataSnapshot.getChildren();
                for (DataSnapshot department : courses) {
                    for (DataSnapshot course : department.getChildren()) {
                        courseList.add(department.getKey() + " " + course.child("catalogNumber").getValue() + " - " + course.child("transcriptTitle").getValue());
                    }
                }
                Log.d(TAG, "onDataChange: Got all info!");

                final AutoCompleteTextView courseText = (AutoCompleteTextView) findViewById(R.id.courseText);
                courseText.setAdapter(new ArrayAdapter<>(StudentSearchActivity.this, R.layout.course_spinner_item, courseList));

                dialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent());
            }
        });
    }
}
