
public class Checker {
	private int row, column;
	private boolean isRed;
	
	public Checker(int Row, int Column, boolean IsRed){
		row  = Row;
		column = Column;
		isRed = IsRed;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getColumn(){
		return column;
	}
	
	public boolean isRed(){
		return isRed;
	}
	
	public String toString(){
		return "Checker";
	}
}
