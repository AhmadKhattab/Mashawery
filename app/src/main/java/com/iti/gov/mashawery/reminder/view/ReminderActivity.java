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
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.model.Trip;

public class ReminderActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final int NOTIFICATION_ID = 1234;
    private static AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Trip incomingTrip = (Trip) getIntent().getExtras(TripAlarm.TRIP_TAG);
        // Create the object of
        // AlertDialog Builder class
        audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        AlertDialog.Builder builder = new AlertDialog.Builder(ReminderActivity.this);
        final int i= audioManager.getRingerMode();
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        final MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.ringing);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100,100);

        // Set the message show for the Alert time
        builder.setMessage("Remember your trip,");

        // Set Alert Title
        builder.setTitle("Mashawery !");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        // When the user click yes button
                        // then app will close
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        finish();
                    }
                });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder.setNegativeButton(
                "No",
                new DialogInterface
                        .OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        // If user click no
                        // then dialog box is canceled.
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        dialog.cancel();
                    }
                });

        builder.setNeutralButton("Snooze", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                finish();
                showNotification();
            }
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
        mediaPlayer.start();

    }

    private void showNotification() {
        Intent notificationIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name ="Don't forget your trip!";
            String description = "Mashawery";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel= new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager;
            notificationManager =getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder builder=  new NotificationCompat.Builder (this, CHANNEL_ID);
            builder.setSmallIcon(R.drawable.ic_car);
            builder.setContentText(name);
            builder.setContentTitle(description);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.setContentIntent(pendingIntent);
            notificationManager.notify(NOTIFICATION_ID, builder.build());

        }


    }




}
