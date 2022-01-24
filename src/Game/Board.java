package Game;

import Utils.WordChecker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class Board {


    //ToDo
    //Scores
    //Check if letters are from rack.
    //Check input.
    //Refill sack after turn;

    private static final int DIM = 15;
    // private static final String[] NUMBERING = { " 0 | 1 | 2 ", "---+---+---", " 3 | 4 | 5 ", "---+---+---",
    //        " 6 | 7 | 8 " };
    private static final String LINE = "---+---+---+---+---+---+---+---+---+---+---+---+---+---+---";
    // private static final String DELIM = "     ";

    //all dark red tile coordinates.
    private static ArrayList<Integer> DarkRedX3;//8tiles
    //all pale red tile coordinates.
    private static ArrayList<Integer> PaleRedX2;//16tiles
    //start tile.
    private int StartX2;//1tile
    //all dark blue tile coordinates.
    private static ArrayList<Integer> DarkBlueX3;//12iles
    //all pale blue tile coordinates.
    private static ArrayList<Integer> PaleBlueX2;//24tiles

    //All tile type enums.
    private static ArrayList<Tile> allEnums;
//    //All words present on board.
//    private static ArrayList<String> wordsOnBoard;
//
//    private static ArrayList<Integer> wordsOnBoardCoord;


    private static ArrayList<String> blackList;

    //All words that can be used for game.
    private static ArrayList<String> checkWords;


    //Testing atrr
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";

    //Board fields.
    private Tile[][] field;
    //All possible letters
    public ArrayList<Tile> letterBag;

    //Using for testing.
    public static void main(String[] args) {
        Board test = new Board();
        // WordChecker test = new WordChecker();
        //TestChecker lol = new TestChecker();

        //test.toString();


        Tile[] letters = test.stringToTile("LITH".split(""));
        test.setMove(7,7,letters,"hor");
        System.out.println(test.toString());

        letters = test.stringToTile("A".split(""));
       System.out.println("Move legal? " + test.checkIfMoveLegal(6,9,letters,"ver"));
        test.setMove(6,9,letters,"ver");
        System.out.println("Game board: \n" + test.toString());

    }

    //Purley for testing,just displays special tiles to check if cords are good.
    public void setSpecialTilesOnBoard() {
        for (int i = 0; i < DarkRedX3.size(); i++) {//R
            setField(DarkRedX3.get(i), Tile.R);
        }

        for (int i = 0; i < PaleRedX2.size(); i++) {//R
            setField(PaleRedX2.get(i), Tile.P);
        }
        //7 7 for star DarkBlueX3
        setField(StartX2, Tile.S);

        for (int i = 0; i < DarkBlueX3.size(); i++) {//R
            setField(DarkBlueX3.get(i), Tile.B);
        }

        for (int i = 0; i < PaleBlueX2.size(); i++) {//R
            setField(PaleBlueX2.get(i), Tile.C);
        }
    }

    //Creates board,calls reset() method;
    public Board() {
        this.field = new Tile[DIM][DIM];
        reset();
    }

//    //returns wordsOnBoard array.
//    public ArrayList<String> getWordsOnBoard() {
//        return this.wordsOnBoard;
//    }
    //returns letterBag array.
    public ArrayList<Tile> getbag() {
        return this.letterBag;
    }

    //Fills letter bag and adds all Enums in list.
    public void fillBag() {
        letterBag = new ArrayList<Tile>();
        allEnums = new ArrayList<Tile>();
        Collections.addAll(letterBag, Tile.A, Tile.A, Tile.A, Tile.A, Tile.A, Tile.A, Tile.A, Tile.A, Tile.A, Tile.B,
                Tile.B, Tile.C, Tile.C, Tile.D, Tile.D, Tile.D, Tile.D, Tile.E, Tile.E, Tile.E, Tile.E, Tile.E, Tile.E
                , Tile.E, Tile.E, Tile.E, Tile.E, Tile.E, Tile.E, Tile.F, Tile.F, Tile.G, Tile.G, Tile.H, Tile.H, Tile.I
                , Tile.I, Tile.I, Tile.I, Tile.I, Tile.I, Tile.I, Tile.I, Tile.J, Tile.J, Tile.K, Tile.K, Tile.L, Tile.L
                , Tile.L, Tile.L, Tile.M, Tile.M, Tile.N, Tile.N, Tile.N, Tile.N, Tile.N, Tile.N, Tile.O
                , Tile.O, Tile.O, Tile.O, Tile.O, Tile.O, Tile.O, Tile.O, Tile.P, Tile.P, Tile.Q, Tile.R
                , Tile.R, Tile.R, Tile.R, Tile.R, Tile.R, Tile.S, Tile.S, Tile.S, Tile.S, Tile.T, Tile.T
                , Tile.T, Tile.T, Tile.T, Tile.T, Tile.U, Tile.U, Tile.U, Tile.U, Tile.V, Tile.V, Tile.W
                , Tile.W, Tile.X, Tile.Y, Tile.Y, Tile.Z, Tile.BLANK, Tile.BLANK);
        Collections.shuffle(letterBag);

        Collections.addAll(allEnums, Tile.A, Tile.B, Tile.C, Tile.D, Tile.E, Tile.F, Tile.G, Tile.H, Tile.I,
                Tile.J, Tile.K, Tile.L, Tile.M, Tile.N, Tile.O, Tile.P, Tile.Q, Tile.R, Tile.S, Tile.T, Tile.U,
                Tile.V, Tile.W, Tile.X, Tile.Y, Tile.Z, Tile.BLANK, Tile.EMPTY);

//        Collections.addAll(letterBag,"a","a","a","a","a","a","a","a","a","b","b","c","c","d","d","d","d","e"
//                ,"e","e","e","e","e","e","e","e","e","e","e","f","f","g","g","h","h","i","i","i","i","i","i","i","i"
//                ,"j","j","k","k","l","l","l","l","m","m","n","n","n","n","n","n","o","o","o","o","o","o","o","o"
//                ,"p","p","q","r","r","r","r","r","r","s","s","s","s","t","t","t","t","t","t","u","u","u","u","v"
//                ,"v","w","w","x","y","y","z","0","0");
    }

    //Checks if field is valid,not used rn.
    public static boolean isField(int row, int col) {
        return row >= 0 && row < 15 && col >= 0 && col < 15 ? true : false;
    }

    //returns Tile of specified field.
    public Tile getField(int row, int col) {
        return this.field[row][col];
    }

    //returns Tile of specified field.
    public Tile getField(int index) {
        int row = coordinates(index)[0];
        int col = coordinates(index)[1];
        return this.field[row][col];
    }

    //Creates and returns specified boards copy.
    public Board boardCopy(Board board) {
        Board newBoard = new Board();
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                newBoard.setField(i, j, board.getField(i, j));
            }
        }
        return newBoard;
    }

    //Creates and returns this boards copy.
    public Board boardCopy() {
        Board newBoard = new Board();
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                newBoard.setField(i, j,getField(i,j));
            }
        }
        return newBoard;
    }

    //sets field to specified tile in specific field;
    public void setField(int row, int col, Tile tile) {
        if (isField(row, col)) {
            this.field[row][col] = tile;
        }
    }

    //sets field to specified tile in specific field;
    public void setField(int index, Tile tile) {
        int row = coordinates(index)[0];
        int col = coordinates(index)[1];
        setField(row, col, tile);
    }


    //Checks if first move is legal.
    public boolean checkIfFirstMoveLegal(int row, int col, Tile[] letters) {
        if(row == 7 && col == 7){
            String word = tileToString(letters);
            if(checkWord(word)){
                 return true;
            }return false;
        }return false;
    }


    //Based on hor or ver calls specific moveLegal method,after which list of words on board are added.
    public boolean checkIfMoveLegal(int row, int col, Tile[] letters, String dir) {
        if (dir.toLowerCase().equals("hor")) {
            return moveHorLegal(row, col, letters) ? true : false;
        } else if (dir.toLowerCase().equals("ver")) {
            return moveVerLegal(row, col, letters) ? true : false;
        }
        return false;
    }

    //if fields are empty horizontally it places it on temporary board and
    // calls methode to check if there is new word on board.
    private boolean moveHorLegal(int row, int col, Tile[] letters) {//wordsOnBoard
        ArrayList<String> allWordsOfMove = new ArrayList<String>();//Not returning for now.
        boolean legal = false;
        String word = "";
        Board tempBoard = boardCopy();
        tempBoard.setMove(row,col,letters,"hor");
        int[] wordStart = horWordStart(row,col,tempBoard);
        int nRow = wordStart[0];
        int nCol = wordStart[1];


        word = horGetWord(nRow,nCol,tempBoard);
        if (tempBoard.checkWord(word)){
            legal = true;
            allWordsOfMove.add(word);
        }
        for(int i=nCol;i<DIM;i++){
            if(topEmpty(nRow,i,tempBoard) || bottomEmpty(nRow,i,tempBoard)){
                int[] vStart = verWordStart(nRow,i,tempBoard);
                word = verGetWord(vStart[0],vStart[1],tempBoard);
                if (tempBoard.checkWord(word)){
                    legal = true;
                    allWordsOfMove.add(word);
                }
            }
        }
        return legal;
    }

    private String horGetWord(int row,int col,Board tempBoard){
        String result = "";
        while(!tempBoard.isEmptyField(row,col) && col < 14){
            result = result + tempBoard.getField(row,col).toString();
            col++;
            //System.out.println("Col:" + col);
        }return result;
    }

    private int[] horWordStart(int row,int col,Board tempBoard){
        int[] result = new int[2];
        for(int i=col;i>=0;i--){
            if(tempBoard.isEmptyField(row,i)){
                return new int[] {row,i+1};
            }
        } return null;
    }

    private boolean moveVerLegal(int row, int col, Tile[] letters) {//wordsOnBoard
        ArrayList<String> allWordsOfMove = new ArrayList<String>();//Not returning for now;
        boolean legal = false;
        String word = "";
        Board tempBoard = boardCopy();
        tempBoard.setMove(row,col,letters,"ver");
        int[] wordStart = verWordStart(row,col,tempBoard);
        int nRow = wordStart[0];
        int nCol = wordStart[1];


        word = verGetWord(nRow,nCol,tempBoard);
        if (tempBoard.checkWord(word)){
            legal = true;
            allWordsOfMove.add(word);
        }
        for(int i=nRow;i<DIM;i++){
            if(leftEmpty(i,nCol,tempBoard) || rightEmpty(i,nCol,tempBoard)){
                int[] vStart = horWordStart(i,nCol,tempBoard);
                word = horGetWord(vStart[0],vStart[1],tempBoard);
                if (checkWord(word)){
                    legal = true;
                    allWordsOfMove.add(word);
                }
            }
        }
        return legal;
    }

    private String verGetWord(int row,int col,Board tempBoard){
        String result = "";
        while(!tempBoard.isEmptyField(row,col) && row >= 0){
            result = result + tempBoard.getField(row,col).toString();
            row--;
            System.out.println("Row: " + row);
        }return result;
    }

    private int[] verWordStart(int row,int col,Board tempBoard){
        int[] result = new int[2];
        for(int i=row;i>=0;i--){
            if(tempBoard.isEmptyField(i,col)){
                return new int[] {i+1,col};
            }
        } return null;
    }

    private boolean topEmpty(int row,int col,Board tempBoard){
        return tempBoard.isEmptyField(row,col-1);
    }
    private boolean bottomEmpty(int row,int col,Board tempBoard){
        return tempBoard.isEmptyField(row,col+1);
    }
    private boolean leftEmpty(int row,int col,Board tempBoard){
        return tempBoard.isEmptyField(row-1,col);
    }
    private boolean rightEmpty(int row,int col,Board tempBoard){
        return tempBoard.isEmptyField(row+1,col);
    }



    //Checks if fields horizontally in which word should go are empty.Doesn't check if letter is "-" aka empty.
    private boolean moveHorEmpty(int row, int col, Tile[] letters) {
        int count = letters.length + col;
        int counter = 0;
        for (int i = col; i < count ; i++) {
            if (letters[counter] != Tile.EMPTY) {
                if (isEmptyField(row, i)) {
                    counter++;
                } else {
                    return false;
                }
            }else{
                counter++;
            }


        }
        return true;

    }

