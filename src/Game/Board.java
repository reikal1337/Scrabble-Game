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

    private static Coordinates DarkRedX3;//8tiles
    private static Coordinates PaleRedX2;//16tiles
    private ArrayList<Integer> StartX2;//1tile
    private static Coordinates DarkBlueX3;//12iles
    private static Coordinates PaleBlueX2;//24tiles

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

        System.out.println(test.toString());

    }
    //Purley for testing,just displays special tiles to check if cords are good.
//    public void displaySpecialTiles(){
//        ArrayList<Integer> DarkRedRows =  DarkRedX3.getRows();
//        ArrayList<Integer> DarkRedCols =  DarkRedX3.getCols();
//        for(int i=0;i<DarkRedRows.size();i++){//R
//            setField(DarkRedRows.get(i),DarkRedCols.get(i),RED+"R"+RESET);
//        }
//
//        ArrayList<Integer> PaleRedRows =  PaleRedX2.getRows();
//        ArrayList<Integer> PaleRedCols =  PaleRedX2.getCols();
//        for(int i=0;i<PaleRedRows.size();i++){//R
//            setField(PaleRedRows.get(i),PaleRedCols.get(i),MAGENTA+"P"+RESET);
//        }
//        //7 7 for star DarkBlueX3
//        setField(7,7,BLACK+"S"+RESET);
//
//        ArrayList<Integer> DarkBlueRows =  DarkBlueX3.getRows();
//        ArrayList<Integer> DarkBlueCols =  DarkBlueX3.getCols();
//        for(int i=0;i<DarkBlueRows.size();i++){//R
//            setField(DarkBlueRows.get(i),DarkBlueCols.get(i),BLUE+"B"+RESET);
//        }
//
//        ArrayList<Integer> PaleBlueRows =  PaleBlueX2.getRows();
//        ArrayList<Integer> PaleBlueCols =  PaleBlueX2.getCols();
//        for(int i=0;i<PaleBlueRows.size();i++){//R
//            setField(PaleBlueRows.get(i),PaleBlueCols.get(i),CYAN+"C"+RESET);
//        }
//    }

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
    public static void setField(int row, int col, Tile tile) {
        if(isField(row,col)) {
            field[row][col] = tile;
        }
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
        //0 1, 0 7, 0 14, 7 0, 7 14, 14 0, 14 7, 14 14
        DarkRedX3 = new Coordinates(new int[]{0, 0, 0, 7, 7, 14, 14, 14},
                new int[]{0, 7, 14, 0, 14, 0, 7, 14});

        //1 1, 1 13, 2 2, 2 12, 3 3, 3 11, 4 4, 4 10, 10 4, 10 10, 11 3, 11 11, 12 2, 12 12, 13 1, 13 13
        PaleRedX2 = new Coordinates(new int[]{1, 1, 2, 2, 3, 3, 4, 4, 10, 10, 11, 11, 12, 12, 13, 13},
                new int[]{1, 13, 2, 12, 3, 11, 4, 10, 4, 10, 3, 11, 2, 12, 1, 13});
        //7 7
        StartX2 = new ArrayList<Integer>();
        StartX2.add(7);
        StartX2.add(7);

        //1 5, 1 9, 5 1, 5 5, 5 9, 5 13, 9 1, 9 5, 9 9, 9 13, 13 5, 13 9;
        DarkBlueX3 = new Coordinates(new int[]{1, 1, 5, 5, 5, 5, 9, 9, 9, 9, 13, 13},
                new int[]{5, 9, 1, 5, 9, 13, 1, 5, 9, 13, 5, 9});

        //0 3, 0 11, 2 6, 2 8, 3 0, 3 7, 3 14, 6 2, 6 6, 6 8, 6 12, 7 3, 7 11, 8 2, 8 6,
        // 8 8, 8 12, 11 0, 11 7, 11 14, 12 6, 12 8, 14 3, 14 11
        PaleBlueX2 = new Coordinates(new int[]{0, 0, 2, 2, 3, 3, 3, 6, 6, 6, 6, 7,
                7, 8, 8, 8, 8, 11, 11, 11, 12, 12, 14, 14},
                new int[]{3, 11, 6, 8, 0, 7, 14, 2, 6, 8, 12, 3, 11, 2, 6, 8, 12, 0, 7, 14, 6, 8, 3, 11});
    }

    //From row and col return one number index;
    private int index(int row, int col) {
        return row * DIM + col;
    }

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
