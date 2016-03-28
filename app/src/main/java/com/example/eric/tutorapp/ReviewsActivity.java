package com.example.eric.tutorapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eric.tutorapp.model.Review;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity
{
    private static final String TAG = "ReviewsActivity";
    //private ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_reviews);

        Intent intent = getIntent();
        final String tutorId = intent.getStringExtra(AvailableTutorsActivity.TUTOR_ID);
        final String tutorUsername = intent.getStringExtra(AvailableTutorsActivity.TUTOR_USERNAME);
        final String tutorRequestId = intent.getStringExtra(AvailableTutorsActivity.TUTOR_REQUEST_ID);

 //       TextView toolbarText = (TextView) findViewById(R.id.toolbarText);
  //      toolbarText.setText(tutorUsername);

        final ImageView imageView = (ImageView)findViewById(R.id.imageID);
        imageView.setImageResource(R.drawable.profile_image);

        RecyclerView reviewList = (RecyclerView) findViewById(R.id.recyclerView);
        final List<Review> temp = new ArrayList<>();

        final MyListAdapter adapter = new MyListAdapter(temp);
        reviewList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        reviewList.setAdapter(adapter);
        /*
        ListView reviewList = (ListView) findViewById(R.id.reviews);
        adapter = new ReviewAdapter(this, R.layout.review);
        reviewList.setAdapter(adapter);
        */

        final ProgressDialog dialog = ProgressDialog.show(this, "Loading Reviews", "Please wait...");

        Firebase reviewsRef = new Firebase(HomeActivity.BASE_URL + "tutors/" + tutorId + "/reviews");
        reviewsRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                adapter.clear();
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    adapter.add(child.getValue(Review.class));
                    //---------------------- make some fake reviews to show -------------------------------
                    for(int i = 0; i < 15; i++)
                    {
                        Review r = new Review();
                        r.setDateSubmitted("date submitted number: " + String.valueOf(i));
                        r.setStars(4);
                        r.setMessage("random message number: " + String.valueOf(i));
                        r.setAuthor("random author number: " + String.valueOf(i));

                        adapter.add(r);
                    }
                    //---------------------------------------------------------------------------------------
                }
                dialog.hide();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError)
            {
            }
        });

        /*
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.list_view_header_image, reviewList, false);
        reviewList.addHeaderView(linearLayout);*/

        //Button contactButton = (Button) findViewById(R.id.contactButton);
        //contactButton.setOnClickListener(new View.OnClickListener()
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v)
            {
                Firebase tutorRequestRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + tutorRequestId);
                tutorRequestRef.child("activeTutorId").setValue(tutorId);

                Intent intent = new Intent(ReviewsActivity.this, MessagingActivity.class);
                intent.putExtra(AvailableTutorsActivity.TUTOR_REQUEST_ID, tutorRequestId);
                intent.putExtra(AvailableTutorsActivity.TUTOR_USERNAME, tutorUsername);
                startActivity(intent);
            }

        });
    }

    @Override
    public void onBackPressed()
    {
        final String tutorRequestId = getIntent().getStringExtra(AvailableTutorsActivity.TUTOR_REQUEST_ID);
        Firebase tutorRequestRef = new Firebase(HomeActivity.BASE_URL + "tutorRequests/" + tutorRequestId);
        tutorRequestRef.child("activeTutorId").removeValue();
        tutorRequestRef.child("tutorAccepted").removeValue();
        tutorRequestRef.child("studentAccepted").removeValue();
        tutorRequestRef.child("messages").removeValue();
        super.onBackPressed();
    }

    /*private class ReviewAdapter extends ArrayAdapter<Review> {
        private List<Review> reviews = new ArrayList<>();
        private int layout;

        public ReviewAdapter(Context context, int resource) {
            super(context, resource);
            this.layout = resource;
        }

        @Override
        public void add(Review object) {
            super.add(object);
            reviews.add(object);
        }

        @Override
        public int getCount() {
            return reviews.size();
        }

        @Override
        public void clear() {
            super.clear();
            reviews.clear();
        }

        @Override
        public Review getItem(int position) {
            return reviews.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(layout, parent, false);
            }

            Review review = getItem(position);

            TextView author = (TextView) convertView.findViewById(R.id.author);
            author.setText(getResources().getString(R.string.authorFormat, review.getAuthor()));
            TextView message = (TextView) convertView.findViewById(R.id.message);
            message.setText(review.getMessage());
            ImageView stars = (ImageView) convertView.findViewById(R.id.stars);
            stars.setImageResource(Utils.getRatingImage(review.getStars()));

            return convertView;
        }
    }*/

    public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>
    {
        private List<Review> mDataSet;

        // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        //A RECYLERVIEW HOLDS THESE. SO WE MAKE OUR OWN VIEWHOLDER.
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView author;
            TextView message;
            ImageView stars;
            public ViewHolder(View v)
            {
                super(v);
                // Define click listener for the ViewHolder's View.
                v.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Log.d("HERE", "Element " + getPosition() + " clicked.");
                    }
                });
//                Review review = mDataSet.get(getAdapterPosition());

                author = (TextView) v.findViewById(R.id.author);

                message = (TextView) v.findViewById(R.id.message);

                stars = (ImageView) v.findViewById(R.id.stars);


            }

            public void initData(Review review)
            {
                author.setText(getResources().getString(R.string.authorFormat, review.getAuthor()));
                message.setText(review.getMessage());
                stars.setImageResource(Utils.getRatingImage(review.getStars()));
            }

        }
        // END_INCLUDE(recyclerViewSampleViewHolder)

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
         */
        public MyListAdapter(List<Review> dataSet)
        {
            mDataSet = dataSet;
        }

        // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
        {
            // Create a new view.

            //----------- CREATE THE VIEW FROM THE XML. -------
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.review, viewGroup, false);

            return new ViewHolder(v);
        }
        // END_INCLUDE(recyclerViewOnCreateViewHolder)

        // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position)
        {


            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            // ---------------THIS GETS CALLED AS YOU SCROLL UP AND DOWN. LOADS THE DATA FROM THE ARRAY
            viewHolder.initData(mDataSet.get(position));
        }
        // END_INCLUDE(recyclerViewOnBindViewHolder)

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount()
        {
            return mDataSet.size();
        }


        public void add(Review input)
        {
            mDataSet.add(input);
            this.notifyDataSetChanged();
        }

        public void clear()
        {
            mDataSet.clear();
        }
    }
}
