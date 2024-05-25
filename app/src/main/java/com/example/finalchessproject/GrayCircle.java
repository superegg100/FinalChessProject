package com.example.finalchessproject;

import android.widget.ImageView;

public class GrayCircle {
    public ImageView grayCircle;

    public GrayCircle(ImageView imageView)
    {
        imageView.setImageResource(R.drawable.star_anim_1);
        this.grayCircle = imageView;
    }
    public ImageView getGrayCircle() {
        return this.grayCircle;
    }
}
