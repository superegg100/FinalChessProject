package com.example.finalchessproject;


import android.widget.ImageView;

public class Bishop extends Piece {
    public Bishop(int i, int j, Piece[][] board,String color, ImageView imageView){
        super(i,j,board,color, imageView);
        if (super.GetColor() == "white"){
            super.GetImageView().setImageResource(R.drawable.whitebishop);
        }
        else{
            super.GetImageView().setImageResource(R.drawable.blackbishop);}

    }
    @Override
    public boolean HasPiece(){
        return true;
    }

    @Override
    public String GetFullType(){
        return super.GetColor() + "bishop";
    }

    public static int ChangeNum(boolean b, int x){
        if(b){
            x++;
        }
        else{
            x--;
        }
        return x;
    }
    @Override
    public boolean CanMove(Piece piece, boolean bool){
        if (!bool){
            if (IsPinned(piece.GetI(), piece.GetJ(), this)){return false;}
        }
        if (piece.GetColor() == super.GetColor()){
            return false;
        }
        boolean ibool = false;
        boolean jbool = false;
        int i = super.GetI();
        int j = super.GetJ();
        if(i < piece.GetI()){
            ibool = true;
        }
        else
            ibool = false;
        if(j < piece.GetJ())
            jbool = true;
        else
            jbool = false;
        i = ChangeNum(ibool, i);
        j = ChangeNum(jbool, j);
        while(i >= 0 && j >= 0 && i < 8 && j < 8){
            if(i == piece.GetI() && j == piece.GetJ()){
                return true;
            }
            if(super.GetBoard()[i][j].HasPiece()){
                return false;
            }
            i = ChangeNum(ibool, i);
            j = ChangeNum(jbool, j);
        }
        return false;
    }

    @Override
    public String GetType(){
        return "B";
    }
    @Override
    public boolean CanEscapeCheckMate() {
        int i = 1;
        int j = 1;
        while (super.GetJ() + j < 8 && super.GetI() + i < 8){
            if (super.GetBoard()[super.GetI() + i][super.GetJ()+j].HasPiece() && super.GetBoard()[super.GetI() + i][super.GetJ()+j].GetColor() == super.GetColor()){
                break;
            }
            if (!IsPinned(super.GetI()+i, super.GetJ()+j, this)){
                return true;
            }
            i++;
            j++;
        }
        i = 1;
        j = 1;
        while (super.GetJ() + j < 8 && super.GetI() - i >= 0 ){
            if (super.GetBoard()[super.GetI() - i][super.GetJ()+j].HasPiece() && super.GetBoard()[super.GetI() - i][super.GetJ()+j].GetColor() == super.GetColor()){
                break;
            }
            if (!IsPinned(super.GetI()-i, super.GetJ()+j, this)){
                return true;
            }
            i++;
            j++;
        }
        i = 1;
        j = 1;
        while (super.GetJ() - j >= 0 && super.GetI() - i >= 0){
            if (super.GetBoard()[super.GetI() - i][super.GetJ()-j].HasPiece() && super.GetBoard()[super.GetI() - i][super.GetJ()-j].GetColor() == super.GetColor()){
                break;
            }
            if (!IsPinned(super.GetI()-i, super.GetJ()-j, this)){
                return true;
            }
            i++;
            j++;
        }
        i = 1;
        j = 1;
        while (super.GetJ() - j >= 0 && super.GetI() + i < 8){
            if (super.GetBoard()[super.GetI() + i][super.GetJ()-j].HasPiece() && super.GetBoard()[super.GetI() + i][super.GetJ()-j].GetColor() == super.GetColor()){
                break;
            }
            if (!IsPinned(super.GetI()+i, super.GetJ()-j, this)){
                return true;
            }
            i++;
            j++;
        }
        return false;
    }
}
