package com.iti.gov.mashawery.trip.create.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.databinding.ActivityAddTripBinding;
import com.iti.gov.mashawery.model.repository.TripsRepo;
import com.iti.gov.mashawery.model.repository.TripsRepoInterface;
import com.iti.gov.mashawery.trip.create.viewmodel.TripViewModel;

public class AddTripActivity extends AppCompatActivity {
    ActivityAddTripBinding binding;
    TripViewModel tripViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        DateAndTimeFragment dateAndTimeFragment = new DateAndTimeFragment();
        TripInfoFragment tripInfoFragment = new TripInfoFragment();

        openFragment(tripInfoFragment);


        TripsRepoInterface tripsRepoInterface = TripsRepo.getInstance(this);
        tripViewModel = ViewModelProviders.of(this).get(TripViewModel.class);
        tripViewModel.setTripsRepoInterface(tripsRepoInterface);


    }

    private void openFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, fragment, "trip_info_fragment");
        transaction.commit();

    }
}