package com.example.eric.tutorapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eric.tutorapp.model.Building;
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

    private ArrayAdapter<String> courseAdapter;
    private ArrayAdapter<String> buildingAdapter;
    private Map<String, Course> courseMap = new HashMap<>();
    private Map<String, Building> buildingMap = new HashMap<>();

    public static final String TUTOR_REQUEST_ID = "com.tutorapp.tutorRequestId";
    public static final String STUDENT_NAME = "com.tutorapp.studentName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_student_search);

        //Start the progress dialog
        ProgressDialog dialog = ProgressDialog.show(this, "Loading", "Please wait...");
        Utils.initDialogCountdown(dialog, 2);

        final Firebase coursesRef = new Firebase(HomeActivity.BASE_URL + "courses");
        final AutoCompleteTextView courseText = (AutoCompleteTextView) findViewById(R.id.courseText);
        courseAdapter = new ArrayAdapter<>(this, R.layout.spinner_item);
        courseText.setAdapter(courseAdapter);
        coursesRef.addValueEventListener(new CourseEventListener());

        final Firebase buildingsRef = new Firebase(HomeActivity.BASE_URL + "buildings");
        final AutoCompleteTextView buildingText = (AutoCompleteTextView) findViewById(R.id.buildingText);
        buildingAdapter = new ArrayAdapter<>(this, R.layout.spinner_item);
        buildingText.setAdapter(buildingAdapter);
        buildingsRef.addValueEventListener(new BuildingEventListener());

        final Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new SearchOnClickListener());
    }

    private class CourseEventListener implements ValueEventListener {

        private CourseTask task = new CourseTask();

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            task.execute(dataSnapshot);
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {}

        private class CourseTask extends AsyncTask<DataSnapshot, Void, Map<String, Course>> {

            @Override
            protected Map<String, Course> doInBackground(DataSnapshot... params) {
                Map<String, Course> map = new HashMap<>();

                for (DataSnapshot department : params[0].getChildren()) {
                    for (DataSnapshot child : department.getChildren()) {
                        Course course = new Course(department.getKey(), child.child("catalogNumber").getValue().toString(), child.child("transcriptTitle").getValue().toString());
                        map.put(course.toDescriptionString(), course);
                    }
                }
                Utils.countdown();
                return map;
            }

            @Override
            protected void onPostExecute(Map<String, Course> map) {
                courseMap = map;
                courseAdapter.clear();
                courseAdapter.addAll(map.keySet());
            }
        }
    }

    private class BuildingEventListener implements ValueEventListener {

        private BuildingTask task = new BuildingTask();

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            task.execute(dataSnapshot);
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {}

        private class BuildingTask extends AsyncTask<DataSnapshot, Void, Map<String, Building>> {

            @Override
            protected Map<String, Building> doInBackground(DataSnapshot... params) {
                Map<String, Building> map = new HashMap<>();

                for (DataSnapshot child : params[0].getChildren()) {
                    Building building = new Building(child.getKey(), child.getValue(String.class));
                    map.put(building.toDescriptionString(), building);
                }
                Utils.countdown();
                return map;
            }

            @Override
            protected void onPostExecute(Map<String, Building> map) {
                buildingMap = map;
                buildingAdapter.clear();
                buildingAdapter.addAll(map.keySet());
            }
        }
    }

    private class SearchOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //Validate data
            TutorRequest request = getRequestIfValid();
            if (request != null) {
                Firebase requestsRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests");
                Firebase newRequestRef = requestsRef.push();
                newRequestRef.setValue(request);

                Intent intent = new Intent(StudentSearchActivity.this, AvailableTutorsActivity.class);
                intent.putExtra(TUTOR_REQUEST_ID, newRequestRef.getKey());
                intent.putExtra(STUDENT_NAME, request.getName());
                startActivity(intent);
//                } else { //FOR TESTING ONLY
//                    Intent intent = new Intent(StudentSearchActivity.this, AvailableTutorsActivity.class);
//                    intent.putExtra(TUTOR_REQUEST_ID, "-KCs4amAoybXmpRn5h7Z");
//                    intent.putExtra(STUDENT_NAME, "Eric");
//                    startActivity(intent);
            }
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
            if (buildingText == null || buildingText.getText() == null || buildingText.getText().toString().isEmpty() || !buildingMap.containsKey(buildingText.getText().toString())) {
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
            Building building = buildingMap.get(buildingText.getText().toString());
            String message = messageText.getText().toString();

            //Return new request
            return new TutorRequest(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID), name, course, price, building, message, null, null, null, null, null);
        }

    }
}
