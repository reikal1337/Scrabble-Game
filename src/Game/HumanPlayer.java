package Game;

import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer extends Player{
    public HumanPlayer(String name, ArrayList<Tile> rack) {
        super(name, rack);
    }

    //Converting here and in Player.java makeMove
    @Override
    public String[] determineMove(Board board) {
        boolean legal = false;
        String[] input = new String[0];
        while(!legal){
            String prompt = "> " + getName() + ", Please input: position,letters,direction in this format: " +
                    "row col letters hor/ver";
            Scanner scnObj = new Scanner(System.in);

            System.out.println(prompt);
            input = scnObj.nextLine().split(" ");

            int row = Integer.parseInt(input[0]);
            int col = Integer.parseInt(input[1]);
            Tile[] words = board.stringToTile(input[2].split(""));
            String direction = input[3];
            if(board.checkIfMoveLegal(row,col,words,direction)){
                legal = true;

            }else{
                System.out.println("Illegal move!!!");
            }
        }return input;
    }
}
