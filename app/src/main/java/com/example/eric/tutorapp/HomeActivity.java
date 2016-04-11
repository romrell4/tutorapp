package com.example.eric.tutorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    public static final String BASE_URL = "https://romrell4-tutorapp.firebaseio.com/";
    public static final String TUTOR_ID = "com.tutorapp.tutorId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button studentButton = (Button) findViewById(R.id.studentButton);
        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Student");
                startActivity(new Intent(HomeActivity.this, StudentSearchActivity.class));
            }
        });

        Button tutorButton = (Button) findViewById(R.id.tutorButton);
        tutorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Tutor");
                Intent intent = new Intent(HomeActivity.this, OpportunitiesActivity.class);
                intent.putExtra(TUTOR_ID, "-KCSVS2kKATtnZ2D1LNY");
                startActivity(intent);
            }
        });

    }
}
