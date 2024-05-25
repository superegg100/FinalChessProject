package com.example.finalchessproject;

import android.widget.ImageView;

public class CheckAlert {
    public ImageView checkIcon;

    public CheckAlert(ImageView imageView)
    {
        imageView.setImageResource(R.drawable.check_icon);
        this.checkIcon = imageView;
    }
    public ImageView getAlertIcon() {
        return this.checkIcon;
    }
}
