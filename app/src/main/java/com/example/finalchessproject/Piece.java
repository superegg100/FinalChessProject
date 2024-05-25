package com.example.finalchessproject;

import android.service.controls.actions.BooleanAction;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;

public class Piece {
    private int i;
    private int j;
    private String color;
    private Piece[][] board;
    private static int NumOfMovesInGame = 0;
    private ImageView imageview;
    public Piece(int i, int j, Piece[][] board, String color, ImageView imageview) {
        this.i = i;
        this.j = j;
        this.board = board;
        this.color = color;
        this.imageview = imageview;
    }

    public int GetI() {
        return this.i;
    }
    public int GetJ() {
        return this.j;
    }
    public ImageView GetImageView(){
        return this.imageview;
    }
    public int GetNumOfMovesInGame() {
        return this.NumOfMovesInGame;
    }
    public void IncNumOfMovesInGame() {
        this.NumOfMovesInGame++;
    }
    public void DecNumOfMovesInGame() {
        this.NumOfMovesInGame--;
    }
    public String GetColor() {
        return this.color;
    }
    public Piece[][] GetBoard(){
        return this.board;
    }
    public void SetI(int i) {
        this.i = i;
    }
    public void SetJ(int j) {
        this.j = j;
    }
    public boolean HasPiece() {
        return false;
    }
    public boolean CanMove(Piece piece, boolean bool) {
        return false;
    }
    public String GetType() {
        return "_";
    }

    public String GetFullType()
    {
        return "";
    }

    public boolean IsChecked(Piece king) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.board[i][j].GetColor() != king.GetColor()) {
                    if (this.board[i][j].CanMove(king, true)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public Piece FindKing(String color){
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board.length; j++) {
                if (this.board[i][j] instanceof King && this.board[i][j].GetColor().equals(color)){
                    return this.board[i][j];
                }
            }
        }
        return null;
    }
    public void Move(Piece piece, int i, int j){
            this.board[piece.GetI()][piece.GetJ()] = new Piece(piece.GetI(), piece.GetJ(), piece.GetBoard(),"gray", null);
            this.board[i][j] = piece;
            piece.SetI(i);
            piece.SetJ(j);
            this.NumOfMovesInGame++;
    }
    public boolean IsPinned(int i, int j, Piece piece){
        if (i > 7 || i < 0){return true;}
        if (j > 7 || j < 0){return true;}

        boolean ischecked;
        int saveI = piece.GetI(),saveJ = piece.GetJ();
        Piece temp = this.board[i][j];
        this.board[piece.GetI()][piece.GetJ()] = new Piece(piece.GetI(), piece.GetJ(), piece.GetBoard(), "gray", null);
        this.board[i][j] = piece;
        piece.SetI(i);
        piece.SetJ(j);
        //DecNumOfMovesInGame();
        ischecked = IsChecked(FindKing(piece.GetColor()));
        this.board[saveI][saveJ] = piece;
        piece.SetI(saveI);
        piece.SetJ(saveJ);
        //DecNumOfMovesInGame();
        this.board[i][j] = temp;
        if (ischecked) return true;
        return false;
    }

    public boolean CanEscapeCheckMate(){
        return false;
    }
}
