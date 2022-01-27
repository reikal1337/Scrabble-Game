package Game;

import Utils.TextIO;
import Game.*;
import Utils.WordChecker;

import java.util.ArrayList;

public class Scrabble {

    public static void main(String[] args) {
        HumanPlayer first ;
        HumanPlayer second;
        WordChecker check = new WordChecker();
        Board gameBoard= new Board(check);

        System.out.println("First player's name: \n");
        String input1 = TextIO.getln();
        first = new HumanPlayer(input1,gameBoard.getRack());

        System.out.println("Second player's name: \n");
        String input2 = TextIO.getln();
        second = new HumanPlayer(input2,gameBoard.getRack());

        Game game = new Game(first,second,gameBoard);
        game.start();

    }


}
