package com.example.finalchessproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.SweepGradient;
import android.util.DisplayMetrics;
import android.view.View;

public class BoardGame extends View {
    final int size = 8;
    Square[][] board;

    int canvasHeight;
    Context context;
    public BoardGame(Context context){
        super(context);
        this.context = context;
        board = new Square[size][size];
    }

    public Square[][] getBoard() {
        return this.board;
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        drawBoard(canvas);
        this.canvasHeight = canvas.getHeight();
    }

    public int getCanvasHeight(){
        return this.canvasHeight;
    }
    public void drawBoard(Canvas canvas){
        int h = canvas.getWidth()/size; //101
        int w = canvas.getWidth()/size; //101
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                board[i][j] = new Square(j*w,  (w*8)/6 + i*h, w, h,i,j); //j*w, 150 + i*h, w, h,i,j
                if (j % 2 == 0 && i % 2 == 0){
                    board[i][j].drawWhite(canvas);}
                if (j % 2 == 1 && i % 2 == 0){
                    board[i][j].drawBlack(canvas);}
                if (j % 2 == 1 && i % 2 == 1){
                    board[i][j].drawWhite(canvas);}
                if (j % 2 == 0 && i % 2 == 1){
                    board[i][j].drawBlack(canvas);}
            }
        }
    }
}
