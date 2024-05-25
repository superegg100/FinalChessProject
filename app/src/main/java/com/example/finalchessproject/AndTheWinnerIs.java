package com.example.finalchessproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AndTheWinnerIs extends AppCompatActivity {

    ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,imageView9,imageView10,imageView11,imageView12,imageView13,imageView14,imageView15,imageView16,imageView17,imageView18;
    Handler handler;
    Button restartBtn, quitBtn;
    ImageView blackWonImage, whiteWonImage;
    TextView theWinnerText, playingText, byText;

    private static final String DRAWABLE_FOLDER = "drawable/";
    private static final String ANIMATION_NAME = "star_anim_";
    private static final int NUM_FRAMES = 30; // Number of frames in the animation
    private static final int FRAME_DURATION_MS = 150; // Duration between frames in milliseconds

    private static int currentFrameIndex = 1; // Start from the first frame

    private MediaPlayerService mediaPlayerService;
    private boolean isServiceBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            mediaPlayerService = binder.getService();
            isServiceBound = true;

            // Start or resume music when the service is connected
            if (!mediaPlayerService.isMusicPlaying()) {
                mediaPlayerService.resumeMusic();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mediaPlayerService = null;
            isServiceBound = false;
        }
    };

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

        byText.setText("By: "+  intent.getStringExtra("Way") + " !");
        theWinnerText.setText("The Winner is:\n" + intent.getStringExtra("Winner") + " !");
        if (intent.getStringExtra("WinnerColor") == "white")
        {
            whiteWonImage.setVisibility(View.VISIBLE);
        }
        else
        {
            blackWonImage.setVisibility(View.VISIBLE);
        }
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);
        imageView9 = findViewById(R.id.imageView9);
        imageView10 = findViewById(R.id.imageView10);
        imageView11 = findViewById(R.id.imageView11);
        imageView12 = findViewById(R.id.imageView12);
        imageView13 = findViewById(R.id.imageView13);
        imageView14 = findViewById(R.id.imageView14);
        imageView15 = findViewById(R.id.imageView15);
        imageView16 = findViewById(R.id.imageView16);
        imageView17 = findViewById(R.id.imageView17);
        imageView18 = findViewById(R.id.imageView18);
        handler = new Handler();

        // Start the animation loop
        startAnimation();
    }

    private void startAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setImage(imageView1, currentFrameIndex);
                setImage(imageView2, currentFrameIndex);
                setImage(imageView3, currentFrameIndex);
                setImage(imageView4, currentFrameIndex);
                setImage(imageView5, currentFrameIndex);
                setImage(imageView6, currentFrameIndex);
                setImage(imageView7, currentFrameIndex);
                setImage(imageView8, currentFrameIndex);
                setImage(imageView9, currentFrameIndex);
                setImage(imageView10, currentFrameIndex);
                setImage(imageView11, currentFrameIndex);
                setImage(imageView12, currentFrameIndex);
                setImage(imageView13, currentFrameIndex);
                setImage(imageView14, currentFrameIndex);
                setImage(imageView15, currentFrameIndex);
                setImage(imageView16, currentFrameIndex);
                setImage(imageView17, currentFrameIndex);
                setImage(imageView18, currentFrameIndex);

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

    private void setImage(ImageView imageView, int frameIndex) {
        String imageName = DRAWABLE_FOLDER + ANIMATION_NAME + frameIndex;
        int imageResource = getResources().getIdentifier(imageName, "drawable", getPackageName());
        imageView.setImageResource(imageResource);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MediaPlayerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isServiceBound) {
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isServiceBound && mediaPlayerService != null && mediaPlayerService.isMusicPlaying()) {
            mediaPlayerService.pauseMusic();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isServiceBound && mediaPlayerService != null && !mediaPlayerService.isMusicPlaying()) {
            mediaPlayerService.resumeMusic();
        }
    }
}