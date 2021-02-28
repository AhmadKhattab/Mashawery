package com.iti.gov.mashawery.model;

public class FirebaseTrip {
    private int id;
    private String userId;
    private String name;
    private String startPoint;
    private String endPoint;
    private String date;
    private String time;
    private String type;
    private String repetition;
    private int Status = 0;
    private String noteList;

    public FirebaseTrip() {
    }

    public FirebaseTrip(int id, String userId, String name, String startPoint, String endPoint, String date, String time, String type, String repetition, int status, String noteList) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
        this.time = time;
        this.type = type;
        this.repetition = repetition;
        Status = status;
        this.noteList = noteList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRepetition() {
        return repetition;
    }

    public void setRepetition(String repetition) {
        this.repetition = repetition;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getNoteList() {
        return noteList;
    }

    public void setNoteList(String noteList) {
        this.noteList = noteList;
    }
}
