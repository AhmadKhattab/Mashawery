package com.iti.gov.mashawery.registeration.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iti.gov.mashawery.R;

import com.iti.gov.mashawery.databinding.ActivityLoginBinding;
import com.iti.gov.mashawery.home.view.MainActivity;
import com.iti.gov.mashawery.localStorage.SharedPref;
import com.iti.gov.mashawery.model.FirebaseTrip;
import com.iti.gov.mashawery.model.FirebaseTripDao;
import com.iti.gov.mashawery.model.NotesHolder;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.model.TripsDatabase;
import com.iti.gov.mashawery.model.User;
import com.iti.gov.mashawery.reminder.view.TripAlarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    DatabaseReference reference;
    FirebaseDatabase fDatabase;
    ProgressDialog progressDialog;
    String userID;
    String email;
    String password;
    public static GoogleSignInClient mGoogleSignInClient;
    public static GoogleSignInAccount account;
    int RC_SIGN_IN = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
      /*  GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        ActivityLoginBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        fAuth=FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        SignInButton signInButton =binding.googleSignInButton;
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        SharedPref.createPrefObject(this);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });



       binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               email= binding.editEmailSignupSignUp.getText().toString();
               password = binding.editPasswordSignUp.getText().toString();
               if (! email.equals("")&& ! password.equals("")) {
                   fAuth.signInWithEmailAndPassword(email, password)
                           .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if (task.isSuccessful()) {

                                       Intent intent = new Intent(LoginActivity.this, MainActivity.class);


//                                       Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                       userID = fAuth.getCurrentUser().getUid();

                                      // progressDialog.dismiss();
                                       SharedPref.setLogin(true);
                                       SharedPref.setLoginWithFirebase(true);
                                       SharedPref.setRegisterWithFirebase(true);
                                       SharedPref.setUserEmail(email);
                                       SharedPref.setUserId(userID);
                                       Log.e("le",email);
                                       syncData();

                                       startActivity(intent);
                                      // progressDialog.dismiss();

//                                       startActivity(intent);

                                       finish();
                                   } else {
                                       Toast.makeText(LoginActivity.this,
                                               task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                     //  progressDialog.dismiss();
                                   }
                               }
                           });
               }
               else{
                   Toast.makeText(LoginActivity.this,"Please Enter Your Email and Password",Toast.LENGTH_LONG).show();
                   // progressDialog.dismiss();
               }

           }
       });
       binding.textRegister.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
               Log.e("register","hello from register");
               finish();

           }
       });
    }
  /*  @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null){

        updateUI(account);}
    }*/
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void updateUI(GoogleSignInAccount account){

        if (account != null){
            userID = fAuth.getCurrentUser().getUid();
            reference =fDatabase.getReference().child("users").child(userID);
            User userData = new User(account.getDisplayName(),account.getEmail());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if(user != null){
                        SharedPref.setUserId(userID);
                         syncData();
                       /* Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent); */
                        //gotoMainActivity
                    }
                    else{
                        reference.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                   // progressDialog.dismiss();
                                   // SharedPref.setRegisterWithFirebase(true);
                                    finish();

                                } }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            Intent intent = new Intent(this, MainActivity.class);
            SharedPref.setUserEmail(account.getEmail());
            SharedPref.setLoginWithFirebase(false);
            Log.i("gmailacc",account.getEmail());
            SharedPref.setLogin(true);

            startActivity(intent);
            finish();
          //  progressDialog.dismiss();
        }  else {
            Toast.makeText(this, "Please login with a valid Google account", Toast.LENGTH_SHORT).show();
        }
    }

    private void syncData() {
        String currentUserId=fAuth.getCurrentUser().getUid();
        FirebaseTripDao.getUserTrips(currentUserId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List <Trip>  tripList ;
                tripList=new ArrayList<>();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    FirebaseTrip trip =dataSnapshot.getValue(FirebaseTrip.class);
                    Log.e("login",trip.getName());
                    tripList.add(new Trip(trip.getId(),trip.getUserId(),trip.getName(),trip.getStartPoint(),trip.getEndPoint(),trip.getDate(), trip.getTime(),
                            trip.getType(),trip.getRepetition(),trip.getStatus(),new Gson().fromJson(trip.getNoteList(),new TypeToken<NotesHolder>() { }.getType())));
                }//habd




                saveFromFirebaseToRoom(tripList);
                //


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void saveFromFirebaseToRoom(List<Trip> tripList) {
       /* for(Trip trip: tripList){
            if(trip.getStatus() == 0){
                if(TripAlarm.getTripDateAndTime(trip).getTimeInMillis()>Calendar.getInstance().getTimeInMillis()){
                    TripAlarm.setAlarm(trip, this);

                }else if( trip.getDate()!=null){
                    trip.setStatus(MainActivity.STATUS_CANCEL);
                }
            }
        }*/
        //My filter goes here
        Observable <Trip>  tripAlarmFilter = Observable.fromIterable(tripList) .filter(trip -> {
            if(trip.getStatus() == 0){
                if (trip.getDate()!=null){
                    if(TripAlarm.getTripDateAndTime(trip).getTimeInMillis()>Calendar.getInstance().getTimeInMillis()){
                        TripAlarm.setAlarm(trip, this);

                    }else {
                        trip.setStatus(MainActivity.STATUS_CANCEL);
                    }

                }
            }
            return true;
        });
        //ends here
        Observable<Trip> tripsObservable = Observable.fromIterable(tripList);
        tripAlarmFilter.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Trip>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull Trip trip) {
                TripsDatabase.getInstance(LoginActivity.this).tripDao()
                        .insertTrip(trip).subscribeOn(Schedulers.computation())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                            }

                            @Override
                            public void onComplete() {
                                Log.e("login",trip.getName());
                            }
                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                            }
                        });
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        for (Trip trip:tripList){
//            //please check if the data and time if status upcomming and set alarm
//            TripsDatabase.getInstance(LoginActivity.this).tripDao()
//                    .insertTrip(trip).subscribeOn(Schedulers.computation())
//                    .subscribe(new CompletableObserver() {
//                        @Override
//                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            Log.e("login",trip.getName());
//                        }
//                        @Override
//                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//
//                        }
//                    });
//
//
//
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
          //  updateUI(account);
            firbaseGoogleAuth(account);
        } catch (ApiException e) {
          //  updateUI(null);
        }
    }

    private void firbaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential= GoogleAuthProvider.getCredential(account.getIdToken(),null);

        fAuth.signInWithCredential(authCredential).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(LoginActivity.this, "successful", Toast.LENGTH_LONG).show();
                   // AllowGoogleAccountToLogen(account);
                    updateUI(account);
                }else{

                }
            }
        });
    }




}