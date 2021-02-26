package com.iti.gov.mashawery.history.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.databinding.ActivityHistoryBinding;
import com.iti.gov.mashawery.databinding.DeleteConfirmationDialogBinding;
import com.iti.gov.mashawery.history.viewmodel.HistoryViewModel;
import com.iti.gov.mashawery.home.view.OnTripListener;
import com.iti.gov.mashawery.localStorage.SharedPref;
import com.iti.gov.mashawery.model.Note;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepo;
import com.iti.gov.mashawery.model.repository.TripsRepoInterface;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ActivityHistoryBinding binding;
    DeleteConfirmationDialogBinding deleteConfirmationDialogBinding;
    TripsHistoryAdapter historyAdapter;
    HistoryViewModel historyViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Initialize HistoryAdapter
        historyAdapter = new TripsHistoryAdapter();


        //Configure HistoryRecyclerView
        configureHistoryRecyclerView();

        //Initialize Repo and HistoryViewModel
        TripsRepoInterface repoInterface = new TripsRepo(this);
        historyViewModel = new HistoryViewModel();
        historyViewModel.setTripsRepoInterface(repoInterface);

        //Set current user id to history view model
        SharedPref.createPrefObject(this);
        historyViewModel.setCurrentUserId(SharedPref.getCurrentUserId());

        //Passing onHistoryListener to HistoryAdapter
        historyAdapter.setOnHistoryListener(new OnHistoryListener() {
            @Override
            public void onHistoryClick(Trip trip) {

            }

            @Override
            public void onHistoryDelete(Trip trip) {
                showDeleteNoteConfirmationDialog(trip);
            }
        });


        //Get the history from room database
        historyViewModel.getHistory();

        historyViewModel.historyListLiveData.observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                historyAdapter.setHistoryList(trips);
            }
        });


    }


    private void configureHistoryRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        binding.historyRecycler.setAdapter(historyAdapter);
        binding.historyRecycler.setLayoutManager(manager);
    }

    private void showDeleteNoteConfirmationDialog(Trip trip) {
        deleteConfirmationDialogBinding = DeleteConfirmationDialogBinding.inflate(HistoryActivity.this.getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this, R.style.AlertDialogTheme);
        builder.setView(deleteConfirmationDialogBinding.getRoot());
        AlertDialog deleteNoteConfirmationDialog = builder.create();


        deleteConfirmationDialogBinding.tvDeletionTitle.setText(R.string.delete_trip_title);
        deleteConfirmationDialogBinding.tvDescription.setText(R.string.are_you_sure_to_delete_trip);
        deleteConfirmationDialogBinding.btnConfirmDeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyViewModel.removeTrip(trip.getId());
                deleteNoteConfirmationDialog.dismiss();

            }
        });

        deleteConfirmationDialogBinding.btnCancelDeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNoteConfirmationDialog.dismiss();
            }
        });


        deleteNoteConfirmationDialog.show();
    }
}