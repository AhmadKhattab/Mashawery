package com.iti.gov.mashawery.trip.edit.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.databinding.FragmentEditTripInfoBinding;
import com.iti.gov.mashawery.databinding.FragmentEditTripNotesBinding;
import com.iti.gov.mashawery.databinding.FragmentTripInfoBinding;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepo;
import com.iti.gov.mashawery.trip.create.view.DateAndTimeFragment;
import com.iti.gov.mashawery.trip.create.viewmodel.TripViewModel;
import com.iti.gov.mashawery.trip.edit.viewmodel.EditTripViewModel;

import java.util.Arrays;
import java.util.List;


public class EditTripInfoFragment extends Fragment {


    FragmentEditTripInfoBinding binding;
    static final int REQUEST_CODE_START_POINT = 100;
    static final int REQUEST_CODE_END_POINT = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_trip_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentEditTripInfoBinding.bind(view);

        //Initialize places
        Places.initialize(getActivity().getApplicationContext(), "AIzaSyBdVG9vzyj-y_qnexqsPyFVpXKOaHjtRUs");
//        Places.initialize(getActivity().getApplicationContext(), "AIzaSyBL_za9z0eWrk_VFVN1TCuP32mQW19P52o");
        //Set EditText trip start point non-focusable

        binding.etTripStartPoint.setFocusable(false);
        binding.etTripStartPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG,
                        Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                        .build(getActivity());


