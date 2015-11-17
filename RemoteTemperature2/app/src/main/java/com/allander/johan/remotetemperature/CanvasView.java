package com.allander.johan.remotetemperature;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.sql.Time;
import java.util.Date;

public class CanvasView extends View {

    private final static int MAX_VALUES = 400; // Total value points multiplied by 2.
    public int width;
    public int height;
    public float YMin;
    public float YMax;
//    private Bitmap mBitmap;
//    private Canvas mCanvas;
    Context context;
    private Paint mPaint,pPaint,glowPaint;
    float valuePoints[] = new float[MAX_VALUES];
    Date datePoint[] = new Date[MAX_VALUES/2];
    Time timePoint[] = new Time[MAX_VALUES/2];
    float tempPoint[] = new float[MAX_VALUES/2];
    int nextValuePoint = 0;
    float marginal = 40.0f;
    float marking = 10.0f;
    int touchValuePoint;
    private float[] markLines;


    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        // mPaint for drawing the fram for the diagram
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#c6c6c6"));
        mPaint.setStrokeWidth(2f);
        mPaint.setTextSize((marginal*3)/4);

        pPaint = new Paint();
        pPaint.setAntiAlias(true);
        pPaint.setColor(Color.RED);
        pPaint.setStyle(Paint.Style.STROKE);
        pPaint.setStrokeJoin(Paint.Join.ROUND);
        pPaint.setStrokeWidth(8f);

        glowPaint = new Paint();
        glowPaint.setAntiAlias(true);
        glowPaint.setColor(Color.GREEN);
        glowPaint.setStyle(Paint.Style.STROKE);
        glowPaint.setStrokeJoin(Paint.Join.ROUND);
        glowPaint.setStrokeWidth(10f);
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
 //       mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
  //      mCanvas = new Canvas(mBitmap);
    }

    private void calculateGraphic(Canvas canvas) {

        markLines = new float[10*4+5*4]; // 10 i x-led, 5 i y-led alla 4st coorinater stora
        int a;
        // markings in x-line
        for(a=0;a<(10*4);a+=4) {
            // Add one marking line X for every loop
            markLines[a] = marginal + a*((canvas.getWidth()-marginal*2)/40);
            markLines[a+1] = canvas.getHeight()-marginal;
            markLines[a+2] = marginal + a*((canvas.getWidth()-marginal*2)/40);
            markLines[a+3] = canvas.getHeight()-marginal-marking;
        }
        // markings in x-line
        for(a=40;a<(10*4 + 5*4);a+=4) {
            // Add one marking line X for every loop
            markLines[a] = marginal;
            markLines[a+1] = canvas.getHeight()-marginal-(a-40)*((canvas.getHeight()-marginal*2)/20);
            markLines[a+2] = marginal+marking;
            markLines[a+3] = canvas.getHeight()-marginal-(a-40)*((canvas.getHeight()-marginal*2)/20);
        }
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float lines[] = {
                marginal, canvas.getHeight()-marginal, canvas.getWidth()-marginal, canvas.getHeight()-marginal,
                marginal, canvas.getHeight()-marginal,marginal, marginal,
                marginal, marginal, marginal+5,marginal+10,
                marginal, marginal, marginal-5,marginal+10,
                canvas.getWidth()-marginal, canvas.getHeight()-marginal, canvas.getWidth()-marginal-10,canvas.getHeight()-marginal-5,
                canvas.getWidth()-marginal, canvas.getHeight()-marginal, canvas.getWidth()-marginal-10,canvas.getHeight()-marginal+5,
        };
        width = canvas.getWidth();
        height = canvas.getHeight();

        calculateGraphic(canvas);

        // Draw diagram base
        canvas.drawLines(markLines, 0, 60, mPaint);
        canvas.drawLines(lines, 0, 24, mPaint);


        // Draw temperature points
        canvas.drawPoints(valuePoints, 0, nextValuePoint, pPaint);
        canvas.drawPoint(valuePoints[touchValuePoint], valuePoints[touchValuePoint + 1], glowPaint);

        // Draw temperature text
        if(nextValuePoint!=0) {
            String s = datePoint[touchValuePoint/2].toString() + " - " + timePoint[touchValuePoint/2].toString() + " -> " + tempPoint[touchValuePoint/2]  + "°C";
            canvas.drawText(s, marginal, canvas.getHeight(), mPaint);
        } else {
            String s = "Touch chart to get a specific time and temperature";
            canvas.drawText(s, marginal, canvas.getHeight(), mPaint);
        }

        // Draw lines between graph points
        if(nextValuePoint>0) {
            if((nextValuePoint%4) == 0) canvas.drawLines(valuePoints, 0, nextValuePoint, mPaint);
            else canvas.drawLines(valuePoints, 0, nextValuePoint-2, mPaint);

            if(((nextValuePoint-2)%4) == 0) canvas.drawLines(valuePoints, 2, nextValuePoint-2, mPaint);
            else canvas.drawLines(valuePoints, 2, nextValuePoint-4, mPaint);

        }
    }

    public void setYSize(float maxY, float minY) {
        YMax = maxY;
        YMin = minY-((maxY-minY)/10); // move the lowest value up from the x-line
    }

    public void setPoint(float x, float y, int totalX, Date datum, float temp, Time time) {
//        if(temp==-127f) return; // If value is -127 there have been fault in reading temp value and is invalid
        if(nextValuePoint>=MAX_VALUES) return;
        valuePoints[nextValuePoint] = marginal + (x/totalX)*(width-(marginal*2)) + 20; // 10 is to move first value from the line
        valuePoints[nextValuePoint+1] = height-((y-YMin)/(YMax-YMin))*(height-marginal*2)-marginal;
        datePoint[nextValuePoint/2] = datum;
        timePoint[nextValuePoint/2] = time;
        tempPoint[nextValuePoint/2] = temp;
        nextValuePoint += 2;
        invalidate();
    }

    public void clearPoints() {
        nextValuePoint = 0;
    }


    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        int a;
        float x = event.getX();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                a = 0;
                while (valuePoints[a] < x && a < (nextValuePoint-2)) { a += 2; }
                touchValuePoint = a;
                invalidate();
                break;
        }
        return true;
    }
}