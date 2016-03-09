package com.example.eric.tutorapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eric.tutorapp.model.TutorRequest;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OpportunitiesActivity extends AppCompatActivity {
    private static final String TAG = "OpportunitiesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_opportunities);

        final ListView opportunities = (ListView) findViewById(R.id.opportunities);
        final OpportunityAdapter adapter = new OpportunityAdapter(this, R.layout.opportunity);

        opportunities.setAdapter(adapter);
        opportunities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(OpportunitiesActivity.this, );
//                startActivity(intent);
            }
        });

        final Firebase requestRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests");
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    adapter.add(child.getValue(TutorRequest.class));
                }
                Log.d(TAG, "onDataChange: Got data");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    private class OpportunityAdapter extends ArrayAdapter<TutorRequest> {
        private List<TutorRequest> opportunities = new ArrayList<>();

        public OpportunityAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public void clear() {
            opportunities = new ArrayList<>();
        }

        @Override
        public void add(TutorRequest object) {
            super.add(object);
            opportunities.add(object);
        }

        @Override
        public int getCount() {
            return opportunities.size();
        }

        @Override
        public TutorRequest getItem(int position) {
            return opportunities.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.opportunity, parent, false);
            }

            TextView text = (TextView) view.findViewById(R.id.text);
            text.setText(getItem(position).getName());
            return view;
        }
    }
}
