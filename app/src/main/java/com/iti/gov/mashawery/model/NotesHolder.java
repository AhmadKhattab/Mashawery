package com.iti.gov.mashawery.model;

import com.iti.gov.mashawery.model.Note;

import java.util.List;

public class NotesHolder {

    private List<Note> noteList;


    public NotesHolder() {
    }

    public NotesHolder(List<Note> noteList) {
        this.noteList = noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    public List<Note> getNoteList() {
        return noteList;
    }
}
