package com.iti.gov.mashawery.trip.create.view;

import com.iti.gov.mashawery.model.Note;

public interface NoteListenerInterface {

    void onNoteClicked(Note note);
    void onNoteDeleted(Note note);
}
