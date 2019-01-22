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



# How to Compile
In terminal, go into the directory containing Board.java and Checker.java.

Give the following commands: "javac Board.java" and "javac Checker.java"

To run, give the command: "java Board"

# How to Use

On your turn, you will be prompted to enter coordinates for your move. The coordinates are in the form [letter][number] (e.g. a3),
where the letter and number are given on the sides of the board. The first coordinate you enter is the checker you are
moving. Subsequent coordinates are the squares your checker jumps to. You will see the following prompts:

Enter the coordinates of the checker you wish to move: a3

Enter the coordinates of the next square you want your checker to jump to,
or press enter if there are no more jumps:b4

Enter the coordinates of the next square you want your checker to jump to,
or press enter if there are no more jumps:

