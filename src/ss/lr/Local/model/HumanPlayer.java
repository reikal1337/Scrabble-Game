package ss.lr.Local.model;

import java.util.ArrayList;
import java.util.Scanner;


public class HumanPlayer extends Player {
    public HumanPlayer(String name, ArrayList<Tile> rack) {
        super(name, rack);
    }

    public boolean checkInput(String[] input, Board board) {
        if (stringIsInt(input[0]) && stringIsInt(input[1])) {
            int row = Integer.parseInt(input[0]);
            int col = Integer.parseInt(input[1]);
            if (board.isField(row, col)) {
                Tile[] tiles = board.stringToTile(input[2].split(""));
                String word = board.tileToString(tiles);
                String direction = input[3];
                if (stringIsLetters(word)) {
                    for (Tile tile : tiles) {
                        if (getRack().contains(tile)) {

                        } else {
                            System.out.println("Wrong input! [letters] has to be made from tiles of your rack!");
                            return false;
                        }
                    }
                    if (direction.toLowerCase().equals("hor") || direction.toLowerCase().equals("ver")) {

                    } else {
                        System.out.println("Wrong input! Direction has to be either 'hor' or 'ver' ");
                        return false;
                    }
                } else {
                    System.out.println("Wrong input! [letters] has to be made of letters");
                    return false;
                }
            } else {
                System.out.println("Wrong input! row and col should be between 0 and 14!");
                return false;
            }
        } else {
            System.out.println("Wrong input! Should be [int] [int] [letters] [hor/ver]");
            return false;
        }
        return true;
    }


    private boolean stringIsInt(String word) {
        return word.matches("-?\\d+") ? true : false;
    }

    public boolean stringIsLetters(String word) {
        return word.matches("[a-zA-Z]+") ? true : false;
    }


    //Converting here and in Player.java makeMove
    @Override
    public ArrayList<String> determineMove(Board board) {
        ArrayList<String> result = new ArrayList<String>();
        int movesScore = 0;
        int firstMoveScore = 0;
        boolean legal = false;
        String[] input = new String[0];
        Tile[] words = null;
        while (!legal) {
            String prompt = "> " + getName() + ", Please input: position, letters, direction in this format: " +
                    "\nrow col letters hor/ver";
            Scanner scnObj = new Scanner(System.in);

            System.out.println(prompt);
            input = scnObj.nextLine().split(" ");
            if (checkInput(input, board)) {


                int row = Integer.parseInt(input[0]);
                int col = Integer.parseInt(input[1]);
                words = board.stringToTile(input[2].split(""));
                String direction = input[3];
                movesScore = board.checkIfMoveLegal(row, col, words, direction);
                if (board.isEmptyField(7, 7)) {
                    firstMoveScore = board.checkIfFirstMoveLegal(row, col, words, direction);
                    if (firstMoveScore > 0) {
                        legal = true;
                        movesScore = firstMoveScore;
                    } else {
                        System.out.println("Illegal move! First move has to be direction 7 7");
                    }
                } else if (movesScore > 0) {
                    legal = true;

                } else {
                    System.out.println("Illegal move!");
                }
            }
        }
        this.setRack(board.removeFromRackAndFill(words, this.getRack()));
        for (String info : input) {
            result.add(info);
        }
        result.add(String.valueOf(movesScore));
        return result;
    }


}
