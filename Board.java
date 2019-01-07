
public class Board {
	private Checker[][] checkers;
	private int player;
	
	public static void main(String[] args){
		Board board = new Board();
		System.out.print(board);
	}
	
	public Board(){
		player = 1;
		checkers = new Checker[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(((i+j) % 2 == 0) || (i > 2 && i < 5)) checkers[i][j] = null;
				else checkers[i][j] = (i > 4) ? new Checker(i, j, true) : new Checker(i, j, false);
			}
		}
	}
	
	public String toString(){
		String output = "  _ _ _ _ _ _ _ _\n";
		String add;
		for(int i = 0; i < 8; i++){
			output = output + "|";
			for(int j = 0; j < 8; j++){
				if(checkers[i][j] == null){
					add = "-";
				}else{add = checkers[i][j].toString();}
				output = output + " " + add;
			}
			output = output + " |\n";
		}
		output = output + "  ‾ ‾ ‾ ‾ ‾ ‾ ‾ ‾\n\n It is player " + player + "'s turn.";
		return output;
	}
}
