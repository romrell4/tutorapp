package com.example.eric.tutorapp;

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

        final String tutorId = getIntent().getStringExtra(AvailableTutorsActivity.TUTOR_ID);
        final String tutorRequestId = getIntent().getStringExtra(AvailableTutorsActivity.TUTOR_REQUEST_ID);

        final Button confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setVisibility(View.INVISIBLE);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Accepted!");
            }
        });

        final Button rejectButton = (Button) findViewById(R.id.rejectButton);
        rejectButton.setVisibility(View.INVISIBLE);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Rejected!");
            }
        });

        Firebase tutorRef = new Firebase(HomeActivity.BASE_URL + "tutors/" + tutorId);
        tutorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Loaded! " + dataSnapshot);
                Tutor tutor = dataSnapshot.getValue(Tutor.class);
                tutor.setId(dataSnapshot.getKey());
                ((TextView) findViewById(R.id.toolbarText)).setText(tutor.getUsername());
                if (tutor.getUsername().equals("romrell4")) {
                    confirmButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        Firebase tutorRequestRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + tutorRequestId);
        tutorRequestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TutorRequest request = dataSnapshot.getValue(TutorRequest.class);
                if (request.getTutorAccepted()) {
                    confirmButton.setVisibility(View.VISIBLE);
                    rejectButton.setVisibility(View.VISIBLE);
                } else {
                    confirmButton.setVisibility(View.INVISIBLE);
                    rejectButton.setVisibility(View.INVISIBLE);
                }
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MessageFragment(), "Messages");
        adapter.addFragment(new ReviewFragment(), "Reviews");
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
