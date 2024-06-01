package com.example.finalchessproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

@SuppressLint("ViewConstructor")
public class BoardGame extends View {
    final int size = 8;  // Size of the chessboard (8x8)
    Square[][] board;  // 2D array to store squares of the board
    int offset;  // Height of the offset
    Context context;

    // Constructor for BoardGame class
    public BoardGame(Context context, int offset){
        super(context);
        this.context = context;
        board = new Square[size][size];  // Initialize the board
        this.offset = offset;  // Set the canvas height
    }

    public Square[][] getBoard() {
        return this.board;  // Get the board
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        drawBoard(canvas, this.offset);  // Draw the board on the canvas
    }

    // Method to draw the board on the canvas
    public void drawBoard(Canvas canvas, int offset){
        int canvasWidth = canvas.getWidth();  // Get the width of the canvas
        int size = 8;  // Size of the board (8x8)
        int squareSize = canvasWidth / size;  // Calculate the size of each square

        // Loop through each square on the board
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                // Create a new square at the appropriate position
                board[i][j] = new Square(j*squareSize,  this.offset + i*squareSize, squareSize, squareSize,i,j);

                // Draw the square based on its position (alternating black and white)
                if (j % 2 == 0 && i % 2 == 0){
                    board[i][j].drawWhite(canvas);}  // Draw white square
                if (j % 2 == 1 && i % 2 == 0){
                    board[i][j].drawBlack(canvas);}  // Draw black square
                if (j % 2 == 1 && i % 2 == 1){
                    board[i][j].drawWhite(canvas);}  // Draw white square
                if (j % 2 == 0 && i % 2 == 1){
                    board[i][j].drawBlack(canvas);}  // Draw black square
            }
        }
    }
}
