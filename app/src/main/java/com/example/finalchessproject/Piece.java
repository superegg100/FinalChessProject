package com.example.finalchessproject;

import android.widget.ImageView;

public class Piece {
    private int i;  // Row index
    private int j;  // Column index
    private String color;  // Piece color (e.g., "white" or "black")
    private Piece[][] board;  // The chessboard
    private static int NumOfMovesInGame = 0;  // Total number of moves in the game
    private ImageView imageview;  // ImageView for displaying the piece

    // Constructor for Piece class
    public Piece(int i, int j, Piece[][] board, String color, ImageView imageview) {
        this.i = i;
        this.j = j;
        this.board = board;
        this.color = color;
        this.imageview = imageview;
    }

    // Getters and setters for various attributes
    public int GetI() {
        return this.i;  // Get row index
    }

    public int GetJ() {
        return this.j;  // Get column index
    }

    public ImageView GetImageView(){
        return this.imageview;  // Get ImageView
    }

    public int GetNumOfMovesInGame() {
        return this.NumOfMovesInGame;  // Get total number of moves in the game
    }

    public void IncNumOfMovesInGame() {
        this.NumOfMovesInGame++;  // Increment total number of moves in the game
    }

    public void DecNumOfMovesInGame() {
        this.NumOfMovesInGame--;  // Decrement total number of moves in the game
    }

    public String GetColor() {
        return this.color;  // Get piece color
    }

    public Piece[][] GetBoard(){
        return this.board;  // Get the chessboard
    }

    public void SetI(int i) {
        this.i = i;  // Set row index
    }

    public void SetJ(int j) {
        this.j = j;  // Set column index
    }

    public boolean HasPiece() {
        return false;  // Check if the cell has a piece (always returns false for base Piece class)
    }

    public boolean CanMove(Piece piece, boolean bool) {
        return false;  // Check if the piece can move to a certain position (always returns false for base Piece class)
    }

    public String GetType() {
        return "_";  // Get the type of the piece (default is "_")
    }

    public String GetFullType()
    {
        return "";  // Get the full type of the piece
    }

    // Method to check if a piece is under check from an opponent's piece
    public boolean IsChecked(King king) {
        System.out.println(king.GetI() + "#####" + king.GetJ());
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.board[i][j].GetColor() != king.GetColor()) {
                    if (this.board[i][j].CanMove(king, true)) {
                        return true;  // The piece is under check
                    }
                }
            }
        }
        return false;  // The piece is not under check
    }

    // Method to find the king of a specific color on the board
    public King FindKing(String color){
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board.length; j++) {
                if (this.board[i][j] instanceof King && this.board[i][j].GetColor().equals(color)){
                    return (King)this.board[i][j];  // Return the king piece
                }
            }
        }
        return null;  // King not found
    }

    // Method to move a piece on the board
    public void Move(Piece piece, int i, int j){
        this.board[piece.GetI()][piece.GetJ()] = new Piece(piece.GetI(), piece.GetJ(), piece.GetBoard(),"gray", null);
        this.board[i][j] = piece;
        piece.SetI(i);
        piece.SetJ(j);
        this.NumOfMovesInGame++;  // Increment total moves in the game
    }

    // Method to check if a piece is pinned
    public boolean IsPinned(int i, int j, Piece piece){
        if (i > 7 || i < 0){return true;}  // Check if row index is out of bounds
        if (j > 7 || j < 0){return true;}  // Check if column index is out of bounds

        if (this.board[i][j].GetColor() == piece.GetColor())
        {
            return true;
        }
        boolean ischecked;
        int saveI = piece.GetI(),saveJ = piece.GetJ();
        Piece temp = this.board[i][j];
        this.board[piece.GetI()][piece.GetJ()] = new Piece(piece.GetI(), piece.GetJ(), piece.GetBoard(), "gray", null);
        this.board[i][j] = piece;
        piece.SetI(i);
        piece.SetJ(j);
        ischecked = IsChecked(FindKing(piece.GetColor()));
        this.board[saveI][saveJ] = piece;
        piece.SetI(saveI);
        piece.SetJ(saveJ);
        this.board[i][j] = temp;
        if (ischecked) return true;  // The piece is pinned
        return false;  // The piece is not pinned
    }

    // Method to check if a piece can escape checkmate
    public boolean CanEscapeCheckMate(){
        return false;  // Default behavior for base Piece class
    }
}
