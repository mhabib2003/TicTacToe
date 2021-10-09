/**
 * Play a game of TicTacToe with the computer. The computer graciously lets you go first and you're 'O', but winning
 * may be harder than you think...
 * <p>
 * This program was built to explore the basic concepts of the Minimax algorithm and how it can be used to employ basic
 * Machine-Learning type techniques. There is much to be modified, yet this program is more a proof of concept than a
 * pleasant game to be played over and over again.
 * <p>
 * NOTE: This code was influenced by and partially borrowed from https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/
 * and I do NOT claim this program as completely being my own work -- this program was built solely for educative purposes for myself.
 *
 * @author Michael Habib
 * @date 2019-2020, originally built sometime senior year
 * @date October, 2021, modified and commented in 2nd year of college
 */

import java.util.Scanner;

public class TicTacToeTurnBased {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);        // initialize the Scanner object to read inputs from user

        // print description of how the game will work
        System.out.println("Human player is 'O' and goes first. '*' is an empty space.");

        char[][] board = new char[3][3];            // initialize 3 x 3 TicTacToe board represented by characters

        for (int i = 0; i < 3; i++) {                // populate board with '*' symbols to represent empty spaces
            for (int z = 0; z < 3; z++) {
                board[i][z] = '*';
            }
        }

        printBoard(board);                            // print initial board
        // initialize integer to hold value if computer has won, lost, or neither (initialize to meaningless value)
        int won = -1000;
        // this loop does continues game play until there are no moves left (tie) or someone has won
        while (isMovesLeft(board) && evaluate(board) != -1 && evaluate(board) != 1) {

            // output message telling user to submit their move
            System.out.print("Select the indices (separated by a comma, e.g. 0,0 for the top left space) where you would like to play: ");
            String move = input.nextLine();                        // input move using Scanner

            // checks if move is valid (i.e. desired space is unoccupied); if invalid move, have user redo their move
            if (board[Integer.parseInt(move.substring(0, 1))][Integer.parseInt(move.substring(2))] == 'O' || board[Integer.parseInt(move.substring(0, 1))][Integer.parseInt(move.substring(2))] == 'X') {
                System.out.println("Invalid play, this space is already occupied. Please try again.");
                printBoard(board);
            }
            // if valid, update board and have computer play
            else {

                // update board with valid move and display updated board to user
                board[Integer.parseInt(move.substring(0, 1))][Integer.parseInt(move.substring(2))] = 'O';
                System.out.println("You played:");
                printBoard(board);

                // check if someone has won or if there are no moves left (tie); if so, break out of loop and output result
                won = evaluate(board);
                if (won == 1 || won == -1 || (won == 0 && isMovesLeft(board) == false))
                    break;

                // find the best move for the computer using the Minimax algorithm (see Minimax and findBestMove methods later in code)
                // update board and display updated board
                int[] computerMove = findBestMove(board);
                board[computerMove[0]][computerMove[1]] = 'X';
                System.out.println("Computer played: ");
                printBoard(board);

            }
        }

        // display result of game based on evaluation of board earlier in the loop (the game is over at this point)
        if (won == 1)
            System.out.println("Computer won");
        else if (won == -1)
            System.out.println("Human won");
        else
            System.out.println("Tie.");

        // close Scanner
        input.close();

    }


    /**
     * Prints the board as represented by 'X', 'O', and '*' (for empty spaces)
     *
     * @param board is a double char array representing the TicTacToe board
     */
    public static void printBoard(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int z = 0; z < 3; z++) {
                System.out.print(board[i][z]);
            }
            System.out.println();
        }
    }

    /**
     * Checks if there are any moves left (i.e. if there are any empty spaces represented by '*')
     *
     * @param board is a double char array representing the TicTacToe board
     */
    public static boolean isMovesLeft(char[][] board) {
        boolean isLeft = false;

        for (int i = 0; i < 3; i++) {
            for (int z = 0; z < 3; z++) {
                if (board[i][z] == '*')
                    isLeft = true;
            }
        }

        return isLeft;
    }


    /**
     * Determines if the computer has won, lost, or if the current state is indecisive (i.e. there is a tie or
     * still moves to be played)
     * This process is performed explicitly and iteratively
     *
     * @param board is a double char array representing the TicTacToe board
     */
    public static int evaluate(char[][] board) {
        int score = 0;    //initialized to 0 to default as indecisive score if neither win nor loss found

        // check rows
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                if (board[0][i] == 'X')
                    score = 1;
                else if (board[0][i] == 'O')
                    score = -1;
            }
        }

        // check columns
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                if (board[i][0] == 'X')
                    score = 1;
                else if (board[i][0] == 'O')
                    score = -1;
            }
        }

        // check diagonals
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == 'X')
                score = 1;
            else if (board[0][0] == 'O')
                score = -1;
        } else if (board[2][0] == board[1][1] && board[1][1] == board[0][2]) {
            if (board[2][0] == 'X')
                score = 1;
            else if (board[2][0] == 'O')
                score = -1;
        }

        return score;

    }


    /**
     * This method is essentially the heart of the minimax algorithm, which works by trying out different moves and
     * recursively finding the ultimate outcome of each move, comparing this move with all other possible moves.
     * This algorithm works well for such a short game as there are relatively few possible moves at any given point,
     * which allows for so many recursive calls to be made.
     * This method in particular assesses the outcome of each potential move made by the findBestMove method, assuming
     * that the computer and player make their respective optimal move at each given turn.
     *
     * @param board is a double char array representing the TicTacToe board
     * @return an integer representing ultimate result of tried potential move
     */
    public static int minimax(char[][] board, boolean isMax) {

        // find current status of game
        int score = evaluate(board);

        // check if game is already over
        if (score == 1)
            return score;
        else if (score == -1)
            return score;
        else if (!isMovesLeft(board))
            return 0;

        // max (computer) turn
        if (isMax) {

            int best = -1000;   // initialize to value that will certainly be lower than the possible results (-1, 0, 1)

            // try all possible moves and find the best one to play as the computer
            for (int i = 0; i < 3; i++) {
                for (int z = 0; z < 3; z++) {

                    if (board[i][z] == '*') {

                        board[i][z] = 'X';

                        best = Math.max(best, minimax(board, !isMax));

                        board[i][z] = '*';
                    }

                }

            }

            return best;

            // min (simulated user) turn
        } else {

            int best = 1000;  // initialize to value that will certainly be higher than the possible results (-1, 0, 1)

            // try all possible moves and find the best one to play as the simulated user
            for (int i = 0; i < 3; i++) {
                for (int z = 0; z < 3; z++) {

                    if (board[i][z] == '*') {

                        board[i][z] = 'O';

                        best = Math.min(best, minimax(board, !isMax));

                        board[i][z] = '*';
                    }

                }

            }

            return best;

        }

    }


    /**
     * This method iteratively tries potential moves that the computer can make and tests each move using the Minimax
     * method (see above for explanation of how Minimax works)
     *
     * @param board is a double char array representing the TicTacToe board
     * @return an integer array representing the indices where the computer should play (the optimal move indices)
     */
    public static int[] findBestMove(char[][] board) {
        // initialize variables to meaningless values
        int bestVal = -1000;
        int bestMoveRow = -1;
        int bestMoveColumn = -1;

        // loop through board, check if move is possible, evaluate how successful said move will ultimately be
        for (int i = 0; i < 3; i++) {
            for (int z = 0; z < 3; z++) {

                if (board[i][z] == '*') {
                    board[i][z] = 'X';

                    int moveVal = minimax(board, false);

                    board[i][z] = '*';

                    if (moveVal > bestVal) {
                        bestVal = moveVal;
                        bestMoveRow = i;
                        bestMoveColumn = z;
                    }

                }

            }


        }

        // array representing indices of best possible move
        int[] bestMoves = {bestMoveRow, bestMoveColumn};

        return bestMoves;

    }
}
