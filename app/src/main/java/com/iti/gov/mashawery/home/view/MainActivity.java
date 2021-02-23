package com.iti.gov.mashawery.home.view;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.databinding.ActivityMainBinding;
import com.iti.gov.mashawery.localStorage.SharedPref;
import com.iti.gov.mashawery.registeration.view.LoginActivity;
import com.iti.gov.mashawery.history.view.HistoryActivity;
import com.iti.gov.mashawery.home.viewmodel.HomeViewModel;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepo;
import com.iti.gov.mashawery.model.repository.TripsRepoInterface;

import com.iti.gov.mashawery.trip.create.view.AddTripActivity;
import com.iti.gov.mashawery.trip.edit.view.EditTripActivity;



import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String TRIP_ID = "TRIP_ID";
    ActivityMainBinding binding;
    TripsAdapter tripsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        tripsAdapter = new TripsAdapter();
        configureTripsRecyclerView();

        TripsRepoInterface tripsRepoInterface = new TripsRepo(this);

        HomeViewModel homeViewModel = new HomeViewModel();
        homeViewModel.setTripsRepoInterface(tripsRepoInterface);
        homeViewModel.getTrips();

        tripsAdapter.setOnTripListener(new OnTripListener() {
            @Override
            public void onTripClick(Trip trip) {
                homeViewModel.navigateToTripDetails(trip.getId());
            }

            @Override
            public void onTripDelete(Trip trip) {
                homeViewModel.removeTrip(trip.getId());
            }

            @Override
            public void onTripStart(Trip trip) {

            }
        });

        homeViewModel.tripListLiveData.observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {

                tripsAdapter.setTripList(trips);

            }
        });


        homeViewModel.tripIdLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer != -1) {
                    Intent intent = new Intent(MainActivity.this, EditTripActivity.class);
                    intent.putExtra(TRIP_ID, integer);
                    startActivity(intent);
                }
            }
        });



        binding.fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, MaineActivity.class);
                Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
                startActivity(intent);
            }
        });
        binding.fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, MaineActivity.class);
                Intent intent = new Intent(MainActivity.this, MaineActivity.class);
                startActivity(intent);
            }
        });
        binding.fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, MaineActivity.class);
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

    }




    private void configureTripsRecyclerView() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        binding.tripRecycler.setAdapter(tripsAdapter);
        binding.tripRecycler.setLayoutManager(manager);

    }


}