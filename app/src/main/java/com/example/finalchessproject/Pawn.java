package com.example.finalchessproject;

import android.widget.ImageView;

public class Pawn extends Piece{
    private int NumOfMoves;
    private int PawnInc; // determines if the pawn goes up the board or down
    private int MoveWhenPlayed;
    private Boolean IsEnPeasent;
    public Pawn(int i, int j, Piece[][] board,String color, ImageView imageView){
        super(i,j, board,color, imageView);
        if (super.GetColor().toLowerCase() == "white"){
            this.PawnInc = -1;
        }
        else this.PawnInc = 1;
        this.IsEnPeasent = false;
        this.NumOfMoves = 0;
        if (super.GetColor() == "white"){
            super.GetImageView().setImageResource(R.drawable.whitepawn);
        }
        else{
            super.GetImageView().setImageResource(R.drawable.blackpawn);}
    }

    @Override
    public String GetFullType(){
        return super.GetColor() + "pawn";
    }

    @Override
    public String GetType(){
        return "P";
    }
    public int GetMoveWhenPlayed(){
        return this.MoveWhenPlayed;
    }
    @Override
    public boolean HasPiece(){
        return true;
    }

    //Checks if this piece can move to the square it wants
    @Override
    public boolean CanMove(Piece piece, boolean bool)
    {
        if (piece.GetI() < 0 || piece.GetJ() < 0 || piece.GetI() > 7 || piece.GetJ() > 7){
            return false;
        }
        if (!bool){
            if (IsPinned(piece.GetI(), piece.GetJ(), this)){return false;}
        }
        if (piece.GetI() == super.GetI() + this.PawnInc && !piece.HasPiece() && piece.GetJ() == super.GetJ()){
            this.IsEnPeasent = false;
            return true;
        }
        if (piece.GetI() == super.GetI() + this.PawnInc && (piece.GetJ() == super.GetJ() +1 || piece.GetJ() == super.GetJ() - 1) &&
                piece.HasPiece() && piece.GetColor() != super.GetColor()){
            this.IsEnPeasent = false;
            return true;
        }
        if (this.NumOfMoves == 0){
            if (piece.GetI() == super.GetI() + (2*this.PawnInc) && !super.GetBoard()[super.GetI() + this.PawnInc][super.GetJ()].HasPiece()
                    && !piece.HasPiece() && piece.GetJ() == super.GetJ()){
                this.IsEnPeasent = false;
                return true;
            }
        }
        if (piece.GetI() - this.PawnInc == super.GetI() && (piece.GetJ() == super.GetJ() + 1 || piece.GetJ() == super.GetJ() - 1) &&
                super.GetBoard()[piece.GetI() - this.PawnInc][piece.GetJ()] instanceof Pawn
                && (super.GetI() == 3 || super.GetI() == 4)){
            Pawn EnPeasent = (Pawn) (super.GetBoard()[piece.GetI() - this.PawnInc][piece.GetJ()]);
            if (EnPeasent.NumOfMoves == 1){
                this.IsEnPeasent = true;
                return true;
            }
        }
        return false;
    }
    //Moves the piece
    public void Move(Piece piece, int i, int j){
        super.IncNumOfMovesInGame();
        super.GetBoard()[piece.GetI()][piece.GetJ()] = new Piece(piece.GetI(), piece.GetJ(), piece.GetBoard(),"gray", null);
        super.GetBoard()[i][j] = piece;
        piece.SetI(i);
        piece.SetJ(j);
        if (IsEnPeasent){
            super.GetBoard()[piece.GetI() - this.PawnInc][piece.GetJ()] = new Piece(piece.GetI(), piece.GetJ(), piece.GetBoard(),"gray", null);
        }
        if (this.NumOfMoves == 0)
        {this.MoveWhenPlayed = super.GetNumOfMovesInGame();}
        this.NumOfMoves++;
    }

    public Boolean IsEnPeasnt(){
        return this.IsEnPeasent;
    }
    public int ReturnPawnInc(){
        return this.PawnInc;
    }

    //Checks if this piece can save the king from checkmate
    @Override
    public boolean CanEscapeCheckMate() {
        if (this.CanMove(super.GetBoard()[super.GetI()+this.PawnInc][super.GetJ()], false)){
            return true;
        }
        if (this.CanMove(super.GetBoard()[super.GetI()+2*this.PawnInc][super.GetJ()], false)){
            return true;
        }
        if (super.GetJ()+ 1 < 8){
            if (this.CanMove(super.GetBoard()[super.GetI()+this.PawnInc][super.GetJ()+ 1], false)){
                return true;
            }
        }
        if (super.GetJ() - 1 >= 0){
            if (this.CanMove(super.GetBoard()[super.GetI()+this.PawnInc][super.GetJ() - 1], false)){
                return true;
            }
        }

        return false;
    }
}
