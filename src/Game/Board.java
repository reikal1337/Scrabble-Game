package Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class Board {

    private static final int DIM = 15;
   // private static final String[] NUMBERING = { " 0 | 1 | 2 ", "---+---+---", " 3 | 4 | 5 ", "---+---+---",
   //        " 6 | 7 | 8 " };
    private static final String LINE = "---+---+---+---+---+---+---+---+---+---+---+---+---+---+---";
   // private static final String DELIM = "     ";

    private static ArrayList<Integer> DarkRedX3;//8tiles
    private static ArrayList<Integer> PaleRedX2;//16tiles
    private int StartX2;//1tile
    private static ArrayList<Integer> DarkBlueX3;//12iles
    private static ArrayList<Integer> PaleBlueX2;//24tiles

    //Testing atrr
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String MAGENTA	= "\u001B[35m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";

    private static Tile[][] field;
    public ArrayList<Tile> letterBag;

    public static void main(String[] args) {
        Board test = new Board();
       // WordChecker test = new WordChecker();
        TestChecker lol = new TestChecker();
        System.out.println(test.toString());

    }
    //Purley for testing,just displays special tiles to check if cords are good.
    public void setSpecialTilesOnBoard(){
        for(int i=0;i<DarkRedX3.size();i++){//R
            setField(DarkRedX3.get(i),Tile.R);
        }

        for(int i=0;i<PaleRedX2.size();i++){//R
            setField(PaleRedX2.get(i),Tile.P);
        }
        //7 7 for star DarkBlueX3
        setField(StartX2,Tile.S);

        for(int i=0;i<DarkBlueX3.size();i++){//R
            setField(DarkBlueX3.get(i),Tile.B);
        }

        for(int i=0;i<PaleBlueX2.size();i++){//R
            setField(PaleBlueX2.get(i),Tile.C);
        }
    }

    //Separator is “;”
    //Blank tiles are “0”, then you fill in your desired letter.
    //Empty squares are indicated with “-”.
    public Board() {
        this.field = new Tile[DIM][DIM];
        reset();
    }

    public void fillBag(){
        letterBag = new ArrayList<Tile>();
        Collections.addAll(letterBag,Tile.A,Tile.A,Tile.A,Tile.A,Tile.A,Tile.A,Tile.A,Tile.A,Tile.A,Tile.B,
                Tile.B,Tile.C,Tile.C,Tile.D,Tile.D,Tile.D,Tile.D,Tile.E,Tile.E,Tile.E,Tile.E,Tile.E,Tile.E
                ,Tile.E,Tile.E,Tile.E,Tile.E,Tile.E,Tile.E,Tile.F,Tile.F,Tile.G,Tile.G,Tile.H,Tile.H,Tile.I
                ,Tile.I,Tile.I,Tile.I,Tile.I,Tile.I,Tile.I,Tile.I,Tile.J,Tile.J,Tile.K,Tile.K,Tile.L,Tile.L
                ,Tile.L,Tile.L,Tile.M,Tile.M,Tile.N,Tile.N,Tile.N,Tile.N,Tile.N,Tile.N,Tile.O
                ,Tile.O,Tile.O,Tile.O,Tile.O,Tile.O,Tile.O,Tile.O,Tile.P,Tile.P,Tile.Q,Tile.R
                ,Tile.R,Tile.R,Tile.R,Tile.R,Tile.R,Tile.S,Tile.S,Tile.S,Tile.S,Tile.T,Tile.T
                ,Tile.T,Tile.T,Tile.T,Tile.T,Tile.U,Tile.U,Tile.U,Tile.U,Tile.V,Tile.V,Tile.W
                ,Tile.W,Tile.X,Tile.Y,Tile.Y,Tile.Z,Tile.BLANK,Tile.BLANK);
        Collections.shuffle(letterBag);

//        Collections.addAll(letterBag,"a","a","a","a","a","a","a","a","a","b","b","c","c","d","d","d","d","e"
//                ,"e","e","e","e","e","e","e","e","e","e","e","f","f","g","g","h","h","i","i","i","i","i","i","i","i"
//                ,"j","j","k","k","l","l","l","l","m","m","n","n","n","n","n","n","o","o","o","o","o","o","o","o"
//                ,"p","p","q","r","r","r","r","r","r","s","s","s","s","t","t","t","t","t","t","u","u","u","u","v"
//                ,"v","w","w","x","y","y","z","0","0");
    }

    public static boolean isField(int row, int col) {
        return row>=0 && row<15 && col>=0 && col<15 ? true:false;
    }
    public Tile getField(int row, int col) {
        return this.field[row][col];
    }

    public Tile getField(int index) {
        int row = coordinates(index)[0];
        int col = coordinates(index)[1];
        return this.field[row][col];
    }

    public static void setField(int row, int col, Tile tile) {
        if(isField(row,col)) {
            field[row][col] = tile;
        }
    }
    public static void setField(int index, Tile tile) {
        int row = coordinates(index)[0];
        int col = coordinates(index)[1];
        setField(row,col,tile);
    }

    public void reset() {
        emptyBoard();
    }

    public boolean isEmptyBoard(){
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if(!this.field[i][j].equals(Tile.EMPTY)){
                    return false;
                }
            }
        }return true;
    }

    private void emptyBoard(){
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                this.field[i][j] = Tile.EMPTY;
            }
        }
    }

    private void specialTiles(){
        //0 0, 0 7, 0 14, 7 0, 7 14, 14 0, 14 7, 14 14 -8
        DarkRedX3 = new ArrayList<Integer>();
        Collections.addAll(DarkRedX3,index(0,0),index(0,7),index(0,14),
                index(7,0),index(7,14),index(14,0),index(14,7),
                index(14,14));


        //1 1, 1 13, 2 2, 2 12, 3 3, 3 11, 4 4, 4 10, 10 4, 10 10, 11 3, 11 11, 12 2, 12 12, 13 1, 13 13 -16
        PaleRedX2 = new ArrayList<Integer>();
        Collections.addAll(PaleRedX2,index(1,1),index(1,13),index(2,2)
                ,index(2,12),index(3,3),index(3,11),index(4,4)
                ,index(4,10),index(10,4),index(10,10),index(11,3)
                ,index(11,11),index(12,2),index(12,12),index(13,1)
                ,index(13,13));

        //7 7
        StartX2 = index(7,7);

        //1 5, 1 9, 5 1, 5 5, 5 9, 5 13, 9 1, 9 5, 9 9, 9 13, 13 5, 13 9; -12
        DarkBlueX3 = new ArrayList<Integer>();
        Collections.addAll(DarkBlueX3,index(1,5),index(1,9),index(5,1)
                ,index(5,5),index(5,9),index(5,13),index(9,1)
                ,index(9,5),index(9,9),index(9,13),index(13,5)
                ,index(13,9));

        //0 3, 0 11, 2 6, 2 8, 3 0, 3 7, 3 14, 6 2, 6 6, 6 8, 6 12, 7 3, 7 11, 8 2, 8 6,
        // 8 8, 8 12, 11 0, 11 7, 11 14, 12 6, 12 8, 14 3, 14 11 -24
        PaleBlueX2 = new ArrayList<Integer>();
        Collections.addAll(PaleBlueX2,index(0,3),index(0,11),index(2,6)
                ,index(2,8),index(3,0),index(3,7),index(3,14)
                ,index(6,2),index(6,6),index(6,8),index(6,12)
                ,index(7,3),index(7,11),index(8,2),index(8,6)
                ,index(8,8),index(8,12),index(11,0),index(11,7)
                ,index(11,14),index(12,6),index(12,8),index(14,3)
                ,index(14,11));
    }

    //From row and col return one number index;
    private int index(int row, int col) {
        return row * DIM + col;
    }

    private static int[] coordinates(int index) {
        int row = index / DIM;
        int col = index % DIM;
        int[] result = {row,col};
        return result;
    }


    //Old but working
    @Override
    public String toString() {
        String printBoard = "";
        for (int i = 0; i < DIM; i++) {
            String row = "";
            for (int j = 0; j < DIM; j++) {
                row = row + " " + getField(i, j) + " ";
                if (j < DIM - 1) {
                    row = row + "|";
                }
            }
            printBoard = printBoard + row;
            if (i < DIM - 1) {
                printBoard = printBoard + "\n" + LINE + "\n";
            }
        }
        return printBoard;
    }



}
