package com.example.eric.tutorapp;

/**
 * Created by eric on 3/9/16.
 */
public class Utils {
    public static int getRatingImage(int stars) {
        switch (stars) {
            case 5:
                return R.drawable.five_stars;
            case 4:
                return R.drawable.four_stars;
            case 3:
                return R.drawable.three_stars;
            case 2:
                return R.drawable.two_stars;
            case 1:
                return R.drawable.one_star;
            default:
                return R.drawable.zero_stars;
        }
    }
}
