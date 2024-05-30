package com.example.finalchessproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private ArrayList<Game> gameList;
    private OnItemClickListener listener;

    // Interface for handling item click events
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Setter for the item click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Constructor to initialize the adapter with a list of games
    public GameAdapter(ArrayList<Game> gameList) {
        this.gameList = gameList;
    }

    // ViewHolder class to hold references to views for each item in the list
    public static class GameViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewGameInfo;
        public TextView textViewGameDate;
        public TextView textViewGameWinner;
        public ImageView imageViewGameIcon;
        public ImageView imageViewMore;
        public ImageView imageViewWinnerIcon;
        public ImageView imageViewWinnerPieceColor;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGameInfo = itemView.findViewById(R.id.textViewGameInfo);
            textViewGameDate = itemView.findViewById(R.id.textViewGameDate);
            textViewGameWinner = itemView.findViewById(R.id.textViewGameWinner);
            imageViewGameIcon = itemView.findViewById(R.id.imageViewGameIcon);
            imageViewMore = itemView.findViewById(R.id.imageViewMore);
            imageViewWinnerIcon = itemView.findViewById(R.id.imageViewWinnerIcon);
            imageViewWinnerPieceColor = itemView.findViewById(R.id.imageViewWinnerPieceColor);
        }
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the list
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game currentGame = gameList.get(position);
        // Bind data to the views in each ViewHolder
        holder.textViewGameInfo.setText(currentGame.getWhiteName() + " vs " + currentGame.getBlackName());
        holder.textViewGameDate.setText(currentGame.getDate());

        // Determine and set the winner's information
        if (checkFirstWordOfWinner(currentGame.getWinner(), currentGame.getWhiteName())) {
            holder.textViewGameWinner.setText(currentGame.getWhiteName());
            holder.imageViewWinnerIcon.setVisibility(View.VISIBLE);
            holder.imageViewWinnerPieceColor.setVisibility(View.VISIBLE);
            holder.imageViewWinnerPieceColor.setImageResource(R.drawable.whitepawn);
        } else if (checkFirstWordOfWinner(currentGame.getWinner(), currentGame.getBlackName())) {
            holder.textViewGameWinner.setText(currentGame.getBlackName());
            holder.imageViewWinnerIcon.setVisibility(View.VISIBLE);
            holder.imageViewWinnerPieceColor.setVisibility(View.VISIBLE);
            holder.imageViewWinnerPieceColor.setImageResource(R.drawable.blackpawn);
        } else {
            holder.textViewGameWinner.setText("Draw");
            holder.imageViewWinnerIcon.setVisibility(View.GONE);
            holder.imageViewWinnerPieceColor.setVisibility(View.GONE);
        }

        // Handle item click events
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    // Method to check if the first word of the winner's name matches a specified name
    public boolean checkFirstWordOfWinner(String input, String wordToCheck) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        String[] words = input.split("\\s+");
        return words.length > 0 && words[0].equalsIgnoreCase(wordToCheck);
    }

}
