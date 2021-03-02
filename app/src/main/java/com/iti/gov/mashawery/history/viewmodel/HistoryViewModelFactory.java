package com.iti.gov.mashawery.history.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.iti.gov.mashawery.model.repository.TripsRepoInterface;

public class HistoryViewModelFactory implements ViewModelProvider.Factory {

    private TripsRepoInterface tripsRepoInterface;
    private HistoryViewModel historyViewModel;

    public HistoryViewModelFactory(TripsRepoInterface tripsRepoInterface) {
        this.tripsRepoInterface = tripsRepoInterface;
        historyViewModel = HistoryViewModel.getInstance(tripsRepoInterface);

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) historyViewModel;
    }
}
