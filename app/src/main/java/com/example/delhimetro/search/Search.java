package com.example.delhimetro.search;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.delhimetro.MainActivity;
import com.example.delhimetro.R;
import com.example.delhimetro.bottomCard.BottomCardFragment;
import com.example.delhimetro.map.MapDb;
import com.example.delhimetro.map.MetroMap;


public class Search {
    private Context context;
    int screenHeight;
    private InputMethodManager imm;
    public CoordinatorLayout screen;
    private String fromStation;
    private String toStation;
    public CardView bottomSheet;
    private LinearLayout fromsearchlayout;
    private AutoCompleteTextView fromStationView;
    private AutoCompleteTextView toStationView;
    private MapDb mapDb;
    MetroMap metroMap;
    private String[] stationList;
    public Search(Context context, CoordinatorLayout screen, int height, InputMethodManager imm) {
        this.context = context;
        this.screen = screen;
        this.fromsearchlayout = screen.findViewById(R.id.fromsearchlayout);
        this.fromStationView = screen.findViewById(R.id.searchFrom);
        this.toStationView = screen.findViewById(R.id.searchTO);
        this.bottomSheet = screen.findViewById(R.id.bottomSheet);
        this.imm = imm;
        screenHeight = height;
        mapDb = MapDb.getInstance();
        metroMap = MetroMap.getInstance();
        stationList = mapDb.NameIdMap.keySet().toArray(new String[0]);
        CustomAutoCompleteAdapter adapter = new CustomAutoCompleteAdapter(context, android.R.layout.simple_dropdown_item_1line, stationList);
        fromStationView.setAdapter(adapter);
        toStationView.setAdapter(adapter);
        fromsearchlayout.setVisibility(View.GONE);
        bottomSheet.setVisibility(View.GONE);
        fromStation = "Peeragarhi"; // remove after the geolocation implementation
    }

    public void setItemListener(){

        fromStationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fromStation = (String) parent.getItemAtPosition(position);
                plotMap();
                hideKeyboard();
            }
        });
        toStationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toStation = (String) parent.getItemAtPosition(position);
                plotMap();
                hideKeyboard();
            }
        });
    }
    public void plotMap(){
        metroMap.setRoute(mapDb.NameIdMap.get(fromStation), mapDb.NameIdMap.get(toStation));
        MainActivity.redraw();
        fromsearchlayout.setVisibility(View.VISIBLE);
        bottomSheet.setVisibility(View.VISIBLE);
        BottomCardFragment bottomCardFragment = new BottomCardFragment(bottomSheet, screenHeight, context);
        bottomCardFragment.setAllData();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide the element after 5 seconds
                fromsearchlayout.setVisibility(View.GONE);
            }
        }, 7000);
    }
    private void addTextListeners(){
        fromStationView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();
            }
        });
        toStationView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();
            }
        });

    }
    private void toggleKeyboardVisibility(View autoCompleteTextView) {
        if (imm != null) {
            if (autoCompleteTextView.hasFocus()) {
                // Hide the keyboard
                imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
            } else {
                // Show the keyboard
                imm.showSoftInput(autoCompleteTextView, InputMethodManager.SHOW_IMPLICIT);
                autoCompleteTextView.requestFocus();
            }
        }
    }

    private void setKeyboardListener(){
        fromStationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleKeyboardVisibility(view);
            }
        });
        toStationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleKeyboardVisibility(view);
            }
        });
    }
    private void hideKeyboard() {
        if (imm != null) {
            imm.hideSoftInputFromWindow(toStationView.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(fromStationView.getWindowToken(), 0);
        }
    }


}
