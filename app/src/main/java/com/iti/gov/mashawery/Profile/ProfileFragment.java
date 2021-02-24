package com.iti.gov.mashawery.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.home.view.MainActivity;
import com.iti.gov.mashawery.localStorage.SharedPref;
import com.iti.gov.mashawery.model.Trip;
import com.iti.gov.mashawery.registeration.view.LoginActivity;
import com.iti.gov.mashawery.registeration.view.RegisterActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProfileFragment extends Fragment {
    private Button logoutButton;
    private Button emailButton;
    private Button syncButton;
    private String userEmail;
    private ProfileViewModel profileViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
       init(view);
        return view;
    }
    private void init(View view) {
        logoutButton = view.findViewById(R.id.btn_logout);
        emailButton = view.findViewById(R.id.btn_email);
        syncButton = view.findViewById(R.id.btn_sync);
        SharedPref.createPrefObject(getContext());
        userEmail = SharedPref.getUserEmail();
        if (!userEmail.equals("")) {
            emailButton.setText(SharedPref.getUserEmail());
        }


        Log.e("len", userEmail);

        if (!userEmail.equals(" ")) {
            profileViewModel = new ViewModelProvider(this, new ProfileViewModelFactory(getActivity().getApplication(),
                    userEmail)).get(ProfileViewModel.class);
        }

      //  getTrips();

        buttonsClicked();
    }
    private void buttonsClicked() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.setLogin(false);
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //getActivity().getFragmentManager().popBackStack();
                if(!SharedPref.checkLoginWithFirebase()){
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



        }


}