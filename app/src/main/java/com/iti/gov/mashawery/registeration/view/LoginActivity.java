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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iti.gov.mashawery.R;

import com.iti.gov.mashawery.databinding.ActivityLoginBinding;
import com.iti.gov.mashawery.home.view.MainActivity;
import com.iti.gov.mashawery.localStorage.SharedPref;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    DatabaseReference reference;
    FirebaseDatabase fDatabase;
    ProgressDialog progressDialog;
    String userID;
    String email;
    String password;
    public static GoogleSignInClient mGoogleSignInClient;
    public  static GoogleSignInAccount account;
    int RC_SIGN_IN = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        ActivityLoginBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        fAuth=FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        SignInButton signInButton =binding.googleSignInButton;
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        SharedPref.createPrefObject(LoginActivity.this);
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
                                       startActivity(intent);
                                      // progressDialog.dismiss();
                                       SharedPref.setLogin(true);
                                       SharedPref.setLoginWithFirebase(true);
                                       SharedPref.setRegisterWithFirebase(true);
                                       SharedPref.setUserEmail(email);
                                       SharedPref.setUserId(userID);
                                       Log.e("le",email);
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
               finish();

           }
       });
    }
    @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void updateUI(GoogleSignInAccount account){

        if (account != null){
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
            updateUI(account);
        } catch (ApiException e) {
            updateUI(null);
        }
    }



}