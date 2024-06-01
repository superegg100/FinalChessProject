package com.example.finalchessproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String[] FILES = {"a", "b", "c", "d", "e", "f", "g", "h"};
    String whitePlayerName_FromIntent,whitePlayerTime_FromIntent, blackPlayerName_FromIntent, blackPlayerTime_FromIntent, winnerToDb;
    int moveIndicatorScrollViewNum = 1;
    String moveName;
    int prevColOfPawnForMoveList;
    TextView whitePlayerName, blackPlayerName, timerWhiteText, timerBlackText;
    FrameLayout frame;
    private BoardGame boardGame;
    Piece[][] pieces;
    GrayCircle[][] possibleMoves;
    CheckAlert[][] checkAlerts;
    Piece SavePiece;
    int SaveY = 0, SaveX = 0;
    int SaveI = 0, SaveJ = 0;
    ImageView iv, blackChosenPiece, whiteChosenPiece;
    RelativeLayout myRelativeLayout;
    boolean IsClicked = false;
    int turn = 0;
    long whiteTotalTime, blackTotalTime;
    boolean DidPromote = false, DidCastle = false;
    King king;
    String moveLogToAddToFireBase;
    private CountDownTimer timerWhite;
    private CountDownTimer timerBlack;
    private long remainingTimeWhite;
    private long remainingTimeBlack;
    private TextView moveLog;
    private FrameLayout blackPlayerFrame;
    private FrameLayout whitePlayerFrame;
    ImageButton PBQ, PBR, PBKN, PBB, PWR, PWB, PWQ, PWKN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gamesRef = database.getReference("Games");

        RelativeLayout.LayoutParams layoutParams = NewLayout();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        frame = findViewById(R.id.frm);
        blackPlayerFrame = findViewById(R.id.blackPlayerFrame);
        whitePlayerFrame = findViewById(R.id.whitePlayerFrame);

        Intent intentGame = getIntent();

        blackChosenPiece = findViewById(R.id.BlackChosenPiece);
        whiteChosenPiece = findViewById(R.id.WhiteChosenPiece);

        whitePlayerName = findViewById(R.id.WhitePlayerName);
        blackPlayerName = findViewById(R.id.BlackPlayerName);

        whitePlayerName_FromIntent = intentGame.getStringExtra("whiteName");
        blackPlayerName_FromIntent = intentGame.getStringExtra("blackName");

        whitePlayerName.setText(whitePlayerName_FromIntent);
        blackPlayerName.setText(blackPlayerName_FromIntent);

        whitePlayerTime_FromIntent = intentGame.getStringExtra("whiteTime");
        blackPlayerTime_FromIntent = intentGame.getStringExtra("blackTime");

        whiteTotalTime = Long.parseLong(whitePlayerTime_FromIntent);
        blackTotalTime = Long.parseLong(blackPlayerTime_FromIntent);

        remainingTimeWhite = whiteTotalTime * 60000;
        remainingTimeBlack = blackTotalTime * 60000;

        timerBlackText = findViewById(R.id.TimerBlack);
        timerWhiteText = findViewById(R.id.TimerWhite);

        timerBlackText.setText(formatTime(remainingTimeBlack));

        moveLog = findViewById(R.id.move_log);

        PBQ = findViewById(R.id.PBQ);
        PBQ.setClickable(false);
        PBQ.setVisibility(View.INVISIBLE);

        PBB = findViewById(R.id.PBB);
        PBB.setClickable(false);
        PBB.setVisibility(View.INVISIBLE);

        PBR = findViewById(R.id.PBR);
        PBR.setClickable(false);
        PBR.setVisibility(View.INVISIBLE);

        PBKN = findViewById(R.id.PBKN);
        PBKN.setClickable(false);
        PBKN.setVisibility(View.INVISIBLE);

        PWQ = findViewById(R.id.PWQ);
        PWQ.setClickable(false);
        PWQ.setVisibility(View.INVISIBLE);

        PWR = findViewById(R.id.PWR);
        PWR.setClickable(false);
        PWR.setVisibility(View.INVISIBLE);

        PWB = findViewById(R.id.PWB);
        PWB.setClickable(false);
        PWB.setVisibility(View.INVISIBLE);

        PWKN = findViewById(R.id.PWKN);
        PWKN.setClickable(false);
        PWKN.setVisibility(View.INVISIBLE);

        boardGame = new BoardGame(this, displayMetrics.widthPixels/6);

        frame.addView(boardGame);
        pieces = new Piece[8][8];
        possibleMoves = new GrayCircle[8][8];
        checkAlerts = new CheckAlert[8][8];
        SavePiece = new Piece(5, 5, pieces,"gray", null);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.imageView14);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        iv = findViewById(R.id.imageView14);
        myRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);


        for (int i = 0; i<8; i++){
            for (int j = 0; j<8; j++){
                possibleMoves[i][j] = new GrayCircle(new ImageView(MainActivity.this));
                layoutParams = NewLayout();
                layoutParams.width = width/20;
                layoutParams.height = width/20;
                layoutParams.addRule(RelativeLayout.BELOW, R.id.imageView14);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                myRelativeLayout.addView(possibleMoves[i][j].getGrayCircle(), layoutParams);
                possibleMoves[i][j].getGrayCircle().setVisibility(View.INVISIBLE);
            }
        }
        RelativeLayout.LayoutParams layoutParams2;
        for (int i =0; i < 8; i++){
            for (int j=0; j < 8; j++){
                layoutParams2 = (RelativeLayout.LayoutParams) possibleMoves[i][j].getGrayCircle().getLayoutParams();
                layoutParams2.topMargin = width/6 + (width/8)*(i-1);
                layoutParams2.rightMargin = (width/8)*(7-j);
                possibleMoves[i][j].getGrayCircle().setVisibility(View.INVISIBLE);
            }
        }

        for (int i = 0; i<8; i++){
            for (int j = 0; j<8; j++){
                checkAlerts[i][j] = new CheckAlert(new ImageView(MainActivity.this));
                layoutParams = NewLayout();
                layoutParams.width = width/20;
                layoutParams.height = width/20;
                layoutParams.addRule(RelativeLayout.BELOW, R.id.imageView14);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                myRelativeLayout.addView(checkAlerts[i][j].getAlertIcon(), layoutParams);
                checkAlerts[i][j].getAlertIcon().setVisibility(View.INVISIBLE);
            }
        }
        RelativeLayout.LayoutParams layoutParams3;
        for (int i =0; i < 8; i++){
            for (int j=0; j < 8; j++){
                layoutParams3 = (RelativeLayout.LayoutParams) checkAlerts[i][j].getAlertIcon().getLayoutParams();
                layoutParams3.topMargin = width/6 + (width/8)*(i-1);
                layoutParams3.rightMargin = (width/8)*(7-j);
                checkAlerts[i][j].getAlertIcon().setVisibility(View.INVISIBLE);
            }
        }

        pieces[0][0] = new Rook(0,0,pieces, "black", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[0][0].GetImageView());

        pieces[0][7] = new Rook(0,7,pieces, "black", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[0][7].GetImageView());

        pieces[7][0] = new Rook(7,0,pieces, "white", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[7][0].GetImageView());

        pieces[7][7] = new Rook(7,7,pieces, "white", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[7][7].GetImageView());

        pieces[0][1] = new Knight(0,1,pieces, "black", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[0][1].GetImageView());

        pieces[0][6] = new Knight(0,6,pieces, "black", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[0][6].GetImageView());

        pieces[7][1] = new Knight(7,1,pieces, "white", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[7][1].GetImageView());

        pieces[7][6] = new Knight(7,6,pieces, "white", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[7][6].GetImageView());

        pieces[0][2] = new Bishop(0,2,pieces, "black", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[0][2].GetImageView());

        pieces[0][5] = new Bishop(0,5,pieces, "black", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[0][5].GetImageView());

        pieces[7][2] = new Bishop(7,2,pieces, "white", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[7][2].GetImageView());

        pieces[7][5] = new Bishop(7,5,pieces, "white", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[7][5].GetImageView());

        pieces[0][3] = new Queen(0,3,pieces, "black", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[0][3].GetImageView());

        pieces[7][3] = new Queen(7,3,pieces, "white", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[7][3].GetImageView());

        pieces[0][4] = new King(0,4,pieces, "black", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[0][4].GetImageView());

        pieces[7][4] = new King(7,4,pieces, "white", new ImageView(MainActivity.this));
        AddToScreen(width/16, width/16, pieces[7][4].GetImageView());

        for (int j = 0; j<8; j++){
            pieces[1][j] = new Pawn(1,j,pieces, "black", new ImageView(MainActivity.this));
            AddToScreen(width/16, width/16, pieces[1][j].GetImageView());

        }
        for (int j = 0; j<8; j++){
            pieces[6][j] = new Pawn(6,j,pieces, "white", new ImageView(MainActivity.this));
            AddToScreen(width/16, width/16, pieces[6][j].GetImageView());
        }
        for (int i = 2; i <= 5; i++){
            for(int j = 0; j < 8; j++){
                pieces[i][j] = new Piece(i, j, pieces, "gray", null);
            }
        }
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if (pieces[i][j].HasPiece()){
                    createPiece(pieces[i][j], i, j);
                }
            }
        }
        RelativeLayout.LayoutParams layoutParams1;
        for (int i =0; i <= 1; i++){
            for (int j=0; j < 8; j++){
                layoutParams1 = (RelativeLayout.LayoutParams) pieces[i][j].GetImageView().getLayoutParams();
                layoutParams1.topMargin = width/6 + (width/8)*(i-1);
                layoutParams1.rightMargin = (width/8)*(7-j);
                pieces[i][j].GetImageView().setClickable(false);
            }
        }
        for (int i =6; i <= 7; i++){
            for (int j=0; j < 8; j++){
                layoutParams1 = (RelativeLayout.LayoutParams) pieces[i][j].GetImageView().getLayoutParams();
                layoutParams1.topMargin = width/6 + (width/8)*(i-1);
                layoutParams1.rightMargin = (width/8)*(7-j);
                pieces[i][j].GetImageView().setClickable(false);
            }
        }

        king = (King)FindKing("black");

        startTimer(true);
        showTurn(true);

    }

    public void AddToScreen(int width, int height, ImageView image){
        RelativeLayout.LayoutParams layoutParams;
        layoutParams = NewLayout();
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.addRule(RelativeLayout.BELOW, R.id.imageView14);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        myRelativeLayout.addView(image, layoutParams);
        image.setClickable(false);
    }

    private void showTurn(boolean isWhiteTurn) {
        if (isWhiteTurn) {
            whitePlayerFrame.setBackgroundResource(R.drawable.frame_border);
            blackPlayerFrame.setBackgroundResource(0);
        } else {
            whitePlayerFrame.setBackgroundResource(0);
            blackPlayerFrame.setBackgroundResource(R.drawable.frame_border);
        }
    }
    public void AddGameToFireBase(String winner){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gamesRef = database.getReference("Games");

        gamesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long gameCount = dataSnapshot.getChildrenCount();
                String nextGameKey = "Game " + (gameCount + 1);

                DatabaseReference currentGameRef = gamesRef.child(nextGameKey);

                Game currentGame = new Game(
                        whitePlayerName_FromIntent,
                        formatTime(whiteTotalTime * 60000),
                        blackPlayerName_FromIntent,
                        formatTime(blackTotalTime * 60000),
                        moveLogToAddToFireBase,
                        winner
                );

                currentGameRef.setValue(currentGame);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void addMoveToLog(String move) {
        String currentText = moveLog.getText().toString();
        String newText;
        if (turn % 2 != 0)
        {
            newText = currentText + move + " ";
        }
        else
        {
            newText = currentText + moveIndicatorScrollViewNum + ". " + move + " ";
            moveIndicatorScrollViewNum++;
        }
        moveLog.setText(newText);
        moveLogToAddToFireBase = (newText.substring(0, newText.length() - 1) + "#").replace("\n", " ");
    }

    public Piece FindKing(String color){
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces.length; j++) {
                if (pieces[i][j] instanceof King && pieces[i][j].GetColor() == color){
                    return pieces[i][j];
                }
            }
        }
        return null;
    }
    public void createPiece(Piece piece, int i, int j){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        android.view.ViewGroup.LayoutParams params = piece.GetImageView().getLayoutParams();
        params.width = width/8;
        params.height = width/8;
        piece.GetImageView().setLayoutParams(params);
    }
    public RelativeLayout.LayoutParams NewLayout(){
        return new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
    }
    // checks if hte piece can move to the square that i wants and i yes moves it there and check for check mate if not it returns false
    public void ActualMove(Piece piece, int i, int j){
        King king1 = FindKingColor(piece.GetColor());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        if (piece instanceof King){
            King king = (King)piece;
            if (king.CanCastleKingSide(pieces[i][j])){
                RelativeLayout.LayoutParams layoutParamsking = (RelativeLayout.LayoutParams) piece.GetImageView().getLayoutParams();
                RelativeLayout.LayoutParams layoutParamsrook = (RelativeLayout.LayoutParams) pieces[i][j].GetImageView().getLayoutParams();
                layoutParamsking.topMargin = width/6 + (width/8)*(king.GetI()-1);
                layoutParamsking.rightMargin = ((width/8));
                myRelativeLayout.removeView(piece.GetImageView());
                myRelativeLayout.addView(piece.GetImageView(), layoutParamsking);
                layoutParamsrook.topMargin = width/6 + (width/8)*(king.GetI()-1);
                layoutParamsrook.rightMargin = ((width/8) * 2);
                myRelativeLayout.removeView(pieces[i][j].GetImageView());
                myRelativeLayout.addView(pieces[i][j].GetImageView(), layoutParamsrook);
                king.MoveCastle(pieces[i][j]);
                turn++;
                addMoveToLog("O-O");
                SoundPlayer.playSound(this, R.raw.castle);
                DidCastle = true;
            }
            if (king.CanCastleQueenSide(pieces[i][j])){
                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) piece.GetImageView().getLayoutParams();
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) pieces[i][j].GetImageView().getLayoutParams();
                layoutParams1.topMargin = width/6 + (width/8)*(king.GetI()-1);
                layoutParams1.rightMargin = ((width/8) * (5));
                myRelativeLayout.removeView(piece.GetImageView());
                myRelativeLayout.addView(piece.GetImageView(), layoutParams1);
                layoutParams2.topMargin = width/6 + (width/8)*(king.GetI()-1);
                layoutParams2.rightMargin = ((width/8) * (4));
                myRelativeLayout.removeView(pieces[i][j].GetImageView());
                myRelativeLayout.addView(pieces[i][j].GetImageView(), layoutParams2);
                king.MoveCastle(pieces[i][j]);
                turn++;
                addMoveToLog("O-O-O");
                SoundPlayer.playSound(this, R.raw.castle);
                DidCastle = true;
            }
            RemoveAllPossibleMoves();

            if (turn % 2 == 0) {
                boolean isChecked = piece.IsChecked(piece.FindKing("white"));
                ShowCheckAlert((King)piece.FindKing("white"), isChecked);
                startTimer(true);
                showTurn(true);
            } else {
                boolean isChecked = piece.IsChecked(piece.FindKing("black"));
                ShowCheckAlert((King)piece.FindKing("black"), isChecked);
                startTimer(false);
                showTurn(false);
            }
        }
        RemoveCheckAlert((King)piece.FindKing("black"), false);
        RemoveCheckAlert((King)piece.FindKing("white"), false);
        if (piece.CanMove(pieces[i][j], false) && !DidCastle){
            RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) piece.GetImageView().getLayoutParams();
            layoutParams1.topMargin = width/6 + (width/8)*(i-1);
            layoutParams1.rightMargin = ((width/8) * (7-j));

            if (piece instanceof Pawn){
                Pawn pawn = (Pawn)piece;
                if (pawn.IsEnPeasnt()){
                    RemoveEatenPiece(i - pawn.ReturnPawnInc(),j);
                }
            }
            RemoveEatenPiece(i,j);
            if (pieces[i][j].GetColor() == "white" || pieces[i][j].GetColor() == "black")
            {
                SoundPlayer.playSound(this, R.raw.capture);
            }
            else
            {
                SoundPlayer.playSound(this, R.raw.move);
            }
            prevColOfPawnForMoveList = piece.GetJ();
            moveName = MoveName(piece, j, i);
            piece.Move(piece, i, j);
            addMoveToLog(moveName);
            if (piece instanceof Pawn)
            {
                IsPromotable(piece, pieces);
            }
            if((king1.IsCheckMated())){
                Intent intentWin = new Intent(MainActivity.this, AndTheWinnerIs.class);
                Intent intentGame = getIntent();
                SoundPlayer.playSound(this, R.raw.notify);
                if (king1.GetColor() == "black"){
                    winnerToDb = intentGame.getStringExtra("whiteName") + " Beat " + intentGame.getStringExtra("blackName") + " By Checkmate !";
                    AddGameToFireBase(winnerToDb);
                    intentWin.putExtra("Winner", intentGame.getStringExtra("whiteName"));
                    intentWin.putExtra("WinnerColor", "white");
                }

                else if (king1.GetColor() == "white"){
                    winnerToDb = intentGame.getStringExtra("blackName") + " Beat " + intentGame.getStringExtra("whiteName") + " By Checkmate !";
                    AddGameToFireBase(winnerToDb);
                    intentWin.putExtra("Winner", intentGame.getStringExtra("blackName"));
                    intentWin.putExtra("WinnerColor","black");
                }
                intentWin.putExtra("Way", "Checkmate");
                startActivity(intentWin);
            }
            if (IsDraw())
            {
                Intent intentDraw = new Intent(MainActivity.this, AndTheWinnerIs.class);
                winnerToDb = "Draw";
                AddGameToFireBase(winnerToDb);
                intentDraw.putExtra("Winner", "Draw");
                startActivity(intentDraw);
            }
            myRelativeLayout.removeView(piece.GetImageView());
            myRelativeLayout.addView(piece.GetImageView(), layoutParams1);
            turn++;
            RemoveAllPossibleMoves();
            RemoveCheckAlert((King)piece.FindKing("black"), false);
            RemoveCheckAlert((King)piece.FindKing("white"), false);
            if (turn % 2 == 0) {
                boolean isChecked = piece.IsChecked(piece.FindKing("white"));
                ShowCheckAlert((King)piece.FindKing("white"), isChecked);
                startTimer(true);
                showTurn(true);
            } else {
                boolean isChecked = piece.IsChecked(piece.FindKing("black"));
                ShowCheckAlert((King)piece.FindKing("black"), isChecked);
                startTimer(false);
                showTurn(false);
            }
        }
        DidCastle = false;
    }

    public Boolean IsDraw(){
        int pieceCounter = 0;
        for (int i =0; i<8; i++)
        {
            for (int j =0; j<8; j++)
            {
                if (pieces[i][j].HasPiece())
                {
                    pieceCounter++;
                }
            }
        }
        if (pieceCounter == 2)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public String MoveName(Piece piece, int col, int row) {
        if (pieces[row][col].HasPiece())
        {
            if (piece instanceof Pawn) {
                return FILES[prevColOfPawnForMoveList] + "x" + getSquareName(row, col);
            }

            return piece.GetType() + "x" + getSquareName(row, col);
        }
        else
        {
            if (piece instanceof Pawn) {
                return getSquareName(row, col);
            }

            return piece.GetType() + getSquareName(row, col);
        }
    }
    private String getSquareName(int row, int col) {
        return FILES[col] + (8 - row);
    }

    private void setImage(ImageView imageView, String imageName) {
        int imageResource = getResources().getIdentifier(imageName, "drawable", getPackageName());
        imageView.setImageResource(imageResource);
    }

    // Waits for touch and handles the logic of touch
    public boolean onTouchEvent(MotionEvent event){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int x,y;
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (event.getX() <= width && event.getX() > 0 && event.getY() > width/6 && event.getY() <= width/6  + width){
                x =  (int)event.getX() - (int)event.getX() % (width/8);
                y = ((int)event.getY() - (int)event.getY() % (width/8)) - (width/6);
                SaveY = y/(width/8);
                SaveX = x/(width/8);
                if (IsClicked){
                    SaveI = SavePiece.GetI();
                    SaveJ = SavePiece.GetJ();

                    if (!(pieces[SaveI][SaveJ].GetColor() == pieces[SaveY][SaveX].GetColor()) || (pieces[SaveI][SaveJ] instanceof King && pieces[SaveY][SaveX] instanceof Rook
                            && (((King) pieces[SaveI][SaveJ]).CanCastleKingSide(pieces[SaveY][SaveX]) ||((King) pieces[SaveI][SaveJ]).CanCastleQueenSide(pieces[SaveY][SaveX]))))
                    {
                        if (turn % 2 == 0) {
                            if (pieces[SaveI][SaveJ].GetColor() == "white") {
                                if (!DidPromote) {
                                    ActualMove(pieces[SaveI][SaveJ], SaveY, SaveX);
                                }
                            }
                        } else {
                            if (pieces[SaveI][SaveJ].GetColor() == "black") {
                                if (!DidPromote) {
                                    ActualMove(pieces[SaveI][SaveJ], SaveY, SaveX);

                                }
                            }
                        }
                        IsClicked = false;
                        RemoveAllPossibleMoves();
                    }
                    else
                    {
                        ShowChosenPiece(pieces[SaveY][SaveX]);
                        RemoveAllPossibleMoves();
                        ShowAllPossibleMoves(pieces[SaveY][SaveX]);
                        SavePiece = pieces[SaveY][SaveX];
                        IsClicked = true;
                    }
                }

                else{
                    if (pieces[SaveY][SaveX].HasPiece())
                    {
                        ShowChosenPiece(pieces[SaveY][SaveX]);
                        RemoveAllPossibleMoves();
                        ShowAllPossibleMoves(pieces[SaveY][SaveX]);
                        SavePiece = pieces[SaveY][SaveX];
                        IsClicked = true;
                    }
                    else{
                        SavePiece = new Piece(420, 69, pieces,"gray", null);
                    }
                }
            }
        }
        return true;
    }

    private void ShowAllPossibleMoves(Piece chosenPiece) {
        if (chosenPiece.GetColor() == "white")
        {
            if (turn % 2 == 0)
            {
                King king_instance = (King) chosenPiece.FindKing(chosenPiece.GetColor());
                boolean isChecked = chosenPiece.IsChecked(king_instance);
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (chosenPiece.CanMove(pieces[i][j], true)) {
                            possibleMoves[i][j].getGrayCircle().setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
        else
        {
            if (turn % 2 != 0)
            {
                King king_instance = (King) chosenPiece.FindKing(chosenPiece.GetColor());
                boolean isChecked = chosenPiece.IsChecked(king_instance);
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (chosenPiece.CanMove(pieces[i][j], true)) {
                            possibleMoves[i][j].getGrayCircle().setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }


    }
    private void RemoveAllPossibleMoves() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                possibleMoves[i][j].getGrayCircle().setVisibility(View.INVISIBLE);
            }
        }
    }

    private void ShowCheckAlert(King king, boolean isChecked) {
        if (isChecked){checkAlerts[king.GetI()][king.GetJ()].getAlertIcon().setVisibility(View.VISIBLE);}
    }
    private void RemoveCheckAlert(King king, boolean isChecked) {
        if (!isChecked){checkAlerts[king.GetI()][king.GetJ()].getAlertIcon().setVisibility(View.INVISIBLE);}
    }


    public void ShowChosenPiece(Piece chosenPiece)
    {
        String chosenPieceName = chosenPiece.GetFullType();
        if (chosenPiece.GetColor() == "white")
        {
            if (turn % 2 == 0)
            {
                setImage(whiteChosenPiece, chosenPieceName);
            }
        }
        else
        {
            if (turn % 2 != 0)
            {
                setImage(blackChosenPiece, chosenPieceName);
            }
        }
    }

    private void startTimer(boolean isWhiteTurn) {
        CountDownTimer timer;
        long remainingTime;
        if (isWhiteTurn) {
            timer = timerWhite;
            remainingTime = remainingTimeWhite;
            stopBlackTimer();
        } else {
            timer = timerBlack;
            remainingTime = remainingTimeBlack;
            stopWhiteTimer();
        }

        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(remainingTime, 1000) {
            public void onTick(long millisUntilFinished) {
                if (isWhiteTurn) {
                    remainingTimeWhite = millisUntilFinished;
                    timerWhiteText.setText(formatTime(millisUntilFinished));
                } else {
                    remainingTimeBlack = millisUntilFinished;
                    timerBlackText.setText(formatTime(millisUntilFinished));
                }
            }

            public void onFinish() {
                if (isWhiteTurn) {
                    Intent intentWin = new Intent(MainActivity.this, AndTheWinnerIs.class);
                    Intent intentGame = getIntent();
                    winnerToDb = intentGame.getStringExtra("blackName") + " Beat " + intentGame.getStringExtra("whiteName") + " By Timeout !";
                    AddGameToFireBase(winnerToDb);
                    intentWin.putExtra("Winner", intentGame.getStringExtra("blackName"));
                    intentWin.putExtra("WinnerColor","black");
                    intentWin.putExtra("Way", "Timeout");
                    startActivity(intentWin);
                } else {
                    Intent intentWin = new Intent(MainActivity.this, AndTheWinnerIs.class);
                    Intent intentGame = getIntent();
                    winnerToDb = intentGame.getStringExtra("whiteName") + " Beat " + intentGame.getStringExtra("blackName") + " By Timeout !";
                    AddGameToFireBase(winnerToDb);
                    intentWin.putExtra("Winner", intentGame.getStringExtra("whiteName"));
                    intentWin.putExtra("WinnerColor","white");
                    intentWin.putExtra("Way", "Timeout");
                    startActivity(intentWin);
                }
            }
        }.start();

        if (isWhiteTurn) {
            timerWhite = timer;
        } else {
            timerBlack = timer;
        }
    }
    private void stopWhiteTimer() {
        if (timerWhite != null) {
            timerWhite.cancel();
            timerWhite = null;
        }
    }

    private void stopBlackTimer() {
        if (timerBlack != null) {
            timerBlack.cancel();
            timerBlack = null;
        }
    }
    private String formatTime(long millis) {
        int seconds = (int) (millis / 1000) % 60;
        int minutes = (int) ((millis / (1000 * 60)) % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }
    public void RemoveEatenPiece(int i, int j){
        myRelativeLayout.removeView(pieces[i][j].GetImageView());
    }
    public King FindKingColor(String color){
        if (color == "white"){
            return  (King)FindKing("black");
        }
        else{
            return (King)FindKing("white");
        }
    }

    public void IsPromotable(Piece piece, Piece[][] pieces){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        if (piece.GetI() == 7)
        {
            DidPromote = true;
            PBQ.setVisibility(View.VISIBLE);
            PBQ.setClickable(true);
            PBB.setVisibility(View.VISIBLE);
            PBB.setClickable(true);
            PBR.setVisibility(View.VISIBLE);
            PBR.setClickable(true);
            PBKN.setVisibility(View.VISIBLE);
            PBKN.setClickable(true);
            PBQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout.LayoutParams layoutParamsP = NewLayout();
                    RemoveEatenPiece(7, piece.GetJ());
                    pieces[7][piece.GetJ()] = new Queen(7,piece.GetJ(),pieces, piece.GetColor(),  new ImageView(MainActivity.this));
                    layoutParamsP = NewLayout();
                    layoutParamsP.width = width/8;
                    layoutParamsP.height = width/8;
                    layoutParamsP.addRule(RelativeLayout.BELOW, R.id.imageView14);
                    layoutParamsP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    myRelativeLayout.addView(pieces[7][piece.GetJ()].GetImageView(), layoutParamsP);
                    layoutParamsP.topMargin = width/6 + (width/8)*(piece.GetI()-1);
                    layoutParamsP.rightMargin = (width/8)*(7- piece.GetJ());
                    PBQ.setVisibility(View.INVISIBLE);
                    PBQ.setClickable(false);
                    PBB.setVisibility(View.INVISIBLE);
                    PBB.setClickable(false);
                    PBR.setVisibility(View.INVISIBLE);
                    PBR.setClickable(false);
                    PBKN.setVisibility(View.INVISIBLE);
                    PBKN.setClickable(false);
                    DidPromote = false;
                }
            });
            PBB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout.LayoutParams layoutParamsP = NewLayout();
                    RemoveEatenPiece(7, piece.GetJ());
                    pieces[7][piece.GetJ()] = new Bishop(7,piece.GetJ(),pieces, piece.GetColor(),  new ImageView(MainActivity.this));
                    layoutParamsP = NewLayout();
                    layoutParamsP.width = width/8;
                    layoutParamsP.height = width/8;
                    layoutParamsP.addRule(RelativeLayout.BELOW, R.id.imageView14);
                    layoutParamsP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    myRelativeLayout.addView(pieces[7][piece.GetJ()].GetImageView(), layoutParamsP);
                    layoutParamsP.topMargin = width/6 + (width/8)*(piece.GetI()-1);
                    layoutParamsP.rightMargin = (width/8)*(7- piece.GetJ());
                    PBQ.setVisibility(View.INVISIBLE);
                    PBQ.setClickable(false);
                    PBB.setVisibility(View.INVISIBLE);
                    PBB.setClickable(false);
                    PBR.setVisibility(View.INVISIBLE);
                    PBR.setClickable(false);
                    PBKN.setVisibility(View.INVISIBLE);
                    PBKN.setClickable(false);
                    DidPromote = false;
                }
            });
            PBR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout.LayoutParams layoutParamsP = NewLayout();
                    RemoveEatenPiece(7, piece.GetJ());
                    pieces[7][piece.GetJ()] = new Rook(7,piece.GetJ(),pieces, piece.GetColor(),  new ImageView(MainActivity.this));
                    layoutParamsP = NewLayout();
                    layoutParamsP.width = width/16;
                    layoutParamsP.height = width/16;
                    layoutParamsP.addRule(RelativeLayout.BELOW, R.id.imageView14);
                    layoutParamsP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    myRelativeLayout.addView(pieces[7][piece.GetJ()].GetImageView(), layoutParamsP);
                    layoutParamsP.topMargin = width/6 + (width/8)*(piece.GetI()-1);
                    layoutParamsP.rightMargin = (width/8)*(7- piece.GetJ());
                    PBQ.setVisibility(View.INVISIBLE);
                    PBQ.setClickable(false);
                    PBB.setVisibility(View.INVISIBLE);
                    PBB.setClickable(false);
                    PBR.setVisibility(View.INVISIBLE);
                    PBR.setClickable(false);
                    PBKN.setVisibility(View.INVISIBLE);
                    PBKN.setClickable(false);
                    DidPromote = false;
                }
            });
            PBKN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout.LayoutParams layoutParamsP = NewLayout();
                    RemoveEatenPiece(7, piece.GetJ());
                    pieces[7][piece.GetJ()] = new Knight(7,piece.GetJ(),pieces, piece.GetColor(),  new ImageView(MainActivity.this));
                    layoutParamsP = NewLayout();
                    layoutParamsP.width = width/8;
                    layoutParamsP.height = width/8;
                    layoutParamsP.addRule(RelativeLayout.BELOW, R.id.imageView14);
                    layoutParamsP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    myRelativeLayout.addView(pieces[7][piece.GetJ()].GetImageView(), layoutParamsP);
                    layoutParamsP.topMargin = width/6 + (width/8)*(piece.GetI()-1);
                    layoutParamsP.rightMargin = (width/8)*(7- piece.GetJ());
                    PBQ.setVisibility(View.INVISIBLE);
                    PBQ.setClickable(false);
                    PBB.setVisibility(View.INVISIBLE);
                    PBB.setClickable(false);
                    PBR.setVisibility(View.INVISIBLE);
                    PBR.setClickable(false);
                    PBKN.setVisibility(View.INVISIBLE);
                    PBKN.setClickable(false);
                    DidPromote = false;
                }
            });

        }
        if (piece.GetI() == 0)
        {
            DidPromote = true;
            PWQ.setVisibility(View.VISIBLE);
            PWQ.setClickable(true);
            PWB.setVisibility(View.VISIBLE);
            PWB.setClickable(true);
            PWR.setVisibility(View.VISIBLE);
            PWR.setClickable(true);
            PWKN.setVisibility(View.VISIBLE);
            PWKN.setClickable(true);
            PWQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout.LayoutParams layoutParamsP = NewLayout();
                    RemoveEatenPiece(0, piece.GetJ());
                    pieces[0][piece.GetJ()] = new Queen(0,piece.GetJ(),pieces, piece.GetColor(),  new ImageView(MainActivity.this));
                    layoutParamsP = NewLayout();
                    layoutParamsP.width = width/8;
                    layoutParamsP.height = width/8;
                    layoutParamsP.addRule(RelativeLayout.BELOW, R.id.imageView14);
                    layoutParamsP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    myRelativeLayout.addView(pieces[0][piece.GetJ()].GetImageView(), layoutParamsP);
                    layoutParamsP.topMargin = width/6 + (width/8)*(piece.GetI()-1);
                    layoutParamsP.rightMargin = (width/8)*(7- piece.GetJ());
                    PWQ.setVisibility(View.INVISIBLE);
                    PWQ.setClickable(false);
                    PWB.setVisibility(View.INVISIBLE);
                    PWB.setClickable(false);
                    PWR.setVisibility(View.INVISIBLE);
                    PWR.setClickable(false);
                    PWKN.setVisibility(View.INVISIBLE);
                    PWKN.setClickable(false);
                    DidPromote = false;
                }
            });
            PWB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout.LayoutParams layoutParamsP = NewLayout();
                    RemoveEatenPiece(0, piece.GetJ());
                    pieces[0][piece.GetJ()] = new Bishop(0,piece.GetJ(),pieces, piece.GetColor(),  new ImageView(MainActivity.this));
                    layoutParamsP = NewLayout();
                    layoutParamsP.width = width/8;
                    layoutParamsP.height = width/8;
                    layoutParamsP.addRule(RelativeLayout.BELOW, R.id.imageView14);
                    layoutParamsP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    myRelativeLayout.addView(pieces[0][piece.GetJ()].GetImageView(), layoutParamsP);
                    layoutParamsP.topMargin = width/6 + (width/8)*(piece.GetI()-1);
                    layoutParamsP.rightMargin = (width/8)*(7- piece.GetJ());
                    PWQ.setVisibility(View.INVISIBLE);
                    PWQ.setClickable(false);
                    PWB.setVisibility(View.INVISIBLE);
                    PWB.setClickable(false);
                    PWR.setVisibility(View.INVISIBLE);
                    PWR.setClickable(false);
                    PWKN.setVisibility(View.INVISIBLE);
                    PWKN.setClickable(false);
                    DidPromote = false;
                }
            });
            PWR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout.LayoutParams layoutParamsP = NewLayout();
                    RemoveEatenPiece(0, piece.GetJ());
                    pieces[0][piece.GetJ()] = new Rook(0,piece.GetJ(),pieces, piece.GetColor(),  new ImageView(MainActivity.this));
                    layoutParamsP = NewLayout();
                    layoutParamsP.width = width/8;
                    layoutParamsP.height = width/8;
                    layoutParamsP.addRule(RelativeLayout.BELOW, R.id.imageView14);
                    layoutParamsP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    myRelativeLayout.addView(pieces[0][piece.GetJ()].GetImageView(), layoutParamsP);
                    layoutParamsP.topMargin = width/6 + (width/8)*(piece.GetI()-1);
                    layoutParamsP.rightMargin = (width/8)*(7- piece.GetJ());
                    PWQ.setVisibility(View.INVISIBLE);
                    PWQ.setClickable(false);
                    PWB.setVisibility(View.INVISIBLE);
                    PWB.setClickable(false);
                    PWR.setVisibility(View.INVISIBLE);
                    PWR.setClickable(false);
                    PWKN.setVisibility(View.INVISIBLE);
                    PWKN.setClickable(false);
                    DidPromote = false;
                }
            });
            PWKN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout.LayoutParams layoutParamsP = NewLayout();
                    RemoveEatenPiece(0, piece.GetJ());
                    pieces[0][piece.GetJ()] = new Knight(0,piece.GetJ(),pieces, piece.GetColor(),  new ImageView(MainActivity.this));
                    layoutParamsP = NewLayout();
                    layoutParamsP.width = width/8;
                    layoutParamsP.height = width/8;
                    layoutParamsP.addRule(RelativeLayout.BELOW, R.id.imageView14);
                    layoutParamsP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    myRelativeLayout.addView(pieces[0][piece.GetJ()].GetImageView(), layoutParamsP);
                    layoutParamsP.topMargin = width/6 + (width/8)*(piece.GetI()-1);
                    layoutParamsP.rightMargin = (width/8)*(7- piece.GetJ());
                    PWQ.setVisibility(View.INVISIBLE);
                    PWQ.setClickable(false);
                    PWB.setVisibility(View.INVISIBLE);
                    PWB.setClickable(false);
                    PWR.setVisibility(View.INVISIBLE);
                    PWR.setClickable(false);
                    PWKN.setVisibility(View.INVISIBLE);
                    PWKN.setClickable(false);
                    DidPromote = false;
                }
            });
        }
    }
}