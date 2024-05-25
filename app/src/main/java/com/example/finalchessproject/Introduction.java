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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.Locale;

public class Introduction extends AppCompatActivity {

    ImageView blackImage, whiteImage;
    TextView blackNameText, whiteNameText;
    Button start, setBlack, setWhite, cancelWhite, cancelBlack, gameList;
    EditText blackNameInput, whiteNameInput;

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
        setContentView(R.layout.activity_introduction);

        blackImage = findViewById(R.id.BlackPickedImage);
        whiteImage = findViewById(R.id.WhitePickedImage);
        start = findViewById(R.id.Start);
        setWhite = findViewById(R.id.SetWhiteButton);
        setBlack = findViewById(R.id.SetBlackButton);
        cancelWhite = findViewById(R.id.CancelWhiteButton);
        cancelBlack = findViewById(R.id.CancelBlackButton);
        gameList = findViewById(R.id.GoToGameList);
        blackNameInput = findViewById(R.id.BlackPiecesNameInput);
        whiteNameInput = findViewById(R.id.WhitePiecesNameInput);
        blackNameText = findViewById(R.id.BlackPiecesNameText);
        whiteNameText = findViewById(R.id.WhitePiecesNameText);

        setBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blackNameInput.getText().toString().length() >= 3 && blackNameInput.getText().toString().length() <= 9 && isAlphanumeric(blackNameInput.getText().toString())){
                    blackNameText.setText(blackNameInput.getText().toString());
                    blackNameText.setGravity(Gravity.CENTER);
                    setBlack.setClickable(false);
                    setBlack.setVisibility(View.INVISIBLE);
                    blackNameInput.setVisibility(View.INVISIBLE);
                    blackNameInput.setClickable(false);
                    cancelBlack.setVisibility(View.VISIBLE);
                    cancelBlack.setClickable(true);
                    blackImage.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(Introduction.this, "Name needs to be between 3-9 characters long and consist of letters and numbers only !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whiteNameInput.getText().toString().length() >= 3 && whiteNameInput.getText().toString().length() <= 9 && isAlphanumeric(whiteNameInput.getText().toString())){
                    whiteNameText.setText(whiteNameInput.getText().toString());
                    whiteNameText.setGravity(Gravity.CENTER);
                    setWhite.setClickable(false);
                    setWhite.setVisibility(View.INVISIBLE);
                    whiteNameInput.setVisibility(View.INVISIBLE);
                    whiteNameInput.setClickable(false);
                    cancelWhite.setVisibility(View.VISIBLE);
                    cancelWhite.setClickable(true);
                    whiteImage.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(Introduction.this, "Name needs to be between 3-9 characters long and consist of letters and numbers only !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blackNameText.setText("Black Player:");
                setBlack.setClickable(true);
                setBlack.setVisibility(View.VISIBLE);
                blackNameInput.setVisibility(View.VISIBLE);
                blackNameInput.setClickable(true);
                cancelBlack.setVisibility(View.INVISIBLE);
                cancelBlack.setClickable(false);
                blackImage.setVisibility(View.INVISIBLE);
            }
        });
        cancelWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whiteNameText.setText("White Player:");
                setWhite.setClickable(true);
                setWhite.setVisibility(View.VISIBLE);
                whiteNameInput.setVisibility(View.VISIBLE);
                whiteNameInput.setClickable(true);
                cancelWhite.setVisibility(View.INVISIBLE);
                cancelWhite.setClickable(false);
                whiteImage.setVisibility(View.INVISIBLE);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whiteNameText.toString().length() >= 3 && whiteNameInput.getText().toString().length() <= 9 && isAlphanumeric(whiteNameInput.getText().toString()) && setWhite.getVisibility() == View.INVISIBLE &&
                blackNameText.toString().length() >= 3 && blackNameInput.getText().toString().length() <= 9 && isAlphanumeric(blackNameInput.getText().toString()) && setBlack.getVisibility() == View.INVISIBLE)
                {
                    Intent intent = new Intent(Introduction.this, ManageTimer.class);
                    intent.putExtra("blackName", blackNameInput.getText().toString());
                    intent.putExtra("whiteName", whiteNameText.getText().toString());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(Introduction.this, "Set names First !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gameList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Introduction.this, GameListActivity.class);
                startActivity(intent);
            }
        });

    }
    public static boolean isAlphanumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
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