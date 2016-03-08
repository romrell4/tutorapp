package com.example.eric.tutorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.eric.tutorapp.model.Tutor;
import com.firebase.client.Firebase;

public class AvailableTutorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_available_tutor);

        Intent incomingIntent = getIntent();
        Tutor tutor = (Tutor) incomingIntent.getSerializableExtra(AvailableTutorsActivity.TUTOR_INFO_ID);
        TextView toolbarText = (TextView) findViewById(R.id.toolbarText);
        toolbarText.setText(tutor.getUsername());

    }
}
