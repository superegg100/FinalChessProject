package com.example.finalchessproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import java.util.ArrayList;

public class GameListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewGames;
    private ArrayList<Game> gameList;
    private GameAdapter gameAdapter;
    private DatabaseReference gamesRef;

    Button butttonReturnToWelcome;

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
        setContentView(R.layout.activity_game_list);

        // Initialize RecyclerView and its layout manager
        recyclerViewGames = findViewById(R.id.recyclerViewGames);
        recyclerViewGames.setHasFixedSize(true);
        recyclerViewGames.setLayoutManager(new LinearLayoutManager(this));
        butttonReturnToWelcome = findViewById(R.id.buttonReturnToWelcome);

        // Set click listener for the return button
        butttonReturnToWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Initialize game list and adapter
        gameList = new ArrayList<>();
        gameAdapter = new GameAdapter(gameList);
        recyclerViewGames.setAdapter(gameAdapter);

        // Set item click listener for RecyclerView items
        gameAdapter.setOnItemClickListener(new GameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Game clickedGame = gameList.get(position);
                Gson gson = new Gson();
                String serializedGame = gson.toJson(clickedGame);
                // Start GameDetailsActivity with serialized game data
                Intent intent = new Intent(GameListActivity.this, GameDetailsActivity.class);
                intent.putExtra("serializedGame", serializedGame);
                startActivity(intent);
            }
        });

        // Initialize Firebase database reference to "Games" node
        gamesRef = FirebaseDatabase.getInstance().getReference("Games");
        gamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gameList.clear();
                // Iterate through database snapshots to populate game list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Game game = snapshot.getValue(Game.class);
                    gameList.add(game);
                }
                // Notify adapter of data changes
                gameAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Show error message if data loading fails
                Toast.makeText(GameListActivity.this, "Failed to load games.", Toast.LENGTH_SHORT).show();
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
}
