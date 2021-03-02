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

import android.util.Log;
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
    DatePickerDialog datePickerDialog;
    int yearVm, monthVm, dayVm;
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

        //Declare EditTripViewModel
        editTripViewModel = ViewModelProviders.of(getActivity()).get(EditTripViewModel.class);

        if (editTripViewModel.tripLiveData.getValue().getDate() != null
                && editTripViewModel.tripLiveData.getValue().getTime() != null) {

            String[] timeSplit = editTripViewModel.tripLiveData.getValue().getTime().split(":");
            hour = Integer.parseInt(timeSplit[0]);
            min = Integer.parseInt(timeSplit[1]);

            String[] dateSplit = editTripViewModel.tripLiveData.getValue().getDate().split("/");

            yearVm = Integer.parseInt(dateSplit[2]);
            monthVm = Integer.parseInt(dateSplit[1]);
            dayVm = Integer.parseInt(dateSplit[0]);

        } else {
            Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            min = calendar.get(Calendar.MINUTE);
            yearVm = calendar.get(Calendar.YEAR);
            monthVm = calendar.get(Calendar.MONTH);
            dayVm = calendar.get(Calendar.DAY_OF_MONTH);
        }



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
                        }, hour, min, false);

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

                datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener, yearVm, monthVm - 1, dayVm);

                //Set transparent background
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //Disable past date
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                //datePickerDialog.updateDate(year, month-1, day);
                //Show the date picker dialog
                datePickerDialog.show();

            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String date = dayOfMonth + "/" + month + "/" + year;
                yearVm = year;
                monthVm = month;
                dayVm = dayOfMonth;

                datePickerDialog.updateDate(year, month, dayOfMonth);
                binding.tvDate.setText(date);
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (editTripViewModel.tripLiveData.getValue().getDate() != null
                && editTripViewModel.tripLiveData.getValue().getTime() != null) {

            //Initialize date and time
            binding.tvDate.setText(editTripViewModel.tripLiveData.getValue().getDate());
            binding.tvTime.setText(editTripViewModel.tripLiveData.getValue().getTime());

        }


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
                    if (compareTime(binding.tvTime.getText().toString())) {
                        editTripViewModel.navigateToEditNotes();
                    } else {
                        Toast.makeText(getActivity(), "This time is elapsed", Toast.LENGTH_SHORT).show();
                    }

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


    private String[] getTimeSplit(String time) {

        return time.split(":");
    }

    private boolean compareTime(String selectedTime) {


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date selectedDate = null;

        String selectedDateString = binding.tvDate.getText().toString();
        try {
            selectedDate = simpleDateFormat.parse(selectedDateString);
            if (selectedDate.getTime() > System.currentTimeMillis()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTime = sdf.format(new Date());

        int hour1 = Integer.parseInt(getTimeSplit(selectedTime)[0]);
        int min1 = Integer.parseInt(getTimeSplit(selectedTime)[1]);

        int hour2 = Integer.parseInt(getTimeSplit(currentTime)[0]);
        int min2 = Integer.parseInt(getTimeSplit(currentTime)[1]);

        Log.i("TAG", "compareTime: " + hour1 + ":" + min1);
        Log.i("TAG", "compareTime: " + hour2 + ":" + min2);
        if (hour1 < hour2) {
            return false;
        } else if (hour1 == hour2 && min1 <= min2) {
            return false;
        } else {
            return true;
        }
    }
}