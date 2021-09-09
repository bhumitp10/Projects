package com.bhumitpatel.project4;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


public class CalculateMove extends Thread{

    public static final int MAKE_A_MOVE = 0;

    public Handler mHandler;
    private Handler UIHandler;       // handler used to communicated results back to UI Thread
    private GetMoves moveCalculator;  // GetMoves is a common interface that will be used by both of the two strategy algorithm classes
    private String player;  // will be either "O" or "X"


    CalculateMove(Handler handler, GetMoves getMoves, String playerIdentifier){
        UIHandler = handler;
        moveCalculator = getMoves;
        player = playerIdentifier;
        this.setPriority(7);
    }

    public void run(){
        Looper.prepare();

        mHandler = new Handler(Looper.myLooper()){
            public void handleMessage(Message msg){
                int what = msg.what;
                switch (what){
                    case MAKE_A_MOVE:   // message to make a move
                        makeMove(msg.arg1);
                        break;
                }

            }

        };


      if(player.equals("X") ){     //  if the player is X  then make the first move since UI Thread calls start() on object of this class
          mHandler.sendMessage(  mHandler.obtainMessage(MAKE_A_MOVE, -1, 0));
      }

      Looper.loop();

    }

    // This method calculates the move to make and sends result back to UI Thread using UI handler

    public void makeMove(int move){
        Message msg = UIHandler.obtainMessage(MainActivity.MOVE_CALCULATED);

        if(move > -1){      // making any move after the first move
            moveCalculator.addOppMove(move, getOtherPlayer());
        }

        int finalMove = moveCalculator.getFinalMove();  // calculate the move
        msg.arg1 = finalMove;  // store move
        msg.obj = player;     // store the player who is making that move
        UIHandler.sendMessage(msg);  // send them message to UI thread
    }



    // Gets the string identifier of the other player

    public String getOtherPlayer(){
        return (player.equals("X")) ? "O" : "X";
    }


}
