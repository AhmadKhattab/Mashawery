package com.iti.gov.mashawery.trip.edit.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.databinding.ActivityEditTripBinding;
import com.iti.gov.mashawery.home.view.MainActivity;
import com.iti.gov.mashawery.model.repository.TripsRepo;
import com.iti.gov.mashawery.model.repository.TripsRepoInterface;
import com.iti.gov.mashawery.trip.create.viewmodel.TripViewModel;
import com.iti.gov.mashawery.trip.edit.viewmodel.EditTripViewModel;

public class EditTripActivity extends AppCompatActivity {

    ActivityEditTripBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Declare TripsRep
        TripsRepoInterface repoInterface = TripsRepo.getInstance(this);

        //Declare ViewModel and set Repo
        EditTripViewModel editTripViewModel = ViewModelProviders.of(this).get(EditTripViewModel.class);
        editTripViewModel.setTripsRepoInterface(repoInterface);


        //Open the first fragment
        openFragment(new EditTripInfoFragment());

        //Get trip id
        int tripId = getIntent().getIntExtra(MainActivity.TRIP_ID, -1);

        if(tripId != -1) {

            editTripViewModel.setTripId(tripId);
        }


    }

    private void openFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.editTripContainer, fragment, null);
        transaction.commit();

    }
}