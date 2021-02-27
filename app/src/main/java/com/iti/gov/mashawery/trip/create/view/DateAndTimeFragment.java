package com.iti.gov.mashawery.trip.create.view;

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
import com.iti.gov.mashawery.databinding.FragmentDateAndTimeBinding;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.trip.create.viewmodel.TripViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateAndTimeFragment extends Fragment {

    FragmentDateAndTimeBinding binding;
    TripViewModel tripViewModel;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    DatePickerDialog.OnDateSetListener onDateSetListener;
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

        return inflater.inflate(R.layout.fragment_date_and_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentDateAndTimeBinding.bind(view);

        //Initialize TripViewModel
        tripViewModel = ViewModelProviders.of(getActivity()).get(TripViewModel.class);

        Calendar calendar = Calendar.getInstance();
        yearVm = calendar.get(Calendar.YEAR);
        monthVm = calendar.get(Calendar.MONTH);
        dayVm = calendar.get(Calendar.DAY_OF_MONTH);
        final int cHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int cMin = calendar.get(Calendar.MINUTE);

        hour = cHour;
        min = cMin;


        binding.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize time picker dialog
                timePickerDialog = new TimePickerDialog(

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

                                    //Set selected time on Text View
                                    binding.tvTime.setText(f24Hours.format(date));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, cHour, cMin, false);
                //Set transparent background
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                if (tripViewModel.tripLiveData.getValue().getTime() != null) {
                    int hour = Integer.parseInt(getTimeSplit(tripViewModel.tripLiveData.getValue().getTime())[0]);
                    int min = Integer.parseInt(getTimeSplit(tripViewModel.tripLiveData.getValue().getTime())[1]);

                    timePickerDialog.updateTime(hour, min);
                } else {
                    //Display selected time
                    timePickerDialog.updateTime(hour, min);
                }
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
                        onDateSetListener, yearVm, monthVm-1, dayVm);

                //Set transparent background
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                if (tripViewModel.tripLiveData.getValue().getDate() != null) {
                    int year = Integer.parseInt(getDateSplit(tripViewModel.tripLiveData.getValue().getDate())[2]);
                    int month = Integer.parseInt(getDateSplit(tripViewModel.tripLiveData.getValue().getDate())[1]);
                    int day = Integer.parseInt(getDateSplit(tripViewModel.tripLiveData.getValue().getDate())[0]);

                    datePickerDialog.updateDate(year, month - 1, day);
                }
                //Disable past date
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

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
                binding.tvDate.setText(date);
            }
        };

//        if (tripViewModel.tripLiveData.getValue().getTime() != null &&
//                tripViewModel.tripLiveData.getValue().getDate() != null) {
//
//            String date = tripViewModel.tripLiveData.getValue().getDate();
//            String time = tripViewModel.tripLiveData.getValue().getTime();
//
//            binding.tvDate.setText(date);
//            binding.tvTime.setText(time);
//
//
//        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        if (tripViewModel.tripLiveData.getValue().getDate() != null &&
//                tripViewModel.tripLiveData.getValue().getTime() != null) {
        //Initialize date and time
//            binding.tvDate.setText(tripViewModel.tripLiveData.getValue().getDate());
//            binding.tvTime.setText(tripViewModel.tripLiveData.getValue().getTime());
//        }


        tripViewModel.addNotesFlag.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean flag) {
                if (flag) {
                    String date = binding.tvDate.getText().toString();
                    String time = binding.tvTime.getText().toString();
                    tripViewModel.updateTripTime(date, time);
                    openAddNotesFragment();
                    tripViewModel.completeNavigateToAddNotes();
                }
            }
        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(binding.tvDate.getText().toString().equals("Select Date"))
                        && !(binding.tvTime.getText().toString().equals("Select Time"))) {
                    if (compareTime(binding.tvTime.getText().toString())) {
                        tripViewModel.navigateToAddNotes();
                    } else {
                        Toast.makeText(getActivity(), "This time is elapsed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


        tripViewModel.tripLiveData.observe(getActivity(), new Observer<Trip>() {
            @Override
            public void onChanged(Trip trip) {
                if (trip.getDate() != null && trip.getTime() != null) {

                    String date = tripViewModel.tripLiveData.getValue().getDate();
                    String time = tripViewModel.tripLiveData.getValue().getTime();

                    binding.tvDate.setText(date);
                    binding.tvTime.setText(time);


                }
            }
        });


    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (tripViewModel.tripLiveData.getValue().getTime() != null &&
//                tripViewModel.tripLiveData.getValue().getDate() != null) {
//
//            String date = tripViewModel.tripLiveData.getValue().getDate();
//            String time = tripViewModel.tripLiveData.getValue().getTime();
//
//            binding.tvDate.setText(date);
//            binding.tvTime.setText(time);
//
//
//        }
//    }

    private void openAddNotesFragment() {
        AddNotesFragment addNotesFragment = new AddNotesFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, addNotesFragment, "notes_fragment")
                .addToBackStack("notes_fragment");
        transaction.commit();

    }


    private String[] getTimeSplit(String time) {

        return time.split(":");
    }

    private String[] getDateSplit(String date) {

        return date.split("/");
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