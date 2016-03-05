package com.example.eric.tutorapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eric.tutorapp.model.Tutor;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AvailableTutorsActivity extends AppCompatActivity {
    private static final String TAG = "AvailableTutorsActivity";
    private TutorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_available_tutors);

        adapter = new TutorAdapter(this, new ArrayList<Tutor>());
        final ListView availableTutors = (ListView) findViewById(R.id.availableTutors);
        availableTutors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Set up intent
//                startActivity(new Intent(AvailableTutorsActivity.this, AvailableTutorActivity.class));
            }
        });


        final Firebase tutorRef = new Firebase(HomeActivity.BASE_URL + "tutors");
        tutorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Tutor> tutors = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    tutors.add(child.getValue(Tutor.class));
                    Log.d(TAG, "onDataChange: " + child.getValue(Tutor.class));
                }
                adapter = new TutorAdapter(AvailableTutorsActivity.this, tutors);
                availableTutors.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private static class TutorAdapter extends BaseAdapter {
        private LayoutInflater inflater = null;
        private List<Tutor> data;

        public TutorAdapter(Context context, List<Tutor> data) {
            this.data = data;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.tutor, null);
            }


            ((TextView) view.findViewById(R.id.username)).setText(data.get(position).getUsername());
            ((TextView) view.findViewById(R.id.ratings)).setText("Ratings: " + String.valueOf(data.get(position).getRatings().size()));
            ((TextView) view.findViewById(R.id.major)).setText(data.get(position).getMajor());

            return view;
        }
    }
}
