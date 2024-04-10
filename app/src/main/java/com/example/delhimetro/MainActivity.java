package com.example.delhimetro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.UserManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.delhimetro.map.MapView;
import com.example.delhimetro.map.MetroMap;
import com.example.delhimetro.search.Search;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView fromStationView;
    private AutoCompleteTextView toStationView;


    private static final int IMMERSIVE_MODE_DELAY = 2000;
    public static MapView metroMapView;
    public CoordinatorLayout screen;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Map loading
        metroMapView = new MapView(this);
        LinearLayout layout = findViewById(R.id.myCustomView);
        layout.addView(metroMapView);

        // initialize the initialScreen
        screen = findViewById(R.id.screen);
        Search search = new Search(this, screen, getScreenHeight(), getInputManager());
        search.setItemListener();
        RequestConfiguration requestConfiguration = new RequestConfiguration.Builder()
                .setTestDeviceIds(Arrays.asList("8A52F228385F40A9042CEB716C8FE06A"))
                .build();
        MobileAds.initialize(this);
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.bringToFront();
        AdView cardViewAd = (AdView) findViewById(R.id.cardViewAd);
        AdRequest cardViewAdRequest = new AdRequest.Builder().build();
        cardViewAd.loadAd(cardViewAdRequest);
        cardViewAd.bringToFront();
        UserManager manager = getApplicationContext().getSystemService(UserManager.class);
        // m1.dbug systemUI translucent
        hideSystemUI();
//
//        // Set a delayed runnable to re-hide the UI after a few seconds
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hideSystemUI();
//            }
//        }, IMMERSIVE_MODE_DELAY);
        getWindow().setStatusBarColor(Color.argb(20,45,50,55));
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isDarkMode()){
            Toast.makeText(getApplicationContext(),"hello darkness",Toast.LENGTH_SHORT).show();
            MapView.col = Color.rgb(45,55,85);
            MetroMap.innerPaint.setColor(Color.rgb(46,56,87));
            MetroMap.labelColor.setColor(Color.rgb(250,250,250));
        } else {
            MapView.col= Color.rgb(237,247,242);
            MetroMap.innerPaint.setColor(Color.rgb(247,252,247));
//            MetroMap.labelColor.setColor(Color.rgb(1,1,1));
            int flags = getWindow().getDecorView().getSystemUiVisibility();
        }
    }

    private void hideSystemUI() {
        // Hide the navigation bar and make the status bar translucent
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );

    }


    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // When the activity gains focus, re-hide the UI
            hideSystemUI();
        }
    }

    public static void redraw(){
        if(metroMapView!=null){
            metroMapView.invalidate();
        }
    }
    private int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private boolean isDarkMode(){
        int curr = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        return curr== Configuration.UI_MODE_NIGHT_YES;
    }
    private InputMethodManager getInputManager() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm;
    }
}