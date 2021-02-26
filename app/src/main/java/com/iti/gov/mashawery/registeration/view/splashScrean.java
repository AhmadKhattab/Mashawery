package com.iti.gov.mashawery.registeration.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.home.view.MainActivity;
import com.iti.gov.mashawery.localStorage.SharedPref;

public class splashScrean extends AppCompatActivity {
    private static int splash = 3000;

    Animation top , buttom;
    ImageView logo;
    TextView app , des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screan2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        top  = AnimationUtils.loadAnimation(splashScrean.this,R.anim.top_anim);
        buttom = AnimationUtils.loadAnimation(splashScrean.this,R.anim.buttom_anim);
        logo = findViewById(R.id.icon);
        app = findViewById(R.id.AppName);
        des = findViewById(R.id.appDes)   ;
        logo.setAnimation(top);
        logo.setAnimation(buttom);
        app.setAnimation(buttom);
        des.setAnimation(top);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPref.createPrefObject(splashScrean.this);
                boolean isLogin =SharedPref.checkLogin();
                //
                if(isLogin) {
                    Intent intent = new Intent(splashScrean.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(splashScrean.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

            }}
        },splash);

    }
}