//    //if fields are empty vertically it places it on temporary board and
//    // calls methode to check if there is new word on board.
//    private boolean moveVerLegal(int row, int col, Tile[] letters) {
//        int count = letters.length + row;
//
//        Board tempBoard = boardCopy();
//        //tempBoard.addWordsFromBoard();
//        int wordsBefore = tempBoard.getWordsOnBoard().size();
//        int wordsAfter = 0;
//        if (moveVerEmpty(row, col, letters)) {
//            tempBoard.setMove(row, col, letters, "ver");
//            wordsAfter = tempBoard.getWordsOnBoard().size();
//            if (wordsAfter > wordsBefore) {
//                return true;
//            }
//        }
//        return false;
//
//    }

    //Checks if fields vertically in which word should go are empty.Doesn't check if letter is "-" aka empty.
    private boolean moveVerEmpty(int row, int col, Tile[] letters) {
        int count = letters.length + row;
        int counter = 0;
        for (int i = col; i < count ; i++) {
            //System.out.println("Row: " + row + " Col: " + col + " lSize: " + letters.length + " counter: " + counter);
            if (letters[counter] != Tile.EMPTY) {
                if (isEmptyField(i, col)) {
                    counter++;
                } else {
                    return false;
                }
            }else{
                counter++;
            }

        }
        return true;
    }


    //Based on hor or ver calls specific setMove method,after which list of words on board are added.
    public void setMove(int row, int col, Tile[] letters, String dir) {
        if (dir.toLowerCase().equals("hor")) {
            setMoveHor(row, col, letters);
        } else if (dir.toLowerCase().equals("ver")) {
            setMoveVer(row, col, letters);
        }
    }

    //Set tiles in positions horizontally.
    private void setMoveHor(int row, int col, Tile[] letters) {
        int count = letters.length + col;
        int counter = 0;
        for (int i = col; i < count + 1; i++) {
            if (counter < letters.length) {
                if (isEmptyField(row,i)) {
                    setField(row, i, letters[counter]);
                    counter++;
                }
            }
        }
    }


    //Set tiles in positions vertically.
    private void setMoveVer(int row, int col, Tile[] letters) {
        int count = letters.length + row;
        int counter = 0;
        for (int i = row; i < count+1 ; i++) {
            if (counter < letters.length) {
                if (isEmptyField(i,col)) {
                    setField(i, col, letters[counter]);
                    counter++;
                }
            }

        }
    }

    //Resets board and returns 2 racks.
    public ArrayList<ArrayList<Tile>> reset() {
        ArrayList<ArrayList<Tile>> result = new ArrayList<ArrayList<Tile>>();
        emptyBoard();
        fillBag();
        papulateCheckWords();
        result.add(getRack());
        result.add(getRack());
        return result;

    }

    //returns one Tile rack(7 letters);
    public ArrayList<Tile> getRack() {
        ArrayList<Tile> result = new ArrayList<Tile>();
        for (int i = 0; i < 7; i++) {
            result.add(letterBag.get(i));
            letterBag.remove(i);
        }

        return result;
    }
    //Checks if field is empty.
    public boolean isEmptyField(int row, int col) {
        return this.field[row][col].equals(Tile.EMPTY) ? true : false;
    }

    //Checks if board is empty.
    public boolean isEmptyBoard() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (!this.field[i][j].equals(Tile.EMPTY)) {
                    return false;
                }
            }
        }
        return true;
    }

    //Make board empty aka fills it's fields with Tile.EMPTY.
    private void emptyBoard() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                this.field[i][j] = Tile.EMPTY;
            }
        }
    }

    //Fills Array with special tile coordinates.
    private void specialTiles() {
        //0 0, 0 7, 0 14, 7 0, 7 14, 14 0, 14 7, 14 14 -8
        DarkRedX3 = new ArrayList<Integer>();
        Collections.addAll(DarkRedX3, index(0, 0), index(0, 7), index(0, 14),
                index(7, 0), index(7, 14), index(14, 0), index(14, 7),
                index(14, 14));


        //1 1, 1 13, 2 2, 2 12, 3 3, 3 11, 4 4, 4 10, 10 4, 10 10, 11 3, 11 11, 12 2, 12 12, 13 1, 13 13 -16
        PaleRedX2 = new ArrayList<Integer>();
        Collections.addAll(PaleRedX2, index(1, 1), index(1, 13), index(2, 2)
                , index(2, 12), index(3, 3), index(3, 11), index(4, 4)
                , index(4, 10), index(10, 4), index(10, 10), index(11, 3)
                , index(11, 11), index(12, 2), index(12, 12), index(13, 1)
                , index(13, 13));

        //7 7
        StartX2 = index(7, 7);

        //1 5, 1 9, 5 1, 5 5, 5 9, 5 13, 9 1, 9 5, 9 9, 9 13, 13 5, 13 9; -12
        DarkBlueX3 = new ArrayList<Integer>();
        Collections.addAll(DarkBlueX3, index(1, 5), index(1, 9), index(5, 1)
                , index(5, 5), index(5, 9), index(5, 13), index(9, 1)
                , index(9, 5), index(9, 9), index(9, 13), index(13, 5)
                , index(13, 9));

        //0 3, 0 11, 2 6, 2 8, 3 0, 3 7, 3 14, 6 2, 6 6, 6 8, 6 12, 7 3, 7 11, 8 2, 8 6,
        // 8 8, 8 12, 11 0, 11 7, 11 14, 12 6, 12 8, 14 3, 14 11 -24
        PaleBlueX2 = new ArrayList<Integer>();
        Collections.addAll(PaleBlueX2, index(0, 3), index(0, 11), index(2, 6)
                , index(2, 8), index(3, 0), index(3, 7), index(3, 14)
                , index(6, 2), index(6, 6), index(6, 8), index(6, 12)
                , index(7, 3), index(7, 11), index(8, 2), index(8, 6)
                , index(8, 8), index(8, 12), index(11, 0), index(11, 7)
                , index(11, 14), index(12, 6), index(12, 8), index(14, 3)
                , index(14, 11));
    }

    //From row and col return one number index;
    private int index(int row, int col) {
        return row * DIM + col;
    }

    //From index returns row and col.
    private static int[] coordinates(int index) {
        int row = index / DIM;
        int col = index % DIM;
        int[] result = {row, col};
        return result;
    }


    //Prints out board.
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


