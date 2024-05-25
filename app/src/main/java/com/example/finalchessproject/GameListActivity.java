package com.example.finalchessproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.io.Serializable;
import java.util.ArrayList;

public class GameListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewGames;
    private ArrayList<Game> gameList;
    private GameAdapter gameAdapter;
    private DatabaseReference gamesRef;

    Button butttonReturnToWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        recyclerViewGames = findViewById(R.id.recyclerViewGames);
        recyclerViewGames.setHasFixedSize(true);
        recyclerViewGames.setLayoutManager(new LinearLayoutManager(this));
        butttonReturnToWelcome = findViewById(R.id.buttonReturnToWelcome);

        butttonReturnToWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gameList = new ArrayList<>();
        gameAdapter = new GameAdapter(gameList);
        recyclerViewGames.setAdapter(gameAdapter);

        gameAdapter.setOnItemClickListener(new GameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Game clickedGame = gameList.get(position);
                Gson gson = new Gson();
                String serializedGame = gson.toJson(clickedGame);
                Intent intent = new Intent(GameListActivity.this, GameDetailsActivity.class);
                intent.putExtra("serializedGame", serializedGame);
                startActivity(intent);
            }
        });

        gamesRef = FirebaseDatabase.getInstance().getReference("Games");
        gamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gameList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Game game = snapshot.getValue(Game.class);
                    gameList.add(game);
                }
                gameAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GameListActivity.this, "Failed to load games.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}