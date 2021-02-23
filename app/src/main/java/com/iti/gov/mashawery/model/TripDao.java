package com.iti.gov.mashawery.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface TripDao {

    @Insert
    Completable insertTrip(Trip trip);

    @Query("select * from trips_table")
    Single<List<Trip>> getTrips();

    @Query("Select * from trips_table Where id=:tripId")
    Single<Trip> getTripById(int tripId);

    @Query("select * from trips_table Where status = 1 or status = 2")
    Single<List<Trip>> getHistory();

    @Query("Delete from trips_table Where id=:tripId")
    Completable removeTrip(int tripId);

    @Update
    Completable updateTrip(Trip trip);

}