//    //Calls methodes too add words from board.Not fully works.
//    public void addWordsFromBoard() {
//        addWordsFromBoardHor();
//        addWordsFromBoardVer();
//
//    }

//    //Adds all words horizontally currently on board to wordsOnBoard array.
//    public void addWordsFromBoardHor() {//wordsOnBoardCoord
//        String word = "";
//        int wordsIndex = 0;
//        for (int i = 0; i < DIM; i++) {
//            for (int j = 0; j < DIM; j++) {
//                System.out.println("Tile: " + getField(i,j).toString());
//                if (!isEmptyField(i, j)) {
//                    System.out.println(this.toString());
//                    if(word.equals("")){
//                        wordsIndex = index(i,j);
//                    }
//                    word = word + getField(i, j).toString();
//                    System.out.println("Row: "+ i + " Col: "+j+"TestWord: " + word);
//                    if (checkWord(word)) {
//                        //System.out.println("List: " + getWordsOnBoard().toString());
//                        if(wordsOnBoard.contains(word) && wordsOnBoardCoord.get(wordsOnBoard.indexOf(word)).equals(index(i,j))){
//                        }else{
//                            wordsOnBoard.add(word);
//                            wordsOnBoardCoord.add(wordsIndex);
//                            int[] coord = coordinates(wordsIndex);
//                            i = coord[0];
//                            j = coord[1];
//                            word = word.substring(0);
//                        }
//                    }
//                }else{
//                    word = "";
//                }
//            }
//        }
//    }

