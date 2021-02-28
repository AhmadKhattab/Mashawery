package com.iti.gov.mashawery.helpPackag;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.localStorage.SharedPref;
import com.iti.gov.mashawery.model.Note;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FloatingViewService extends Service {
    private WindowManager mWindowManager;
    private View mFloatingView;
    View collapsedView;
    View expandedView;
    RecyclerView mRecycler;
    FloatingNoteAdapter adapter;
    public int id;
    private List<Note> notesList = new ArrayList<>();

    public FloatingViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String note = intent.getStringExtra("tripList");
        Log.i("IBinder", String.valueOf(id));
        SharedPref.createPrefObject(getApplicationContext());
        String notesString = SharedPref.getFloatingNotes();
        Log.i("notes from service", notesString);
        if (note!= null && !note.equals("") ) {
            Type collectionType = new TypeToken<List<Note>>() {
            }.getType();
            notesList = new Gson()
                    .fromJson(note, collectionType);
          //Log.i("notesFromService", notesList.get(0).getTitle());

        }

      //  mRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        if (notesList !=null) {
            Log.i("before Adapter" ,"hello from before adapter");
            adapter = new FloatingNoteAdapter(this, notesList);
            mRecycler.setAdapter(adapter);
            adapter.setNotes(notesList);
            Log.i("after Adapter" ,"hello from after adapter");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

       params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 100;
        params.y = 100;


        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);


        collapsedView = mFloatingView.findViewById(R.id.collapse_view);

        expandedView = mFloatingView.findViewById(R.id.expanded_container);
        mRecycler = mFloatingView.findViewById(R.id.notesRecyclerView);


        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                      /*  initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;*/
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                      /*  int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {*/
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                         //   }
                       // }
                        return true;
                    case MotionEvent.ACTION_MOVE:

                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });

        ImageView closeButton = (ImageView) mFloatingView.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* collapsedView.setVisibility(View.GONE);
                expandedView.setVisibility(View.GONE);*/
                stopSelf();
            }
        });

        ImageView viewClose = (ImageView) mFloatingView.findViewById(R.id.close_button);
        viewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);

            }
        });

    }


    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }


}

