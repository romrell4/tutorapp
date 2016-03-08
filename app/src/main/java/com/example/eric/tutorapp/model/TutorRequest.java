package com.example.eric.tutorapp.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by eric on 3/1/16.
 */
public class TutorRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String phoneId;
    private String name;
    private Course course;
    private BigDecimal price;
    private String building;
    private String message;

    public TutorRequest() {
    }

    public TutorRequest(String phoneId, String name, Course course, BigDecimal price, String building, String message) {
        this.phoneId = phoneId;
        this.name = name;
        this.course = course;
        this.price = price;
        this.building = building;
        this.message = message;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TutorRequest{" +
                "phoneId='" + phoneId + '\'' +
                ", name='" + name + '\'' +
                ", course=" + course +
                ", price=" + price +
                ", building='" + building + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
