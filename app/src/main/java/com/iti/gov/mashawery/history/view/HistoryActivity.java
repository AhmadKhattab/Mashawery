package com.iti.gov.mashawery.history.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.iti.gov.mashawery.databinding.ActivityHistoryBinding;
import com.iti.gov.mashawery.history.viewmodel.HistoryViewModel;
import com.iti.gov.mashawery.home.view.OnTripListener;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepo;
import com.iti.gov.mashawery.model.repository.TripsRepoInterface;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ActivityHistoryBinding binding;
    TripsHistoryAdapter historyAdapter;
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
        HistoryViewModel historyViewModel = new HistoryViewModel();
        historyViewModel.setTripsRepoInterface(repoInterface);


        //Passing onHistoryListener to HistoryAdapter
        historyAdapter.setOnHistoryListener(new OnHistoryListener() {
            @Override
            public void onHistoryClick(Trip trip) {

            }

            @Override
            public void onHistoryDelete(Trip trip) {
                historyViewModel.removeTrip(trip.getId());
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
}