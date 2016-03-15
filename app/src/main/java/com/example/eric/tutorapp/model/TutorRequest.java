package com.example.eric.tutorapp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by eric on 3/1/16.
 */
public class TutorRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String phoneId;
    private String name;
    private Course course;
    private BigDecimal price;
    private String building;
    private String message;
    private List<Tutor> interestedTutors;
    private String activeTutorId;
    private Boolean tutorAccepted;
    private Boolean studentAccepted;

    public TutorRequest() {
    }

    public TutorRequest(String phoneId, String name, Course course, BigDecimal price, String building, String message, List<Tutor> interestedTutors, String activeTutorId, Boolean tutorAccepted, Boolean studentAccepted) {
        this.phoneId = phoneId;
        this.name = name;
        this.course = course;
        this.price = price;
        this.building = building;
        this.message = message;
        this.interestedTutors = interestedTutors;
        this.activeTutorId = activeTutorId;
        this.tutorAccepted = tutorAccepted;
        this.studentAccepted = studentAccepted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Tutor> getInterestedTutors() {
        return interestedTutors;
    }

    public void setInterestedTutors(List<Tutor> interestedTutors) {
        this.interestedTutors = interestedTutors;
    }

    public void addInterestedTutor(Tutor interestedTutor) {
        if (this.interestedTutors == null) {
            this.interestedTutors = new ArrayList<>();
        }
        this.interestedTutors.add(interestedTutor);
    }

    public void removeInterestedTutor(Tutor interestedTutor) {
        this.interestedTutors.remove(interestedTutor);
    }

    public String getActiveTutorId() {
        return activeTutorId;
    }

    public void setActiveTutorId(String activeTutorId) {
        this.activeTutorId = activeTutorId;
    }

    public Boolean getTutorAccepted() {
        return tutorAccepted;
    }

    public void setTutorAccepted(Boolean tutorAccepted) {
        this.tutorAccepted = tutorAccepted;
    }

    public Boolean getStudentAccepted() {
        return studentAccepted;
    }

    public void setStudentAccepted(Boolean studentAccepted) {
        this.studentAccepted = studentAccepted;
    }

    @Override
    public String toString() {
        return "TutorRequest{" +
                "id='" + id + "'" +
                ", phoneId='" + phoneId + '\'' +
                ", name='" + name + '\'' +
                ", course=" + course +
                ", price=" + price +
                ", building='" + building + '\'' +
                ", message='" + message + '\'' +
                ", interestedTutors=" + ((interestedTutors == null) ? "null" : Arrays.toString(interestedTutors.toArray())) +
                ", activeTutorId='" + activeTutorId + "'" +
                ", tutorAccepted=" + tutorAccepted +
                ", studentAccepted=" + studentAccepted +
                '}';
    }
}
