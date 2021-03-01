package com.iti.gov.mashawery.trip.create.viewmodel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.localStorage.SharedPref;
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
    public MutableLiveData<Boolean> setDateAndTimeFlag;

    private TripsRepoInterface tripsRepoInterface;


    {
        dateAndTimeFlag = new MutableLiveData<>();
        addNotesFlag = new MutableLiveData<>();
        finishFlag = new MutableLiveData<>();
        noteListLiveData = new MutableLiveData<>();
        noteListLiveData.setValue(new ArrayList<Note>());
        setDateAndTimeFlag = new MutableLiveData<>();

        initializeNotesLiveData();
        dateAndTimeFlag.setValue(false);
        addNotesFlag.setValue(false);
        finishFlag.setValue(false);
        note = new MutableLiveData<>();
        note.setValue(new Note());
        tripLiveData = new MutableLiveData<>();
        tripLiveData.setValue(new Trip());
        setDateAndTimeFlag.setValue(false);


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

        if(tripLiveData.getValue().getType().equals("Round trip")) {
            Trip roundTrip = new Trip();
            List<Note> emptyNoteList = new ArrayList<>();

            roundTrip.setName(tripLiveData.getValue().getName().concat(" round trip"));
            roundTrip.setUserId(tripLiveData.getValue().getUserId());
            roundTrip.setStartPoint(tripLiveData.getValue().getEndPoint());
            roundTrip.setEndPoint(tripLiveData.getValue().getStartPoint());
            roundTrip.setRepetition("None");
            roundTrip.setType("One way");
            roundTrip.setNoteList(new NotesHolder(emptyNoteList));
            tripsRepoInterface.insertTrip(roundTrip);
        }
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


    public void setUserId(String currentUserId) {
        tripLiveData.getValue().setUserId(currentUserId);
    }

    public void setTripId(int tripId) {
        tripLiveData.getValue().setId(tripId);
    }

    public void triggerTrip() {

        tripLiveData.setValue(tripLiveData.getValue());
    }

    public void enableDateAndTimeFlag() {
        setDateAndTimeFlag.setValue(true);
    }
}
