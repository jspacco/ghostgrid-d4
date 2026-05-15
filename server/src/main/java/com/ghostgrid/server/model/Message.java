package com.ghostgrid.server.model;

import java.time.ZonedDateTime;

public class Message {
    private String user;
    private String text;
    private ZonedDateTime timestamp;

    public Message() {}

    public Message(String user, String text, ZonedDateTime timestamp) {
        this.user = user;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
