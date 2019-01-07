
public class Board {
	private Checker[][] checkers;
	
	public static void main(String[] args){
		Board board = new Board();
		System.out.print(board);
	}
	
	public Board(){
		checkers = new Checker[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if((i+j) % 2 == 1) checkers[i][j] = null;
				else checkers[i][j] = new Checker(i, j, true);
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
				}else{if(checkers[i][j].isRed()) add = "R"; else add = "B";}
				output = output + " " + add;
			}
			output = output + " |\n";
		}
		output = output + "  _ _ _ _ _ _ _ _";
		return output;
	}
}
