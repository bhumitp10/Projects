package com.bhumitpatel.project4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final int MOVE_CALCULATED = 0;
    private static final int  SIZE = 9;

    private GridView gridView;
    private Button button;
    private TextView textView;

    private CalculateMove threadX;
    private CalculateMove threadO;
    private String[][] board;
    private String whoWon;
    private int numMoves;
    private boolean isRunning;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            int what = msg.what ;
            switch (what) {
                case MOVE_CALCULATED: // message from worker threads upon move calcuation
                    moveCalculated(msg);
                    break;
            }

        }
    }; // Handler is associated with UI Thread

    public void moveCalculated(Message msg){
        int move = msg.arg1;
        String player = (String)msg.obj;

        int[] cord = numToCord(move);
        board[cord[0]][cord[1]] = player;
        numMoves += 1;

        //System.out.println("move : " + Integer.toString(move));

        //printBoard();

        if(player.equals("X")){   // Message was received from Player X's worker thread

            ImageView imageView = (ImageView)gridView.getChildAt(move);
            imageView.setImageResource(R.drawable.x);

        }else{                  // Message was received from Player O's worker thread
            ImageView imageView = (ImageView)gridView.getChildAt(move);
            imageView.setImageResource(R.drawable.o);
        }

        boolean isWin = checkForWin();  // check for a win

        if(isWin || (numMoves  == 9)){   // if the name has been won or 9 moves have been made
            stopRunning();
            String winString = "";

            if(isWin){   // was the result a win
                winString = "Player " + whoWon + " wins !";
            }else{    // the result was a draw
                winString = "The Game is a Draw !";
            }

            textView.setText(winString);   // display the result
            return;
        }

        if(player.equals("X")){   // Message was received from Player X's worker thread
            Message msgNew = threadO.mHandler.obtainMessage(CalculateMove.MAKE_A_MOVE);
            msgNew.arg1 = move;
            threadO.mHandler.sendMessageDelayed(msgNew, 1000);
        }else{                  // Message was received from Player O's worker thread
            Message msgNew = threadX.mHandler.obtainMessage(CalculateMove.MAKE_A_MOVE);
            msgNew.arg1 = move;
            threadX.mHandler.sendMessageDelayed(msgNew, 1000);
        }

    }

    // This method will bring the board to the console.

    public void printBoard() {

        System.out.println();
        for(int i =0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                System.out.print(" " + board[i][j]);
            }
            System.out.println();
        }


        System.out.println();
    }

    // given a number, give the row and col of that number on the board

    private int[] numToCord(int x) {
        int row = ((int)(x/3));
        int col = x % 3;

        return new int[] {row,col};
    }

    // Checks if either player X or player O have won

    private boolean checkForWin() {

        // Check for horizontal win

        for(int i = 0; i < 3; i++) {

            if(board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2]) && !board[i][0].equals("b") ) {
                whoWon = board[i][0];
                return true;
            }
        }

        // Check for vertical win

        for(int i = 0; i < 3; i++) {
            if(board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i]) && !board[0][i].equals("b"))  {
                whoWon = board[0][i];
                return true;
            }
        }

        // Check for win on either of the two diagonals

        if(board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]) && !board[0][0].equals("b")) {
            whoWon = board[0][0];
            return true;
        }

        if(board[2][0].equals(board[1][1]) && board[2][0].equals(board[0][2]) && !board[2][0].equals("b")) {
            whoWon = board[2][0];
            return true;
        }


        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // gridView is set and adjusted for layout

        gridView = (GridView)findViewById(R.id.gridView);
        gridView.setHorizontalSpacing(5);
        gridView.setVerticalSpacing(60);
        gridView.setNumColumns(3);
        gridView.setAdapter(new ImageAdapter(getApplicationContext()));


        // listener is added to the button onClick to start the game

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(e->{

            if(isRunning) {   // if the game is already running, stop it
               stopRunning();
            }

            clearBoard();   // clear the board
            initBoard();    // initialize the board double array that stores the moves
            numMoves = 0;   // reset to 0
            whoWon = "";   // reset to empty string

            textView.setText("Game in Progress");  // indicate to the user the game is in progress

            // Create two different strategy class object that implement the same interface called GetMoves
            // Note : these are two different strategies one is AI_MinMax and other is AdvancedMode, both of them implement GetMoves inferace this makes it easy for me to use both in CalculateMove Class

           GetMoves HardAI = (GetMoves)(new AI_MinMax("max",1));
           GetMoves AdvancedAI = (GetMoves)(new AdvancedMode(0)) ;

           threadX = new CalculateMove(mHandler, HardAI, "X" );
           threadO = new CalculateMove(mHandler, AdvancedAI, "O");

           threadX.start();
           threadO.start();
           isRunning = true;

        });

        textView = (TextView) findViewById(R.id.textView);
        textView.setText("");

        board = new String[3][3];
        initBoard();
        numMoves = 0;
        isRunning = false;
        whoWon = "";

    }

    // Signals the looper to stopRunning of both threads
    private void stopRunning(){

        threadO.mHandler.getLooper().quit();  // quit player o thread
        threadX.mHandler.getLooper().quit();  //  quit player x thread
        mHandler.removeMessages(MOVE_CALCULATED); // remove all remaining message
        isRunning = false;  // not running anymore so set to false;

    }

    // Clear the GUI board of previous moves and now the board will be blank

    public void clearBoard(){

        for(int i = 0; i < SIZE; i++){
            ((ImageView)gridView.getChildAt(i)).setImageResource(R.drawable.tile);
        }

    }

    private void initBoard() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                board[i][j] = "b";
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(isRunning){  // If threads are running then we have to stop them
            stopRunning();
        }

    }
}