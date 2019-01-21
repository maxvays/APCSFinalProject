
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
	
	public Checker(Checker checker){
		row = checker.row;
		column = checker.column;
		isRed = checker.isRed;
		isKing = checker.isKing;
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
	
	public void setIsRed(boolean _isRed){
		isRed = _isRed;
	}
	
	public void setColumn(int _column){
		column = _column;
	}
	
	public void setRow(int _row){
		row = _row;
	}
	
	public boolean availableCaptures(Board board){
		if(this.isKing()){
			for(int k = -7; k < 8; k++){
				if(this.getRow() + k > 0 && this.getRow() + k < 8 && this.getColumn() + k > 0 && this.getColumn() + k < 8){
					if(board.checkCapture(new int[][] {new int[] {this.getColumn() + 1, 8 - this.getRow()}, new int[] {this.getColumn() + k + 1, 8 - (this.getRow() + k)}})) return true;
				}
			}
			for(int k = -7; k < 8; k++){
				if(this.getRow() + k > 0 && this.getRow() + k < 8 && this.getColumn() - k > 0 && this.getColumn() - k < 8){
					if(board.checkCapture(new int[][] {new int[] {this.getColumn() - 1, 8 - this.getRow()}, new int[] {this.getColumn() - k + 1, 8 - (this.getRow() + k)}})) return true;
				}
			}
		}else{
			if(this.getRow() > 1 && this.getColumn() > 1){
				if(board.getCheckers()[this.getRow() - 1][this.getColumn() - 1] != null){
					if(board.getCheckers()[this.getRow() - 1][this.getColumn() - 1].isRed() != this.isRed() && board.getCheckers()[this.getRow() - 2][this.getColumn() - 2] == null) return true;
				}
			}
			if(this.getRow() > 1 && this.getColumn() < 6){
				if(board.getCheckers()[this.getRow() - 1][this.getColumn() + 1] != null){
					if(board.getCheckers()[this.getRow() - 1][this.getColumn() + 1].isRed() != this.isRed() && board.getCheckers()[this.getRow() - 2][this.getColumn() + 2] == null) return true;
				}
			}
			if(this.getRow() < 6 && this.getColumn() > 1){
				if(board.getCheckers()[this.getRow() + 1][this.getColumn() - 1] != null){
					if(board.getCheckers()[this.getRow() + 1][this.getColumn() - 1].isRed() != this.isRed() && board.getCheckers()[this.getRow() + 2][this.getColumn() - 2] == null) return true;
				}
			}
			if(this.getRow() < 6 && this.getColumn() < 6){
				if(board.getCheckers()[this.getRow() + 1][this.getColumn() + 1] != null){
					if(board.getCheckers()[this.getRow() + 1][this.getColumn() + 1].isRed() != this.isRed() && board.getCheckers()[this.getRow() + 2][this.getColumn() + 2] == null) return true;
				}
			}
		}
		return false;
	}
	
	public String toString(){
		return (isRed) ? (isKing) ? "R" : "r" : (isKing) ? "B" : "b";
	}
}
