package com.iti.gov.mashawery.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "trips_table")
public class Trip  {

    @PrimaryKey(autoGenerate = true)
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
    private NotesHolder noteList;

    public Trip() {
    }

    public Trip(int id, String userId, String name, String startPoint, String endPoint, String date, String time, String type, String repetition, int status, NotesHolder noteList) {
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

    public String getName() {
        return name;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getRepetition() {
        return repetition;
    }

    public int getStatus() {
        return Status;
    }

    public NotesHolder getNoteList() {
        return noteList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRepetition(String repetition) {
        this.repetition = repetition;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setNoteList(NotesHolder noteList) {
        this.noteList = noteList;
    }
}
