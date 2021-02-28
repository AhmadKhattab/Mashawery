package com.iti.gov.mashawery.history.viewmodel;

import android.content.Intent;
import android.util.Pair;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.iti.gov.mashawery.history.view.HistoryActivity;
import com.iti.gov.mashawery.history.view.HistoryMapActivity;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepoInterface;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends ViewModel{

    private TripsRepoInterface tripsRepoInterface;
    public MediatorLiveData<List<Trip>> historyListLiveData;
    private MutableLiveData<String> currentUserIdLiveData;
    private static HistoryViewModel instance;
    public MutableLiveData<Boolean> allowedToNavigateToHistoryMap;

    private HistoryViewModel(TripsRepoInterface tripsRepoInterface){

        historyListLiveData = new MediatorLiveData<>();
        historyListLiveData.setValue(new ArrayList<>());

        currentUserIdLiveData= new MutableLiveData<>();
        currentUserIdLiveData.setValue("default user");

        allowedToNavigateToHistoryMap = new MutableLiveData<>();
//        allowedToNavigateToHistoryMap.setValue(false);

        this.tripsRepoInterface = tripsRepoInterface;

    }

    public static HistoryViewModel getInstance(TripsRepoInterface tripsRepoInterface) {
        if(instance == null) {
            synchronized (HistoryViewModel.class) {
                if (instance == null) {

                    instance = new HistoryViewModel(tripsRepoInterface);
                }
            }
        }
        return instance;
    }

    public void setTripsRepoInterface(TripsRepoInterface tripsRepoInterface) {
        this.tripsRepoInterface = tripsRepoInterface;

    }

    public void getHistory() {
        historyListLiveData.addSource(tripsRepoInterface.getHistory(currentUserIdLiveData.getValue()), new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> history) {
                historyListLiveData.setValue(history);
            }
        });

    }

    public void removeTrip(int id) {

        tripsRepoInterface.removeTrip(id);
    }

    public void setCurrentUserId(String currentUserId) {
        currentUserIdLiveData.setValue(currentUserId);
    }

    public void navigateToHistoryOnMap() {
        if(historyListLiveData.getValue().size() > 0 &&
                historyListLiveData.getValue() != null) {
           allowedToNavigateToHistoryMap.setValue(true);
        } else {
            allowedToNavigateToHistoryMap.setValue(false);
        }
    }


}
