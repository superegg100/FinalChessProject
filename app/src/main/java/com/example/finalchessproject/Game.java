package com.example.finalchessproject;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Game {
    private String whiteName;
    private String whiteTime;
    private String blackName;
    private String blackTime;
    private String moveList;
    private String winner;
    private String date; // Add this line

    // No-argument constructor (required by Firebase)
    public Game() {
    }

    // Constructor with arguments
    public Game(String whiteName, String whiteTime, String blackName, String blackTime, String moveList, String winner) {
        this.whiteName = whiteName;
        this.whiteTime = whiteTime;
        this.blackName = blackName;
        this.blackTime = blackTime;
        this.moveList = moveList;
        this.winner = winner;
        this.date = getCurrentDate(); // Add this line
    }

    // Getter and setter methods
    public String getWhiteName() {
        return whiteName;
    }

    public void setWhiteName(String whiteName) {
        this.whiteName = whiteName;
    }

    public String getWhiteTime() {
        return whiteTime;
    }

    public void setWhiteTime(String whiteTime) {
        this.whiteTime = whiteTime;
    }

    public String getBlackName() {
        return blackName;
    }

    public void setBlackName(String blackName) {
        this.blackName = blackName;
    }

    public String getBlackTime() {
        return blackTime;
    }

    public void setBlackTime(String blackTime) {
        this.blackTime = blackTime;
    }

    public String getMoveList() {
        return moveList;
    }

    public void setMoveList(String moveList) {
        this.moveList = moveList;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getDate() { // Add this getter method
        return date;
    }

    public void setDate(String date) { // Add this setter method if needed
        this.date = date;
    }

    // Method to get the current date
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date());
    }
}