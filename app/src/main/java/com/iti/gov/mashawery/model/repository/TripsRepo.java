package com.iti.gov.mashawery.model.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.iti.gov.mashawery.localStorage.SharedPref;
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
    private MutableLiveData<Trip> tripLiveData = new MutableLiveData<>();
    private TripsDatabase tripsDatabase;
    private Context context;

    public TripsRepo(Context context) {
        this.context = context;
        tripsDatabase = TripsDatabase.getInstance(context);
        tripListLiveData.setValue(new ArrayList<>());
        historyListLiveData.setValue(new ArrayList<>());
        tripLiveData.setValue(new Trip());
    }

    @Override
    public MutableLiveData<List<Trip>> getTrips(String userId) {

        tripsDatabase.tripDao().getTrips(userId)
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
    public MutableLiveData<Trip> getTripById(int tripId) {
        tripsDatabase.tripDao().getTripById(tripId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Trip>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Trip trip) {
                        tripLiveData.setValue(trip);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

        return tripLiveData;
    }

    @Override
    public MutableLiveData<List<Trip>> getHistory(String userId) {

        tripsDatabase.tripDao().getHistory(userId)
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
                        SharedPref.createPrefObject(context);
                        getHistory(SharedPref.getCurrentUserId());
                        getTrips(SharedPref.getCurrentUserId());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }

    @Override
    public void updateTrip(Trip trip) {

        tripsDatabase.tripDao().updateTrip(trip)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        SharedPref.createPrefObject(context);
                        getHistory(SharedPref.getCurrentUserId());
                        getTrips(SharedPref.getCurrentUserId());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }
    public MutableLiveData<Trip> getAllTripsForFirebase() { return tripLiveData; }
}
