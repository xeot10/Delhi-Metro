package com.example.delhimetro.map;

import android.graphics.Color;

import java.sql.Time;
import java.util.List;

import android.graphics.Paint;
import android.graphics.PointF;

public class Station {
    public float x;
    public float y;
    public String name;
    public Paint paint;
    Time firstTime,lastTIme;
    public int id = -1;

    public boolean isInterSection = false;



    public Station(int id,float x, float y, String color, boolean interchange, String name) {

        String[] arr = color.split(",");
        this.x=x;
        this.y=y;
        this.paint = new Paint();
        this.paint.setColor(Color.rgb(Integer.parseInt(arr[0]),Integer.parseInt(arr[1]),Integer.parseInt(arr[2])));
        this.id = id;
        this.name = name;
        this.isInterSection = interchange;
    }

    public void setTiming(Time t1,Time t2){
        this.firstTime = t1;
        this.lastTIme = t2;
    }

}
