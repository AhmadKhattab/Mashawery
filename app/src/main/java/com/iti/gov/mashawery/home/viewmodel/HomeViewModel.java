package com.iti.gov.mashawery.home.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepoInterface;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private TripsRepoInterface tripsRepoInterface;
    public MediatorLiveData<List<Trip>> tripListLiveData;
    public MutableLiveData<Integer> tripIdLiveData;
    private MutableLiveData<String> currentUserIdlIveData;

    {

        tripListLiveData = new MediatorLiveData<>();
        tripListLiveData.setValue(new ArrayList<>());

        tripIdLiveData = new MutableLiveData<>();
        tripIdLiveData.setValue(-1);

        currentUserIdlIveData = new MutableLiveData<>();
        currentUserIdlIveData.setValue("default user");

    }

    public void setCurrentUserId (String currentUserId) {
        currentUserIdlIveData.setValue(currentUserId);
    }

    public void setTripsRepoInterface(TripsRepoInterface tripsRepoInterface) {
        this.tripsRepoInterface = tripsRepoInterface;

    }

    public void getTrips() {
        tripListLiveData.addSource(tripsRepoInterface.getTrips(currentUserIdlIveData.getValue()), new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                tripListLiveData.setValue(trips);
            }
        });

    }

    public void removeTrip(int id) {

        tripsRepoInterface.removeTrip(id);
    }

    public void navigateToTripDetails(int id) {
        tripIdLiveData.setValue(id);
    }

    public void updateTripInDB(Trip trip) {
        tripsRepoInterface.updateTrip(trip);
    }


}
