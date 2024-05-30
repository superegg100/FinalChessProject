package com.example.finalchessproject;

import android.widget.ImageView;

public class King extends Piece {
    // Number of moves made by the king
    private int NumOfMoves;
    // Indicates the side for castling: -1 for none, 0 for king side, 1 for queen side
    private int CastleSide = -1;

    // Constructor for King piece
    public King(int i, int j, Piece[][] board, String color, ImageView imageView){
        super(i,j, board, color, imageView);
        this.NumOfMoves = 0;
        // Set the image resource based on the color of the king
        if (super.GetColor() == "white"){
            super.GetImageView().setImageResource(R.drawable.whiteking);
        }
        else{
            super.GetImageView().setImageResource(R.drawable.blackking);
        }
    }

    // Returns the full type of the piece (e.g., whiteking, blackking)
    @Override
    public String GetFullType(){
        return super.GetColor() + "king";
    }

    // Returns the abbreviated type of the piece (e.g., K for king)
    @Override
    public String GetType(){
        return "K";
    }

    // Checks if the king has a piece on it
    @Override
    public boolean HasPiece(){
        return true;
    }

    // Checks if castling on the king side is possible
    public boolean CanCastleKingSide(Piece piece){
        if (!(piece instanceof Rook)){return false;}
        Rook rook = (Rook)piece;
        if (super.GetBoard()[super.GetI()][super.GetJ()+1].HasPiece() || super.GetBoard()[super.GetI()][super.GetJ()+2].HasPiece()){
            return false;
        }
        if (this.NumOfMoves == 0 && piece.GetColor() == super.GetColor() && rook.GetNumOfMoves() == 0) {
            if (!IsPinned(super.GetI(), super.GetJ(), this) && !IsPinned(super.GetI(), super.GetJ() + 1, this) && !IsPinned(super.GetI(), super.GetJ() + 2, this)){
                this.CastleSide = 0;
                return true;
            }
        }
        return false;
    }

    // Checks if castling on the queen side is possible
    public boolean CanCastleQueenSide(Piece piece){
        if (!(piece instanceof Rook)){return false;}
        Rook rook = (Rook)piece;
        if (super.GetBoard()[super.GetI()][super.GetJ()-1].HasPiece() || super.GetBoard()[super.GetI()][super.GetJ()-2].HasPiece() || super.GetBoard()[super.GetI()][super.GetJ()-3].HasPiece()){
            return false;
        }
        if (this.NumOfMoves == 0 && piece.GetColor() == super.GetColor() && rook.GetNumOfMoves() == 0) {
            if (!IsPinned(super.GetI(), super.GetJ(), this) && !IsPinned(super.GetI(), super.GetJ() - 1, this) && !IsPinned(super.GetI(), super.GetJ() - 2, this)){
                this.CastleSide = 1;
                return true;
            }
        }
        return false;
    }

    // Checks if the king can move to a specific position
    @Override
    public boolean CanMove(Piece piece, boolean bool) {
        if (piece.GetColor() == this.GetColor()){return false;}

        if (!bool) {
            if (IsPinned(piece.GetI(), piece.GetJ(), this)) {
                return false;
            }
        }

        if (super.GetI()+1 < 8)
        {
            if ((piece.GetI() == super.GetI() + 1) && piece.GetJ() == super.GetJ()){
                return true;
            }
        }
        if (super.GetI()-1 >= 0 ){
            if ((piece.GetI() == super.GetI() - 1) && piece.GetJ() == super.GetJ()){
                return true;
            }
        }

        if (super.GetI()+1 < 8 && super.GetJ()+1 < 8){
            if ((piece.GetI() == super.GetI() + 1) && piece.GetJ() == super.GetJ() + 1){
                return true;
            }
        }
        if (super.GetI()+1 < 8 && super.GetJ()-1 >= 0){
            if ((piece.GetI() == super.GetI() + 1) && piece.GetJ() == super.GetJ() - 1){
                return true;
            }
        }

        if (super.GetJ()+1 < 8 ){
            if (piece.GetI() == super.GetI() && piece.GetJ() == super.GetJ() + 1){
                return true;
            }
        }
        if (super.GetJ()-1 >= 0){
            if ((piece.GetI() == super.GetI()) && piece.GetJ() == super.GetJ() - 1){
                return true;
            }
        }

        if (super.GetI()-1 >= 0 && super.GetJ()+1 < 8){
            if ((piece.GetI() == super.GetI() - 1) && piece.GetJ() == super.GetJ() + 1){
                return true;
            }
        }
        if (super.GetI()-1 >= 0 && super.GetJ()-1 >= 0){
            if ((piece.GetI() == super.GetI() - 1) && piece.GetJ() == super.GetJ() - 1){
                return true;
            }
        }

        return false;
    }

