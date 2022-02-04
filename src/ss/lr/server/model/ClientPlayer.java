package ss.lr.server.model;

import ss.lr.local.model.Board;
import ss.lr.local.model.Tile;

import java.util.ArrayList;

public class ClientPlayer {
    private String name;
    private int score;
    private ArrayList<Tile> playerRack;

    public ClientPlayer(String name, ArrayList<Tile> rack) {
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
        this.score = this.score + (sc);
    }

    public ArrayList<Tile> getRack(){
        return this.playerRack;
    }
    public void setRack(ArrayList<Tile> rack){
        this.playerRack = rack;
    }



    //need to add direction!!!!!!!!!!!!!!!!!!!
    //And to receve string,or change string to Enum.
    public void makeMove(Board board, String[] move) {
        int row = Integer.parseInt(move[0]);
        int col = Integer.parseInt(move[1]);
        Tile[] words = board.stringToTile(move[2].split(""));
        String direction = move[3];
        board.setMove(row,col,words,direction);
    }
}
