package com.iti.gov.mashawery.reminder.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import com.iti.gov.mashawery.model.Trip;

public class TripReminderReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Trip incomingTrip =(Trip)intent.getSerializableExtra(TripAlarm.TRIP_TAG);
        Intent dialogIntent = new Intent(context, ReminderActivity.class);
        dialogIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        dialogIntent.putExtra(TripAlarm.TRIP_TAG, incomingTrip);
        context.getApplicationContext().startActivity(dialogIntent);
    }
}
