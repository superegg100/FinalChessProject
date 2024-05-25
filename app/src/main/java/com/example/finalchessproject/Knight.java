package com.example.finalchessproject;

import android.widget.ImageView;

public class Knight extends Piece{
    public Knight(int i, int j, Piece[][] board, String color, ImageView imageView){
        super(i,j, board, color, imageView);
        if (super.GetColor() == "white"){
            super.GetImageView().setImageResource(R.drawable.whiteknight);
        }
        else{
            super.GetImageView().setImageResource(R.drawable.blackknight);}
    }

    @Override
    public String GetFullType(){
        return super.GetColor() + "knight";
    }
    @Override
    public String GetType(){
        return "N";
    }
    @Override
    public boolean HasPiece(){
        return true;
    }
    @Override
    public boolean CanMove(Piece piece, boolean bool)
    {
        if (piece.GetI() > 7 || piece.GetI() < 0){return false;}
        if (piece.GetJ() > 7 || piece.GetJ() < 0){return false;}

        if (!bool){
            if (IsPinned(piece.GetI(), piece.GetJ(), this)){return false;}
        }
        if (piece.GetColor() == super.GetColor()){
            return false;
        }

        if (piece.GetI() == super.GetI() + 2 && (piece.GetJ() == super.GetJ() + 1)){
            if (super.GetI() + 2 < 8 && (super.GetJ() + 1 < 8 || super.GetJ() -1 >= 0)){
                return true;}
        }
        if (piece.GetI() == super.GetI() + 2 && ( piece.GetJ() == super.GetJ() -1)){
            if (super.GetI() + 2 < 8 && (super.GetJ() -1 >= 0)){
                return true;}
        }



        if (piece.GetI() == super.GetI() - 2 && piece.GetJ() == super.GetJ() -1){
            if (super.GetI() - 2 >= 0 && ( super.GetJ() -1 >= 0)){
                return true;}
        }
        if (piece.GetI() == super.GetI() - 2 && piece.GetJ() == super.GetJ() + 1){
            if (super.GetI() - 2 >= 0 && (super.GetJ() + 1 < 8 )){
                return true;}
        }


        if (piece.GetJ() == super.GetJ() + 2 && (piece.GetI() == super.GetI() + 1)){
            if (super.GetJ() + 2 < 8 && (super.GetI() + 1 < 8)){
                return true;}
        }
        if (piece.GetJ() == super.GetJ() + 2 && ( piece.GetI() == super.GetI() -1)){
            if (super.GetJ() + 2 < 8 && (super.GetI() -1 >= 0)){
                return true;}
        }


        if (piece.GetJ() == super.GetJ() - 2 && piece.GetI() == super.GetI() + 1){
            if (super.GetJ() - 2 >= 0 && (super.GetI() + 1 < 8)){
                return true;}
        }
        if (piece.GetJ() == super.GetJ() - 2 && piece.GetI() == super.GetI() -1){
            if (super.GetJ() - 2 >= 0 && (super.GetI() -1 >= 0)){
                return true;}
        }


        return false;
    }

    @Override
    public boolean CanEscapeCheckMate() {
        if (!IsPinned(super.GetI() + 2, super.GetJ() + 1, this)){
            System.out.println("1 knigt protect");
            return true;
        }
        if (!IsPinned(super.GetI() + 2, super.GetJ() - 1, this)){
            System.out.println("2 knigt protect");
            return true;
        }

        if (!IsPinned(super.GetI() - 2, super.GetJ() + 1, this)){
            System.out.println("3 knigt protect");
            return true;
        }
        if (!IsPinned(super.GetI() - 2, super.GetJ() - 1, this)){
            System.out.println("4 knigt protect");
            return true;
        }

        if (!IsPinned(super.GetJ() + 2, super.GetI() + 1, this)){
            System.out.println("5 knigt protect");
            return true;
        }
        if (!IsPinned(super.GetJ() + 2, super.GetI() - 1, this)){
            System.out.println("6 knigt protect");
            return true;
        }

        if (!IsPinned(super.GetJ() - 2, super.GetI() + 1, this)){
            System.out.println("7 knigt protect");
            return true;
        }
        if (!IsPinned(super.GetJ() - 2, super.GetI() - 1, this)){
            System.out.println("8 knigt protect");
            return true;
        }

        return false;
    }
}
