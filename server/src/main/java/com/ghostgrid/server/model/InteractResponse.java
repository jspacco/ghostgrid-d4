package com.ghostgrid.server.model;

import java.util.List;

public class InteractResponse {
    private String object_type;
    private Position coordinates;
    private List<Message> messages;
    private String status;
    private String message;

    public InteractResponse() {}

    public InteractResponse(String object_type, Position coordinates, List<Message> messages) {
        this.object_type = object_type;
        this.coordinates = coordinates;
        this.messages = messages;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

    public Position getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Position coordinates) {
        this.coordinates = coordinates;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
