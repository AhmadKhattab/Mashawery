package com.iti.gov.mashawery.home.viewmodel;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepoInterface;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel {

    private TripsRepoInterface tripsRepoInterface;
    public MediatorLiveData<List<Trip>> tripListLiveData;
    public MutableLiveData<Integer> tripIdLiveData;

    {

        tripListLiveData = new MediatorLiveData<>();
        tripListLiveData.setValue(new ArrayList<>());

        tripIdLiveData = new MutableLiveData<>();
        tripIdLiveData.setValue(-1);

    }

    public void setTripsRepoInterface(TripsRepoInterface tripsRepoInterface) {
        this.tripsRepoInterface = tripsRepoInterface;

    }

    public void getTrips() {
        tripListLiveData.addSource(tripsRepoInterface.getTrips(), new Observer<List<Trip>>() {
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
}
