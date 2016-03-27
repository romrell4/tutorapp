package com.example.eric.tutorapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 3/4/16.
 */
public class Tutor implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    private String password;
    private String major;
    private List<Course> courses;
    private List<Review> reviews;

    public Tutor() {
    }

    public Tutor(String username, String password, String major, List<Course> courses, List<Review> reviews) {
        this.username = username;
        this.password = password;
        this.major = major;
        this.courses = courses;
        this.reviews = reviews;
    }

    public Tutor(String id, String username, String password, String major, List<Course> courses, List<Review> reviews) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.major = major;
        this.courses = courses;
        this.reviews = reviews;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review) {
        if (this.reviews == null) {
            this.reviews = new ArrayList<>();
        }
        this.reviews.add(review);
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "Tutor{" +
                "id='" + id + "'" +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", major='" + major + '\'' +
                ", courses=" + courses +
                ", reviews=" + reviews +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Tutor && this.id.equals(((Tutor) o).id);
    }
}
