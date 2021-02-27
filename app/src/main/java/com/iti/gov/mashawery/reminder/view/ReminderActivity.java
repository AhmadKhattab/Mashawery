package com.iti.gov.mashawery.reminder.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.gson.Gson;
import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.home.view.MainActivity;
import com.iti.gov.mashawery.home.viewmodel.HomeViewModel;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepo;
import com.iti.gov.mashawery.model.repository.TripsRepoInterface;

public class ReminderActivity extends AppCompatActivity {


    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final int NOTIFICATION_ID = 1234;
    private static AudioManager audioManager;
    private long tripID = 0;


    HomeViewModel reminderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Trip incomingTrip = (Trip) getIntent().getExtras(TripAlarm.TRIP_TAG);

        Trip incomingTrip = (Trip) new Gson().fromJson(getIntent().getStringExtra(TripAlarm.TRIP_TAG), Trip.class);

        // Create the object of
        // AlertDialog Builder class
        audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        AlertDialog.Builder builder = new AlertDialog.Builder(ReminderActivity.this);
        final int i = audioManager.getRingerMode();
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        final MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.ringing);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100, 100);

        // Set the message show for the Alert time
        builder.setMessage("Remember your trip, " + incomingTrip.getName());

        // Set Alert Title
        builder.setTitle("Mashawery !");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.
        TripsRepoInterface tripsRepoInterface = new TripsRepo(this);

        reminderViewModel = new HomeViewModel();
        reminderViewModel.setTripsRepoInterface(tripsRepoInterface);


        builder.setPositiveButton("Start",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        // When the user click Start button
                        // then app will close
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        TripAlarm.cancelAlarm(ReminderActivity.this, incomingTrip.getId());
                        incomingTrip.setStatus(MainActivity.STATUS_DONE);
                        reminderViewModel.updateTripInDB(incomingTrip);
                        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr=" + incomingTrip.getEndPoint());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);

                        finish();
                    }
                });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder.setNegativeButton(
                "Cancel",
                new DialogInterface
                        .OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        // If user click no
                        // then dialog box is canceled.

                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        dialog.cancel();
                        TripAlarm.cancelAlarm(ReminderActivity.this, incomingTrip.getId());

                        incomingTrip.setStatus(MainActivity.STATUS_CANCEL);
                        reminderViewModel.updateTripInDB(incomingTrip);


                        finish();

                    }
                });

        builder.setNeutralButton("Snooze", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                finish();
                Log.i("TAG", "@@@@@@@@@@@@@@@@@@@@" + incomingTrip.getName() + "@@@@@@@@@@@@@@@@@@@@" + incomingTrip.getId() + "@@@@@@@@@@@@@@@@@@@@" + incomingTrip.getDate() + "@@@@@@@@@@@@@@@@@@@@");
                showNotification(incomingTrip);
            }
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
        mediaPlayer.start();

    }

    private void showNotification(Trip trip) {
        Intent notificationIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent startIntent = new Intent(ReminderActivity.this, StartReciever.class);
        //Log.i("TAG", "+++++++++++++++++++"+trip.getId()+"++++++++++++++++++++");
        startIntent.putExtra(MainActivity.TRIP_ID, new Gson().toJson(trip));
        //Log.i("TAG", "+---------------------"+trip.getId()+"----------------");
        //startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startIntent.putExtra("ID", NOTIFICATION_ID);
        PendingIntent startPending = PendingIntent.getBroadcast(this, trip.getId(), startIntent, 0);

        Intent cancelIntent = new Intent(this, CancelReciever.class);
        cancelIntent.putExtra(MainActivity.TRIP_ID, new Gson().toJson(trip));
        //cancelIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        cancelIntent.putExtra("ID", NOTIFICATION_ID);
        PendingIntent cancelPending = PendingIntent.getBroadcast(this, trip.getId(), cancelIntent, 0);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Don't forget your trip!";
            String description = "Mashawery";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager;
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            builder.setSmallIcon(R.drawable.ic_car);
            builder.setContentText(name);
            builder.setContentTitle(description);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.setContentIntent(pendingIntent);
            builder.setOnlyAlertOnce(true);
            builder.addAction(R.drawable.ic_start, "Start Trip", startPending).setAutoCancel(true);
            builder.addAction(R.drawable.ic_cancel, "Cancel Trip", cancelPending).setAutoCancel(true);
            builder.setAutoCancel(true);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }


    }


}
