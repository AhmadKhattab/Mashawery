package com.iti.gov.mashawery.trip.edit.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.databinding.FragmentEditTripDateAndTimeBinding;
import com.iti.gov.mashawery.trip.create.view.AddNotesFragment;
import com.iti.gov.mashawery.trip.edit.viewmodel.EditTripViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditTripDateAndTimeFragment extends Fragment {

    FragmentEditTripDateAndTimeBinding binding;
    EditTripViewModel editTripViewModel;
    DatePickerDialog.OnDateSetListener onDateSetListener;

    int hour, min;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_edit_trip_date_and_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentEditTripDateAndTimeBinding.bind(view);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        binding.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(

                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //Initialize hour and minutes
                                hour = hourOfDay;
                                min = minute;
                                String time = hour + ":" + min;

                                //Initialize 24 hours time format
                                SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                                try {
                                    Date date = f24Hours.parse(time);

                                    //Initialize 12 hours time format
                                    SimpleDateFormat f12Format = new SimpleDateFormat("hh:mm aa");
                                    //Set selected time on textview
                                    binding.tvTime.setText(f24Hours.format(date));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false);

                //Set transparent background
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //Display selected time
                timePickerDialog.updateTime(hour, min);

                //Show time picker dialog
                timePickerDialog.show();


            }
        });


        binding.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener, year, month, day);

                //Set transparent background
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //Show the date picker dialog
                datePickerDialog.show();

            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String date = dayOfMonth + "/" + month + "/" + year;

                binding.tvDate.setText(date);
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Declare EditTripViewModel
        editTripViewModel = ViewModelProviders.of(getActivity()).get(EditTripViewModel.class);

        //Initialize date and time
        binding.tvDate.setText(editTripViewModel.tripLiveData.getValue().getDate());
        binding.tvTime.setText(editTripViewModel.tripLiveData.getValue().getTime());

        editTripViewModel.editNotesFlag.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean flag) {
                if (flag) {
                    String date = binding.tvDate.getText().toString();
                    String time = binding.tvTime.getText().toString();
                    editTripViewModel.updateTripTime(date, time);
                    openEditNotesFragment();
                    editTripViewModel.completeNavigateToAddNotes();
                }
            }
        });


        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(binding.tvDate.getText().toString().equals("Select Date"))
                        && !(binding.tvTime.getText().toString().equals("Select Time"))) {
                    editTripViewModel.navigateToEditNotes();
                } else {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void openEditNotesFragment() {
        EditTripNotesFragment editTripNotesFragment = new EditTripNotesFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.editTripContainer, editTripNotesFragment, null)
                .addToBackStack("notes_fragment");
        transaction.commit();

    }
}