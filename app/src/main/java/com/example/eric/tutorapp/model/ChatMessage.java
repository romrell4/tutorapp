package com.example.eric.tutorapp.model;

import java.io.Serializable;

/**
 * Created by eric on 3/9/16.
 */
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String text;
    private Boolean left;
    private Boolean request;

    public ChatMessage() {
    }

    public ChatMessage(String text, Boolean left, Boolean request) {
        this.text = text;
        this.left = left;
        this.request = request;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getLeft() {
        return left;
    }

    public void setLeft(Boolean left) {
        this.left = left;
    }

    public Boolean getRequest() {
        return request;
    }

    public void setRequest(Boolean request) {
        this.request = request;
    }
}
