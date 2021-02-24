package com.iti.gov.mashawery.trip.create.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iti.gov.mashawery.R;

import com.iti.gov.mashawery.databinding.FragmentAddNotesBinding;
import com.iti.gov.mashawery.databinding.InsertNewNoteBinding;
import com.iti.gov.mashawery.home.view.MainActivity;
import com.iti.gov.mashawery.localStorage.SharedPref;
import com.iti.gov.mashawery.model.Note;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.reminder.view.TripAlarm;
import com.iti.gov.mashawery.trip.create.viewmodel.TripViewModel;

import java.util.Calendar;
import java.util.List;


public class AddNotesFragment extends Fragment {
    FragmentAddNotesBinding binding;
    InsertNewNoteBinding insertNewNoteBinding;
    TripViewModel tripViewModel;
    NotesAdapter notesAdapter;
    Dialog dialog;
    Trip newTrip = new Trip();


    Note currentNote;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesAdapter = new NotesAdapter();
        currentNote = new Note();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_notes, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentAddNotesBinding.bind(view);
        configureNotesRecyclerView();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tripViewModel = ViewModelProviders.of(getActivity()).get(TripViewModel.class);
        dialog = new Dialog(getActivity());

        insertNewNoteBinding = InsertNewNoteBinding.inflate(getLayoutInflater());
        dialog.setContentView(insertNewNoteBinding.getRoot());

        tripViewModel.note.observe(getViewLifecycleOwner(), new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                currentNote = note;
            }
        });

        insertNewNoteBinding.btnCancelNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        insertNewNoteBinding.btnSaveNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteTitle = insertNewNoteBinding.etNoteTitle.getText().toString();
                String noteDesc = insertNewNoteBinding.etNoteDesc.getText().toString();

                if(!noteTitle.isEmpty() && !noteDesc.isEmpty()) {
                    if(currentNote.getTitle() != null &&
                            currentNote.getDescription() != null) {

                        tripViewModel.updateNoteList(currentNote, noteTitle, noteDesc);
                        
                    } else {
                        tripViewModel.addNoteToList(new Note(noteTitle, noteDesc));
                    }
                    dialog.dismiss();
                }


            }
        });

        binding.btnAddNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentNote.setTitle(null);
                currentNote.setDescription(null);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                insertNewNoteBinding.etNoteTitle.setText(null);
                insertNewNoteBinding.etNoteDesc.setText(null);
                dialog.show();
            }
        });



        notesAdapter.setNoteList(tripViewModel.noteListLiveData.getValue());

        notesAdapter.setNoteListenerInterface(new NoteListenerInterface() {
            @Override
            public void onNoteClicked(Note note) {
                tripViewModel.note.setValue(note);
                insertNewNoteBinding.etNoteTitle.setText(note.getTitle());
                insertNewNoteBinding.etNoteDesc.setText(note.getDescription());
                dialog.show();
            }

            @Override
            public void onNoteDeleted(Note note) {
                tripViewModel.deleteSelectedNote(note);
            }
        });

        tripViewModel.noteListLiveData.observe(getActivity(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                notesAdapter.setNoteList(notes);
            }
        });



        tripViewModel.finishFlag.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean finishFlag) {
                if(finishFlag) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        binding.btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.createPrefObject(getActivity());
                tripViewModel.setTripId(Math.abs((int)Calendar.getInstance().getTimeInMillis()));
                tripViewModel.triggerTrip();
                TripAlarm.setAlarm(newTrip,getActivity());
                String currentUserId = SharedPref.getCurrentUserId();
                tripViewModel.setUserId(currentUserId);
                tripViewModel.insertTrip();
                tripViewModel.creationCompleted();
            }
        });

        tripViewModel.tripLiveData.observe(getActivity(), new Observer<Trip>() {
            @Override
            public void onChanged(Trip trip) {
                newTrip = trip;
            }
        });





    }


    private void configureNotesRecyclerView() {

        binding.recycler.setAdapter(notesAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recycler.setLayoutManager(layoutManager);

    }


}










