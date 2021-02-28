package com.iti.gov.mashawery.history.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.history.viewmodel.HistoryViewModel;
import com.iti.gov.mashawery.model.Trip;

import java.io.IOException;
import java.util.List;

public class HistoryMapsFragment extends Fragment {
    private GoogleMap historyMap;
    private OnMapReadyCallback callback;
    private List<Trip> historyList;
    private Geocoder geocoder;
    double lat, lng, lat2, lng2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        geocoder = new Geocoder(getActivity());

        HistoryViewModel historyViewModel = ViewModelProviders.of(getActivity()).get(HistoryViewModel.class);
        historyList = historyViewModel.historyListLiveData.getValue();

        if (historyList.size() > 0) {

            try {
                List<Address> addressesStart = geocoder.getFromLocationName(historyList.get(0).getStartPoint(), 1);
                List<Address> addressesEnd = geocoder.getFromLocationName(historyList.get(0).getEndPoint(), 1);
                lat = addressesStart.get(0).getLatitude();
                lng = addressesStart.get(0).getLongitude();
                lat2 = addressesEnd.get(0).getLatitude();
                lng2 = addressesEnd.get(0).getLongitude();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        callback = new OnMapReadyCallback() {


            @Override
            public void onMapReady(GoogleMap googleMap) {
                Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                        .clickable(true).color(Color.BLUE)
                        .add(
                                new LatLng(lat, lng),
                                new LatLng(lat2, lng2)
                        ));

                // Position the map's camera near Alice Springs in the center of Australia,
                // and set the zoom factor so most of Australia shows on the screen.
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 4));

            }
        };

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }


    }
}