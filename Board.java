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
			System.out.println(board);}
		reader.close();
	}
	
	public Board(){
		player = 1;
		gameover = false;
		checkers = new Checker[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(((i+j) % 2 == 0) || (i > 2 && i < 5)) checkers[i][j] = null;
				else checkers[i][j] = (i > 4) ? new Checker(i, j, true) : new Checker(i, j, false);
			}
		}
	}
	
	public void makeMove(String[] positions){
		int[][] coordinates = new int[positions.length][2];
		for (int i = 0; i < positions.length; i++){
			coordinates[i][0] = (int)positions[i].charAt(0) - 96;
			coordinates[i][1] = (int)positions[i].charAt(1) - 48;
		}
		if(checkValidMove(coordinates)) swapCheckers(coordinates[0],coordinates[coordinates.length - 1]);
	}
	
	public void swapCheckers(int[] start, int[] end){
		int startrow = 8 - start[1];
		int startcol = start[0] - 1;
		int endcol = end[0] - 1;
		int endrow = 8 - end[1];
		System.out.println(startrow);
		System.out.println(startcol);
		checkers[startrow][startcol].setColumn(endcol);
		checkers[startrow][startcol].setRow(endrow);
		checkers[endrow][endcol] = checkers[startrow][startcol];
		checkers[startrow][startcol] = null;
	}
	
	public boolean checkValidMove(int[][] moves){
		boolean valid = false;
		Checker startChecker = checkers[8 - moves[0][1]][moves[0][0] - 1];
		System.out.println(startChecker);
		if(startChecker == null) return false;
		if(moves.length == 2) valid = valid || checkNonCapture(moves);
		valid = valid || checkCapture(moves);
		return valid;
	}
	
	public boolean checkNonCapture(int[][] moves){
		Checker startChecker = checkers[8 - moves[0][1]][moves[0][0] - 1];
		System.out.print("hi");
		if(Math.abs(moves[0][1] - moves[1][1]) != Math.abs(moves[0][0] - moves[1][0])) return false;
		if(checkers[8 - moves[1][1]][moves[1][0] - 1] != null) return false;
		if(startChecker.isKing()){
			int numCheckersBetween = 0;
			for(int i = 1; i <= Math.abs(moves[0][1] - moves[1][1]); i++){
				if(checkers[(int) (8 - moves[0][1] + Math.signum(moves[0][1] - moves[1][1]) * i)][(int) (moves[0][0] - 1 - Math.signum(moves[0][0] - moves[1][0]) * i)] != null) numCheckersBetween++;
			}
			if(numCheckersBetween > 0) return false;
		}else{
			if(startChecker.isRed()){
				if(startChecker.getColumn() > 0 && startChecker.getColumn() < 7) {if(!(checkers[startChecker.getRow() - 1][startChecker.getColumn() - 1] == null || checkers[startChecker.getRow() - 1][startChecker.getColumn() + 1] == null)) return false;}
				else {if(startChecker.getColumn() > 0) {if(!(checkers[startChecker.getRow() - 1][startChecker.getColumn() - 1] == null)) return false;}
				else {if(!(checkers[startChecker.getRow() - 1][startChecker.getColumn() + 1] == null)) return false;}}
			}else{
				if(startChecker.getColumn() > 0 && startChecker.getColumn() < 7) {if(!(checkers[startChecker.getRow() + 1][startChecker.getColumn() - 1] == null || checkers[startChecker.getRow() + 1][startChecker.getColumn() + 1] == null)) return false;}
				else {if(startChecker.getColumn() > 0) {if(!(checkers[startChecker.getRow() + 1][startChecker.getColumn() - 1] == null)) return false;}
				else {if(!(checkers[startChecker.getRow() - 1][startChecker.getColumn() + 1] == null)) return false;}}
			}
		}
		return true;
	}
	
	public boolean checkCapture(int[][] moves){
		return false;
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
