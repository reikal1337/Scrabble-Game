package ss.lr.local.model;

import utils.WordChecker;

import javax.swing.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class Board {


    //ToDo
    //Scores +- delete tiles after turn..++
    //Check if letters are from rack.++
    //Check input.++
    //Refill sack after turn;++
    //BLANK tile.++
    //GAme over.++ Should do better game over....s++
    //More descriptive errors,input +- Need to do on server side..
    //Diferecate between ilegal move and illegal word.++
    //Do server discconect..++
    //Complete skip button..++
    //Check if on border end good...Idk wtf I meant +- :D
    //handle GUI in client++
    //Check if you can skip not on your turn..++
    //Change public,private.. +-
    //Install checkstyle
    //Winner on gui after game stop..You can see score... ++
    //Empty bag errors...
    //Sync all handles in server..
    //In client mabe split GUI and model control...
    //-------------------------Other day
    //Should fix exceptions ,too many
    //If I want colorfully console in GUi need to change to JTextPane and use Documentation.
    //Getters and setter at the top,use them in constructor..
    //Changing all Tile[] to ArrayList[]


    //When word goes out of bonds..should check if field+letter size is in bounds.

    private static final int DIM = 15;
    // private static final String[] NUMBERING = { " 0 | 1 | 2 ", "---+---+---", " 3 | 4 | 5 ", "---+---+---",
    //        " 6 | 7 | 8 " };
    private static final String LINE = "---+---+---+---+---+---+---+---+---+---+---+---+---+---+---";
    // private static final String DELIM = "     ";

    private int startPosition;
    private HashMap<String, ArrayList<Integer>> specialTiles;
    //All tile type enums.
    private static ArrayList<Tile> allEnums;
//    //All words present on board.
//    private static ArrayList<String> wordsOnBoard;
//
//    private static ArrayList<Integer> wordsOnBoardCoord;


    //Board fields.
    private Tile[][] field;
    //All possible letters
    public ArrayList<Tile> letterBag;
    public WordChecker checker;
    private static JFrame gui;

    //Using for testing.
//    public static void main(String[] args) {
//        WordChecker testCheck = new WordChecker();
//        Board test = new Board(testCheck);
//
//
//        //gui.setVisible(true);
//        // WordChecker test = new WordChecker();
//        //TestChecker lol = new TestChecker();
//
//        //test.toString();
//
//        //System.out.println("Lol: "+test.checkWord("res"));
//
//
////        Tile[] letters = test.stringToTile("LITH".split(""));
////        test.setMove(7,7,letters,"hor");
////        System.out.println(test.toString());
////        Board newBoard = test.boardCopy();
////        System.out.println("\nNew board: \n" + newBoard.toString());
//
////        letters = test.stringToTile("A".split(""));
////       System.out.println("Move legal? " + test.checkIfMoveLegal(6,9,letters,"ver"));
////        test.setMove(6,9,letters,"ver");
////        System.out.println("Game board: \n" + test.toString());
//
//    }


    //Creates board,calls reset() method;
    public Board() {
        this.field = new Tile[DIM][DIM];
        this.checker = new WordChecker();
        reset();
    }

    public Board(WordChecker checker) {
        this.field = new Tile[DIM][DIM];
        this.checker = checker;
        reset();
    }

    public ArrayList<Tile> getBag() {
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

    }

    //Checks if field is valid,not used rn.
    public boolean isField(int row, int col) {
        return row >= 0 && row < 15 && col >= 0 && col < 15 ? true : false;
    }

    //returns Tile of specified field.
    public Tile getField(int row, int col) {
        return this.field[row][col];
    }


    //Creates and returns this boards copy.
    public Board boardCopy() {
        Board newBoard = new Board(this.checker);
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                newBoard.setField(i, j, getField(i, j));
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


    //Might have bug that it will think if blank is can't make word or is in bad position makes it that it's bad word..
    public int checkIfFirstMoveLegal(int row, int col, Tile[] letters, String dir) {
        int score = 0;
        if (row == 7 && col == 7) {
            List<Tile> tileList = Arrays.asList(letters);
            if (tileList.contains(Tile.BLANK)) {
                int index = tileList.indexOf(Tile.BLANK);
                Tile newTile = checkMoveWithBlank(row, col, tileList, dir, true);
                if (!newTile.equals(Tile.BLANK)) {
                    letters[index] = newTile;
                } else {
                    return score;
                }
            }
            if (dir.equalsIgnoreCase("hor")) {
                score = firstMoveHorLegal(row, col, letters);
                return score > 0 ? score : 0;
            } else if (dir.equalsIgnoreCase("ver")) {
                score = firstMoveVerLegal(row, col, letters);
                return score > 0 ? score : 0;
            }
            ;
        }
        return score;
    }

    private int firstMoveHorLegal(int row, int col, Tile[] letters) {
        HashMap<String, ArrayList<Integer>> letterScore = new HashMap<String, ArrayList<Integer>>();
        int score = 0;
        Board tempBoard = boardCopy();
        tempBoard.setMove(row, col, letters, "hor");
        String word = tileToString(letters);
        if (checker.checkWord(word)) {
            letterScore = horGetWord(row, col, tempBoard);
            word = letterScore.keySet().toString().replaceAll("[\\[\\]]", "");
            score = calculateScore(letterScore);
        }
        return score;
    }

    private int firstMoveVerLegal(int row, int col, Tile[] letters) {
        HashMap<String, ArrayList<Integer>> letterScore = new HashMap<String, ArrayList<Integer>>();
        int score = 0;
        Board tempBoard = boardCopy();
        tempBoard.setMove(row, col, letters, "ver");
        String word = tileToString(letters);
        if (checker.checkWord(word)) {
            letterScore = verGetWord(row, col, tempBoard);
            word = letterScore.keySet().toString().replaceAll("[\\[\\]]", "");
            score = calculateScore(letterScore);
        }
        return score;
    }


    //Might have bug that it will think if blank is can't make word or is in bad position makes it that it's bad word..
    public int checkIfMoveLegal(int row, int col, Tile[] letters, String dir) {
        System.out.println(this.toString());
        List<Tile> tileList = Arrays.asList(letters);
        int score = 0;
        if (tileList.contains(Tile.BLANK)) {
            int index = tileList.indexOf(Tile.BLANK);
            Tile newTile = checkMoveWithBlank(row, col, tileList, dir, false);
            if (!newTile.equals(Tile.BLANK)) {
                letters[index] = newTile;
            } else {
                return score;
            }
        }
        if (dir.equalsIgnoreCase("hor")) {
            score = moveHorLegal(row, col, letters);
        } else if (dir.equalsIgnoreCase("ver")) {
            score = moveVerLegal(row, col, letters);
        }
        return score;
    }

    private Tile checkMoveWithBlank(int row, int col, List<Tile> tiles, String dir, boolean first) {
        int index = tiles.indexOf(Tile.BLANK);
        for (int i = 0; i < 26; i++) {
            Tile tile = allEnums.get(i);
            tiles.set(index, tile);
            if (!first) {
                if (checkIfMoveLegal(row, col, tiles.toArray(new Tile[tiles.size()]), dir) > 0) {
                    return tile;
                }
            } else {
                if (checkIfFirstMoveLegal(row, col, tiles.toArray(new Tile[tiles.size()]), dir) > 0) {
                    return tile;
                }
            }
        }
        return Tile.BLANK;
    }


    //if fields are empty horizontally it places it on temporary board and
    // calls methode to check if there is new word on board.
//    private int moveHorLegal(int row, int col, Tile[] letters) {//wordsOnBoard
//        HashMap<String,ArrayList<Integer>> movesWords = new HashMap<String,ArrayList<Integer>>();
//        int score = 0;
//        boolean legal = false;
//        String word = "";
//        Board tempBoard = boardCopy();
//        tempBoard.setMove(row,col,letters,"hor");
//        int[] wordStart = horWordStart(row,col,tempBoard);
//        int nRow = wordStart[0];
//        int nCol = wordStart[1];
//
//        if(aroundNotEmptyHor(nRow,nCol,tempBoard)) {
//            movesWords = horGetWord(nRow, nCol, tempBoard);
//            word = movesWords.keySet().toString().replaceAll("[\\[\\]]", "");
//            if (checker.checkWord(word)) {
//                for (int i = nCol; i < DIM; i++) {
//                    if (topEmpty(nRow, i, tempBoard) || bottomEmpty(nRow, i, tempBoard)) {
//                        legal = true;
//                        score = score + calculateScore(word, movesWords.get(word));
//                        break;
//                    }
//                }
//
//            }
//            for (int i = nCol; i < DIM; i++) {
//                if (topEmpty(nRow, i, tempBoard) || bottomEmpty(nRow, i, tempBoard)) {
//                    int[] vStart = verWordStart(nRow, i, tempBoard);
//                    movesWords = verGetWord(vStart[0], vStart[1], tempBoard);
//                    word = movesWords.keySet().toString().replaceAll("[\\[\\]]", "");
//                    if (checker.checkWord(word)) {
//                        legal = true;
//                        score = score + calculateScore(word, movesWords.get(word));
//                    }
//                }
//            }
//        }
//        return score;
//    }

    private int moveHorLegal(int row, int col, Tile[] letters) {//wordsOnBoard
        HashMap<String, ArrayList<Integer>> movesWords = new HashMap<String, ArrayList<Integer>>();
        int score = 0;
        boolean legal = false;
        String word = "";
        Board tempBoard = boardCopy();
        Board boardBefore = boardCopy();
        ;
        tempBoard.setMove(row, col, letters, "hor");
        int[] wordStart = horWordStart(row, col, tempBoard);
        int nRow = wordStart[0];
        int nCol = wordStart[1];
        movesWords = horGetWord(nRow, nCol, tempBoard);

        word = movesWords.keySet().toString().replaceAll("[\\[\\]]", "");
        if (checker.checkWord(word)) {
            for (int i = nCol; i < nCol + word.length(); i++) {//row
                if (checkTopBottomNotEmpty(nRow, i, tempBoard)) {
                    int[] newStart = verWordStart(nRow, i, tempBoard);
                    HashMap<String, ArrayList<Integer>> newWords = verGetWord(newStart[0], newStart[1], tempBoard);
                    String newWord = newWords.keySet().toString().replaceAll("[\\[\\]]", "");
                    if (checker.checkWord(newWord)) {
                        //need to check if we can add word...;
                        if (checkIfWordNotExistedBeforeVer(newStart[0], newStart[1], newWord.length(), boardBefore)) {
                            movesWords.putAll(newWords);
                            legal = true;
                        }
                        legal = true;
//                        else{
//                            legal = false;
//                        }
                    } else {
                        legal = false;
                        break;
                    }

                }
            }
        } else {
            return score - 1;
            //illegal word....
        }
        if (legal) {
            score = calculateScore(movesWords);
        }
        return score;
    }

    public boolean checkIfWordNotExistedBeforeVer(int row, int col, int size, Board tempBoard) {
        for (int i = row; i < row + size; i++) {
            if (tempBoard.isEmptyField(i, col)) {
                return true;
            }
        }
        return false;
    }


    private boolean checkTopBottomNotEmpty(int row, int col, Board tempBoard) {
        if (!bottomEmpty(row, col, tempBoard) || !topEmpty(row, col, tempBoard)) {
            return true;
        }
        return false;
    }

    private boolean checkLeftRightNotEmpty(int row, int col, Board tempBoard) {
        if (!leftEmpty(row, col, tempBoard) || !rightEmpty(row, col, tempBoard)) {
            return true;
        }
        return false;
    }


    private HashMap<String, ArrayList<Integer>> horGetWord(int row, int col, Board tempBoard) {
        ArrayList<Integer> coords = new ArrayList<Integer>();
        String word = "";
        HashMap<String, ArrayList<Integer>> movesWords = new HashMap<String, ArrayList<Integer>>();
        while (col < DIM && !tempBoard.isEmptyField(row, col)) {
            word = word + tempBoard.getField(row, col).toString();
            coords.add(index(row, col));
            col++;
        }
        movesWords.put(word, coords);
        return movesWords;
    }


    private int[] horWordStart(int row, int col, Board tempBoard) {
        int[] result = new int[2];
        for (int i = col; i >= 0; i--) {
            if (tempBoard.isEmptyField(row, i)) {
                return new int[]{row, i + 1};
            }
        }
        return null;
    }

//    private int moveVerLegal(int row, int col, Tile[] letters) {//wordsOnBoard
//        HashMap<String,ArrayList<Integer>> movesWords = new HashMap<String,ArrayList<Integer>>();
//        int score = 0;
//        boolean legal = false;
//        String word = "";
//        Board tempBoard = boardCopy();
//        tempBoard.setMove(row,col,letters,"ver");
//        int[] wordStart = verWordStart(row,col,tempBoard);
//        int nRow = wordStart[0];
//        int nCol = wordStart[1];
//
//        if(aroundNotEmptyVer(nRow,nCol,tempBoard)) {
//            movesWords = verGetWord(nRow, nCol, tempBoard);
//
//            word = movesWords.keySet().toString().replaceAll("[\\[\\]]", "");
//            if (checker.checkWord(word)) {
//                for (int i = nRow; i < DIM; i++) {
//                    if (leftEmpty(i, nCol, tempBoard) || rightEmpty(i, nCol, tempBoard)) {
//                        legal = true;
//                        score = score + calculateScore(word, movesWords.get(word));
//                        break;
//                    }
//                }
//
//            }
//            for (int i = nRow; i < DIM; i++) {
//                if (leftEmpty(i, nCol, tempBoard) || rightEmpty(i, nCol, tempBoard)) {
//                    int[] vStart = horWordStart(i, nCol, tempBoard);
//                    movesWords = horGetWord(vStart[0], vStart[1], tempBoard);
//                    word = movesWords.keySet().toString().replaceAll("[\\[\\]]", "");
//                    if (checker.checkWord(word)) {
//                        legal = true;
//                        score = score + calculateScore(word, movesWords.get(word));
//                    }
//                }
//            }
//        }
//        return score;
//    }

    private int moveVerLegal(int row, int col, Tile[] letters) {//wordsOnBoard
        HashMap<String, ArrayList<Integer>> movesWords = new HashMap<String, ArrayList<Integer>>();
        int score = 0;
        boolean legal = false;
        String word = "";
        Board tempBoard = boardCopy();
        Board boardBefore = boardCopy();
        tempBoard.setMove(row, col, letters, "ver");
        int[] wordStart = verWordStart(row, col, tempBoard);
        int nRow = wordStart[0];
        int nCol = wordStart[1];
        movesWords = verGetWord(nRow, nCol, tempBoard);

        word = movesWords.keySet().toString().replaceAll("[\\[\\]]", "");
        if (checker.checkWord(word)) {
            for (int i = nRow; i < nRow + word.length(); i++) {//row
                if (checkLeftRightNotEmpty(i, nCol, tempBoard)) {
                    int[] newStart = horWordStart(i, nCol, tempBoard);
                    HashMap<String, ArrayList<Integer>> newWords = horGetWord(newStart[0], newStart[1], tempBoard);
                    String newWord = newWords.keySet().toString().replaceAll("[\\[\\]]", "");
                    if (checker.checkWord(newWord)) {
                        //need to check if we can add word...;
                        if (checkIfWordNotExistedBeforeHor(newStart[0], newStart[1], newWord.length(), boardBefore)) {
                            movesWords.putAll(newWords);
                            legal = true;
                        }
                        legal = true;
                    } else {
                        legal = false;
                        break;
                    }

                }
            }
        } else {
            return score - 1;//Should be -1 here to show that word was invalid so i can skip on server..
            //illegal word....
        }
        if (legal) {
            score = calculateScore(movesWords);
        }

        return score;
    }

    public boolean checkIfWordNotExistedBeforeHor(int row, int col, int size, Board tempBoard) {
        for (int i = col; i < col + size; i++) {
            if (tempBoard.isEmptyField(row, i)) {
                return true;
            }
        }
        return false;
    }


    private HashMap<String, ArrayList<Integer>> verGetWord(int row, int col, Board tempBoard) {
        ArrayList<Integer> coords = new ArrayList<Integer>();
        String word = "";
        HashMap<String, ArrayList<Integer>> movesWords = new HashMap<String, ArrayList<Integer>>();
        while (row < DIM && !tempBoard.isEmptyField(row, col)) {
            word = word + tempBoard.getField(row, col).toString();
            coords.add(index(row, col));
            row++;
            //System.out.println("Row: " + row);
        }
        movesWords.put(word, coords);
        return movesWords;
    }

    private int[] verWordStart(int row, int col, Board tempBoard) {
        int[] result = new int[2];
        for (int i = row; i > 0; i--) {
            if (tempBoard.isEmptyField(i, col)) {
                return new int[]{i + 1, col};
            }
        }
        return null;
    }

    public boolean topEmpty(int row, int col, Board tempBoard) {
        if (row == 0) {
            return true;
        }
        return tempBoard.isEmptyField(row - 1, col);
    }

    public boolean bottomEmpty(int row, int col, Board tempBoard) {
        if (row == 14) {
            return true;
        }
        return tempBoard.isEmptyField(row + 1, col);
    }

    public boolean leftEmpty(int row, int col, Board tempBoard) {
        if (col == 0) {
            return true;
        }
        return tempBoard.isEmptyField(row, col - 1);
    }

    public boolean rightEmpty(int row, int col, Board tempBoard) {
        if (col == 14) {
            return true;
        }
        return tempBoard.isEmptyField(row, col + 1);
    }


//    private int calculateScore(String word,ArrayList<Integer> coords){
//        Tile[] tiles = stringToTile(word.split(""));
//        int score = 0;
//        int oldScore = 0;
//        for (int i=0;i<tiles.length;i++){
//            oldScore = score;
//            //System.out.println("Cord: " + coords.get(i));
//            score = oldScore + tiles[i].getValue() * getMultiplier(coords.get(i));
//
//        }return score;
//    }

    private int calculateScore(HashMap<String, ArrayList<Integer>> wordsScore) {
        int score = 0;
        for (Map.Entry set : wordsScore.entrySet()) {
            int wordScore = 0;
            int wordMultiplier = 1;
            String[] letters = set.getKey().toString().split("");
            Tile[] tiles = stringToTile(letters);
            for (int i = 0; i < tiles.length; i++) {
                ArrayList<Integer> coords = wordsScore.get(set.getKey());
                System.out.println("Tile l: " + tiles.length + " Cord l: " + coords.size());
                int tileMultiplier = getTileMultiplier(coords.get(i));
                wordScore = wordScore + tiles[i].getValue() * tileMultiplier;
                wordMultiplier = wordMultiplier * getWordMultiplier(coords.get(i));
                removeSpecialTile(coords.get(i));
            }
            score = score + wordScore * wordMultiplier;
            System.out.println("Word: " + set.getKey().toString() + " Score: " + wordScore * wordMultiplier);

        }

        return score;

    }

    private void removeSpecialTile(int coord) {

        if (specialTiles.get("darkBlueX3").contains(coord)) {
            specialTiles.get("darkBlueX3").remove(Integer.valueOf(coord));
        } else if (specialTiles.get("paleBlueX2").contains(coord)) {
            specialTiles.get("paleBlueX2").remove(Integer.valueOf(coord));
        } else if (specialTiles.get("paleRedX2").contains(coord)) {
            specialTiles.get("paleRedX2").remove(Integer.valueOf(coord));
        } else if (specialTiles.get("darkRedX3").contains(coord)) {
            specialTiles.get("darkRedX3").remove(Integer.valueOf(coord));
        } else if (coord == startPosition) {
            startPosition = -1;
        }
    }

    private int getTileMultiplier(int coord) {
        int multiplier = 1;
        if (specialTiles.get("darkBlueX3").contains(coord)) {
            multiplier = 3;
        } else if (specialTiles.get("paleBlueX2").contains(coord)) {
            multiplier = 2;
        }
        return multiplier;

    }

    private int getWordMultiplier(int coord) {
        int multiplier = 1;
        if (specialTiles.get("paleRedX2").contains(coord)) {
            multiplier = 2;
        } else if (coord == startPosition) {
            multiplier = 2;
        } else if (specialTiles.get("darkRedX3").contains(coord)) {
            multiplier = 3;
        }
        return multiplier;

    }


//    //Checks if fields horizontally in which word should go are empty.Doesn't check if letter is "-" aka empty.
//    private boolean moveHorEmpty(int row, int col, Tile[] letters) {
//        int count = letters.length + col;
//        int counter = 0;
//        for (int i = col; i < count ; i++) {
//            if (letters[counter] != Tile.EMPTY) {
//                if (isEmptyField(row, i)) {
//                    counter++;
//                } else {
//                    return false;
//                }
//            }else{
//                counter++;
//            }
//
//
//        }
//        return true;
//
//    }

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

//    //Checks if fields vertically in which word should go are empty.Doesn't check if letter is "-" aka empty.
//    private boolean moveVerEmpty(int row, int col, Tile[] letters) {
//        int count = letters.length + row;
//        int counter = 0;
//        for (int i = col; i < count ; i++) {
//            //System.out.println("Row: " + row + " Col: " + col + " lSize: " + letters.length + " counter: " + counter);
//            if (letters[counter] != Tile.EMPTY) {
//                if (isEmptyField(i, col)) {
//                    counter++;
//                } else {
//                    return false;
//                }
//            }else{
//                counter++;
//            }
//
//        }
//        return true;
//    }


    //Based on hor or ver calls specific setMove method,after which list of words on board are added.
    public void setMove(int row, int col, Tile[] letters, String dir) {
        List<Tile> tileList = Arrays.asList(letters);
        if (tileList.contains(Tile.BLANK)) {
            int index = tileList.indexOf(Tile.BLANK);
            Tile newTile = checkMoveWithBlank(row, col, tileList, dir, false);
            if (!newTile.equals(Tile.BLANK)) {
                letters[index] = newTile;
            }
        }
        //System.out.println("Row: " + (row+letters.length) + " Col: " +(col+letters.length));
        if (dir.equalsIgnoreCase("hor")) {
            if (col + letters.length < 15) {
                setMoveHor(row, col, letters);
            }
        } else if (dir.equalsIgnoreCase("ver")) {
            if (row + letters.length < 15) {
                setMoveVer(row, col, letters);
            }
        }
    }

    //Set tiles in positions horizontally.
    private void setMoveHor(int row, int col, Tile[] letters) {
        int count = letters.length + col;
        int counter = 0;
        for (int i = col; i < count + 1; i++) {
            //New
//            if(count+1>14){
//                return;
//            }
            if (counter < letters.length) {
                if (isEmptyField(row, i)) {
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
        for (int i = row; i < count + 1; i++) {
            //New
//            if(count+1>14){
//                return;
//            }
            if (counter < letters.length) {
                if (isEmptyField(i, col)) {
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
        resetSpecialTiles();
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

    public ArrayList<Tile> removeFromRackAndFill(Tile[] tilesRem, ArrayList<Tile> oldRack) {
        //ArrayList<Tile> tempArray = new ArrayList<Tile>();
        int counter;
        if (getBag().size() >= tilesRem.length) {
            counter = 0;
            for (Tile tile : tilesRem) {
                oldRack.remove(tile);
            }
            int rackSize = oldRack.size();
            for (int i = rackSize; i < 7; i++) {
                oldRack.add(getBag().get(counter));
                getBag().remove(counter);
                counter++;
            }

        } else {
            counter = 0;
            for (int i = 0; i < getBag().size(); i++) {
                oldRack.remove(tilesRem[i]);
            }
            int rackSize = oldRack.size();
            for (int i = rackSize; i < rackSize + getBag().size(); i++) {
                oldRack.add(getBag().get(counter));
                getBag().remove(counter);
                counter++;
            }


        }
        return oldRack;

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
    private void resetSpecialTiles() {
        specialTiles = new HashMap<String, ArrayList<Integer>>();
        //0 0, 0 7, 0 14, 7 0, 7 14, 14 0, 14 7, 14 14 -8
        ArrayList<Integer> darkRedX3 = new ArrayList<Integer>();
        Collections.addAll(darkRedX3, index(0, 0), index(0, 7), index(0, 14),
                index(7, 0), index(7, 14), index(14, 0), index(14, 7),
                index(14, 14));

        //1 1, 1 13, 2 2, 2 12, 3 3, 3 11, 4 4, 4 10, 10 4, 10 10, 11 3, 11 11, 12 2, 12 12, 13 1, 13 13 -16
        ArrayList<Integer> paleRedX2 = new ArrayList<Integer>();
        Collections.addAll(paleRedX2, index(1, 1), index(1, 13), index(2, 2)
                , index(2, 12), index(3, 3), index(3, 11), index(4, 4)
                , index(4, 10), index(10, 4), index(10, 10), index(11, 3)
                , index(11, 11), index(12, 2), index(12, 12), index(13, 1)
                , index(13, 13));

        startPosition = index(7, 7);
        //1 5, 1 9, 5 1, 5 5, 5 9, 5 13, 9 1, 9 5, 9 9, 9 13, 13 5, 13 9; -12
        ArrayList<Integer> darkBlueX3 = new ArrayList<Integer>();
        Collections.addAll(darkBlueX3, index(1, 5), index(1, 9), index(5, 1)
                , index(5, 5), index(5, 9), index(5, 13), index(9, 1)
                , index(9, 5), index(9, 9), index(9, 13), index(13, 5)
                , index(13, 9));

        //0 3, 0 11, 2 6, 2 8, 3 0, 3 7, 3 14, 6 2, 6 6, 6 8, 6 12, 7 3, 7 11, 8 2, 8 6,
        // 8 8, 8 12, 11 0, 11 7, 11 14, 12 6, 12 8, 14 3, 14 11 -24
        ArrayList<Integer> paleBlueX2 = new ArrayList<Integer>();
        Collections.addAll(paleBlueX2, index(0, 3), index(0, 11), index(2, 6)
                , index(2, 8), index(3, 0), index(3, 7), index(3, 14)
                , index(6, 2), index(6, 6), index(6, 8), index(6, 12)
                , index(7, 3), index(7, 11), index(8, 2), index(8, 6)
                , index(8, 8), index(8, 12), index(11, 0), index(11, 7)
                , index(11, 14), index(12, 6), index(12, 8), index(14, 3)
                , index(14, 11));

        specialTiles.put("darkRedX3", darkRedX3);
        specialTiles.put("paleRedX2", paleRedX2);
        specialTiles.put("darkBlueX3", darkBlueX3);
        specialTiles.put("paleBlueX2", paleBlueX2);


    }

    //From row and col return one number index;
    public int index(int row, int col) {
        return row * DIM + col;
    }

    //Working for server !!!!!!!!!
//    @Override
//    public String toString() {
//        String printBoard = "";
//        for (int i = 0; i < DIM; i++) {
//            String row = "";
//            for (int j = 0; j < DIM; j++) {
//                row = row  + getField(i, j) ;
//                if (j < DIM - 1) {
//                    row = row + ",";
//                }
//            }
//            printBoard = printBoard + row;
//            if (i < DIM - 1) {
//                printBoard = printBoard + ",";
//            }
//        }
//        return printBoard;
//    }


    //For running test use this!
//    @Override
//    public String toString() {
//        String printBoard = "";
//        for (int i = 0; i < DIM; i++) {
//            String row = "";
//            for (int j = 0; j < DIM; j++) {
//                row = row + " " + getField(i, j) + " ";
//                if (j < DIM - 1) {
//                    row = row + "|";
//                }
//            }
//            printBoard = printBoard + row;
//            if (i < DIM - 1) {
//                printBoard = printBoard + "\n" + LINE + "\n";
//            }
//        }
//        return printBoard;
//    }


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


//    //From document takes words and adds them to checkWords array.
//    private void papulateCheckWords() {
//        File file = new File("src\\Utils\\collins_scrabble_words_2019.txt");
//        Scanner sc = null;
//        checkWords = new ArrayList<String>();
//        blackList = new ArrayList<String>();
//        Collections.addAll(blackList,"II","III","IV","VI","VII","VIII","IX","XI","XII","XIII");
//        try {
//            sc = new Scanner(file, StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        while (sc.hasNext()) {
//            String[] line = sc.next().split(" ");
//            String theWord = line[0].replace("\t", "");
//            theWord = theWord.replace(",", "");
//            if (wordIsUpperCase(theWord) && theWord.length() > 1 && !blackList.contains(theWord)) {
//                checkWords.add(theWord);
//            }
//        }sc.close();
//
//    }
//
//    //checks if word exists in checkWords array.
//    public boolean checkWord(String word) {
//        if (checkWords.contains(word.toUpperCase())) {
//            return true;
//        }
//        return false;
//    }

//    public boolean checkWord(String word) {//Not working!!
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

//    //Checks if word is uppercase if yes returns true otherwise false.
//    public boolean wordIsUpperCase(String string) {
//        for (int i = 0; i < string.length(); i++) {
//            if (Character.isUpperCase(string.charAt(i))) {
//            } else {
//                return false;
//            }
//        }
//        return true;
//    }

    //From letter list returns representing Tile list.
    public String tileToString(Tile[] letters) {
        String result = "";
        for (int i = 0; i < letters.length; i++) {
            result = result + letters[i].toString();
        }
        return result;
    }

    public String tileToString(ArrayList<Tile> letters) {
        String result = "";
        for (int i = 0; i < letters.size(); i++) {
            result = result + letters.get(i).toString();
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
