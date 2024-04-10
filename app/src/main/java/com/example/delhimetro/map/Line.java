package com.example.delhimetro.map;

import android.graphics.Color;
import android.graphics.Paint;

public class Line {
    public float x1,y1,x2,y2;
    public Paint defaultColor;
    public int colorHash = 0;

    public String platformFromTo=null,platformToFrom=null;
    public Line(float x1,float y1, float x2, float y2,String pt,String plt,String pltOpp){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        if(pt!=null){
            String[] ptcol = pt.split(",");
            Paint p = new Paint();
            int r,g,b;
            r = Integer.parseInt(ptcol[0]);
            g = Integer.parseInt(ptcol[1]);
            b = Integer.parseInt(ptcol[2]);
            p.setColor(Color.rgb(r,g,b));
            colorHash = r*g+b*3;
            p.setStrokeWidth(18);
            defaultColor = p;
        }
        platformFromTo=plt;
        platformToFrom=pltOpp;
    }
}
