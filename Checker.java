
public class Checker {
	private int row, column;
	private boolean isRed;
	private boolean isKing;
	
	public Checker(int _row, int _column, boolean _isRed){
		row  = _row;
		column = _column;
		isRed = _isRed;
		isKing = false;
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
	
	public boolean isKing(){
		return isKing;
	}
	
	public void setIsKing(boolean _isKing){
		isKing = _isKing;
	}
	
	public void setColumn(int _column){
		column = _column;
	}
	
	public void setRow(int _row){
		row = _row;
	}
	
	public String toString(){
		return (isRed) ? (isKing) ? "R" : "r" : (isKing) ? "B" : "b";
	}
}
