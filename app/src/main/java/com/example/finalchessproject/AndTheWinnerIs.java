package com.example.finalchessproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AndTheWinnerIs extends AppCompatActivity {

    ImageView blackWonImage, whiteWonImage;
    TextView theWinnerText, playingText, byText;

    Button restartBtn, quitBtn, returnBtn;

    Handler handler;

    private static final int NUM_FRAMES = 30; // Number of frames in the animation
    private static final int FRAME_DURATION_MS = 150; // Duration between frames in milliseconds

    private static int currentFrameIndex = 1; // Start from the first frame

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_and_the_winner_is);
        Intent intent = getIntent();

        byText = findViewById(R.id.By);
        theWinnerText = findViewById(R.id.WinnerText);
        playingText = findViewById(R.id.Playing);
        blackWonImage = findViewById(R.id.BlackWonImage);
        whiteWonImage = findViewById(R.id.WhiteWonImage);

        quitBtn = findViewById(R.id.QuitButton);
        restartBtn = findViewById(R.id.RestartButton);
        returnBtn = findViewById(R.id.ReturnButton);

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AndTheWinnerIs.this, Introduction.class);
                startActivity(intent);
            }
        });
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        byText.setText("By: " + intent.getStringExtra("Way") + " !");
        theWinnerText.setText("The Winner is:\n" + intent.getStringExtra("Winner") + " !");
        if (intent.getStringExtra("WinnerColor").equals("white")) {
            whiteWonImage.setVisibility(View.VISIBLE);
        } else {
            blackWonImage.setVisibility(View.VISIBLE);
        }

        handler = new Handler();

        // Start the animation loop
        startAnimation();
    }

    private void startAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Update image frames in your animation
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

                // Move to the next frame
                currentFrameIndex++;

                // Reset to the first frame if it exceeds the maximum frames
                if (currentFrameIndex > NUM_FRAMES) {
                    currentFrameIndex = 1;
                }

                // Continue the animation loop
                handler.postDelayed(this, FRAME_DURATION_MS);
            }
        }, FRAME_DURATION_MS);
    }

    private void setImage(int imageViewId) {
        ImageView imageView = findViewById(imageViewId);
        String drawableName = "star_anim_" + currentFrameIndex;
        int resId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        imageView.setImageResource(resId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause animation if needed
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume animation if needed
        startAnimation();
    }
}
