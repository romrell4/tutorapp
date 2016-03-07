package com.example.eric.tutorapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    public static final String BASE_URL = "https://romrell4-tutorapp.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        TextView continueAsText = (TextView) findViewById(R.id.continueAsText);
        continueAsText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/alegreya-sans-sc/AlegreyaSansSC-Regular.ttf"));

        Button studentButton = (Button) findViewById(R.id.studentButton);
        studentButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/hk-grotesk/HKGrotesk-Regular.otf"));
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
            }
        });

    }
}
