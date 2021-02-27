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
import com.iti.gov.mashawery.R;

import com.iti.gov.mashawery.databinding.ActivityLoginBinding;
import com.iti.gov.mashawery.home.view.MainActivity;
import com.iti.gov.mashawery.localStorage.SharedPref;
import com.iti.gov.mashawery.model.User;

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
                                       userID = fAuth.getCurrentUser().getUid();

                                      // progressDialog.dismiss();
                                       SharedPref.setLogin(true);
                                       SharedPref.setLoginWithFirebase(true);
                                       SharedPref.setRegisterWithFirebase(true);
                                       SharedPref.setUserEmail(email);
                                       SharedPref.setUserId(userID);
                                       Log.e("le",email);
                                       startActivity(intent);
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
    }/*
    @Override
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
                        // syncData
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

            startActivity(intent);
        }  else {
            Toast.makeText(this, "Please login with a valid Google account", Toast.LENGTH_SHORT).show();
        }
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