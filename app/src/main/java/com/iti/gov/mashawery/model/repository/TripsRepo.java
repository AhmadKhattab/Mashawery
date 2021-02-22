package com.iti.gov.mashawery.model.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.TripsDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TripsRepo implements TripsRepoInterface {

    private MutableLiveData<List<Trip>> tripListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Trip>> historyListLiveData = new MutableLiveData<>();
    private TripsDatabase tripsDatabase;

    public TripsRepo(Context context) {
        tripsDatabase = TripsDatabase.getInstance(context);
        tripListLiveData.setValue(new ArrayList<>());
        historyListLiveData.setValue(new ArrayList<>());
    }

    @Override
    public MutableLiveData<List<Trip>> getTrips() {

        tripsDatabase.tripDao().getTrips()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Trip>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Trip> trips) {

                        tripListLiveData.setValue(trips);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

        return tripListLiveData;
    }

    @Override
    public MutableLiveData<List<Trip>> getHistory() {

        tripsDatabase.tripDao().getHistory()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Trip>>() {
                               @Override
                               public void onSubscribe(@NonNull Disposable d) {

                               }

                               @Override
                               public void onSuccess(@NonNull List<Trip> trips) {

                                   historyListLiveData.postValue(trips);

                               }

                               @Override
                               public void onError(@NonNull Throwable e) {

                               }
                           }
                );

        return historyListLiveData;
    }


    public void insertTrip(Trip trip) {

        tripsDatabase.tripDao().insertTrip(trip)
                .subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }

    @Override
    public void removeTrip(int tripId) {
        tripsDatabase.tripDao().removeTrip(tripId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        getHistory();
                        getTrips();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }
}
