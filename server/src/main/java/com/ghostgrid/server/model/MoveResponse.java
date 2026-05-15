package com.ghostgrid.server.model;

public class MoveResponse {
    private String user;
    private Position coordinates;
    private String status;
    private String message;
    private String[][] view;

    public MoveResponse() {}

    public MoveResponse(String user, Position coordinates, String status, String message, String[][] view) {
        this.user = user;
        this.coordinates = coordinates;
        this.status = status;
        this.message = message;
        this.view = view;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Position getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Position coordinates) {
        this.coordinates = coordinates;
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

    public String[][] getView() {
        return view;
    }

    public void setView(String[][] view) {
        this.view = view;
    }
}
