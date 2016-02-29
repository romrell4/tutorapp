package com.example.eric.tutorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class StudentSearchActivity extends AppCompatActivity {
    private static final String[] COURSES = new String[] {"CS 142", "CS 224", "CS 235", "CS 236", "CS 240"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_student_search);

        AutoCompleteTextView courseText = (AutoCompleteTextView) findViewById(R.id.courseText);
        courseText.setAdapter(new ArrayAdapter<>(this, R.layout.course_spinner_item, COURSES));
    }
}
