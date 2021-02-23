package com.iti.gov.mashawery.trip.create.viewmodel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iti.gov.mashawery.model.Note;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.NotesHolder;
import com.iti.gov.mashawery.model.repository.TripsRepoInterface;

import java.util.ArrayList;
import java.util.List;


public class TripViewModel extends ViewModel {

    public MutableLiveData<Boolean> dateAndTimeFlag;
    public MutableLiveData<Boolean> addNotesFlag;
    public MutableLiveData<Boolean> finishFlag;
    public MutableLiveData<List<Note>> noteListLiveData;
    public MutableLiveData<Trip> tripLiveData;
    public MutableLiveData<Note> note;

    //    public MediatorLiveData<List<Trip>> tripList;
    private TripsRepoInterface tripsRepoInterface;


    {
        dateAndTimeFlag = new MutableLiveData<>();
        addNotesFlag = new MutableLiveData<>();
        finishFlag = new MutableLiveData<>();
        noteListLiveData = new MutableLiveData<>();
        initializeNotesLiveData();
        dateAndTimeFlag.setValue(false);
        addNotesFlag.setValue(false);
        finishFlag.setValue(false);
        note = new MutableLiveData<>();
        note.setValue(new Note());
        tripLiveData = new MutableLiveData<>();
        tripLiveData.setValue(null);

//        tripList = new MediatorLiveData<>();
//        tripList.setValue(new ArrayList<>());

    }

    public void setTripsRepoInterface(TripsRepoInterface tripsRepoInterface) {
        this.tripsRepoInterface = tripsRepoInterface;
    }


//    public void getTrips() {
//
//        tripList.addSource(tripsRepoInterface.getTrips(), new Observer<List<Trip>>() {
//            @Override
//            public void onChanged(List<Trip> trips) {
//                tripList.setValue(trips);
//            }
//        });
//
//
//    }

    public void insertTrip() {
        NotesHolder notesHolder = new NotesHolder(noteListLiveData.getValue());
        tripLiveData.getValue().setNoteList(notesHolder);
        tripsRepoInterface.insertTrip(tripLiveData.getValue());
    }

    private void initializeNotesLiveData() {
        List<Note> noteList = new ArrayList<>();
        noteListLiveData.setValue(noteList);

    }



    public void addNoteToList(Note note) {
        noteListLiveData.getValue().add(note);
        List<Note> noteList = noteListLiveData.getValue();
        noteListLiveData.setValue(noteList);
    }


    public void navigateToDateTime() {
        dateAndTimeFlag.setValue(true);
    }

    public void completeNavigateToDateTime() {
        dateAndTimeFlag.setValue(false);
    }

    public void navigateToAddNotes() {
        addNotesFlag.setValue(true);
    }

    public void completeNavigateToAddNotes() {
        addNotesFlag.setValue(false);
    }


    public void creationCompleted() {
        finishFlag.setValue(true);
    }

    public void completeCreationCompleted() {
        finishFlag.setValue(false);
    }

    public void deleteSelectedNote(Note note) {
        noteListLiveData.getValue().remove(note);
        List<Note> noteList = noteListLiveData.getValue();
        noteListLiveData.setValue(noteList);
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

    public void updateTrip(Trip trip) {

        this.tripLiveData.setValue(trip);
    }

    public void updateTripTime(String date, String time) {

        tripLiveData.getValue().setDate(date);
        tripLiveData.getValue().setTime(time);
    }
}
