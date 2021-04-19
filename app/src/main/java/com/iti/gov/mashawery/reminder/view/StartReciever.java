package com.iti.gov.mashawery.reminder.view;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.iti.gov.mashawery.helpPackag.FloatingViewService;
import com.iti.gov.mashawery.home.view.MainActivity;
import com.iti.gov.mashawery.home.viewmodel.HomeViewModel;
import com.iti.gov.mashawery.localStorage.SharedPref;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepo;
import com.iti.gov.mashawery.model.repository.TripsRepoInterface;

public class StartReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        TripsRepoInterface tripsRepoInterface= TripsRepo.getInstance(context);
        HomeViewModel reminderViewModel =new HomeViewModel();
        reminderViewModel.setTripsRepoInterface(tripsRepoInterface);
        Trip trip = (Trip) new Gson().fromJson(intent.getStringExtra(MainActivity.TRIP_ID), Trip.class);
        Log.i("TAG", trip.getName()+ " ===================================================== "+ trip.getId());
        //TripAlarm.cancelAlarm(context, trip.getId());
        NotificationManager notificationManager= context.getSystemService(NotificationManager.class);
        //Log.i("TAG", trip.getName()+ " =============************************777 "+ trip.getId());
        notificationManager.cancel(intent.getIntExtra("ID", -1));
       // Log.i("TAG", trip.getName()+ " =========================************=============== "+ trip.getId());
        trip.setStatus(MainActivity.STATUS_DONE);
        reminderViewModel.updateTripInDB(trip);

        // floating icon
        Intent fintent = new Intent(context, FloatingViewService.class);
        fintent.putExtra("tripList", new Gson().toJson(trip.getNoteList().getNoteList()));
        Log.i("tripList notes", (trip.getNoteList().getNoteList().toString()));

        if (trip.getNoteList().getNoteList() != null) {
            SharedPref.createPrefObject(context);
            SharedPref.setFloatingNotes( new Gson().toJson(trip.getNoteList().getNoteList()));
            Log.i("tripList notes", (trip.getNoteList().getNoteList().toString()));
        }context.startService(fintent);
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + trip.getEndPoint());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(mapIntent);
       // context.startService(fintent);


    }
}