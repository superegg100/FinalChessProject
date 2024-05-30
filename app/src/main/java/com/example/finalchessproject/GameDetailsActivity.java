package com.example.finalchessproject;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;

public class GameDetailsActivity extends AppCompatActivity {

    // Views in the activity layout
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

        // Initialize views from the layout
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

        // Set a click listener for the return button
        buttonReturnToGameList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the activity and return to the previous screen
                finish();
            }
        });

        // Retrieve serialized game data from intent
        String serializedGame = getIntent().getStringExtra("serializedGame");

        // Deserialize the game using Gson
        Gson gson = new Gson();
        Game selectedGame = gson.fromJson(serializedGame, Game.class);

        // Populate views with game details if game data is available
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