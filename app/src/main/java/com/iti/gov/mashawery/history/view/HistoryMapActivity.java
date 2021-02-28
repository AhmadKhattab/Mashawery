package com.iti.gov.mashawery.history.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.databinding.ActivityHistoryMapBinding;
import com.iti.gov.mashawery.history.viewmodel.HistoryViewModel;
import com.iti.gov.mashawery.history.viewmodel.HistoryViewModelFactory;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepo;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class HistoryMapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {

    ActivityHistoryMapBinding binding;
    private List<Trip> historyTrips;
    List<Pair> historyToBeDrawn;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize History View Model
        HistoryViewModel historyViewModel = ViewModelProviders.of(this, new HistoryViewModelFactory(TripsRepo.getInstance(this)))
                .get(HistoryViewModel.class);

        historyTrips = historyViewModel.historyListLiveData.getValue();

        historyToBeDrawn = historyTrips.stream().map(trip -> {
            double lat = 0, lng = 0, lat2 = 0, lng2 = 0;
            Pair<LatLng, LatLng> coordinatePair = null;
            try {

                 lat = new Geocoder(this).getFromLocationName(trip.getStartPoint(), 1).get(0).getLatitude();
                 lng = new Geocoder(this).getFromLocationName(trip.getStartPoint(), 1).get(0).getLongitude();

                 lat2 = new Geocoder(this).getFromLocationName(trip.getEndPoint(), 1).get(0).getLatitude();
                 lng2 = new Geocoder(this).getFromLocationName(trip.getEndPoint(), 1).get(0).getLongitude();


            } catch (IOException e) {
                e.printStackTrace();
            }

            coordinatePair = Pair.create(new LatLng(lat, lng), new LatLng(lat2, lng2));
            return coordinatePair;
        }).collect(Collectors.toList());

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.historyMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



        for (Pair current : historyToBeDrawn) {

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                    .clickable(true).color(color).
                            add((LatLng)current.first, (LatLng)current.second));

        }

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 4));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((LatLng)historyToBeDrawn.get(0).first, 4));

        // Set listeners for click events.
        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);


    }
}