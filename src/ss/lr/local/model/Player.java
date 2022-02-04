package ss.lr.local.model;

import java.util.ArrayList;

public abstract class Player {
    private String name;
    private int score;
    private ArrayList<Tile> playerRack;

    public Player(String name, ArrayList<Tile> rack) {
        this.name = name;
        this.playerRack = rack;
        this.score = 0;
    }
    public String getName() {
        return name;
    }

    public int getScore(){
        return this.score;
    }

    public void addScore(int sc){
        this.score = this.score + sc;
    }

    public ArrayList<Tile> getRack(){
        return this.playerRack;
    }
    public void setRack(ArrayList<Tile> rack){
        this.playerRack = rack;
    }

    public abstract ArrayList<String> determineMove(Board board);

    //need to add direction!!!!!!!!!!!!!!!!!!!
    //And to receve string,or change string to Enum.
    public void makeMove(Board board) {
        ArrayList<String> info = determineMove(board);
        int row = Integer.parseInt(info.get(0));
        int col = Integer.parseInt(info.get(1));
        Tile[] words = board.stringToTile(info.get(2).split(""));
        //System.out.println("Test: " + words.toString());
        String direction = info.get(3);
        int movesScore = Integer.parseInt(info.get(4));
        this.addScore(movesScore);
        board.setMove(row,col,words,direction);

    }
}
