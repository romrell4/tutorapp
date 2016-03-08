package com.example.eric.tutorapp.model;

import java.io.Serializable;

/**
 * Created by eric on 3/1/16.
 */
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String departmentName;
    private String catalogNumber;
    private String transcriptTitle;

    public Course() {
    }

    public Course(String departmentName, String catalogNumber, String transcriptTitle) {
        this.departmentName = departmentName;
        this.catalogNumber = catalogNumber;
        this.transcriptTitle = transcriptTitle;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public String getTranscriptTitle() {
        return transcriptTitle;
    }

    public void setTranscriptTitle(String transcriptTitle) {
        this.transcriptTitle = transcriptTitle;
    }

    public String toDescriptionString() {
        return departmentName + " " + catalogNumber + " - " + transcriptTitle;
    }

    @Override
    public String toString() {
        return "'" + departmentName + " " + catalogNumber + " - " + transcriptTitle + "'";
    }
}
