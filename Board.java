import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Board --- main class for program to play checkers
 * @author mwizard777
 */
public class Board {
	private Checker[][] checkers; //an array containing the positions of all the checkers (with null in places with no checkers)
	private int player; //the number of the player whose turn it is
	private boolean gameover; //whether or not the game has ended
	
	/**
	 * Starts a game of checkers, and has players make moves until one of them wins.
	 * @param args A string array containing the command
	 * line arguments (which should be none).
	 * @return No return value.
	 */
	public static void main(String[] args){
		Board board = new Board(); //initialize the game board
		Scanner reader = new Scanner(System.in); //initialize a scanner to read user input
		System.out.println("Player 1 is red.\n");
		System.out.println(board);
		System.out.println();
		String moveposition; //the next position in a player's move, as a coordinate in the form [letter][number]
		List<String> move = new ArrayList<String>(); //the sequence of positions making up a player's move
		//while the game is not over, have the player whose turn it is make a move
		while(!board.gameover){
			move.clear(); //reset the move
			System.out.print("Enter the coordinates of the checker you wish to move:");
			moveposition = reader.nextLine(); //get the position of the checker the player wishes to move
			//while there is an input, if it's valid, add it to move
			while(!moveposition.isEmpty()){
				try{
					if(moveposition.length() != 2) throw new IllegalArgumentException(); //throw an exception if moveposition does not have length two
					else{
						//throw an exception if moveposition does not correspond to a square on the board
						if((int)moveposition.charAt(0) - 96 > 8 || (int)moveposition.charAt(0) - 96 < 1) throw new IllegalArgumentException();
						if((int)moveposition.charAt(1) - 48 > 8 || (int)moveposition.charAt(0) - 48 < 1) throw new IllegalArgumentException();
					}
				}catch(IllegalArgumentException e){
					System.out.print("That is not a valid coordinate. Coordinates should be in the form [letter][number].\nExample: a3\nEnter a valid coordinate:");
					moveposition = reader.nextLine(); //attempt to get valid coordinate
					continue; //skip adding moveposition to move (since it's invalid)
				}
				move.add(moveposition); //add moveposition to the sequence of moves
				System.out.print("Enter the coordinates of the next square you want your checker to jump to,\nor press enter if there are no more jumps: ");
				moveposition = reader.nextLine(); //get the next position the player wants to move the checker to
			}
			String[] a = new String[1]; //variable of type String array used to convert move to a String array
			try{
				board.makeMove(move.toArray(a)); //attempt to make a move
			}catch(IllegalArgumentException e){
				//if the move is invalid, tell the player and have them input another move
				System.out.println(e.getMessage());
				continue;
			}
			board.player = 3 - board.player; //switch which player's turn it is
			if(!board.availableCaptureMoves() && !board.availableNonCaptureMoves()) board.gameover = true; //end the game if the player has no moves
			System.out.println(board);
			System.out.println();
		}
		reader.close();
		System.out.println("Gameover. Player " + Integer.toString(3 - board.player) + " won!");
	}
	
