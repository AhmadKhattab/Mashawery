package com.iti.gov.mashawery.reminder.view;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.iti.gov.mashawery.home.view.MainActivity;
import com.iti.gov.mashawery.home.viewmodel.HomeViewModel;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepo;
import com.iti.gov.mashawery.model.repository.TripsRepoInterface;

public class CancelReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        TripsRepoInterface tripsRepoInterface = new TripsRepo(context);
        HomeViewModel reminderViewModel = new HomeViewModel();
        reminderViewModel.setTripsRepoInterface(tripsRepoInterface);

        Trip trip = (Trip) new Gson().fromJson(intent.getStringExtra(MainActivity.TRIP_ID), Trip.class);
        Log.i("TAG", trip.getName() + " ===================================================== " + trip.getId());
        // TripAlarm.cancelAlarm(context, trip.getId());
        trip.setStatus(MainActivity.STATUS_CANCEL);
        reminderViewModel.updateTripInDB(trip);
        //Log.i("TAG", trip.getName()+ " @@@@@@@@@@@@@@@@@@@@@@@@@@@@2==============@@@@@@@@@@@@@@@@@@2==== "+ trip.getId());

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.cancel(intent.getIntExtra("ID", -1));
    }
}