    // Moves the king and the rook during castling
    public void MoveCastle(Piece piece){
        if (this.CastleSide == 1){
            Move(this, this.GetI(),this.GetJ()-2);
            Move(piece, this.GetI(),piece.GetJ()+3);
        }
        if (this.CastleSide == 0){
            Move(this, this.GetI(),this.GetJ()+2);
            Move(piece, this.GetI(),piece.GetJ()-2);
        }
        super.DecNumOfMovesInGame();
    }

    // Checks if the king is in a checkmate position
    public boolean IsCheckMated(){
        System.out.println("Yo hegati lw checkematoda");
        if (super.GetI()+1 < 8 && !(super.GetBoard()[super.GetI()+1][super.GetJ()].GetColor() == super.GetColor()) && !IsPinned(super.GetI()+1, super.GetJ(), this)){System.out.println("nafalti in 1"); return false;}
        if (super.GetI()-1 >= 0 && !(super.GetBoard()[super.GetI()-1][super.GetJ()].GetColor() == super.GetColor()) && !IsPinned(super.GetI()-1, super.GetJ(), this)){System.out.println("nafalti in 2"); return false;}

        if (super.GetI()+1 < 8 && super.GetJ()+1 < 8 && !(super.GetBoard()[super.GetI()+1][super.GetJ()+1].GetColor() == super.GetColor()) &&        !IsPinned(super.GetI()+1, super.GetJ()+1, this)){System.out.println("nafalti in 3"); return false;}
        if (super.GetI()+1 < 8 && super.GetI()-1 >= 0 && !(super.GetBoard()[super.GetI()+1][super.GetJ()-1].GetColor() == super.GetColor()) && !IsPinned(super.GetI()+1, super.GetJ()-1, this)){System.out.println("nafalti in 4"); return false;}

        if (super.GetJ()+1 < 8 && !(super.GetBoard()[super.GetI()][super.GetJ()+1].GetColor() == super.GetColor()) && !IsPinned(super.GetI(), super.GetJ()+1, this)){System.out.println("nafalti in 5"); return false;}
        if (super.GetJ()-1 >= 0 && !(super.GetBoard()[super.GetI()][super.GetJ()-1].GetColor() == super.GetColor()) && !IsPinned(super.GetI(), super.GetJ()-1, this)){System.out.println("nafalti in 6"); return false;}

        if (super.GetI()-1 >= 0 && super.GetJ()+1 < 8 && !(super.GetBoard()[super.GetI()-1][super.GetJ()+1].GetColor() == super.GetColor()) && !IsPinned(super.GetI()-1, super.GetJ()+1, this)){System.out.println("nafalti in 7"); return false;}
        if (super.GetI()-1 >= 0 && super.GetJ()-1 >= 0 && !(super.GetBoard()[super.GetI()-1][super.GetJ()-1].GetColor() == super.GetColor()) && !IsPinned(super.GetI()-1, super.GetJ()-1, this)){System.out.println("nafalti in 8"); return false;}

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if(super.GetColor() == super.GetBoard()[i][j].GetColor() && super.GetBoard()[i][j].CanEscapeCheckMate()){
                    System.out.println("nafalti in 9" + " and" + i + " " + j);
                    return false;
                }
            }
        }
        return true;
    }
}