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

    @Query("select * from trips_table where status = 1 or status = 2")
    Single<List<Trip>> getHistory();

    @Query("Delete from trips_table WHERE id=:tripId")
    Completable removeTrip(int tripId);

    @Update
    Comparable updateTrip(Trip trip);
}
