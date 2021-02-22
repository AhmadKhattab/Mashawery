package com.iti.gov.mashawery.history.viewmodel;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.repository.TripsRepoInterface;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel {

    private TripsRepoInterface tripsRepoInterface;
    public MediatorLiveData<List<Trip>> historyListLiveData;

    {

        historyListLiveData = new MediatorLiveData<>();
        historyListLiveData.setValue(new ArrayList<>());

    }

    public void setTripsRepoInterface(TripsRepoInterface tripsRepoInterface) {
        this.tripsRepoInterface = tripsRepoInterface;

    }

    public void getHistory() {
        historyListLiveData.addSource(tripsRepoInterface.getHistory(), new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> history) {
                historyListLiveData.setValue(history);
            }
        });

    }

    public void removeTrip(int id) {

        tripsRepoInterface.removeTrip(id);
    }
}