                startActivityForResult(intent, REQUEST_CODE_START_POINT);

            }
        });


        //Set EditText trip end point non-focusable

        binding.etTripEndPoint.setFocusable(false);
        binding.etTripEndPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG,
                        Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                        .build(getActivity());


                startActivityForResult(intent, REQUEST_CODE_END_POINT);

            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        EditTripViewModel editTripViewModel = ViewModelProviders.of(getActivity()).get(EditTripViewModel.class);
        editTripViewModel.setTripsRepoInterface(TripsRepo.getInstance(getActivity()));

        //Observe the received trip id and get the selected trip
        editTripViewModel.tripIdLiveData.observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != -1) {
                    editTripViewModel.getTripById(integer);
                }
            }
        });

        Log.i("TAG", "onViewCreated: " + editTripViewModel.tripIdLiveData.getValue());

        //Observe the selected trip
        editTripViewModel.tripLiveData.observe(getActivity(), new Observer<Trip>() {
            @Override
            public void onChanged(Trip trip) {
                if (trip.getName() != null) {


                    binding.etTripName.setText(trip.getName());
                    binding.etTripStartPoint.setText(trip.getStartPoint());
                    binding.etTripEndPoint.setText(trip.getEndPoint());

                    int tripRepetitionSelectedBtnId = getIdOfRepetitionSelectedBtn(trip.getRepetition());

                    int tripTypeSelectedBtnId = getIdOfTypeSelectedBtn(trip.getType());

                    if (tripRepetitionSelectedBtnId != -1) {

                        binding.repetitionRaioGroup.check(tripRepetitionSelectedBtnId);
                    }

                    if (tripTypeSelectedBtnId != -1) {

                        binding.tripTypeRadioGroup.check(tripTypeSelectedBtnId);
                    }


                }
            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AreFieldsFilled()) {
                    Trip trip = new Trip();
                    trip.setName(binding.etTripName.getText().toString());
                    trip.setStartPoint(binding.etTripStartPoint.getText().toString());
                    trip.setEndPoint(binding.etTripEndPoint.getText().toString());

                    int selectedFromRepetition = binding.repetitionRaioGroup.getCheckedRadioButtonId();
                    RadioButton selectedBtnFromRepetition = binding.getRoot().findViewById(selectedFromRepetition);

                    int selectedFromType = binding.tripTypeRadioGroup.getCheckedRadioButtonId();
                    RadioButton selectedBtnFromType = binding.getRoot().findViewById(selectedFromType);

                    trip.setRepetition(selectedBtnFromRepetition.getText().toString());
                    trip.setType(selectedBtnFromType.getText().toString());

                    editTripViewModel.updateTrip(trip);
                    editTripViewModel.navigateToDateTime();
                } else {

                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        editTripViewModel.dateAndTimeFlag.observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    openDateAndTimeFragment();
                    editTripViewModel.completeNavigateToDateTime();
                }
            }
        });


    }

    private int getIdOfRepetitionSelectedBtn(String tripRepetition) {

        int id = -1;

        String tripRepetitionNone = getActivity().getResources().getString(R.string.none);
        String tripRepetitionDaily = getActivity().getResources().getString(R.string.daily);
        String tripRepetitionWeekly = getActivity().getResources().getString(R.string.weekly);
        String tripRepetitionMonthly = getActivity().getResources().getString(R.string.monthly);

        RadioButton rBtnTripRepetition = null;


        if (tripRepetitionNone.equals(tripRepetition)) {

            rBtnTripRepetition = binding.rBtnNone;

        } else if (tripRepetitionDaily.equals(tripRepetition)) {

            rBtnTripRepetition = binding.rBtnDaily;

        } else if (tripRepetitionWeekly.equals(tripRepetition)) {

            rBtnTripRepetition = binding.rBtnWeekly;

        } else if (tripRepetitionMonthly.equals(tripRepetition)) {

            rBtnTripRepetition = binding.rBtnMonthly;

        }

        if (rBtnTripRepetition != null) {

            id = rBtnTripRepetition.getId();
        }

        return id;
    }

    private int getIdOfTypeSelectedBtn(String tripType) {
        int id = -1;

        String tripTypeOneWay = getActivity().getResources().getString(R.string.one_way);
        String tripTypeRoundTrip = getActivity().getResources().getString(R.string.round_trip);


        RadioButton rBtnTripType = null;


        if (tripType.equals(tripTypeOneWay)) {

            rBtnTripType = binding.rBtnOneWay;

        } else if (tripType.equals(tripTypeRoundTrip)) {

            rBtnTripType = binding.rBtnRoundTrip;

        }

        if (rBtnTripType != null) {

            id = rBtnTripType.getId();
        }

        return id;

    }

    private void openDateAndTimeFragment() {
        EditTripDateAndTimeFragment dateAndTimeFragment = new EditTripDateAndTimeFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.editTripContainer, dateAndTimeFragment, "date_time_fragment")
                .addToBackStack("date_time_fragment");
        transaction.commit();

    }

    private boolean AreFieldsFilled() {

        String tripName = binding.etTripName.getText().toString();
        String tripStartPoint = binding.etTripStartPoint.getText().toString();
        String tripEndPoint = binding.etTripEndPoint.getText().toString();

        int selectedFromRepetition = binding.repetitionRaioGroup.getCheckedRadioButtonId();
        int selectedFromType = binding.tripTypeRadioGroup.getCheckedRadioButtonId();

        return (!isBlank(tripName) && !isBlank(tripStartPoint)
                && !isBlank(tripEndPoint)
                && selectedFromRepetition > -1
                && selectedFromType > -1);


    }

    private boolean isBlank(String text) {

        return !(text.trim().length() > 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_START_POINT && resultCode == getActivity().RESULT_OK) {

            //When success
            //Initialize place

            Place place = Autocomplete.getPlaceFromIntent(data);

            //Set address on EditText
            binding.etTripStartPoint.setText(place.getAddress());

        } else if(resultCode == AutocompleteActivity.RESULT_ERROR) {

            //When error
            //Initialize status

            Status status = Autocomplete.getStatusFromIntent(data);

            //Display toast
            Toast.makeText(getActivity().getApplicationContext(), status.getStatusMessage(),
                    Toast.LENGTH_SHORT).show();


        }

        if(requestCode == REQUEST_CODE_END_POINT && resultCode == getActivity().RESULT_OK) {

            //When success
            //Initialize place

            Place place = Autocomplete.getPlaceFromIntent(data);

            //Set address on EditText
            binding.etTripEndPoint.setText(place.getAddress());

        } else if(resultCode == AutocompleteActivity.RESULT_ERROR) {

            //When error
            //Initialize status

            Status status = Autocomplete.getStatusFromIntent(data);

            //Display toast
            Toast.makeText(getActivity().getApplicationContext(), status.getStatusMessage(),
                    Toast.LENGTH_SHORT).show();


        }
    }


}