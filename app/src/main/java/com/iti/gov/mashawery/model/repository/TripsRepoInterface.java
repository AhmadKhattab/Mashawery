package com.iti.gov.mashawery.model.repository;

import androidx.lifecycle.MutableLiveData;

import com.iti.gov.mashawery.model.Trip;

import java.util.List;

public interface TripsRepoInterface {

    MutableLiveData<List<Trip>> getTrips(String userId);
    MutableLiveData<Trip> getTripById(int tripId);
    MutableLiveData<List<Trip>> getHistory(String userId);
    void insertTrip(Trip trip);
    void removeTrip(int tripId);
    void updateTrip(Trip trip);
}
