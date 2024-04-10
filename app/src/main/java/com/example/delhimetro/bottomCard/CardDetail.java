package com.example.delhimetro.bottomCard;

import android.graphics.Paint;

import com.example.delhimetro.map.Line;
import com.example.delhimetro.map.MapDb;
import com.example.delhimetro.map.MetroMap;

import java.util.ArrayList;

public class CardDetail {
    String fare;
    public String time;
    String From, To;
    public ArrayList<RouteView> routeCardList;
    public String f2t;

    private CardDetail(){
        map = MetroMap.getInstance();
        db = MapDb.getInstance();
    }

    private MetroMap map;
    private MapDb db;
    private static CardDetail mCard;

    public static CardDetail getInstance(){
        if(mCard==null){
            mCard = new CardDetail();
        }
        return mCard;
    }

    public String getFromTo(){
        return f2t;
    }

    public void generateCurrentDetails(){
        int n = map.route.size();
        f2t = db.allStations[map.route.get(0)].name +"  --  "+db.allStations[map.route.get(n-1)].name;
        routeCardList = new ArrayList<>();
        Line line = db.allLineMap.get(db.getLineId(map.route.get(0), map.route.get(1)));
        int pcol = line.colorHash;
        RouteView rv = new RouteView();
        rv.FromStation = db.allStations[map.route.get(0)].name;
        rv.Platform = db.getPlatform(map.route.get(0),map.route.get(1));
        rv.lineColor = line.defaultColor;
        for(int i=2;i<n;i++){
            line = db.allLineMap.get(db.getLineId(map.route.get(i-1),map.route.get(i)));
            if(line.colorHash!=pcol){
                pcol = line.colorHash;
                rv.ToStaion = db.allStations[map.route.get(i-1)].name;
                routeCardList.add(rv);
                rv = new RouteView();
                rv.FromStation = db.allStations[map.route.get(i-1)].name;
                rv.Platform = db.getPlatform(map.route.get(i-1),map.route.get(i));
                rv.lineColor = line.defaultColor;
            }
        }
        rv.ToStaion = db.allStations[map.route.get(n-1)].name;
        routeCardList.add(rv);
        int t = map.routeTime;
        int h=0,m=0;

        m=t/60;
        if(t%60!=0){
            m+=1;
        }
        h = m/60;
        m=m%60;
        if(m==1){
            m+=1;
        }
        if(h==0){
            time = String.valueOf(m)+" mins";
        } else {
            time = String.valueOf(h)+"hrs "+String.valueOf(m)+" mins";
        }
        fare = String.valueOf(calcualteCost(t,n,routeCardList.size())) + " rs";
    }

    private int calcualteCost(int totalTime, int totalStations, int interChange){
        int res = 30;
        return res;
    }

    public class RouteView{
        String FromStation;
        String ToStaion;
        String Platform;
        Paint lineColor;
    }
}
