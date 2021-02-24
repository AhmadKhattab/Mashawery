package com.iti.gov.mashawery.reminder.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import com.google.gson.Gson;
import com.iti.gov.mashawery.model.Trip;

public class TripReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Trip incomingTrip =new Gson().fromJson(intent.getStringExtra(TripAlarm.TRIP_TAG), Trip.class);
        Log.i("TAG", "onReceive: from receiver" + incomingTrip.getName());

        Intent dialogIntent = new Intent("android.intent.action.MAIN");
        dialogIntent.setClass(context, ReminderActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dialogIntent.putExtra(TripAlarm.TRIP_TAG,intent.getStringExtra(TripAlarm.TRIP_TAG));
        context.startActivity(dialogIntent);
    }
}
