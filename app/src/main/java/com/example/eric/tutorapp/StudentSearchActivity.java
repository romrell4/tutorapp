package com.example.eric.tutorapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eric.tutorapp.model.Course;
import com.example.eric.tutorapp.model.TutorRequest;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class StudentSearchActivity extends AppCompatActivity {
    private static final String TAG = "StudentSearchActivity";
    private static final String PRICE_REG_EX = "[0-9]+([.][0-9]{1,2})?";
    private Map<String, Course> courseMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_student_search);

        final ProgressDialog dialog = ProgressDialog.show(StudentSearchActivity.this, "Loading Courses", "Please wait...");

        Firebase myCoursesRef = new Firebase(HomeActivity.BASE_URL + "courses");
        myCoursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                courseMap = new HashMap<>();
                Iterable<DataSnapshot> courses = dataSnapshot.getChildren();
                for (DataSnapshot department : courses) {
                    for (DataSnapshot course : department.getChildren()) {

                        Course courseObject = new Course(department.getKey(), course.child("catalogNumber").getValue().toString(), course.child("transcriptTitle").getValue().toString());
                        courseMap.put(courseObject.toDescriptionString(), courseObject);
                    }
                }

                Log.d(TAG, "onDataChange: Got all info!");

                final AutoCompleteTextView courseText = (AutoCompleteTextView) findViewById(R.id.courseText);
                courseText.setAdapter(new ArrayAdapter<>(StudentSearchActivity.this, R.layout.course_spinner_item, courseMap.keySet().toArray()));

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
                //Validate data
                TutorRequest request = getRequestIfValid();
                if (request != null) {
                    Log.d(TAG, "onClick: " + request);
                    Firebase requestsRef = new Firebase(HomeActivity.BASE_URL + "requests");
                    requestsRef.push().setValue(request);

                    Intent intent = new Intent(StudentSearchActivity.this, AvailableTutorsActivity.class);
                    intent.putExtra("com.tutorapp.request", request);
                    startActivity(intent);
                }

                //For testing only...
                Intent intent = new Intent(StudentSearchActivity.this, AvailableTutorsActivity.class);
                intent.putExtra("com.tutorapp.request", request);
                startActivity(intent);
            }
        });
    }

    private TutorRequest getRequestIfValid() {
        EditText nameText = (EditText) findViewById(R.id.nameText);
        EditText courseText = (AutoCompleteTextView) findViewById(R.id.courseText);
        EditText priceText = (EditText) findViewById(R.id.priceText);
        EditText buildingText = (EditText) findViewById(R.id.buildingText);
        EditText messageText = (EditText) findViewById(R.id.notesText);
        if (nameText == null || nameText.getText() == null || nameText.getText().toString().isEmpty()) {
            Toast.makeText(StudentSearchActivity.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (courseText == null || courseText.getText() == null || courseText.getText().toString().isEmpty() || !courseMap.containsKey(courseText.getText().toString())) {
            Toast.makeText(StudentSearchActivity.this, "Please enter a valid course", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (priceText == null || priceText.getText() == null || !priceText.getText().toString().matches(PRICE_REG_EX)) {
            Toast.makeText(StudentSearchActivity.this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (buildingText == null || buildingText.getText() == null || buildingText.getText().toString().isEmpty()) {
            Toast.makeText(StudentSearchActivity.this, "Please enter a valid building", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (messageText == null || messageText.getText() == null || messageText.getText().toString().isEmpty()) {
            Toast.makeText(StudentSearchActivity.this, "Please enter a valid message", Toast.LENGTH_SHORT).show();
            return null;
        }

        //Get data
        String name = nameText.getText().toString();
        Course course = courseMap.get(courseText.getText().toString());
        BigDecimal price = new BigDecimal(priceText.getText().toString());
        String building = buildingText.getText().toString();
        String message = messageText.getText().toString();

        //Add request to the database
        return new TutorRequest(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID), name, course, price, building, message);
    }
}
