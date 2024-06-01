package com.example.finalchessproject;

import android.widget.ImageView;

public class Rook extends Piece{
    private int NumOfMoves;
    public Rook(int i, int j, Piece[][] board,String color, ImageView imageView){
        super(i,j,board,color, imageView);
        if (super.GetColor() == "white"){
            super.GetImageView().setImageResource(R.drawable.whiterook);
        }
        else{
            super.GetImageView().setImageResource(R.drawable.blackrook);}
    }
    @Override
    public String GetFullType(){
        return super.GetColor() + "rook";
    }
    @Override
    public String GetType(){
        return "R";
    }
    @Override
    public boolean HasPiece(){
        return true;
    }
    public int GetNumOfMoves(){
        return this.NumOfMoves;
    }
    @Override
    public boolean CanMove(Piece piece, boolean bool) {
        if (super.GetI() != piece.GetI() && super.GetJ() != piece.GetJ()){ return false; }
        if (!bool){
        if (IsPinned(piece.GetI(), piece.GetJ(), this)) { return false;}
        }
        if (piece.GetColor() == super.GetColor()){
            return false;
        }
        if (piece.GetI()>super.GetI()) {return CanMoveDown(piece);}
        if (piece.GetI()<super.GetI()) {return CanMoveUp(piece);}
        if (piece.GetJ()<super.GetJ()) {return CanMoveLeft(piece);}
        if (piece.GetJ()>super.GetJ()) {return CanMoveRight(piece);}
        return true;
    }
    public boolean CanMoveLeft(Piece piece) {
        for (int x = super.GetJ()-1; x > piece.GetJ(); x--){
            if (super.GetBoard()[piece.GetI()][x].HasPiece()) { return false; }
        }
        return true;
    }
    public boolean CanMoveRight(Piece piece) {
        for (int x = super.GetJ()+1; x < piece.GetJ(); x++){
            if (super.GetBoard()[piece.GetI()][x].HasPiece()) { return false; }
        }
        return true;
    }
    public boolean CanMoveDown(Piece piece) {
        for (int x = super.GetI()+1; x < piece.GetI(); x++){
            if (super.GetBoard()[x][piece.GetJ()].HasPiece()) { return false; }
        }
        return true;
    }
    public boolean CanMoveUp(Piece piece) {
        for (int x = super.GetI()-1; x > piece.GetI(); x--){
            if (super.GetBoard()[x][piece.GetJ()].HasPiece()) { return false; }
        }
        return true;
    }
    //Moves the piece
    public void Move(Piece piece, int i, int j){
        this.GetBoard()[piece.GetI()][piece.GetJ()] = new Piece(piece.GetI(), piece.GetJ(), piece.GetBoard(),"gray", GetImageView());
        this.GetBoard()[i][j] = piece;
        piece.SetI(i);
        piece.SetJ(j);
        super.IncNumOfMovesInGame();
        this.NumOfMoves++;
    }

    //Checks if this piece can save the king from checkmate
    @Override
    public boolean CanEscapeCheckMate() {
        for (int x = 1; super.GetJ() + x < 8; x++){
            if (super.GetBoard()[super.GetI()][super.GetJ()+x].HasPiece()){
                if (super.GetColor() == (super.GetBoard()[super.GetI()][super.GetJ()+x].GetColor())){
                    break;
                }
            }
            if (!IsPinned(super.GetI(),super.GetJ()+x,this)){
                return true;
            }
        }
        for (int x = super.GetJ()-1; x >= 0; x--){
            if (super.GetBoard()[super.GetI()][x].HasPiece()){
                if (super.GetColor() == (super.GetBoard()[super.GetI()][x].GetColor())){
                    break;
                }
            }
            if (!IsPinned(super.GetI(),x,this)){
                return true;
            }
        }
        for (int y = 1; super.GetI()+y < 8; y++){
            if (super.GetBoard()[super.GetI()+y][super.GetJ()].HasPiece()){
                if (super.GetColor() == (super.GetBoard()[super.GetI()+y][super.GetJ()].GetColor())){
                    break;
                }
            }
            if (!IsPinned(super.GetI()+y,super.GetJ(),this)){
                return true;
            }
        }
        for (int y = super.GetI()-1; y >= 0; y--){
            if (super.GetBoard()[y][super.GetJ()].HasPiece()){
                if (super.GetColor() == (super.GetBoard()[y][super.GetJ()].GetColor())){
                    break;
                }
            }
            if (!IsPinned(y,super.GetJ(),this)){
                return true;
            }
        }
        return false;
    }

}
