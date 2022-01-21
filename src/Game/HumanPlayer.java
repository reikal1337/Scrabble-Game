package Game;

import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer extends Player{
    public HumanPlayer(String name, ArrayList<Tile> rack) {
        super(name, rack);
    }


    @Override
    public String[] determineMove(Board board) {
        String prompt = "> " + getName() + ", Please input: position,letters,direction in this format: " +
                "row col letters direction";
        Scanner scnObj = new Scanner(System.in);

        System.out.println(prompt);
        String input[] = scnObj.nextLine().split(" ");

        return input;
    }
}