	/***
	 * Default constructor.
	 */
	public Board(){
		player = 1;
		gameover = false;
		checkers = new Checker[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(((i+j) % 2 == 0) || (i > 2 && i < 5)) checkers[i][j] = null;
				else {
					checkers[i][j] = (i > 4) ? new Checker(i, j, true) : new Checker(i, j, false);
				}
			}
		}
	}
	
	/**
	 * Copy constructor.
	 * @param board The board which is being duplicated.
	 */
	public Board(Board board){
		Checker[][] checkers = new Checker[8][8];
		//copy board.checkers to checkers
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(board.checkers[i][j] == null) checkers[i][j] = null;
				else checkers[i][j] = new Checker(board.checkers[i][j]);
			}
		}
		this.checkers = checkers;
		player = board.player;
		gameover = board.gameover;
	}
	
	/**
	 * Attempt to move the checker identified by the first string in positions
	 * along the sequence of squares in positions.
	 * @param positions An array of strings where each string
	 * is a coordinate in the form [letter][number] corresponding
	 * to a square on the game board.
	 * @throws IllegalArgumentException if the move is not valid.
	 */
	public void makeMove(String[] positions) throws IllegalArgumentException{
		if(positions.length < 2) throw new IllegalArgumentException("You need at least two coordinates for a valid move.");
		int[][] coordinates = new int[positions.length][2]; //array of ints to replace positions
		//convert the characters in positions into row and column numbers
		for (int i = 0; i < positions.length; i++){
			coordinates[i][0] = (int)positions[i].charAt(0) - 96;
			coordinates[i][1] = (int)positions[i].charAt(1) - 48;
		}
		char moveType = checkValidMove(coordinates); //get what type of move the player made
		//if the move is a non capture, throw an error if a capture is available, otherwise execute the move
		if(moveType == 'n'){
			if(availableCaptureMoves()) throw new IllegalArgumentException("That is not a valid move, you must capture.");
			else swapCheckers(coordinates[0],coordinates[coordinates.length - 1]);
		}
		//if the move is a capture, execute the move and remove all the checkers which are taken
		else if(moveType == 'c'){
			swapCheckers(coordinates[0], coordinates[coordinates.length - 1]);
			//loop over each jump the checker makes
			for(int i = 1; i < coordinates.length; i++){
				//in case the checker is a king, loop over all squares between the start and end coordinates and set those squares to null
				for(int j = 1; j < Math.abs(coordinates[i-1][1] - coordinates[i][1]); j++){
					checkers[(int) (8 - coordinates[i-1][1] + Math.signum(coordinates[i-1][1] - coordinates[i][1]) * j)][(int) (coordinates[i-1][0] - 1 - Math.signum(coordinates[i-1][0] - coordinates[i][0]) * j)] = null;
				}
			}
		}
		//if the move is neither a capture or non capture, throw an error
		else throw new IllegalArgumentException("That is not a valid move.");
	}
	
	/**
	 * Moves the checker at start to end.
	 * @param start The coordinates (from the user perspective) of the checker being swapped.
	 * @param end The coordinates (from the user perspective) of the destination for the checker.
	 */
	public void swapCheckers(int[] start, int[] end){
		int startrow = 8 - start[1]; //the starting row of the checker
		int startcol = start[0] - 1; //the starting column of the checker
		int endcol = end[0] - 1; //the ending column of the checker
		int endrow = 8 - end[1]; //the ending row of the checker
		//move the checker from the start row and column to the end row and column
		checkers[startrow][startcol].setColumn(endcol);
		checkers[startrow][startcol].setRow(endrow);
		checkers[endrow][endcol] = checkers[startrow][startcol];
		checkers[startrow][startcol] = null;
	}
	
	/**
	 * Checks if a given sequence of moves is valid.
	 * @param moves A possible sequence of jumps for a checker.
	 * @return The type of move the checker is making - 
	 * 'f' for invalid, 'n' for non-capture, and 'c' for capture.
	 */
	public char checkValidMove(int[][] moves){
		Checker startChecker = checkers[8 - moves[0][1]][moves[0][0] - 1]; //the checker being moved
		if(startChecker == null) return 'f'; //check if there is a checker at the starting square
		if(moves.length == 2) if(checkNonCapture(moves)) return 'n';
		if(checkCapture(moves)) return 'c';
		return 'f';
	}
	
	/**
	 * Checks if the given sequence of moves is a valid non-capture move.
	 * @param moves The sequence of jumps of the checker, 
	 * with the coordinates being from the user's perspective.
	 * @return True if the moves are a valid non-capture move, false otherwise.
	 */
	public boolean checkNonCapture(int[][] moves){
		Checker startChecker = checkers[8 - moves[0][1]][moves[0][0] - 1]; //the checker being moved
		if(startChecker == null) return false; //check if there is a starting checker
		if(Math.abs(moves[0][1] - moves[1][1]) != Math.abs(moves[0][0] - moves[1][0])) return false; //check if the checker is moving diagonally
		if(checkers[8 - moves[1][1]][moves[1][0] - 1] != null) return false; //check if the ending position already has a checker
		if(startChecker.isKing()){
			int numCheckersBetween = 0; //a counter for the number of checkers the starting checker jumps over
			//loop over each square the checker jumps over
			for(int i = 1; i <= Math.abs(moves[0][1] - moves[1][1]); i++){
				if(checkers[(int) (8 - moves[0][1] + Math.signum(moves[0][1] - moves[1][1]) * i)][(int) (moves[0][0] - 1 - Math.signum(moves[0][0] - moves[1][0]) * i)] != null) numCheckersBetween++;
			}
			if(numCheckersBetween > 0) return false; //return false if a checker was blocking the starting checker's move
		}else{
			if(Math.abs(moves[0][1] - moves[1][1]) != 1 || Math.abs(moves[0][0] - moves[1][0]) != 1) return false; //check if the checker moved more than one square
			if(startChecker.isRed()){
				if(moves[1][1] < moves[0][1]) return false; //check if the checker is moving the right direction
			}else{
				if(moves[1][1] > moves[0][1]) return false; //check if the checker is moving the right direction
			}
		}
		//
		int kingRow = (startChecker.isRed()) ? 8 : 1; //the number of the row where the checker becomes a king
		if(moves[1][1] == kingRow) startChecker.setIsKing(true); //if the checker moved onto the kinging row, make it a king
		return true; //if the checker passed all the above tests, then the non capture is valid
	}
	
	/**
	 * Checks if the given sequence of moves is a valid capture.
	 * @param moves The sequence of jumps of the checker, 
	 * with the coordinates being from the user's perspective.
	 * @return True if the moves are a valid capture, false otherwise.
	 */
	public boolean checkCapture(int[][] moves){
		boolean makeKing = false; //whether the starting checker should be made a king or not
		Board _board = new Board(this); //make a copy of the board (so it can be manipulated)
		Checker startChecker = _board.checkers[8 - moves[0][1]][moves[0][0] - 1]; //the checker being moved
		if(startChecker == null) return false; //check if there is a starting checker
		//loop over each jump
		for(int i = 1; i < moves.length; i++){
			if(_board.checkers[8 - moves[i][1]][moves[i][0] - 1] != null) return false; //check if there is already a checker where the starting checker is jumping to
			if(startChecker.isKing()){
				if(Math.abs(moves[i][0] - moves[i-1][0]) != Math.abs(moves[i][1] - moves[i-1][1])) return false; //check if the checker is moving diagonally
				int numCheckersBetween = 0; //a counter for the number of checkers the starting checker jumps over (the same color counts as two)
				//loop over each square the starting checker jumps over
				for(int j = 1; j <= Math.abs(moves[i-1][1] - moves[i][1]); j++){
					//if there is a checker on that square, add 2 if it's the same color, 1 otherwise
					if(_board.checkers[(int) (8 - moves[i-1][1] + Math.signum(moves[i-1][1] - moves[i][1]) * j)][(int) (moves[i-1][0] - 1 - Math.signum(moves[i-1][0] - moves[i][0]) * j)] != null){
						numCheckersBetween = (_board.checkers[(int) (8 - moves[i-1][1] + Math.signum(moves[i-1][1] - moves[i][1]) * j)][(int) (moves[i-1][0] - 1 - Math.signum(moves[i-1][0] - moves[i][0]) * j)].isRed() == startChecker.isRed()) ? numCheckersBetween + 2 : numCheckersBetween + 1;
						_board.checkers[(int) (8 - moves[i-1][1] + Math.signum(moves[i-1][1] - moves[i][1]) * j)][(int) (moves[i-1][0] - 1 - Math.signum(moves[i-1][0] - moves[i][0]) * j)].setIsRed(startChecker.isRed()); //make the checker jumped over the same color as the starting checker, so it can't be jumped over twice
					}
				}
			if(numCheckersBetween != 1) return false; //for the move to be a valid capture, numCheckersBetween must be exactly 1 (one checker of the other color jumped over)
			}
			else{
				//check if the checker jumps exactly two squares diagonally
				if(Math.abs(moves[i][0] - moves[i-1][0]) != 2) return false;
				if(Math.abs(moves[i][1] - moves[i-1][1]) != 2) return false;
				if(_board.checkers[8 - (moves[i][1] + moves[i-1][1]) / 2][(moves[i][0] + moves[i-1][0]) / 2 - 1] == null || !(_board.checkers[8 - (moves[i][1] + moves[i-1][1]) / 2][(moves[i][0] + moves[i-1][0]) / 2 - 1].isRed() ^ startChecker.isRed())) return false; //check if the square it jumps over is empty or has a checker of the same color
				else _board.checkers[8 - (moves[i][1] + moves[i-1][1]) / 2][(moves[i][0] + moves[i-1][0]) / 2 - 1].setIsRed(startChecker.isRed()); //makes the checker jumped over the same color as the starting checker so it can't be jumped over twice
			}
			//if the checker landed on the kinging row, make it a king, and set makeKing true
			int kingRow = (startChecker.isRed()) ? 8 : 1;
			if(moves[i][1] == kingRow){
				makeKing = true; //the orignial checker is not yet made a king since the move could still be invalid
				startChecker.setIsKing(true); //the copy checker is made a king
			}
		}
		_board.swapCheckers(moves[0], moves[moves.length-1]);
		if(_board.checkers[8 - moves[moves.length-1][1]][moves[moves.length-1][0] - 1].availableCaptures(_board)) return false; //check if the checker can make any more captures
		if(makeKing) checkers[8 - moves[0][1]][moves[0][0] - 1].setIsKing(true); //if the move is valid, and makeKing is true, make the actual starting checker a king
		return true;
	}
	
	/**
	 * Checks if there are any possible capture moves the current player can make.
	 * @return True if there is at least one capture move, false otherwise.
	 */
	public boolean availableCaptureMoves(){
		boolean playerIsRed = (player == 1) ? true : false; //the color of the player (true if red, false otherwise)
		Checker currChecker; // the current checker being processed for available capture moves
		//loop over all rows
		for(int i = 0; i < 8; i++){
			//loop over half the columns (since the checkers can only be on half of the board)
			for(int j = 0; j < 4; j++){
				currChecker = checkers[i][2 * j + ((i + 1) % 2)]; //set the current checker
				if(currChecker != null && currChecker.isRed() == playerIsRed){
					if(currChecker.isKing()){
						//loop over all squares on the upwards diagonal containing the current checker
						for(int k = -7; k < 8; k++){
							if(currChecker.getRow() + k > 0 && currChecker.getRow() + k < 8 && currChecker.getColumn() + k > 0 && currChecker.getColumn() + k < 8){
								if(checkCapture(new int[][] {new int[] {currChecker.getColumn() + 1, 8 - currChecker.getRow()}, new int[] {currChecker.getColumn() + k + 1, 8 - (currChecker.getRow() + k)}})) return true; //check if the current checker can make a capture onto the square
							}
						}
						//loop over all squares on the downwards diagonal containing the current checker
						for(int k = -7; k < 8; k++){
							if(currChecker.getRow() + k > 0 && currChecker.getRow() + k < 8 && currChecker.getColumn() - k > 0 && currChecker.getColumn() - k < 8){
								if(checkCapture(new int[][] {new int[] {currChecker.getColumn() + 1, 8 - currChecker.getRow()}, new int[] {currChecker.getColumn() - k + 1, 8 - (currChecker.getRow() + k)}})) return true; //check if the current checker can make a capture onto the square
							}
						}
					}else{
						//check if the current checker can capture diagonally up and to the left
						if(currChecker.getRow() > 1 && currChecker.getColumn() > 1){
							if(checkers[currChecker.getRow() - 1][currChecker.getColumn() - 1] != null){
								if(checkers[currChecker.getRow() - 1][currChecker.getColumn() - 1].isRed() != currChecker.isRed() && checkers[currChecker.getRow() - 2][currChecker.getColumn() - 2] == null) return true;
							}
						}
						//check if the current checker can capture diagonally up and to the right
						if(currChecker.getRow() > 1 && currChecker.getColumn() < 6){
							if(checkers[currChecker.getRow() - 1][currChecker.getColumn() + 1] != null){
								if(checkers[currChecker.getRow() - 1][currChecker.getColumn() + 1].isRed() != currChecker.isRed() && checkers[currChecker.getRow() - 2][currChecker.getColumn() + 2] == null) return true;
							}
						}
						//check if the current checker can capture diagonally down and to the left
						if(currChecker.getRow() < 6 && currChecker.getColumn() > 1){
							if(checkers[currChecker.getRow() + 1][currChecker.getColumn() - 1] != null){
								if(checkers[currChecker.getRow() + 1][currChecker.getColumn() - 1].isRed() != currChecker.isRed() && checkers[currChecker.getRow() + 2][currChecker.getColumn() - 2] == null) return true;
							}
						}
						//check if the current checker can capture diagonally down and to the right
						if(currChecker.getRow() < 6 && currChecker.getColumn() < 6){
							if(checkers[currChecker.getRow() + 1][currChecker.getColumn() + 1] != null){
								if(checkers[currChecker.getRow() + 1][currChecker.getColumn() + 1].isRed() != currChecker.isRed() && checkers[currChecker.getRow() + 2][currChecker.getColumn() + 2] == null) return true;
							}
						}
					}
				}
			}
		}
		return false; //if non of the checkers processed had moves, return false
	}
	
	/**
	 * Checks if there are any possible non capture moves the current player can make.
	 * @return True if there is at least one non capture move, false otherwise.
	 */
	public boolean availableNonCaptureMoves(){
		boolean playerIsRed = (player == 1) ? true : false; //the color of the player (true if red, false otherwise)
		Checker currChecker; //the current checker being processed for available moves
		//loop over all rows
		for(int i = 0; i < 8; i++){
			//loop over half the columns (since the checkers can only be on half of the board)
			for(int j = 0; j < 4; j++){
				currChecker = checkers[i][2 * j + ((i + 1) % 2)]; //set the current checker
				if(currChecker != null && currChecker.isRed() == playerIsRed){
					if(currChecker.isKing()){
						if(currChecker.getColumn() > 0) {
							//check if the square to the right are empty
							if(currChecker.getRow() > 0) if((checkers[currChecker.getRow() - 1][currChecker.getColumn() - 1] == null)) return true;
							if(currChecker.getRow() < 7) if((checkers[currChecker.getRow() + 1][currChecker.getColumn() - 1] == null)) return true;
						}
						if(currChecker.getColumn() < 7) {
							//check if the squares to the left are empty
							if(currChecker.getRow() > 0) if((checkers[currChecker.getRow() - 1][currChecker.getColumn() + 1] == null)) return true;
							if(currChecker.getRow() < 7) if((checkers[currChecker.getRow() + 1][currChecker.getColumn() + 1] == null)) return true;
						}
					}else{
						if(currChecker.isRed()){
							//check if the squares above are empty (since red checkers move upwards)
							if(currChecker.getColumn() > 0) {if((checkers[currChecker.getRow() - 1][currChecker.getColumn() - 1] == null)) return true;}
							if(currChecker.getColumn() < 7) {if((checkers[currChecker.getRow() - 1][currChecker.getColumn() + 1] == null)) return true;}
						}else{
							//check if the squares below are empty (since black checkers move downwards)
							if(currChecker.getColumn() > 0) {if((checkers[currChecker.getRow() + 1][currChecker.getColumn() - 1] == null)) return true;}
							if(currChecker.getColumn() < 7) {if((checkers[currChecker.getRow() + 1][currChecker.getColumn() + 1] == null)) return true;}
						}
					}
				}
			}
		}
		return false; //if non of the processed checkers had moves, return false
	}
	
	/**
	 * Get the checkers field from a board.
	 * @return The checkers array for the board.
	 */
	public Checker[][] getCheckers(){
		return checkers;
	}
	
	/**
	 * Overrides the Object toString to print out the board.
	 * The board is from the perspective of the current player.
	 */
	public String toString(){
		String output; //the output
		if(player == 1){
			//print out a board with red on the bottom
			output = "    _ _ _ _ _ _ _ _\n";
			String add; //what to add going down a row, either null or a checker
			//loop over 8 rows
			for(int i = 0; i < 8; i++){
				output = output + Integer.toString(8 - i) + " |";
				//loop over the columns
				for(int j = 0; j < 8; j++){
					add = (checkers[i][j] == null) ? "-" : checkers[i][j].toString();
					output = output + " " + add;
				}
				output = output + " |\n";
			}
			output = output + "    ‾ ‾ ‾ ‾ ‾ ‾ ‾ ‾\n    a b c d e f g h\n\n It is player " + player + "'s turn.";}
		else{
			//print out a board with black on the bottom
			output = "    _ _ _ _ _ _ _ _\n";
			String add;
			//loop backwards over 8 rows
			for(int i = 7; i >= 0; i--){
				output = output + Integer.toString(8 - i) + " |";
				//loop backwards over 8 columns
				for(int j = 7; j >= 0; j--){
					add = (checkers[i][j] == null) ? "-" : checkers[i][j].toString();
					output = output + " " + add;
				}
				output = output + " |\n";
			}
			output = output + "    ‾ ‾ ‾ ‾ ‾ ‾ ‾ ‾\n    h g f e d c b a\n\n It is player " + player + "'s turn.";
		}
		return output;
	}
}
