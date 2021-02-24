package com.iti.gov.mashawery.trip.edit.viewmodel;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.iti.gov.mashawery.model.Note;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepoInterface;

import java.util.ArrayList;
import java.util.List;

public class EditTripViewModel extends ViewModel {
    public MutableLiveData<Boolean> dateAndTimeFlag;
    public MutableLiveData<Boolean> editNotesFlag;
    public MutableLiveData<Boolean> finishFlag;

    public MutableLiveData<Integer> tripIdLiveData;
    public MediatorLiveData<Trip> tripLiveData;
    private TripsRepoInterface tripsRepoInterface;

    public MutableLiveData<Note> noteLiveData;
    public MutableLiveData<List<Note>> noteListLiveData;

    public EditTripViewModel() {
        dateAndTimeFlag = new MutableLiveData<>();
        editNotesFlag = new MutableLiveData<>();
        finishFlag = new MutableLiveData<>();

        dateAndTimeFlag.setValue(false);
        editNotesFlag.setValue(false);
        finishFlag.setValue(false);

        tripIdLiveData = new MutableLiveData<>();
        tripIdLiveData.setValue(-1);
        tripLiveData = new MediatorLiveData<>();
        tripLiveData.setValue(null);

        noteLiveData = new MutableLiveData<>();
        noteLiveData.setValue(new Note());

        noteListLiveData = new MutableLiveData<>();
        noteListLiveData.setValue(new ArrayList<Note>());

    }


    public void setTripId(int tripId) {
        tripIdLiveData.setValue(tripId);
    }

    public void setTripsRepoInterface(TripsRepoInterface repoInterface) {
        tripsRepoInterface = repoInterface;
    }

    public void setNoteListLiveData() {

        noteListLiveData.setValue(tripLiveData.getValue().getNoteList().getNoteList());

    }

    public void getTripById(int tripId) {
        tripLiveData.addSource(tripsRepoInterface.getTripById(tripId), new Observer<Trip>() {
            @Override
            public void onChanged(Trip trip) {
                tripLiveData.setValue(trip);
            }
        });
    }

    public void navigateToDateTime() {
        dateAndTimeFlag.setValue(true);
    }

    public void completeNavigateToDateTime() {
        dateAndTimeFlag.setValue(false);
    }

    public void navigateToEditNotes() {
        editNotesFlag.setValue(true);
    }

    public void completeNavigateToAddNotes() {
        editNotesFlag.setValue(false);
    }

    public void creationCompleted() {
        finishFlag.setValue(true);
    }

    public void updateTrip(Trip trip) {

        tripLiveData.getValue().setName(trip.getName());
        tripLiveData.getValue().setStartPoint(trip.getStartPoint());
        tripLiveData.getValue().setEndPoint(trip.getEndPoint());
        tripLiveData.getValue().setRepetition(trip.getRepetition());
        tripLiveData.getValue().setType(trip.getType());
    }

    public void updateTripTime(String date, String time) {

        tripLiveData.getValue().setDate(date);
        tripLiveData.getValue().setTime(time);
    }


    public void updateNoteList(Note currentNote, String noteTitle, String noteDesc) {

        Note note = null;
        int index = noteListLiveData.getValue().indexOf(currentNote);

        if (index >= 0) {
            note = noteListLiveData.getValue().get(index);
            note.setTitle(noteTitle);
            note.setDescription(noteDesc);
            List<Note> noteList = noteListLiveData.getValue();
            noteListLiveData.setValue(noteList);

        }
    }

    public void addNoteToList(Note note) {
        noteListLiveData.getValue().add(note);
        List<Note> updatedNoteList = noteListLiveData.getValue();
        noteListLiveData.setValue(updatedNoteList);
    }

    public void deleteSelectedNote(Note note) {
        noteListLiveData.getValue().remove(note);
        List<Note> noteList = noteListLiveData.getValue();
        noteListLiveData.setValue(noteList);
    }

    public void updateTripInDB() {
        tripsRepoInterface.updateTrip(tripLiveData.getValue());
    }

    public void triggerTrip() {

        tripLiveData.setValue(tripLiveData.getValue());
    }
}
