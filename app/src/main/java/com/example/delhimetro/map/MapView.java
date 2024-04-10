package com.example.delhimetro.map;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
//import org.dalol.rotate.detector.RotateGestureDetector;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
public class MapView extends View {
    private Matrix matrix;
    private float scaleFactor = .78f;
    private float minScaleFactor = 0.6f;
    private float maxScaleFactor = 4.0f;

    public static int col = (Color.rgb(230,244,230));
    private ScaleGestureDetector scaleGestureDetector;


    private float lastTouchX, lastTouchY;
    private int activePointerId = -1;

    private MetroMap metroMap;

    public MapView(Context context) {
        super(context);
        init();
        metroMap = MetroMap.getInstance();
        metroMap.loadDefaultMap();
        matrix.postTranslate(-1500*scaleFactor,-200*scaleFactor);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

//        setMeasuredDimension(20,20);
    }

    private void init() {
        matrix = new Matrix();
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    float fx=0f,fy=0f;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawColor(col);
        canvas.scale(scaleFactor, scaleFactor,fx,fy);
        canvas.concat(matrix);
        metroMap.loadMap(canvas);
        // m1.dbug translation matrix needed for google maps like functionallity
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        int pointerIndex;
        float x, y;


        // previous code
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                activePointerId = event.getPointerId(0);
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                pointerIndex = event.findPointerIndex(activePointerId);
                x = event.getX(pointerIndex);
                y = event.getY(pointerIndex);

                if (!scaleGestureDetector.isInProgress()) {
                    float dx = x - lastTouchX;
                    float dy = y - lastTouchY;

                    matrix.postTranslate(dx / scaleFactor, dy / scaleFactor);

                    invalidate();
                }

                lastTouchX = x;
                lastTouchY = y;
                break;
//         new code for mapVIew check
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                activePointerId = -1;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                pointerIndex = event.getActionIndex();
                x = event.getX(pointerIndex);
                y = event.getY(pointerIndex);
                lastTouchX = x;
                lastTouchY = y;
                activePointerId = event.getPointerId(pointerIndex);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                pointerIndex = event.getActionIndex();
                int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == activePointerId) {
                    // The pointer that went up is the one we were tracking, choose another one
                    int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    lastTouchX = event.getX(newPointerIndex);
                    lastTouchY = event.getY(newPointerIndex);
                    activePointerId = event.getPointerId(newPointerIndex);
                }
                break;
        }
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactorNew = scaleFactor * detector.getScaleFactor();
            scaleFactorNew = Math.max(minScaleFactor, Math.min(scaleFactorNew, maxScaleFactor));
            scaleFactor = scaleFactorNew;

            // Calculate the scaling pivot point
            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();
            fx = focusX;
            fy = focusY;
            // m1.dbug zoom to matrix then concat
            matrix.postScale(scaleFactorNew / scaleFactor, scaleFactorNew / scaleFactor, focusX, focusY);
            invalidate();
            return true;
        }
    }






//    private class  RotateListner implements RotateGestureDetector

}
