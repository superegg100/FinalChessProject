package com.example.finalchessproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ManageTimer extends AppCompatActivity {

    ImageView blackImage_t, whiteImage_t;
    TextView blackNameText_t, whiteNameText_t;
    Button start_t, setBlack_t, setWhite_t, cancelWhite_t, cancelBlack_t, butttonReturnToWelcome;
    EditText blackNameInput_t, whiteNameInput_t;

    private long BlackTime, WhiteTime;

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
        setContentView(R.layout.activity_manage_timer);

        Intent intent = getIntent();
        String theNameOfBlackFromIntro = intent.getStringExtra("blackName");
        String theNameOfWhiteFromIntro = intent.getStringExtra("whiteName");

        blackImage_t = findViewById(R.id.BlackPickedImageTimer);
        whiteImage_t = findViewById(R.id.WhitePickedImageTimer);
        start_t = findViewById(R.id.TimerStart);
        setWhite_t = findViewById(R.id.SetWhiteTimerButton);
        setBlack_t = findViewById(R.id.SetBlackTimerButton);
        cancelWhite_t = findViewById(R.id.CancelWhiteTimerButton);
        cancelBlack_t = findViewById(R.id.CancelBlackTimerButton);
        blackNameInput_t = findViewById(R.id.BlackPiecesTimerInput);
        whiteNameInput_t = findViewById(R.id.WhitePiecesTimerInput);
        blackNameText_t = findViewById(R.id.BlackPiecesTimerText);
        whiteNameText_t = findViewById(R.id.WhitePiecesTimerText);
        butttonReturnToWelcome = findViewById(R.id.buttonReturnToWelcome);

        butttonReturnToWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setBlack_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNumeric(blackNameInput_t.getText().toString())){
                    if (Long.parseLong(blackNameInput_t.getText().toString()) > 40)
                    {
                        BlackTime = 40;
                        blackNameText_t.setText(formatTimeTimer(BlackTime * 60000));
                        Toast.makeText(ManageTimer.this, "Max time is 40 minutes !", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        BlackTime = Long.parseLong(blackNameInput_t.getText().toString());
                        blackNameText_t.setText(formatTimeTimer(BlackTime * 60000));
                    }
                    blackNameText_t.setGravity(Gravity.CENTER);
                    setBlack_t.setClickable(false);
                    setBlack_t.setVisibility(View.INVISIBLE);
                    blackNameInput_t.setVisibility(View.INVISIBLE);
                    blackNameInput_t.setClickable(false);
                    cancelBlack_t.setVisibility(View.VISIBLE);
                    cancelBlack_t.setClickable(true);
                    blackImage_t.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(ManageTimer.this, "Enter the number of minutes !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setWhite_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNumeric(whiteNameInput_t.getText().toString())){
                    if (Long.parseLong(whiteNameInput_t.getText().toString()) > 40)
                    {
                        WhiteTime = 40;
                        whiteNameText_t.setText(formatTimeTimer(WhiteTime * 60000));
                        Toast.makeText(ManageTimer.this, "Max time is 40 minutes !", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        WhiteTime = Long.parseLong(whiteNameInput_t.getText().toString());
                        whiteNameText_t.setText(formatTimeTimer(WhiteTime * 60000));
                    }
                    whiteNameText_t.setGravity(Gravity.CENTER);
                    setWhite_t.setClickable(false);
                    setWhite_t.setVisibility(View.INVISIBLE);
                    whiteNameInput_t.setVisibility(View.INVISIBLE);
                    whiteNameInput_t.setClickable(false);
                    cancelWhite_t.setVisibility(View.VISIBLE);
                    cancelWhite_t.setClickable(true);
                    whiteImage_t.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(ManageTimer.this, "Enter the number of minutes !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelBlack_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blackNameText_t.setText("Black Player:");
                setBlack_t.setClickable(true);
                setBlack_t.setVisibility(View.VISIBLE);
                blackNameInput_t.setVisibility(View.VISIBLE);
                blackNameInput_t.setClickable(true);
                cancelBlack_t.setVisibility(View.INVISIBLE);
                cancelBlack_t.setClickable(false);
                blackImage_t.setVisibility(View.INVISIBLE);
            }
        });
        cancelWhite_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whiteNameText_t.setText("White Player:");
                setWhite_t.setClickable(true);
                setWhite_t.setVisibility(View.VISIBLE);
                whiteNameInput_t.setVisibility(View.VISIBLE);
                whiteNameInput_t.setClickable(true);
                cancelWhite_t.setVisibility(View.INVISIBLE);
                cancelWhite_t.setClickable(false);
                whiteImage_t.setVisibility(View.INVISIBLE);
            }
        });

        start_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNumeric(whiteNameInput_t.getText().toString()) && isNumeric(blackNameInput_t.getText().toString()) &&
                        setWhite_t.getVisibility() == View.INVISIBLE && setBlack_t.getVisibility() == View.INVISIBLE)
                {
                    Intent intent = new Intent(ManageTimer.this, MainActivity.class);
                    intent.putExtra("blackTime", String.valueOf(BlackTime));
                    intent.putExtra("whiteTime", String.valueOf(WhiteTime));
                    intent.putExtra("blackName", theNameOfBlackFromIntro);
                    intent.putExtra("whiteName", theNameOfWhiteFromIntro);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ManageTimer.this, "Set Timers First !", Toast.LENGTH_SHORT).show();
                }
            }
        });
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



    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
    private String formatTimeTimer(long millis) {
        int seconds = (int) (millis / 1000) % 60;
        int minutes = (int) ((millis / (1000 * 60)) % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }
}