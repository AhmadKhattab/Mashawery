package com.iti.gov.mashawery.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.localStorage.SharedPref;
import com.iti.gov.mashawery.model.FireBaseDB;
import com.iti.gov.mashawery.model.FirebaseTrip;
import com.iti.gov.mashawery.model.FirebaseTripDao;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.TripsDatabase;
import com.iti.gov.mashawery.registeration.view.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Profile extends AppCompatActivity {
    private Button logoutButton;
    private Button emailButton;
    private Button syncButton;
    private String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        logoutButton = findViewById(R.id.btn_logout);
        emailButton = findViewById(R.id.btn_email);
        syncButton = findViewById(R.id.btn_sync);
        SharedPref.createPrefObject(Profile.this);
        userEmail = SharedPref.getUserEmail();
        if (!userEmail.equals("")) {
            emailButton.setText(SharedPref.getUserEmail());
        }


        Log.i("len", userEmail);


        //  getTrips();

       buttonsClicked();
    }

    private void buttonsClicked() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.setLogin(false);
                deleteAllTripsFromRoom();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Profile.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //getActivity().getFragmentManager().popBackStack();
             /*   if (!SharedPref.checkLoginWithFirebase()) {
                    signOut();
                    LoginActivity.account = null;
                }*/
            }
        });


        syncButton.setOnClickListener(v -> {
            syncWithFirebase();


        });
    }

    private void signOut() {


        LoginActivity.mGoogleSignInClient.signOut();
       // SharedPref.clear();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private  void syncWithFirebase (){
        getTripsFromRoom();
    }

    private void getTripsFromRoom() {
        TripsDatabase.getInstance(Profile.this).tripDao()
                .getAllTrips(FirebaseAuth.getInstance().getUid())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Trip>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull List<Trip> trips) {
                Log.e("",""+trips.get(0).getName());
                List<FirebaseTrip>firebaseTrips=convertRoomListToFirebaseList(trips);
                Log.e(" ",""+trips.get(0).getName());
                saveTripsOnFirebase(firebaseTrips);


            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    private void saveTripsOnFirebase(List<FirebaseTrip> firebaseTrips) {
        if (firebaseTrips==null && firebaseTrips.size()==0){
            Toast.makeText(this, "There is no data to sync", Toast.LENGTH_SHORT).show();
        }else{
            /*progressDialog.setTitle(getString(R.string.sync_data));
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();*/
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FireBaseDB.getUsers().child(mAuth.getCurrentUser().getUid()).child(FireBaseDB.USER_Trip_REF).setValue("");
            FirebaseTripDao.addUserTrips(firebaseTrips, mAuth.getCurrentUser().getUid(),new OnCompleteListener<Void>() {
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                      //  progressDialog.dismiss();
                        Toast.makeText(Profile.this, getString(R.string.sync_success), Toast.LENGTH_LONG).show();
                    }else{
                      //  progressDialog.dismiss();
                        Toast.makeText(Profile.this, getString(R.string.sync_faild), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }



    }

    private List<FirebaseTrip> convertRoomListToFirebaseList(List<Trip> trips) {
        List<FirebaseTrip> list = new ArrayList<>();
        for(int i =0;i<trips.size();i++){
            Trip trip = trips.get(i);
            list.add(new FirebaseTrip(trip.getId(),trip.getUserId(),trip.getName(),trip.getStartPoint(),trip.getEndPoint(),trip.getDate(),trip.getTime(),trip.getType(),
                   trip.getRepetition(),trip.getStatus(),new Gson().toJson(trip.getNoteList())));

        }
        return  list;
    }
    //delete from room
    private void deleteAllTripsFromRoom() {
        TripsDatabase.getInstance(Profile.this).tripDao().deleteAllRecords()
                .subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });
    }





}