package com.iti.gov.mashawery.Profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepo;

import java.util.List;

public class ProfileViewModel  extends AndroidViewModel  {
    private LiveData<List<Trip>> allTrips;
    private TripsRepo mRepository;


    public ProfileViewModel(@NonNull Application application) {
        super(application);
    }
}
