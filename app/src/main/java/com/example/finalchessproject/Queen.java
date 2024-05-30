package com.example.finalchessproject;

import android.widget.ImageView;

public class Queen extends Piece{
    public Queen(int i, int j, Piece[][] board, String color, ImageView imageView){
        super(i,j, board, color, imageView);
        if (super.GetColor() == "white"){
            super.GetImageView().setImageResource(R.drawable.whitequeen);
        }
        else{
            super.GetImageView().setImageResource(R.drawable.blackqueen);}
    }
    @Override
    public String GetFullType(){
        return super.GetColor() + "queen";
    }

    @Override
    public String GetType(){
        return "Q";
    }
    @Override
    public boolean HasPiece(){
        return true;
    }
    @Override
    public boolean CanMove(Piece piece, boolean bool){
        if (!bool){
            if (IsPinned(piece.GetI(), piece.GetJ(), this)) { return false;}
        }
        return CanMoveDiagonally(piece) || CanMoveLine(piece);
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
    //Checks if this piece can move the square it wants
    public boolean CanMoveDiagonally(Piece piece){
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
    //Checks if this piece can move the square it wants
    public boolean CanMoveLine(Piece piece) {
        if (super.GetI() != piece.GetI() && super.GetJ() != piece.GetJ()){ return false; }
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

    //Checks if this piece can save the king from checkmate
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
