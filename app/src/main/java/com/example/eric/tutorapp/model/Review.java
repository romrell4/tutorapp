package com.example.eric.tutorapp.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by eric on 3/9/16.
 */
public class Review implements Serializable {
    private static final long serialVersionUID = 1L;

    private int stars;
    private String message;
    private Date dateSubmitted;
    private String author;

    public Review() {}

    public Review(int stars, String message, Date dateSubmitted, String author) {
        this.stars = stars;
        this.message = message;
        this.dateSubmitted = dateSubmitted;
        this.author = author;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
