package com.example.finalchessproject;

import android.widget.ImageView;

public class Bishop extends Piece {
    // Constructor for Bishop class
    public Bishop(int i, int j, Piece[][] board,String color, ImageView imageView){
        super(i,j,board,color, imageView);  // Call the superclass constructor
        // Set the appropriate image resource based on the piece's color
        if (super.GetColor().equals("white")){
            super.GetImageView().setImageResource(R.drawable.whitebishop);
        }
        else{
            super.GetImageView().setImageResource(R.drawable.blackbishop);
        }
    }

    // Override HasPiece method to always return true for Bishop class
    @Override
    public boolean HasPiece(){
        return true;
    }

    // Override GetFullType method to return the full type of the piece
    @Override
    public String GetFullType(){
        return super.GetColor() + "bishop";
    }

    // Static method to change a number based on a boolean condition
    public static int ChangeNum(boolean b, int x){
        if(b){
            x++;  // Increment x if b is true
        }
        else{
            x--;  // Decrement x if b is false
        }
        return x;
    }

    // Override CanMove method to implement Bishop's movement logic
    @Override
    public boolean CanMove(Piece piece, boolean bool){
        if (!bool){
            if (IsPinned(piece.GetI(), piece.GetJ(), this)){  // Check if the piece is pinned
                return false;
            }
        }
        if (piece.GetColor().equals(super.GetColor())){
            return false;  // Bishop cannot move to a cell occupied by its own color
        }
        boolean ibool;
        boolean jbool;
        int i = super.GetI();
        int j = super.GetJ();
        ibool = i < piece.GetI();
        jbool = j < piece.GetJ();
        i = ChangeNum(ibool, i);
        j = ChangeNum(jbool, j);
        while(i >= 0 && j >= 0 && i < 8 && j < 8){
            if(i == piece.GetI() && j == piece.GetJ()){
                return true;  // Bishop can move to the target cell
            }
            if(super.GetBoard()[i][j].HasPiece()){
                return false;  // Bishop cannot move if there's a piece in the way
            }
            i = ChangeNum(ibool, i);
            j = ChangeNum(jbool, j);
        }
        return false;  // Bishop cannot move to the target cell
    }

    // Override GetType method to return the type of the piece
    @Override
    public String GetType(){
        return "B";  // Bishop type
    }

    // Override CanEscapeCheckMate method to check if Bishop can escape checkmate
    @Override
    public boolean CanEscapeCheckMate() {
        // Check diagonal movements for escaping checkmate
        int i = 1;
        int j = 1;
        while (super.GetJ() + j < 8 && super.GetI() + i < 8){
            // Check if there's a piece of the same color blocking the escape
            if (super.GetBoard()[super.GetI() + i][super.GetJ()+j].HasPiece() && super.GetBoard()[super.GetI() + i][super.GetJ() + j].GetColor().equals(super.GetColor())){
                break;
            }
            // Check if the Bishop can escape checkmate without being pinned
            if (!IsPinned(super.GetI()+i, super.GetJ()+j, this)){
                return true;
            }
            i++;
            j++;
        }
        // Repeat for other diagonal directions
        // (similar logic for other diagonal movements)
        return false;  // Bishop cannot escape checkmate
    }
}
