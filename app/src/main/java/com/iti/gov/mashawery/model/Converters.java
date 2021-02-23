package com.iti.gov.mashawery.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

public class Converters {

    @TypeConverter
    public String fromNoteListToString(NotesHolder noteList) {

        return new Gson().toJson(noteList);
    }

    @TypeConverter
    public NotesHolder fromStringToNoteList(String notesString) {
//        Type noteType = new TypeToken<List<Note>>(){}.getType();
        NotesHolder noteList = new Gson().fromJson(notesString, NotesHolder.class);
        return noteList;
    }
}
