import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Board {
	private Checker[][] checkers;
	private int player;
	private boolean gameover;
	
	public static void main(String[] args){
		Board board = new Board();
		Scanner reader = new Scanner(System.in);
		System.out.println("Player 1 is red.\n");
		System.out.println(board);
		String moveposition;
		List<String> move = new ArrayList<String>();
		while(!board.gameover){
			move.clear();
			moveposition = reader.nextLine();
			while(!moveposition.isEmpty()){
				move.add(moveposition);
				moveposition = reader.nextLine();
			}
			System.out.println(move);
			String[] a = new String[1];
			board.makeMove(move.toArray(a));
			board.player = 3 - board.player;
			System.out.println(board);
		}
		reader.close();
	}
	
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
	
	public Board(Board board){
		Checker[][] checkers = new Checker[8][8];
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
	
	public void makeMove(String[] positions){
		int[][] coordinates = new int[positions.length][2];
		for (int i = 0; i < positions.length; i++){
			coordinates[i][0] = (int)positions[i].charAt(0) - 96;
			coordinates[i][1] = (int)positions[i].charAt(1) - 48;
		}
		char moveType = checkValidMove(coordinates);
		if(moveType == 'n') swapCheckers(coordinates[0],coordinates[coordinates.length - 1]);
		else if(moveType == 'c'){
			swapCheckers(coordinates[0], coordinates[coordinates.length - 1]);
			for(int i = 1; i < coordinates.length; i++){
				System.out.println("Removing taken pieces");
				checkers[8 - (coordinates[i][1] + coordinates[i-1][1]) / 2][(coordinates[i][0] + coordinates[i-1][0]) / 2 - 1] = null;
			}
		}
	}
	
	public void swapCheckers(int[] start, int[] end){
		int startrow = 8 - start[1];
		int startcol = start[0] - 1;
		int endcol = end[0] - 1;
		int endrow = 8 - end[1];
		System.out.println("swapping checkers");
		checkers[startrow][startcol].setColumn(endcol);
		checkers[startrow][startcol].setRow(endrow);
		checkers[endrow][endcol] = checkers[startrow][startcol];
		checkers[startrow][startcol] = null;
	}
	
	public char checkValidMove(int[][] moves){
		Checker startChecker = checkers[8 - moves[0][1]][moves[0][0] - 1];
		System.out.println("checking valid move");
		if(startChecker == null) return 'f';
		if(moves.length == 2) if(checkNonCapture(moves)) return 'n';
		if(checkCapture(moves)) return 'c';
		return 'f';
	}
	
	public boolean checkNonCapture(int[][] moves){
		Checker startChecker = checkers[8 - moves[0][1]][moves[0][0] - 1];
		System.out.println("checking non capture");
		if(Math.abs(moves[0][1] - moves[1][1]) != Math.abs(moves[0][0] - moves[1][0])) return false;
		if(checkers[8 - moves[1][1]][moves[1][0] - 1] != null) return false;
		if(startChecker.isKing()){
			int numCheckersBetween = 0;
			for(int i = 1; i <= Math.abs(moves[0][1] - moves[1][1]); i++){
				if(checkers[(int) (8 - moves[0][1] + Math.signum(moves[0][1] - moves[1][1]) * i)][(int) (moves[0][0] - 1 - Math.signum(moves[0][0] - moves[1][0]) * i)] != null) numCheckersBetween++;
			}
			if(numCheckersBetween > 0) return false;
		}else{
			if(Math.abs(moves[0][1] - moves[1][1]) != 1 || Math.abs(moves[0][0] - moves[1][0]) != 1) return false;
			if(startChecker.isRed()){
				if(moves[1][1] < moves[0][1]) return false;
			}else{
				if(moves[1][1] > moves[0][1]) return false;
			}
		}
		return true;
	}
	
	public boolean checkCapture(int[][] moves){
		System.out.println("checking capture");
		boolean makeKing = false;
		Board _board = new Board(this);
		Checker startChecker = _board.checkers[8 - moves[0][1]][moves[0][0] - 1];
		if(startChecker == null) return false;
		for(int i = 1; i < moves.length; i++){
			if(_board.checkers[8 - moves[i][1]][moves[i][0] - 1] != null) return false;
			if(startChecker.isKing()){
				if(Math.abs(moves[i][0] - moves[i-1][0]) != Math.abs(moves[i][1] - moves[i-1][1])) return false;
				int numCheckersBetween = 0;
				for(int j = 1; j <= Math.abs(moves[i-1][1] - moves[i][1]); j++){
					if(_board.checkers[(int) (8 - moves[i-1][1] + Math.signum(moves[i-1][1] - moves[i][1]) * j)][(int) (moves[i-1][0] - 1 - Math.signum(moves[i-1][0] - moves[i][0]) * j)] != null){
						numCheckersBetween = (_board.checkers[(int) (8 - moves[i-1][1] + Math.signum(moves[i-1][1] - moves[i][1]) * j)][(int) (moves[i-1][0] - 1 - Math.signum(moves[i-1][0] - moves[i][0]) * j)].isRed() == startChecker.isRed()) ? numCheckersBetween + 2 : numCheckersBetween + 1;
						_board.checkers[(int) (8 - moves[i-1][1] + Math.signum(moves[i-1][1] - moves[i][1]) * j)][(int) (moves[i-1][0] - 1 - Math.signum(moves[i-1][0] - moves[i][0]) * j)].setIsRed(startChecker.isRed());
					}
				}
			if(numCheckersBetween != 1) return false;
			}else{
				if(Math.abs(moves[i][0] - moves[i-1][0]) != 2) return false;
				if(Math.abs(moves[i][1] - moves[i-1][1]) != 2) return false;
				if(_board.checkers[8 - (moves[i][1] + moves[i-1][1]) / 2][(moves[i][0] + moves[i-1][0]) / 2 - 1] == null || !(_board.checkers[8 - (moves[i][1] + moves[i-1][1]) / 2][(moves[i][0] + moves[i-1][0]) / 2 - 1].isRed() ^ startChecker.isRed())) return false;
				else _board.checkers[8 - (moves[i][1] + moves[i-1][1]) / 2][(moves[i][0] + moves[i-1][0]) / 2 - 1].setIsRed(startChecker.isRed());
			}
			int kingRow = (startChecker.isRed()) ? 8 : 1;
			if(moves[i][1] == kingRow){
				makeKing = true;
				startChecker.setIsKing(true);
			}
		}
		if(makeKing) checkers[8 - moves[0][1]][moves[0][0] - 1].setIsKing(true);
		return true;
	}
	
	public boolean availableCaptureMoves(){
		boolean playerIsRed = (player == 1) ? true : false;
		Checker currChecker;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 4; j++){
				currChecker = checkers[i][2 * j + ((i + 1) % 2)];
				if(currChecker != null && currChecker.isRed() == playerIsRed){
					if(currChecker.isKing()){
						for(int k = -7; k < 8; k++){
							if(currChecker.getRow() + k > 0 && currChecker.getRow() + k < 8 && currChecker.getColumn() + k > 0 && currChecker.getColumn() + k < 8){
								if(checkCapture(new int[][] {new int[] {currChecker.getColumn() + 1, 8 - currChecker.getRow()}, new int[] {currChecker.getColumn() + k + 1, 8 - (currChecker.getRow() + k)}})) return true;
							}
						}
						for(int k = -7; k < 8; k++){
							if(currChecker.getRow() + k > 0 && currChecker.getRow() + k < 8 && currChecker.getColumn() - k > 0 && currChecker.getColumn() - k < 8){
								if(checkCapture(new int[][] {new int[] {currChecker.getColumn() + 1, 8 - currChecker.getRow()}, new int[] {currChecker.getColumn() - k + 1, 8 - (currChecker.getRow() + k)}})) return true;
							}
						}
					}else{
						if(currChecker.getRow() > 1 && currChecker.getColumn() > 1){
							if(checkers[currChecker.getRow() - 1][currChecker.getColumn() - 1] != null){
								if(checkers[currChecker.getRow() - 1][currChecker.getColumn() - 1].isRed() != currChecker.isRed() && checkers[currChecker.getRow() - 2][currChecker.getColumn() - 2] == null) return true;
							}
						}
						if(currChecker.getRow() > 1 && currChecker.getColumn() < 6){
							if(checkers[currChecker.getRow() - 1][currChecker.getColumn() + 1] != null){
								if(checkers[currChecker.getRow() - 1][currChecker.getColumn() + 1].isRed() != currChecker.isRed() && checkers[currChecker.getRow() - 2][currChecker.getColumn() + 2] == null) return true;
							}
						}
						if(currChecker.getRow() < 6 && currChecker.getColumn() > 1){
							if(checkers[currChecker.getRow() + 1][currChecker.getColumn() - 1] != null){
								if(checkers[currChecker.getRow() + 1][currChecker.getColumn() - 1].isRed() != currChecker.isRed() && checkers[currChecker.getRow() + 2][currChecker.getColumn() - 2] == null) return true;
							}
						}
						if(currChecker.getRow() < 6 && currChecker.getColumn() < 6){
							if(checkers[currChecker.getRow() + 1][currChecker.getColumn() + 1] != null){
								if(checkers[currChecker.getRow() + 1][currChecker.getColumn() + 1].isRed() != currChecker.isRed() && checkers[currChecker.getRow() + 2][currChecker.getColumn() + 2] == null) return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean availableNonCaptureMoves(){
		boolean playerIsRed = (player == 1) ? true : false;
		Checker currChecker;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 4; j++){
				currChecker = checkers[i][2 * j + ((i + 1) % 2)];
				if(currChecker != null && currChecker.isRed() == playerIsRed){
					if(currChecker.isKing()){
						if(currChecker.getColumn() > 0) {
							if(currChecker.getRow() > 0) if((checkers[currChecker.getRow() - 1][currChecker.getColumn() - 1] == null)) return true;
							if(currChecker.getRow() < 7) if((checkers[currChecker.getRow() + 1][currChecker.getColumn() - 1] == null)) return true;
						}
						if(currChecker.getColumn() < 7) {
							if(currChecker.getRow() > 0) if((checkers[currChecker.getRow() - 1][currChecker.getColumn() + 1] == null)) return true;
							if(currChecker.getRow() < 7) if((checkers[currChecker.getRow() + 1][currChecker.getColumn() + 1] == null)) return true;
						}
					}else{
						if(currChecker.isRed()){
							if(currChecker.getColumn() > 0) {if((checkers[currChecker.getRow() - 1][currChecker.getColumn() - 1] == null)) return true;}
							if(currChecker.getColumn() < 7) {if((checkers[currChecker.getRow() - 1][currChecker.getColumn() + 1] == null)) return true;}
						}else{
							if(currChecker.getColumn() > 0) {if((checkers[currChecker.getRow() + 1][currChecker.getColumn() - 1] == null)) return true;}
							if(currChecker.getColumn() < 7) {if((checkers[currChecker.getRow() + 1][currChecker.getColumn() + 1] == null)) return true;}
						}
					}
				}
			}
		}
		return false;
	}
	
	public Checker[][] getCheckers(){
		return checkers;
	}
	
	public String toString(){
		String output;
		if(player == 1){
			output = "    _ _ _ _ _ _ _ _\n";
			String add;
			for(int i = 0; i < 8; i++){
				output = output + Integer.toString(8 - i) + " |";
				//output = output + (char)(8 - i) + " |";
				for(int j = 0; j < 8; j++){
					add = (checkers[i][j] == null) ? "-" : checkers[i][j].toString();
					output = output + " " + add;
				}
				output = output + " |\n";
			}
			output = output + "    ‾ ‾ ‾ ‾ ‾ ‾ ‾ ‾\n    a b c d e f g h\n\n It is player " + player + "'s turn.";}
		else{
			output = "    _ _ _ _ _ _ _ _\n";
			String add;
			for(int i = 7; i >= 0; i--){
				output = output + Integer.toString(8 - i) + " |";
				//output = output + (char)(8 - i) + " |";
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
