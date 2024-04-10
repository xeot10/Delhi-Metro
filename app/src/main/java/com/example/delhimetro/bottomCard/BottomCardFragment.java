package com.example.delhimetro.bottomCard;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delhimetro.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;


public class BottomCardFragment {
    CardView bottomSheet;
    protected TextView fromtoTextView;
    protected TextView timeTaken;
    protected TextView farePrice;
    RecyclerView recyclerView;

    public BottomCardFragment(CardView bottomSheet, int screenHeight, Context context) {
        this.bottomSheet = bottomSheet;
        setBottomSheetBehavior(screenHeight);
        fromtoTextView = bottomSheet.findViewById(R.id.fromtotext);
        timeTaken = bottomSheet.findViewById(R.id.time);
        farePrice = bottomSheet.findViewById(R.id.farePrice);
        recyclerView = bottomSheet.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        addDividerLine(context);
    }
    public boolean setConstData(String f2t, String time, String price){
        this.fromtoTextView.setText(f2t);
        this.timeTaken.setText(time);
        this.farePrice.setText(price);
        return true;
    }
    private boolean setRecyclerViewData(List<CardDetail.RouteView> recyclerViewData){
        StationAdapter adapter = new StationAdapter(recyclerViewData);
        recyclerView.setAdapter(adapter);
        return true;
    }
    public void setAllData(){
        CardDetail cd = CardDetail.getInstance();
        List<CardDetail.RouteView> routeViewList = cd.routeCardList;
        setConstData(cd.f2t, cd.time, cd.fare);
        setRecyclerViewData(routeViewList);

    }
    private void setBottomSheetBehavior(int screenHeight){
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setPeekHeight((int)(screenHeight * 0.2));
        bottomSheetBehavior.setFitToContents(false);
        bottomSheetBehavior.setHalfExpandedRatio(0.83f);
    }
    private void addDividerLine(Context mContext){
        RecyclerView recyclerView = bottomSheet.findViewById(R.id.recyclerView);
        DottedDividerItemDecoration itemDecoration = new DottedDividerItemDecoration(mContext, R.dimen.divider_height, Color.rgb(223, 225, 226)); // Replace R.drawable.divider with your divider drawable
        recyclerView.addItemDecoration(itemDecoration);
    }
}
