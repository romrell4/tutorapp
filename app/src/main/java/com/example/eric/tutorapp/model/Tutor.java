package com.example.eric.tutorapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by eric on 3/4/16.
 */
public class Tutor implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String major;
    private List<Course> courses;
    private List<Rating> ratings;

    public Tutor() {
    }

    public Tutor(String username, String password, String major, List<Course> courses, List<Rating> ratings) {
        this.username = username;
        this.password = password;
        this.major = major;
        this.courses = courses;
        this.ratings = ratings;
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

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return "Tutor{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", major='" + major + '\'' +
                ", courses=" + courses +
                ", ratings=" + ratings +
                '}';
    }
}
