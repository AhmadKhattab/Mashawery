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

import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.localStorage.SharedPref;
import com.iti.gov.mashawery.registeration.view.LoginActivity;

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
                Intent intent = new Intent(Profile.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //getActivity().getFragmentManager().popBackStack();
                if (!SharedPref.checkLoginWithFirebase()) {
                    signOut();
                    LoginActivity.account = null;
                }
            }
        });


        syncButton.setOnClickListener(v -> {
            // syncData();
        });
    }

    private void signOut() {

        LoginActivity.mGoogleSignInClient.signOut();
        SharedPref.clear();





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}