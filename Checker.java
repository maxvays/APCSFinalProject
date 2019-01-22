/**
 * Checker object
 * @author mwizard777
 */
public class Checker {
	private int row, column;
	private boolean isRed;
	private boolean isKing;
	
	/**
	 * Constructor.
	 * @param _row The row of the checker.
	 * @param _column The column of the checker.
	 * @param _isRed Whether the checker is red or not.
	 */
	public Checker(int _row, int _column, boolean _isRed){
		row  = _row;
		column = _column;
		isRed = _isRed;
		isKing = false;
	}
	
	/**
	 * Copy constructor.
	 * @param checker The checker to copy.
	 */
	public Checker(Checker checker){
		row = checker.row;
		column = checker.column;
		isRed = checker.isRed;
		isKing = checker.isKing;
	}
	
	/**
	 * Get the row of the checker.
	 * @return The row of the checker.
	 */
	public int getRow(){
		return row;
	}
	
	/**
	 * Get the column of the checker.
	 * @return The column of the checker.
	 */
	public int getColumn(){
		return column;
	}
	
	/**
	 * Get the color of the checker.
	 * @return True if the checker is red, false otherwise.
	 */
	public boolean isRed(){
		return isRed;
	}
	
	/**
	 * Get whether the checker is a king.
	 * @return True if the checker is a king, false otherwise.
	 */
	public boolean isKing(){
		return isKing;
	}
	
	/**
	 * Set whether the checker is a king.
	 * @param _isKing Whether to make the checker a king or not.
	 */
	public void setIsKing(boolean _isKing){
		isKing = _isKing;
	}
	
	/**
	 * Set whether the checker is red.
	 * @param _isRed Whether to make the checker red or not.
	 */
	public void setIsRed(boolean _isRed){
		isRed = _isRed;
	}
	
	/**
	 * Set the column of the checker.
	 * @param _column The number of a column.
	 */
	public void setColumn(int _column){
		column = _column;
	}
	
	/**
	 * Set the row of the checker.
	 * @param _row The number of a row.
	 */
	public void setRow(int _row){
		row = _row;
	}
	
	/**
	 * Check if an individual checker has available captures.
	 * @param board The current board.
	 * @return True if there is at least one capture, false otherwise.
	 */
	public boolean availableCaptures(Board board){
		if(this.isKing()){
			//loop over all squares on the upwards diagonal containing the checker
			for(int k = -7; k < 8; k++){
				if(this.getRow() + k > 0 && this.getRow() + k < 8 && this.getColumn() + k > 0 && this.getColumn() + k < 8){
					if(board.checkCapture(new int[][] {new int[] {this.getColumn() + 1, 8 - this.getRow()}, new int[] {this.getColumn() + k + 1, 8 - (this.getRow() + k)}})) return true; //check if the checker can jump to that square with a capture
				}
			}
			//loop over all squares on the downwards diagonal containing the checker
			for(int k = -7; k < 8; k++){
				if(this.getRow() + k > 0 && this.getRow() + k < 8 && this.getColumn() - k > 0 && this.getColumn() - k < 8){
					if(board.checkCapture(new int[][] {new int[] {this.getColumn() - 1, 8 - this.getRow()}, new int[] {this.getColumn() - k + 1, 8 - (this.getRow() + k)}})) return true; //check if the checker can jump to that square with a capture
				}
			}
		}else{
			//check if the checker can capture up and to the left
			if(this.getRow() > 1 && this.getColumn() > 1){
				if(board.getCheckers()[this.getRow() - 1][this.getColumn() - 1] != null){
					if(board.getCheckers()[this.getRow() - 1][this.getColumn() - 1].isRed() != this.isRed() && board.getCheckers()[this.getRow() - 2][this.getColumn() - 2] == null) return true;
				}
			}
			//check if the checker can capture up and to the right
			if(this.getRow() > 1 && this.getColumn() < 6){
				if(board.getCheckers()[this.getRow() - 1][this.getColumn() + 1] != null){
					if(board.getCheckers()[this.getRow() - 1][this.getColumn() + 1].isRed() != this.isRed() && board.getCheckers()[this.getRow() - 2][this.getColumn() + 2] == null) return true;
				}
			}
			//check if the checker can capture down and to the left
			if(this.getRow() < 6 && this.getColumn() > 1){
				if(board.getCheckers()[this.getRow() + 1][this.getColumn() - 1] != null){
					if(board.getCheckers()[this.getRow() + 1][this.getColumn() - 1].isRed() != this.isRed() && board.getCheckers()[this.getRow() + 2][this.getColumn() - 2] == null) return true;
				}
			}
			//check if the checker can capture down and to the right
			if(this.getRow() < 6 && this.getColumn() < 6){
				if(board.getCheckers()[this.getRow() + 1][this.getColumn() + 1] != null){
					if(board.getCheckers()[this.getRow() + 1][this.getColumn() + 1].isRed() != this.isRed() && board.getCheckers()[this.getRow() + 2][this.getColumn() + 2] == null) return true;
				}
			}
		}
		return false; //if non of the capture moves are possible, return false
	}
	
	/**
	 * Overrides the Object toString
	 */
	public String toString(){
		return (isRed) ? (isKing) ? "R" : "r" : (isKing) ? "B" : "b";
	}
}
