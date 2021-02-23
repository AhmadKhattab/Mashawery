package com.iti.gov.mashawery.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@TypeConverters(Converters.class)
@Database(entities = Trip.class, version = 1, exportSchema = false)

public abstract class TripsDatabase extends RoomDatabase {

    private static TripsDatabase instance;

    public abstract TripDao tripDao();


    public static TripsDatabase getInstance(Context context) {

        if(instance == null) {

            synchronized (TripsDatabase.class) {

                if(instance == null) {

                    instance = Room.databaseBuilder(context, TripsDatabase.class, "Trips_Database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return instance;

    }
}

