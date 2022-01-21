package Game;

import java.util.ArrayList;

public abstract class Player {
    private String name;
    private ArrayList<Tile> playerRack;

    public Player(String name, ArrayList<Tile> rack) {
        this.name = name;
        this.playerRack = rack;
    }
    public String getName() {
        return name;
    }

    public ArrayList<Tile> getRack(){
        return this.playerRack;
    }

    public abstract String[] determineMove(Board board);

    //need to add direction!!!!!!!!!!!!!!!!!!!
    //And to receve string,or change string to Enum.
    public void makeMove(Board board) {
        int row = Integer.parseInt(determineMove(board)[0]);
        int col = Integer.parseInt(determineMove(board)[1]);
        String word = determineMove(board)[2];
        String direction = determineMove(board)[3];
        //board.setField(row,col,word);
    }
}
