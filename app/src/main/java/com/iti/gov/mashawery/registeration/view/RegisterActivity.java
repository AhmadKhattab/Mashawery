package com.iti.gov.mashawery.registeration.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.databinding.ActivityRegisterBinding;

import com.iti.gov.mashawery.home.view.MainActivity;

import com.iti.gov.mashawery.localStorage.SharedPref;
import com.iti.gov.mashawery.model.User;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseDatabase fDatabase;
    DatabaseReference reference;
    ProgressDialog progressDialog;
    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_register);
        fAuth=FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        binding.textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();

            }
        });

        binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String userName=  binding.editUsernameSignupSignUp.getText().toString();
                String email =  binding.editEmailSignupSignUp.getText().toString();
                String pass =   binding.editPasswordSignUp.getText().toString();
                String confirmPass =  binding.editConfirmPasswordSignUp.getText().toString();
                if (TextUtils.isEmpty(userName)){
                    binding.editUsernameSignupSignUp.setError("userName is Required.");
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    binding.editEmailSignupSignUp.setError("email is Required.");
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    binding.editPasswordSignUp.setError("Password is Required.");
                    return;
                }
                if (TextUtils.isEmpty(confirmPass)){
                    binding.editConfirmPasswordSignUp.setError("confirmPassword is Required.");
                    return;
                }
                if (!pass.equals(confirmPass)){
                  binding.editConfirmPasswordSignUp.setError("ConfirmPassword not Equal Password");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       // progressDialog.show();
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User Created!", Toast.LENGTH_LONG).show();
                            userID = fAuth.getCurrentUser().getUid();
                            reference =fDatabase.getReference().child("users").child(userID);
                            User userData = new User(userName,email,pass);
                            reference.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    progressDialog.dismiss();
                                        SharedPref.setRegisterWithFirebase(true);
                                    finish();

                                } }
                            });



                        }else {
                            Toast.makeText(RegisterActivity.this, "Error!"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            //progressDialog.dismiss();
                        }
                    }
                });

            }
        });




    }
    @Override
    protected void onStart() {
        super.onStart();
      /*  SharedPref.createPrefObject(RegisterActivity.this);
        boolean isLogin =SharedPref.checkLogin();

        if(isLogin) {
           // Intent intent = new Intent(this, MainActivity.class);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
           /* Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();*/
        }
    }

