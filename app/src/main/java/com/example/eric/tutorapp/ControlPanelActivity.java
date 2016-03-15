package com.example.eric.tutorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;

public class ControlPanelActivity extends AppCompatActivity {
    private static final String TAG = "ControlPanelActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_control_panel);

        final String requestId = getIntent().getStringExtra(OpportunitiesActivity.REQUEST_ID_TAG);
        final Firebase requestRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + requestId);
        Log.d(TAG, "onCreate: " + requestRef);

        Button acceptJob = (Button) findViewById(R.id.acceptJob);
        acceptJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRef.child("tutorAccepted").setValue(true);
            }
        });
    }
}
