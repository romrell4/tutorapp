package com.example.eric.tutorapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.eric.tutorapp.model.Tutor;
import com.example.eric.tutorapp.model.TutorRequest;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AvailableTutorActivity extends AppCompatActivity {

    private static final String TAG = "AvailableTutorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_available_tutor);

        final ProgressDialog dialog = ProgressDialog.show(this, "Loading Tutor Information", "Please wait...");
        Utils.initDialogCountdown(dialog, 2);

        final String tutorId = getIntent().getStringExtra(AvailableTutorsActivity.TUTOR_ID);
        final String tutorRequestId = getIntent().getStringExtra(AvailableTutorsActivity.TUTOR_REQUEST_ID);

        Firebase tutorRef = new Firebase(HomeActivity.BASE_URL + "tutors/" + tutorId);
        final Firebase tutorRequestRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + tutorRequestId);

        final Button confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setVisibility(View.INVISIBLE);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Accepted!");
                tutorRequestRef.child("studentAccepted").setValue(true);
            }
        });

        final Button rejectButton = (Button) findViewById(R.id.rejectButton);
        rejectButton.setVisibility(View.INVISIBLE);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Rejected!");
                removeTutorFromRequest(tutorId, tutorRequestId);
            }
        });

        tutorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tutor tutor = dataSnapshot.getValue(Tutor.class);
                tutor.setId(dataSnapshot.getKey());
                ((TextView) findViewById(R.id.toolbarText)).setText(tutor.getUsername());
                Utils.countdown();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        tutorRequestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TutorRequest request = dataSnapshot.getValue(TutorRequest.class);
                if (request.getTutorAccepted() != null && request.getTutorAccepted()) {
                    confirmButton.setVisibility(View.VISIBLE);
                    rejectButton.setVisibility(View.VISIBLE);
                } else {
                    confirmButton.setVisibility(View.INVISIBLE);
                    rejectButton.setVisibility(View.INVISIBLE);
                }
                Utils.countdown();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        final String tutorRequestId = getIntent().getStringExtra(AvailableTutorsActivity.TUTOR_REQUEST_ID);
        Firebase tutorRequestRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + tutorRequestId);
        tutorRequestRef.child("activeTutorId").removeValue();
        tutorRequestRef.child("tutorAccepted").removeValue();
        tutorRequestRef.child("studentAccepted").removeValue();
        super.onBackPressed();
    }

    private void removeTutorFromRequest(final String tutorId, String tutorRequestId) {
        final Firebase tutorRequestRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + tutorRequestId);
        tutorRequestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: " + dataSnapshot);
                for (DataSnapshot child : dataSnapshot.child("interestedTutors").getChildren()) {
                    Tutor tutor = child.getValue(Tutor.class);
                    Log.d(TAG, "onDataChange: " + tutor);
                    if (tutorId.equals(tutor.getId())) {
                        Log.d(TAG, "onDataChange: Deleting " + child.getKey());

                        tutorRequestRef.child("activeTutorId").removeValue();
                        tutorRequestRef.child("tutorAccepted").removeValue();
                        tutorRequestRef.child("studentAccepted").removeValue();
                        tutorRequestRef.child("interestedTutors").child(child.getKey()).removeValue();
                        onBackPressed();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ReviewFragment(), "Reviews");
        adapter.addFragment(new MessageFragment(), "Messages");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
