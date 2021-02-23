package com.iti.gov.mashawery.reminder.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.iti.gov.mashawery.model.Trip;

import java.util.Calendar;

public class TripAlarm {

    private  static AlarmManager alarmManager ;
    private  static PendingIntent pendingIntent;
    public static final String TRIP_TAG = "TRIP_TAG";


    public static Calendar getTripDateAndTime(Trip myTrip){
        Calendar calendar = Calendar.getInstance();
        String [] dateSplit = myTrip.getDate().split("/");
        String [] timeSplit = myTrip.getTime().split(":");
        calendar.set(Calendar.YEAR, Integer.parseInt(dateSplit[2]) );
        calendar.set(Calendar.MONTH, Integer.parseInt(dateSplit[1])-1 );
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSplit[0]) );
        calendar.set(Calendar.HOUR, Integer.parseInt(timeSplit[0]) );
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeSplit[1]) );
        calendar.set(Calendar.SECOND, 0 );
        return calendar;
    }


    public static void setAlarm(Trip trip, Context context){

        Calendar tripCalendar = getTripDateAndTime(trip);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, TripReminderReciever.class);
        alarmIntent.putExtra(TRIP_TAG, trip);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, trip.getId(), alarmIntent, 0);
        startAlarm(tripCalendar);
    }



    private static void startAlarm(Calendar calendar) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
           alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }


    }

    public static void cancelAlarm(Context context, int requestCode) {
        Intent alarmIntent = new Intent(context, TripReminderReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent, 0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, "Alarm Cancelled", Toast.LENGTH_LONG).show();
    }


}