//    //Adds all words vertically currently on board to wordsOnBoard array.
//    public void addWordsFromBoardVer() {
//        String word = "";
//        for (int i = 0; i < DIM; i++) {
//            for (int j = 0; j < DIM; j++) {
//                if (!isEmptyField(j, i)) {
//                    word = word + getField(j, i).toString();
//                    if (checkWord(word)) {
//                        wordsOnBoard.add(word);
//                        word = word.substring(word.length() - 1);
//                    } else {
//                        word = "";
//                    }
//                }
//            }
//        }
//    }


    //From document takes words and adds them to checkWords array.
    private void papulateCheckWords() {
        File file = new File("src\\Utils\\collins_scrabble_words_2019.txt");
        Scanner sc = null;
        checkWords = new ArrayList<String>();
        blackList = new ArrayList<String>();
        Collections.addAll(blackList,"II","III","IV","VI","VII","VIII","IX","XI","XII","XIII");
        try {
            sc = new Scanner(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (sc.hasNext()) {
            String[] line = sc.next().split(" ");
            String theWord = line[0].replace("\t", "");
            theWord = theWord.replace(",", "");
            if (wordIsUpperCase(theWord) && theWord.length() > 1 && !blackList.contains(theWord)) {
                checkWords.add(theWord);
            }
        }sc.close();

    }

    //checks if word exists in checkWords array.
    private boolean checkWord(String word) {
        if (checkWords.contains(word.toUpperCase())) {
            return true;
        }
        return false;
    }

//    public boolean checkWord(String word) {
//        File file = new File("src\\Utils\\collins_scrabble_words_2019.txt");
//        Scanner sc = null;
//        try {
//            sc = new Scanner(file, StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        while (sc.hasNext()) {
//            String[] line = sc.next().split(" ");
//            String theWord = line[0].replace("\t", "");
//            theWord = theWord.replace(",", "");
//
//            if(wordIsUpperCase(theWord)){
//                System.out.println(theWord);
//                if(theWord.toLowerCase().equals(word.toLowerCase())){
//                    return true;
//                }
//            }
//
//        }return false;
//    }

    //Checks if word is uppercase if yes returns true otherwise false.
    public boolean wordIsUpperCase(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (Character.isUpperCase(string.charAt(i))) {
            } else {
                return false;
            }
        }
        return true;
    }

    //From letter list returns representing Tile list.
    public String tileToString(Tile[] letters) {
        String result = "";
        for (int i = 0; i < letters.length; i++) {
            result = result + letters[i].toString();
        }
        return result;
    }



    //From letter list returns representing Tile list.
    public Tile[] stringToTile(String[] letters) {
        Tile[] result = new Tile[letters.length];
        for (int i = 0; i < letters.length; i++) {
            for (Tile tile : allEnums) {
                if (letters[i].toLowerCase().equals(tile.toString().toLowerCase())) {
                    result[i] = tile;
                }
            }
        }
        return result;
    }


}
