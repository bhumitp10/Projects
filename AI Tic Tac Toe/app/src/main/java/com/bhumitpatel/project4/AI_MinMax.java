package com.bhumitpatel.project4;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * This class is used to read in a state of a tic tac toe board. It creates a MinMax object and passes the state to it. What returns is a list 
 * of possible moves for the player X that have been given min/max values by the method findMoves. The moves that can result in a win or a 
 * tie for X are printed out with the method printBestMoves()
 * 
 * @author Mark Hallenbeck
 *
 * Copyright© 2014, Mark Hallenbeck, All Rights Reservered.
 *
 */
public class AI_MinMax implements GetMoves{
	
	private String[] init_board;
	
	private ArrayList<Node> movesList;
	
	private String player;
	
	private int min_max;
	
	private int move;
	
	private String[][] board;
	
	private int i1;
	
	AI_MinMax( String s, int i )
	{
		//init_board = getBoard();
		String delim = "[ ]+";
		String str = "b b b b b b b b b";
		init_board = str.split(delim);   // this will represent a tic tac toe board as an array
		player = s;
	
		board = new String[3][3];  // this will represent a tic tac toe board as a 3x3 matrix
	

		if(i == 0) {
			min_max = -10;    // we are looking for player O's win
		}else {  
			min_max = 10;    // we are looking for player X's win
		}
			
		i1 = i;
		

	}
	
	// This method will bring the board to the console.
	
	public void printBoard() {
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				
				board[i][j] = init_board[i*3+j];
				
			}
		}
		
		System.out.println();
		for(int i =0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				System.out.print(" " + board[i][j]);
			}
			System.out.println();
		}
		
		
		System.out.println();
		System.out.println ("The move number is " + move);
	}
	
	// This method is used to get the best tic tac toe move currently possible.
	
	public int getFinalMove() {
		

		MinMax sendIn_InitState = new MinMax(init_board, player,i1 ); 
		
		movesList = sendIn_InitState.findMoves(); // get the list of all the moves possible
				
		ArrayList<Integer> moves = new ArrayList<Integer>();  // list to store all the best moves
		
		getWinMoves(moves); // get all the moves that are winning
		
		if(moves.size() == 0) {  // cannot get any winning moves
			getDrawMoves(moves);  // get all the draw moves
		}
		
		Random rand = new Random();
		int randomNum = rand.nextInt(moves.size()); // get a random int in range of the array-list moves's indices
			
		move = moves.get(randomNum);  // get a random move from the best possible moves
		String s = (player.equals("max")) ? "X" : "O";   // what player are we making the move for
		
		init_board[move-1] = s;     // store the move made in the array being used as a board
		int[] arr = numToCord(move-1);  
		board[arr[0]][arr[1]] = s; // store the move in the 3x3 matrix representing a tic tac toe board

		return move - 1; // return the move
		
	}
	
	// This method gets all the winning moves from the arraylist movesList where all the possible moves are stored 
	
	private void getWinMoves(ArrayList<Integer> moves) {
		for(int i = 0; i < movesList.size(); i++) {
			if(movesList.get(i).getMinMax() == min_max) {  // the move is winning
				moves.add(movesList.get(i).getMovedTo());   // add the move
			}
		}
		
	}
	
	// This method gets all the drawing moves from the arraylist movesList where all the possible moves are stored 
	
	private void getDrawMoves(ArrayList<Integer> moves) {
		for(int i = 0; i < movesList.size(); i++) {
			if(movesList.get(i).getMinMax() == 0) {  // the move is drawing
				moves.add(movesList.get(i).getMovedTo());  // add the move
			}
		}
		
	}
	

	// This method is used to add a move to the board in the current object, this method is usually used to add the move made by the opponent. 

	public void addOppMove(int x, String s) {
		init_board[x] = s;
		int arr[] = numToCord(x);
		board[arr[0]][arr[1]] = s;
	}
	
	public int[] numToCord(int x) {
		int row = ((int)(x/3));
		int col = x % 3;
				
		return new int[] {row,col};
	}
	
	/**
	 * goes through a node list and prints out the moves with the best result for player X
	 * checks the min/max function of each state and only recomends a path that leads to a win or tie
	 */
	private void printBestMoves()
	{
		System.out.print("\n\nThe moves list is: < ");
		
		for(int x = 0; x < movesList.size(); x++)
		{
			Node temp = movesList.get(x);
			
			if(temp.getMinMax() == -10 || temp.getMinMax() == 0)
			{
				System.out.print(temp.getMovedTo() + " ");
			}
		}
		
		System.out.print(">");
	}
	
	// This method will reset everything, so a tic tac toe game can be played again.

	@Override
	public void resetAll() {
		String delim = "[ ]+";
		String str = "b b b b b b b b b";
		init_board = str.split(delim);
	
		board = new String[3][3];
		
	}
	
	// given a number, give the row and col of that number on the board
	
	public String[][] getBoard() {
		return board.clone();
	}

}
