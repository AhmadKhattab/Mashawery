package com.iti.gov.mashawery.Profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepo;

public class ProfileViewModel  extends AndroidViewModel  {
    private MutableLiveData<Trip> allTrips;
    private TripsRepo mRepository;


    public ProfileViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TripsRepo(application);
        allTrips = mRepository.getAllTripsForFirebase();
    }
    MutableLiveData<Trip> getAllTrips() {
        return allTrips;
    }
}
