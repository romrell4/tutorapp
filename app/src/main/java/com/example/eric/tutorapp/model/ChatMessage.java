package com.example.eric.tutorapp.model;

/**
 * Created by eric on 3/9/16.
 */
public class ChatMessage {
    private String text;
    private boolean left;

    public ChatMessage(String text, boolean left) {
        this.text = text;
        this.left = left;
    }

    public String getText() {
        return text;
    }

    public boolean isLeft() {
        return left;
    }
}
