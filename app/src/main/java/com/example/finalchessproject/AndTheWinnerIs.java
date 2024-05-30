package com.example.finalchessproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AndTheWinnerIs extends AppCompatActivity {

    // Declare views and buttons
    ImageView blackWonImage, whiteWonImage, drawImage;
    TextView theWinnerText, playingText, byText ,drawText;

    Button restartBtn, quitBtn, returnBtn;

    Handler handler;

    // Animation constants
    private static final int NUM_FRAMES = 30;
    private static final int FRAME_DURATION_MS = 150;

    private static int currentFrameIndex = 1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_and_the_winner_is);
        Intent intent = getIntent();

        // Initialize views and buttons
        byText = findViewById(R.id.By);
        theWinnerText = findViewById(R.id.WinnerText);
        playingText = findViewById(R.id.Playing);
        blackWonImage = findViewById(R.id.BlackWonImage);
        whiteWonImage = findViewById(R.id.WhiteWonImage);
        drawImage = findViewById(R.id.DrawImage);

        quitBtn = findViewById(R.id.QuitButton);
        restartBtn = findViewById(R.id.RestartButton);
        returnBtn = findViewById(R.id.ReturnButton);

        // Set button click listeners
        returnBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(AndTheWinnerIs.this, Introduction.class);
            startActivity(intent1);
        });
        restartBtn.setOnClickListener(v -> {
            finish();
            System.exit(0);
        });

        quitBtn.setOnClickListener(v -> finishAffinity());

        if (intent.getStringExtra("Winner").equals("Draw"))
        {
            theWinnerText.setText("Draw");
            playingText.setText("");
            byText.setText("");
            drawImage.setVisibility(View.VISIBLE);
        }
        else
        {
            // Set text and images based on intent extras
            byText.setText("By: " + intent.getStringExtra("Way") + " !");
            theWinnerText.setText("The Winner is:\n" + intent.getStringExtra("Winner") + " !");
            if (Objects.equals(intent.getStringExtra("WinnerColor"), "white")) {
                whiteWonImage.setVisibility(View.VISIBLE);
            } else {
                blackWonImage.setVisibility(View.VISIBLE);
            }
        }
        handler = new Handler();

        // Start the animation loop
        startAnimation();
    }

    // Method to start the animation loop
    private void startAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setImage(R.id.imageView1);
                setImage(R.id.imageView2);
                setImage(R.id.imageView3);
                setImage(R.id.imageView4);
                setImage(R.id.imageView5);
                setImage(R.id.imageView8);
                setImage(R.id.imageView9);
                setImage(R.id.imageView10);
                setImage(R.id.imageView11);
                setImage(R.id.imageView12);

                currentFrameIndex++;

                if (currentFrameIndex > NUM_FRAMES) {
                    currentFrameIndex = 1;
                }

                handler.postDelayed(this, FRAME_DURATION_MS);
            }
        }, FRAME_DURATION_MS);
    }

    // Method to set the image for the animation
    private void setImage(int imageViewId) {
        ImageView imageView = findViewById(imageViewId);
        String drawableName = "star_anim_" + currentFrameIndex;
        @SuppressLint("DiscouragedApi") int resId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        imageView.setImageResource(resId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnimation();
    }
}
