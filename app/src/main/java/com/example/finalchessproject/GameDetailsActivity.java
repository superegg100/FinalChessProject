package com.example.finalchessproject;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class GameDetailsActivity extends AppCompatActivity {

    private TextView textViewGameInfo;
    private TextView textViewGameMoveLog;
    private TextView textViewBlackPlayerName;
    private TextView textViewBlackPlayerTime;
    private TextView textViewWhitePlayerName;
    private TextView textViewWhitePlayerTime;
    private TextView textViewWinnerName;
    private TextView textViewWinnerTime;
    private TextView textViewGameDate;
    private Button buttonReturnToGameList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        textViewGameMoveLog = findViewById(R.id.textViewMoveLogGameInfo);
        textViewGameInfo = findViewById(R.id.textViewGameInfo);
        textViewBlackPlayerName = findViewById(R.id.textViewBlackPlayerName);
        textViewBlackPlayerTime = findViewById(R.id.textViewBlackPlayerTime);
        textViewWhitePlayerName = findViewById(R.id.textViewWhitePlayerName);
        textViewWhitePlayerTime = findViewById(R.id.textViewWhitePlayerTime);
        textViewWinnerName = findViewById(R.id.textViewWinnerName);
        textViewWinnerTime = findViewById(R.id.textViewWinnerTime);
        textViewGameDate = findViewById(R.id.textViewGameDate);
        buttonReturnToGameList = findViewById(R.id.buttonReturnToList);

        buttonReturnToGameList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        String serializedGame = getIntent().getStringExtra("serializedGame");
        Gson gson = new Gson();
        Game selectedGame = gson.fromJson(serializedGame, Game.class);
        if (selectedGame != null) {
            textViewGameMoveLog.setText("Move List:\n" + selectedGame.getMoveList());
            textViewGameInfo.setText("Game Info");
            textViewBlackPlayerName.setText("Black Player: " + selectedGame.getBlackName());
            textViewBlackPlayerTime.setText("Time: " + selectedGame.getBlackTime());
            textViewWhitePlayerName.setText("White Player: " + selectedGame.getWhiteName());
            textViewWhitePlayerTime.setText("Time: " + selectedGame.getWhiteTime());
            textViewWinnerName.setText("Winner: " + selectedGame.getWinner());
            textViewGameDate.setText("Date: " + selectedGame.getDate());
        }
    }
}