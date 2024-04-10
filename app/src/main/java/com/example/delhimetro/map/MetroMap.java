package com.example.delhimetro.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.delhimetro.bottomCard.CardDetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class MetroMap {

    private Paint white = new Paint();
    private  Paint black = new Paint();
    private  Paint darkGrey = new Paint();
    private Paint grey  = new Paint();

    private CardDetail mCard;
    private static boolean isDefaultState = false;
    public static int totalStations = 241;
    public static int totalLines = 258;

    public int  routeTime =0;

    public static Paint innerPaint = new Paint();
    public static Paint labelColor = new Paint();

    private int currRoot =0;

    public ArrayList<Integer> route = new ArrayList<>();

    MapDb db;
    private MetroMap(){
        darkGrey.setColor(Color.DKGRAY);
        grey.setColor(Color.GRAY);
        grey.setStrokeWidth(18);
        white.setColor(Color.rgb(235,237,225));
        black.setColor(Color.rgb(0,0,0));
        black.setStrokeWidth(15f*factor);
        black.setTextSize(7.2f*factor);
        labelColor.setTextSize(7.6f*factor);
        labelColor.setFontFeatureSettings(darkGrey.getFontFeatureSettings());
        labelColor.setColor(Color.rgb(20,17,24));
        db = MapDb.getInstance();
    }
    private static MetroMap mmap = null;
    public float factor = 2.4f;
    public static MetroMap getInstance(){
        if(mmap==null){
            mmap = new MetroMap();
        }
        return mmap;
    }

    public void loadDefaultMap(){
        isDefaultState = true;
    }
    public void loadMap(Canvas canvas){
        if(isDefaultState) {
            loadmap(canvas,true);
            drawLabels(canvas);
        } else {
            loadRoutedMap(canvas);
        }
    }
    public void loadRoutedMap(Canvas canvas){
       loadmap(canvas,false);
        float x,y;
        int n = route.size();
        Line l;
        for(int i=1;i<n;i++){
            int id = db.getLineId(route.get(i),route.get(i-1));
            l=db.allLineMap.get(id);
            canvas.drawLine(factor*l.x1,factor*l.y1,factor*l.x2,factor*l.y2,l.defaultColor);
        }
        for(int i=0;i<n;i++){
            int p = route.get(i);
            x=factor*db.allStations[p].x;
            y=factor*db.allStations[p].y;

            if(db.allStations[p].isInterSection){
                x+=factor*5f;
                y+=factor*10f;
                canvas.drawCircle(x,y,24,db.allStations[p].paint);
                canvas.drawCircle(x,y,17,innerPaint);
                continue;
            }
            canvas.drawCircle(x,y,19,db.allStations[p].paint);
            canvas.drawCircle(x,y,12,innerPaint);
        }
        drawLabels(canvas);
    }

    private void loadmap(Canvas canvas, boolean defaultFlag){
        Paint paint;
        paint = grey;
        for(Line line:db.allLineMap.values()){
            if(defaultFlag){
                paint = line.defaultColor;
            }
            canvas.drawLine(factor*line.x1,factor*line.y1,factor*line.x2,factor*line.y2,paint);
        }
        float x,y;
        paint = grey;
        for(int i=1;i<=totalStations;i++){
            x=factor*db.allStations[i].x;
            y=factor*db.allStations[i].y;
            if(defaultFlag){
                paint = db.allStations[i].paint;
            }
            if(db.allStations[i].isInterSection){
                x+=factor*5f;
                y+=factor*10f;
                canvas.drawCircle(x,y,24,paint);
                canvas.drawCircle(x,y,17,innerPaint);
                continue;
            }
            canvas.drawCircle(x,y,19,paint);
            canvas.drawCircle(x,y,12,innerPaint);
        }
    }

    private void drawLabels(Canvas canvas){

        float x,y;
        float textsize;
        for(int i=1;i<db.labelArr.length;i++){
            Label lb = db.labelArr[i];
            x=lb.x;
            y =lb.y;
            x*=factor;
            y*=factor;
            int sz = Math.min(2,lb.name.length);
            textsize = black.measureText(lb.name[0]);
            if(lb.anchorType==3) {
                x-=textsize+factor*9.4f;
            } else if(lb.anchorType==2){
                x-=(textsize/2)-factor*3.3f;
            } else{
                x+=factor*4;
            }
            if(lb.baseType==1){
                y-=6.1f*factor;
            } else if(lb.baseType==3){
                y+=7.4f*factor;
            }
            for(int j=0;j<sz;j++){
                y-=(sz-1-j)*factor;
                y+=j*factor*8;
                canvas.drawText(lb.name[j],x,y,labelColor);
            }
        }
    }



    private  ArrayList<Integer> findShortestPath(int source, int destination) {
        int N = db.connections.length;
        int[] cost = new int[N]; // singifies time as cost
        int[] previous = new int[N]; // to create metroMap arrayList of route
        Arrays.fill(cost, Integer.MAX_VALUE);
        Arrays.fill(previous, -1);

        cost[source] = 0;

        PriorityQueue<Integer> minHeap = new PriorityQueue<>(Comparator.comparingInt(node -> cost[node]));
        minHeap.add(source);
        int val=-1,lid,colHash;
        while (!minHeap.isEmpty()) {
            int current = minHeap.poll();
            int idpre = previous[current];
            if(idpre==-1){
                val = -1;
            } else {
                val = db.allLineMap.get(db.getLineId(idpre,current)).colorHash;
            }
            for (int neighbor = 0; neighbor < N; neighbor++) {
                int weight = db.connections[current][neighbor];
                if (weight > 0) {
                    int currTime = cost[current] + weight;
                    lid = db.getLineId(current,neighbor);
                    colHash = db.allLineMap.get(lid).colorHash;
                    if(val!=-1 && val!=colHash){
                        currTime+=db.interChangeTime;
                    }
                    if (currTime < cost[neighbor]) {
                        cost[neighbor] = currTime;
                        previous[neighbor] = current;
                        minHeap.add(neighbor);
                    }
                }
            }
        }

        ArrayList<Integer> shortestPath = new ArrayList<>();
        int current = destination;

        while (current != -1) {
            shortestPath.add(current);
            current = previous[current];
        }
        routeTime = cost[destination];
        Collections.reverse(shortestPath);
        return shortestPath;
    }


    public void setRoute(int from , int to){
        if(mCard==null){
            mCard = CardDetail.getInstance();
        }
        isDefaultState = false;
        int reqRoot = Math.min(from,to)*300+Math.max(to,from);
        if(reqRoot==currRoot){
            return;
        }
        try{
            route = findShortestPath(from,to);
            currRoot = reqRoot;
            mCard.generateCurrentDetails();
        } catch(Exception e){
            e.printStackTrace();
        }
    }




}
