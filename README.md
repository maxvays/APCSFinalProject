# APCSFinalProject
Final Project for APCS Fall 2018 Semester

Terminal based 2 player checkers.



Development Log

1/6: Initialize classes Board and Checker.

1/7: Make starting board setup, and give Checker setter methods.

1/9: Make a method which moves a checker from one board square to another. Intialize a game loop (as of yet inescapable), where every turn a player makes a move, and then the board flips for the next player.

1/10: Make a method checkValidMove which checks if a move is valid. Uses helper functions checkNonCapture (implemented) and checkCapture (not yet implemented).

1/17: Complete makeMove so that it can handle both non capture and capture moves.

1/20: Finish methods which check if moves are valid, for both king and normal pieces

1/21: Finish game mechanics. Add documentation/comments.

1/22: Final bugfixes


# How to Compile
In terminal, go into the directory containing Board.java and Checker.java.

Give the following commands: "javac Board.java" and "javac Checker.java"

To run, give the command: "java Board"

# How to Use

The checkers on the board are represented by 'r' for red checker and 'b' for black checker. In king form, they are 'R' and 'B'.

The game starts with Player 1, red, going first.

On your turn, you will be prompted to enter coordinates for your move. The coordinates are in the form [letter][number] (e.g. a3),
where the letter and number are given on the sides of the board. The first coordinate you enter is the checker you are
moving. Subsequent coordinates are the squares your checker jumps to. You will see the following prompts:

> Enter the coordinates of the checker you wish to move: a3

> Enter the coordinates of the next square you want your checker to jump to,
or press enter if there are no more jumps:b4

> Enter the coordinates of the next square you want your checker to jump to,
or press enter if there are no more jumps:

After you press enter, if the move is valid, the move will be made, the board will be flipped, and it will be the other player's turn. Keep playing until someone wins.

If a position is reached where both players agree the game is a draw, pressing Ctrl C will end the game with no winner.

# Rules

This version of checkers is Russian draughts. Rules from Wikipedia (slightly edited):

Board. Played on an 8×8 board with alternating dark and light squares. The left down square field should be dark.
Starting position. Each player starts with 12 pieces on the three rows closest to their own side. The row closest to each player is called the "crownhead" or "kings row". Usually, the colors of the pieces are black and red, but possible use other colors (one dark and other light). The player with red pieces (lighter color) moves first.

Pieces. There are two kinds of pieces: "men" and "kings". Kings are differentiated as being capitalized.

Men. Men move forward diagonally to an adjacent unoccupied square.

Kings. If a player's piece moves into the kings row on the opposing player's side of the board, that piece to be "crowned", becoming a "king" and gaining the ability to move back or forward and choose on which free square at this diagonal to stop.

Capture. If the adjacent square contains an opponent's piece, and the square immediately beyond it is vacant, the opponent's piece may be captured (and removed from the game) by jumping over it. Jumping can be done forward and backward. Multiple-jump moves are possible if, when the jumping piece lands, there is another piece that can be jumped. Jumping is mandatory and cannot be passed up to make a non-jumping move. When there is more than one way for a player to jump, one may choose which sequence to make, not necessarily the sequence that will result in the most amount of captures. However, one must make all the captures in that sequence. A captured piece is left on the board until all captures in a sequence have been made but cannot be jumped again (this rule also applies for the kings).
If a man touches the kings row during a capture and can continue a capture, it jumps backwards as a king. The player can choose where to land after the capture.

Winning and draws. A player with no valid move remaining loses. This is the case if the player either has no pieces left or if a player's pieces are obstructed from making a legal move by the pieces of the opponent. A game is drawn if both players agree to a draw.
