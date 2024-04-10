package com.example.delhimetro.map;

import android.graphics.PointF;

public class Label {
    String[] name;
    float x;
    float y;
    int anchorType;
    int baseType;
    public  Label(String[] names,float xrr,float yrr,int start,int base){
        x=xrr;
        y= yrr;
        name = names;
        anchorType = start;
        baseType = base;
    }
}
