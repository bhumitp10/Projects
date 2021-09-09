package com.bhumitpatel.project4;

import java.util.ArrayList;
import java.util.Random;

public class AdvancedMode implements GetMoves{
	private String [][]board;
	private ArrayList<Integer> spots;
	private int o_or_x;
	private int defensive_move;
	private int offensive_move;
	
	AdvancedMode(int choice){
		board = new String[3][3];
		o_or_x = choice;
		spots = new ArrayList<Integer>();
		initBoard();
		
		for(int i = 0; i < 9; i++) {
			spots.add(i);
		}			
	}
	
	private void initBoard() {
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				board[i][j] = "b";
			}
		}
	}

	
	public int getFinalMove() {
		
		if(offensive_move()) {
			spots.remove( ((Object)(offensive_move)) );
			int [] arr = numToCord(offensive_move);
			board[arr[0]][arr[1]]= (o_or_x == 0)? "O" : "X";
			return offensive_move;
		}else if(defensive_move()){
			spots.remove( ((Object)(defensive_move))  );
			int [] arr = numToCord(defensive_move);
			board[arr[0]][arr[1]]= (o_or_x == 0)? "O" : "X";
			return defensive_move;
			
		} 
		
		int i = random_move();
		int move = spots.get(i);
		int [] arr = numToCord(move);
		board[arr[0]][arr[1]] = (o_or_x == 0)? "O" : "X";
		spots.remove(  ((Object)(move)) );
		
		return move;
	}
	
	public int[] numToCord(int x) {
		int row = ((int)(x/3));
		int col = x % 3;
				
		return new int[] {row,col};
	}
	
	
	private boolean defensive_move() {
		
		String str = "X";
		if(o_or_x == 0) {
			str = "O" ;
		}
		
		// defending horizontal win move
		
		
		for(int i = 0; i < 3; i++) {		
			if(  (!board[i][0].equals("b")) && (board[i][2].equals("b"))  &&  (board[i][0].equals(board[i][1])) && (!str.equals(board[i][0]))  ) {
				defensive_move = i*3 + 2;
				return true;
			}else if( (!board[i][0].equals("b")) && (board[i][1].equals("b")) && (board[i][0].equals(board[i][2])) && (!str.equals(board[i][0])) ) {
				defensive_move = i*3 + 1;
				return true;
			}else if((!board[i][1].equals("b")) && (board[i][0].equals("b")) && (board[i][2].equals(board[i][1])) && (!str.equals(board[i][1])) ) {
				defensive_move = i*3 ;
				return true;
			}
		}
		
		// defending vertical win move

		
		for(int i = 0; i < 3; i++) {
			
			if(  (!board[0][i].equals("b")) && (board[2][i].equals("b")) && (board[0][i].equals(board[1][i])) && (!str.equals(board[0][i]))  ) {
				defensive_move = i + 6;
				return true;
			}else if( (!board[0][i].equals("b")) && (board[1][i].equals("b")) && ( board[0][i].equals(board[2][i])) && (!str.equals(board[0][i])) ) {
				defensive_move = i + 3;
				return true;
			}else if((!board[1][i].equals("b")) &&  (board[0][i].equals("b")) && ( board[2][i].equals(board[1][i])) && (!str.equals(board[1][i])) ) {
				defensive_move = i ;
				return true;
			}		
			
		}
		
		return false;	
	}
	
	private boolean offensive_move() {
		String str = "X";
		if(o_or_x == 0) {
			str = "O" ;
		}
			
		// looking for horizontal win
		
		for(int i = 0; i < 3; i++) {		
			if(  (!board[i][0].equals("b")) &&  (board[i][2].equals("b")) && (board[i][0].equals(board[i][1])) && (str.equals(board[i][0]))  ) {
				offensive_move = i*3 + 2;
				return true;
			}else if( (!board[i][0].equals("b")) &&   (board[i][1].equals("b"))  && ( board[i][0].equals(board[i][2])) && (str.equals(board[i][0])) ) {
				offensive_move = i*3 + 1;
				return true;
			}else if((!board[i][1].equals("b")) &&  (board[i][0].equals("b"))  && (board[i][2].equals(board[i][1])) && (str.equals(board[i][1]))) {
				offensive_move = i*3 ;
				return true;
			}
		}
		
		// looking for vertical win
		
		for(int i = 0; i < 3; i++) {
			
			if(  (!board[0][i].equals("b")) &&  (board[2][i].equals("b"))  && ( board[0][i].equals(board[1][i])) && (str.equals(board[0][i]))  ) {
				offensive_move = i + 6;
				return true;
			}else if( (!board[0][i].equals("b")) &&  (board[1][i].equals("b")) && ( board[0][i].equals(board[2][i])) && (str.equals(board[0][i])) ) {
				offensive_move = i + 3;
				return true;
			}else if((!board[1][i].equals("b")) &&  (board[0][i].equals("b"))  && ( board[2][i].equals(board[1][i])) && (str.equals(board[1][i]))) {
				offensive_move = i ;
				return true;
			}
		
		}
		
		return false;	
		
	}
	
	private int random_move() {
		Random rand = new Random();
		int index = rand.nextInt( spots.size() );
		
		return index;
	}

	@Override
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

	@Override
	public void addOppMove(int x, String s) {
		int arr[] = numToCord(x);
		board[arr[0]][arr[1]] = s;
		spots.remove(((Object)(x)));
	}

	@Override
	public void resetAll() {
		spots = new ArrayList<Integer>();
		initBoard();
		
		for(int i = 0; i < 9; i++) {
			spots.add(i);
		}		
	}
	
	
	
}
