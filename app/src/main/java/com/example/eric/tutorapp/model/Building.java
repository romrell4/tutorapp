package com.example.eric.tutorapp.model;

import java.io.Serializable;

/**
 * Created by eric on 3/24/16.
 */
public class Building implements Serializable {
    private static final long serialVersionUID = 1L;

    private String shortName;
    private String fullName;

    public Building() {
    }

    public Building(String shortName, String fullName) {
        this.shortName = shortName;
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String toDescriptionString() {
        return shortName + " - " + fullName;
    }
}
