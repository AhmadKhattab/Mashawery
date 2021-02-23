package com.iti.gov.mashawery.home.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iti.gov.mashawery.Profile.ProfileFragment;
import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.helpPackag.ViewPagerAdapter;

public class MaineActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private int oldItemId;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    FloatingActionButton mainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maine);
        init();


    }
    private void init(){

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        viewPager = findViewById(R.id.pager);
        viewPager.setUserInputEnabled(false);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        viewPagerAdapter.addFragment(new ProfileFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        buttonClicked();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Log.i("BottomNavigationView", String.valueOf(item.getItemId()));
        Log.i("BottomNavigationView", String.valueOf(oldItemId));
        oldItemId = item.getItemId();

        switch (item.getItemId()) {

            case R.id.nave_profile:
                viewPager.setCurrentItem(1);
                return true;
        }

        return  false;
    };


    private void buttonClicked(){
       // mainButton.setOnClickListener(v -> startActivity(new Intent(MaineActivity.this, AddTripActivity.class)));
    }
}