package com.example.finalchessproject;

import android.bluetooth.le.ScanSettings;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Square {
    float x,y,w,h;
    int i,j;
    Paint p;
    public Square(float x, float y,float w,float h,int i, int j){
        this.x = x;
        this.y = y;
        p = new Paint();
        this.w = w;
        this.h = h;
        this.i = i;
        this.j = j;
    }

    public void drawBlack(Canvas canvas){
        p.setStyle((Paint.Style.FILL));
        p.setColor(Color.rgb(137,207,240));
        canvas.drawRect(x,y,x+w,y+h,p);
    }
    public void drawWhite(Canvas canvas){
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.rgb(255,253,230));
        canvas.drawRect(x,y,x+w,y+h,p);
    }
}
