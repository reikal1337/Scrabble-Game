package ss.lr.local.controller;


import utils.TextIO;
import ss.lr.local.model.Board;
import ss.lr.local.model.Player;
import ss.lr.local.model.Tile;

import java.util.ArrayList;

/**
 * Class for maintaining the Tic Tac Toe game.
 * Lab assignment Module 2
 * @author Theo Ruys en Arend Rensink
 */
public class Game {
    public static final int NUMBER_PLAYERS = 2;

    /**
     * The board.
     * @invariant board is never null
     */
    private Board board;

    /**
     * The 2 players of the game.
     * @invariant the length of the array equals NUMBER_PLAYERS
     * @invariant all array items are never null
     */
    private Player[] players;

    /**
     * Index of the current player.
     * @invariant the index is always between 0 and NUMBER_PLAYERS
     */
    private int current;

    // -- Constructors -----------------------------------------------

    /**
     * Creates a new Game object.
     * @requires s0 and s1 to be non-null
     * @param s0 the first player
     * @param s1 the second player
     */
    public Game(Player s0, Player s1,Board board) {
        this.board = board;
        players = new Player[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        current = 0;
    }

    // -- Commands ---------------------------------------------------

    /**
     * Starts the Tic Tac Toe game. <br>
     * Asks after each ended game if the user want to continue. Continues until
     * the user does not want to play anymore.
     */
    public void start() {
        boolean continueGame = true;
        while (continueGame) {

            reset();
            play();
            System.out.println("\n> Play another time? (y/n)?");
            continueGame = TextIO.getBoolean();
        }
    }
    public Board getBoard(){
        return this.board;
    }



    /**
     * Resets the game. <br>
     * The board is emptied and player[0] becomes the current player.
     */
    private void reset() {
        ArrayList<ArrayList<Tile>> racks = new ArrayList<ArrayList<Tile>>();
        current = 0;
        racks =  board.reset();
        players[0].setRack(racks.get(0));
        players[1].setRack(racks.get(1));
    }

    //Should check more things like isFull() ect;
    public boolean gameOver() {
        if(players[0].getRack().isEmpty() || players[1].getRack().isEmpty()){
            return true;
        }return false;
    }

    /**
     * Plays the Tic Tac Toe game. <br>
     * First the (still empty) board is shown. Then the game is played
     * until it is over. Players can make a move one after the other.
     * After each move, the changed game situation is printed.
     */
    private void play() {

        while (!gameOver()) {
            update();
            if(current==0) {
                players[current].makeMove(board);
                current++;
            }else if(current==1) {
                players[current].makeMove(board);
                current--;
            }
        }//printResult();
    }


    /**
     * Prints the game situation.
     */
    private void update() {
        System.out.println("\ncurrent game situation: \n\n" + board.toString()+
                "\nYour score: " + players[current].getScore()
                + "\n" + "Your tiles: " + players[current].getRack().toString());
    }

    /**
     * Prints the result of the last game. <br>
     * @requires the game to be over
     */
//    private void printResult() {
//        if (board.hasWinner()) {
//            Player winner = board.isWinner(players[0].getMark()) ? players[0]
//                    : players[1];
//            System.out.println("Player " + winner.getName() + " ("
//                    + winner.getMark().toString() + ") has won!");
//        } else {
//            System.out.println("Draw. There is no winner!");
//        }
//    }
}

