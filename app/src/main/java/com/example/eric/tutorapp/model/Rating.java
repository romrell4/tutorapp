package com.example.eric.tutorapp.model;

/**
 * Created by eric on 3/4/16.
 */
public class Rating {
    private int stars;
    private String comment;

    public Rating() {
    }

    public Rating(int stars, String comment) {
        this.stars = stars;
        this.comment = comment;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "stars=" + stars +
                ", comment='" + comment + '\'' +
                '}';
    }
}
