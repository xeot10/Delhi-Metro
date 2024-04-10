package com.example.delhimetro.bottomCard;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delhimetro.R;

import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {

    private List<CardDetail.RouteView> routeViewList;


    public StationAdapter(List<CardDetail.RouteView> routeViewList) {
        this.routeViewList = routeViewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.station_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardDetail.RouteView routeView = routeViewList.get(position);
        holder.fromStationName.setText(routeView.FromStation);
        //holder.dotsLinearLayout.getChildAt(0).setBackgroundResource(R.drawable.dot_active);
        holder.toStationName.setText(routeView.ToStaion);
        holder.platformNumber.setText(routeView.Platform);
        int color = routeView.lineColor.getColor();
        ColorStateList colorStateList = ColorStateList.valueOf(color);
//        holder.toStationName.setTextColor(color);
        holder.platformNumber.setTextColor(color);
        holder.dotToStation.setBackgroundTintList(colorStateList);
        holder.dotFromStation.setBackgroundTintList(colorStateList);
        holder.line.setBackgroundTintList(colorStateList);
    }

    @Override
    public int getItemCount() {
        return routeViewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fromStationName;
        public TextView toStationName;
        public TextView platformNumber;
        public View dotFromStation;
        public View dotToStation;
        public View line;
        public LinearLayout dotsLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            fromStationName = itemView.findViewById(R.id.fromStationName);
            toStationName = itemView.findViewById(R.id.toStationName);
            platformNumber = itemView.findViewById(R.id.platformNumber);
            dotFromStation = itemView.findViewById(R.id.dotFromStation);
            dotToStation = itemView.findViewById(R.id.dotToStation);
            line = itemView.findViewById(R.id.line);
//            dotsLinearLayout = itemView.findViewById(R.id.dotsLinearLayout);
        }
    }
}
