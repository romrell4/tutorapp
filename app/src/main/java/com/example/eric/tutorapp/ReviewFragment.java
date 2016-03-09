package com.example.eric.tutorapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eric.tutorapp.model.Review;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by eric on 3/8/16.
 */
public class ReviewFragment extends Fragment {
    private static final String TAG = "ReviewFragment";

    public ReviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        ListView reviewList = (ListView) view.findViewById(R.id.reviewList);
        reviewList.setAdapter(new ReviewAdapter(getActivity(), R.layout.review));
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocus = getActivity().getCurrentFocus();
            if (currentFocus != null) {
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
    }

    private class ReviewAdapter extends ArrayAdapter<Review> {
        List<Review> reviewList = new ArrayList<>();

        public ReviewAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public void add(Review object) {
            reviewList.add(object);
            super.add(object);
        }

        @Override
        public int getCount() {
            return 2;
//            return reviewList.size();
        }

        @Override
        public Review getItem(int position) {
            if (position == 0) {
                return new Review(4, "Mike is a great teacher, but sometimes I just can't stop thinking about sports...", new Date(), "Ray");
            } else {
                return new Review(3, "Mike is a great teacher, but have you ever heard of Clash of Clans? Now that's a great game.", new Date(), "Mike");
            }
//            return reviewList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.review, parent, false);
            }

            Review review = getItem(position);

            TextView author = (TextView) row.findViewById(R.id.author);
            author.setText(getResources().getString(R.string.authorFormat, review.getAuthor()));
            TextView message = (TextView) row.findViewById(R.id.message);
            message.setText(review.getMessage());

            return row;
        }
    }
}
