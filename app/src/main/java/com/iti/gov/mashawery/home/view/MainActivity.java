package com.iti.gov.mashawery.home.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;


import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.databinding.ActivityMainBinding;
import com.iti.gov.mashawery.localStorage.SharedPref;
import com.iti.gov.mashawery.registeration.view.LoginActivity;
import com.iti.gov.mashawery.history.view.HistoryActivity;
import com.iti.gov.mashawery.home.viewmodel.HomeViewModel;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepo;
import com.iti.gov.mashawery.model.repository.TripsRepoInterface;

import com.iti.gov.mashawery.reminder.view.TripAlarm;
import com.iti.gov.mashawery.trip.create.view.AddTripActivity;
import com.iti.gov.mashawery.trip.edit.view.EditTripActivity;


import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String TRIP_ID = "TRIP_ID";
    public static final int STATUS_DONE = 2;
    public static final int STATUS_CANCEL = 1;
    ActivityMainBinding binding;
    TripsAdapter tripsAdapter;
    HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        //Ensure that userID stored correctly
        SharedPref.createPrefObject(this);
        Log.d("User Id", "onCreate: " + SharedPref.getCurrentUserId());

        tripsAdapter = new TripsAdapter();
        configureTripsRecyclerView();

        TripsRepoInterface tripsRepoInterface = new TripsRepo(this);

        homeViewModel = new HomeViewModel();
        homeViewModel.setTripsRepoInterface(tripsRepoInterface);
        homeViewModel.getTrips();

        tripsAdapter.setOnTripListener(new OnTripListener() {
            @Override
            public void onTripClick(Trip trip) {
                homeViewModel.navigateToTripDetails(trip.getId());
            }

            @Override
            public void onTripDelete(Trip trip) {
                TripAlarm.cancelAlarm(MainActivity.this, trip.getId());
                homeViewModel.removeTrip(trip.getId());
            }

            @Override
            public void onTripStart(Trip trip) {
                TripAlarm.cancelAlarm(MainActivity.this, trip.getId());
                trip.setStatus(STATUS_DONE);
                homeViewModel.updateTripInDB(trip);
                Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr=" + trip.getEndPoint());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });

        homeViewModel.tripListLiveData.observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {

                tripsAdapter.setTripList(trips);

            }
        });


        homeViewModel.tripIdLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != -1) {
                    Intent intent = new Intent(MainActivity.this, EditTripActivity.class);
                    intent.putExtra(TRIP_ID, integer);
                    startActivity(intent);
                }
            }
        });


        binding.fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, MaineActivity.class);
                Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
                startActivity(intent);
            }
        });
        binding.fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, MaineActivity.class);
                Intent intent = new Intent(MainActivity.this, MaineActivity.class);
                startActivity(intent);
            }
        });
        binding.fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, MaineActivity.class);
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            //when screen is black but not locked it will light-up
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                checkDrawOverAppsPermissionsDialog();
            }
        }
        runBackgroundPermissions();
    }

    private void checkDrawOverAppsPermissionsDialog() {
        new AlertDialog.Builder(this).setTitle("Permission request").setCancelable(false).setMessage("please allow Draw Over Apps permission to be able to use application properly")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawOverAppPermission();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                errorWarningForNotGivingDrawOverAppsPermissions();
            }
        }).show();
    }

    private void errorWarningForNotGivingDrawOverAppsPermissions() {
        new AlertDialog.Builder(this).setTitle("Warning").setCancelable(false).setMessage("Unfortunately the display over other apps permission" +
                " is not granted so the application might not behave properly \nTo enable this permission kindly restart the application")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                }).show();
    }

    public void runBackgroundPermissions() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (Build.BRAND.equalsIgnoreCase("xiaomi")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                startActivity(intent);
            } else if (Build.BRAND.equalsIgnoreCase("Honor") || Build.BRAND.equalsIgnoreCase("HUAWEI")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                startActivity(intent);
            }
        }
    }

    public void drawOverAppPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 80);
            }
        }
    }


    private void configureTripsRecyclerView() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        binding.tripRecycler.setAdapter(tripsAdapter);
        binding.tripRecycler.setLayoutManager(manager);

    }


}
