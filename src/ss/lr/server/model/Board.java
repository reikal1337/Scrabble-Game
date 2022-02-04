package ss.lr.server.model;

import utils.WordChecker;

import java.util.*;

/***
 board.java class is responsible for creating new 15x15
 board and maintaining it,here lies almost all scrabble game logic.
 In here you can generate racks,make bag of letters and generate special tiles.
 Also, you can check if move is legal or set the move.
 @author Lukas Reika s2596237.
 */


public class Board {

    //Dimensions.
    private static final int DIM = 15;
    //All tile type enums.
    private static ArrayList<Tile> allEnums;
    //Board with fields.
    private final Tile[][] field;
    //Letter bag.
    public ArrayList<Tile> letterBag;
    //Checker with whom you check if word is valid.
    public WordChecker checker;
    //Start position.
    private int startPosition;
    //All premium tiles.
    private HashMap<String, ArrayList<Integer>> specialTiles;


    /**
     * Creates 15x15 board and assigns checker.
     *
     * @param checker
     * @requires checker != null
     * @ensures field is 15x15, reset() is called.
     */
    public Board(WordChecker checker) {
        this.field = new Tile[DIM][DIM];
        this.checker = checker;
        reset();
    }

    /**
     * Returns letter bag as List.
     *
     * @return ArrayList<Tile> of letter bag.
     * @requires letterBag != null
     */
    public ArrayList<Tile> getBag() {
        return this.letterBag;
    }

    /**
     * Returns specified field.
     *
     * @param row
     * @param col
     * @return Tile of specified field
     * @requires row and col is 0-14
     * @ensures field is returned
     */
    public Tile getField(int row, int col) {
        if (isField(row, col)) {
            return this.field[row][col];
        }
        return null;
    }

    /**
     * Sets specified field to specified tile.
     *
     * @param row
     * @param col
     * @param tile
     * @return Tile of specified field
     * @requires row and col is 0-14
     * @ensures field is set/changed
     */
    public void setField(int row, int col, Tile tile) {
        if (isField(row, col)) {
            this.field[row][col] = tile;
        }
    }

    /**
     * Fills bag with all possible Tiles and shuffles it.
     * Fills allEnums with all possible differentiations of Tiles.
     *
     * @ensures letterBag and allEnums are filled.
     */
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

    /**
     * Checks if row and col is 0-14.
     *
     * @param row
     * @param col
     * @return boolean
     * @requires row, col to be integer.
     * @ensures row and col is 0-14
     */
    public boolean isField(int row, int col) {
        return row >= 0 && row < 15 && col >= 0 && col < 15;
    }


    /**
     * Copies current board and returns it.
     *
     * @return boards copy
     * @requires this != null
     */
    public Board boardCopy() {
        Board newBoard = new Board(this.checker);
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                newBoard.setField(i, j, getField(i, j));
            }
        }
        return newBoard;
    }


    /**
     * Checks if thre are BLANK tiles,if yes calls checkMoveWithBlank;
     * If not calls firstMoveHorLegal or firstMoveVerLegal based
     * on direction wich return score,that score is then returned.
     *
     * @param row
     * @param col
     * @param letters
     * @param dir
     * @return > 0 if move is legal,0 if move is illegal,-1 if word is illegal.
     * @requires row, col=7 ,letters != null,dir ="vor"|"hor"
     * @ensures moves score is calculated
     */
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
        }
        return score;
    }

    /**
     * Checks if move is legal horizontally by placing it in copy of board
     * and checking with checker if it's legal word.
     *
     * @param row
     * @param col
     * @param letters
     * @return > 0 if move is legal,0 if move is illegal,-1 if word is illegal.
     * @ensures moves score is calculated
     * @requires letters != null
     */
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

    /**
     * Checks if move is legal vertically by placing it in copy of board
     * and checking with checker if it's a legal word.
     *
     * @param row
     * @param col
     * @param letters
     * @return > 0 if move is legal,0 if move is illegal,-1 if word is illegal.
     * @requires letters != null,
     * @ensures moves score is calculated
     */
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


    /**
     * Checks if there are BLANK tiles,if yes calls checkMoveWithBlank;
     * If not calls moveHorLegal or moveVerLegal based
     * on direction which return score,that score is then returned.
     *
     * @param row
     * @param col
     * @param letters
     * @param dir
     * @return > 0 if move is legal,0 if move is illegal,-1 if word is illegal.
     * @requires row, col=7 ,letters != null,dir ="vor"|"hor"
     * @ensures moves score is calculated
     */
    public int checkIfMoveLegal(int row, int col, Tile[] letters, String dir) {
        System.out.println(this);
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

    /**
     * Checks if move is legal with any possible Tile instead of BLANK.
     * If yes,that Tile is returned,if not BLANK tile is returned.
     *
     * @param row
     * @param col
     * @param tiles
     * @param dir
     * @return Tile.Some letter or Tile.BLANK
     * @requires row, col=7 ,letters != null,dir ="vor"|"hor"
     * @ensures moves score is calculated
     */
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


    /**
     * Checks if move is legal horizontally by placing it in copy of board
     * and checking with checker if it's legal word,then checks if there are any surrounding tiles,
     * if yes, checks if it makes new word,if it's illegal,turn is illegal,if it's legal
     * word is checked if existed before,if not it's added to the score.
     *
     * @param row
     * @param col
     * @param letters
     * @return > 0 if move is legal,0 if move is illegal,-1 if word is illegal.
     * @ensures moves score is calculated
     * @requires letters != null, row,col = 0-14
     */
    private int moveHorLegal(int row, int col, Tile[] letters) {//wordsOnBoard
        HashMap<String, ArrayList<Integer>> movesWords = new HashMap<String, ArrayList<Integer>>();
        int score = 0;
        boolean legal = false;
        String word = "";
        Board tempBoard = boardCopy();
        Board boardBefore = boardCopy();
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
                        if (checkIfWordNotExistedBeforeVer(newStart[0], newStart[1], newWord.length(), boardBefore)) {
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
            return score - 1;
            //illegal word....
        }
        if (legal) {
            score = calculateScore(movesWords);
        }
        return score;
    }

    /**
     * Checks if on specific coordinates and board there is a word.
     *
     * @param row
     * @param col
     * @param size
     * @param tempBoard
     * @return true if existed,false otherwise
     * @requires letters != null, row,col = 0-14,tempBoard != null
     */
    public boolean checkIfWordNotExistedBeforeVer(int row, int col, int size, Board tempBoard) {
        for (int i = row; i < row + size; i++) {
            if (tempBoard.isEmptyField(i, col)) {
                return true;
            }
        }
        return false;
    }

    /**
     * By calling methods checks if top or bottom is empty.
     *
     * @param row
     * @param col
     * @param tempBoard
     * @return true if top || bottom field is not empty, otherwise false
     * @requires ro, col = 0-14,tempBoard != null
     */
    private boolean checkTopBottomNotEmpty(int row, int col, Board tempBoard) {
        return !bottomEmpty(row, col, tempBoard) || !topEmpty(row, col, tempBoard);
    }

    /**
     * By calling methods checks if lef or right is empty.
     *
     * @param row
     * @param col
     * @param tempBoard
     * @return true if left || right field is not empty, otherwise false
     * @requires ro, col = 0-14,tempBoard != null
     */
    private boolean checkLeftRightNotEmpty(int row, int col, Board tempBoard) {
        return !leftEmpty(row, col, tempBoard) || !rightEmpty(row, col, tempBoard);
    }


    /**
     * For given position goes through fields horizontally until empty field
     * is met, then from al those fields makes a word and returns it with
     * all tile coordinates.
     *
     * @param row
     * @param col
     * @param tempBoard
     * @return HashMap with word and Coordinates.
     * @requires ro, col = 0-14, tempBoard != null
     */
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


    /**
     * Goes left until empty field is met,the field before empty is word start.
     *
     * @param row
     * @param col
     * @param tempBoard
     * @return words start coordinates
     * @requires ro, col = 0-14, tempBoard != null
     * @ensure word start is found.
     */
    private int[] horWordStart(int row, int col, Board tempBoard) {
        int[] result = new int[2];
        for (int i = col; i >= 0; i--) {
            if (tempBoard.isEmptyField(row, i)) {
                return new int[]{row, i + 1};
            }
        }
        return null;
    }

    /**
     * Checks if move is legal vertically by placing it in copy of board
     * and checking with checker if it's legal word,then checks if there are any surrounding tiles,
     * if yes, checks if it makes new word,if it's illegal,turn is illegal,if it's legal
     * word is checked if existed before,if not it's added to the score.
     *
     * @param row
     * @param col
     * @param letters
     * @return > 0 if move is legal,0 if move is illegal,-1 if word is illegal.
     * @ensures moves score is calculated
     * @requires letters != null, row,col = 0-14
     */
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
                        if (checkIfWordNotExistedBeforeHor(newStart[0], newStart[1], newWord.length(), boardBefore)) {
                            movesWords.putAll(newWords);
                        }
                        legal = true;
                    } else {
                        legal = false;
                        break;
                    }

                }
            }
        } else {
            return score - 1;
        }
        if (legal) {
            score = calculateScore(movesWords);
        }

        return score;
    }

    /**
     * Checks if on specific coordinates and board there is a word.
     *
     * @param row
     * @param col
     * @param size
     * @param tempBoard
     * @return true if existed,false otherwise
     * @requires letters != null, row,col = 0-14,tempBoard != null
     */
    public boolean checkIfWordNotExistedBeforeHor(int row, int col, int size, Board tempBoard) {
        for (int i = col; i < col + size; i++) {
            if (tempBoard.isEmptyField(row, i)) {
                return true;
            }
        }
        return false;
    }


    /**
     * For given position goes through fields vertically until empty field
     * is met, then from al those fields makes a word and returns it with
     * all tile coordinates.
     *
     * @param row
     * @param col
     * @param tempBoard
     * @return HashMap with word and Coordinates.
     * @requires ro, col = 0-14, tempBoard != null
     */
    private HashMap<String, ArrayList<Integer>> verGetWord(int row, int col, Board tempBoard) {
        ArrayList<Integer> coords = new ArrayList<Integer>();
        String word = "";
        HashMap<String, ArrayList<Integer>> movesWords = new HashMap<String, ArrayList<Integer>>();
        while (row < DIM && !tempBoard.isEmptyField(row, col)) {
            word = word + tempBoard.getField(row, col).toString();
            coords.add(index(row, col));
            row++;
        }
        movesWords.put(word, coords);
        return movesWords;
    }

    /**
     * Goes to top until empty field is met,the field before empty is word start.
     *
     * @param row
     * @param col
     * @param tempBoard
     * @return words start coordinates
     * @requires ro, col = 0-14, tempBoard != null
     * @ensure word start is found.
     */
    private int[] verWordStart(int row, int col, Board tempBoard) {
        int[] result = new int[2];
        for (int i = row; i > 0; i--) {
            if (tempBoard.isEmptyField(i, col)) {
                return new int[]{i + 1, col};
            }
        }
        return null;
    }

    /**
     * Method checks if top is empty.
     *
     * @param row
     * @param col
     * @param tempBoard
     * @return true if top is empty, otherwise false
     * @requires ro, col = 0-14,tempBoard != null
     */

    public boolean topEmpty(int row, int col, Board tempBoard) {
        if (row == 0) {
            return true;
        }
        return tempBoard.isEmptyField(row - 1, col);
    }


    /**
     * Method checks if bottom is empty.
     *
     * @param row
     * @param col
     * @param tempBoard
     * @return true if bottom field is empty, otherwise false
     * @requires ro, col = 0-14,tempBoard != null
     */
    public boolean bottomEmpty(int row, int col, Board tempBoard) {
        if (row == 14) {
            return true;
        }
        return tempBoard.isEmptyField(row + 1, col);
    }

    /**
     * Methods checks if left of field is empty.
     *
     * @param row
     * @param col
     * @param tempBoard
     * @return true if left field empty, otherwise false
     * @requires ro, col = 0-14,tempBoard != null
     */
    public boolean leftEmpty(int row, int col, Board tempBoard) {
        if (col == 0) {
            return true;
        }
        return tempBoard.isEmptyField(row, col - 1);
    }

    /**
     * Method checks if right of field is empty.
     *
     * @param row
     * @param col
     * @param tempBoard
     * @return true if right field empty, otherwise false
     * @requires ro, col = 0-14,tempBoard != null
     */
    public boolean rightEmpty(int row, int col, Board tempBoard) {
        if (col == 14) {
            return true;
        }
        return tempBoard.isEmptyField(row, col + 1);
    }


    /**
     * Calculates each provided tiles score,than by provided coordinates
     * it checks if it was on any premium tiles and adds it.
     *
     * @param wordsScore
     * @return calculated score
     * @ensures score is calculated based on game rules.
     * @requires this.board is not empty, wordScore != null
     */
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
        }
        return score;
    }

    /**
     * Based on inputted coordinates special tile is removed form specialTiles map.
     *
     * @param coord
     * @ensures special tile is removed.
     * @requires coord != null, specialTiles != null
     */
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

    /**
     * Based on inputted coordinates checks if it is
     * special tile,if yes returns its multiplier
     *
     * @param coord
     * @return tiles multiplier
     * @ensures tile multiplier is checked
     * @requires coord != null, specialTiles != null
     */
    private int getTileMultiplier(int coord) {
        int multiplier = 1;
        if (specialTiles.get("darkBlueX3").contains(coord)) {
            multiplier = 3;
        } else if (specialTiles.get("paleBlueX2").contains(coord)) {
            multiplier = 2;
        }
        return multiplier;

    }

    /**
     * Based on inputted coordinates checks if it is
     * special word tile,if yes returns its multiplier
     *
     * @param coord
     * @return word multiplier
     * @ensures word multiplier is checked
     * @requires coord != null, specialTiles != null
     */
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

    /**
     * Checks if it has blank tile,if yes removes it and add new tile that would make word legal.
     * Based on direction calls methods.
     *
     * @param row
     * @param col
     * @param letters
     * @param dir
     * @requires letters != null, row,col = 0-14,tempBoard != null,dir = "vor"/"hor"
     * @ensures letters are placed on board.
     */
    public void setMove(int row, int col, Tile[] letters, String dir) {
        List<Tile> tileList = Arrays.asList(letters);
        if (tileList.contains(Tile.BLANK)) {
            int index = tileList.indexOf(Tile.BLANK);
            Tile newTile = checkMoveWithBlank(row, col, tileList, dir, false);
            if (!newTile.equals(Tile.BLANK)) {
                letters[index] = newTile;
            }
        }
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

    /**
     * Puts tiles horizontally,if it meets another tile,it jumps through it and continues.
     *
     * @param row
     * @param col
     * @param letters
     * @requires letters != null, row,col = 0-14,tempBoard != null.
     * @ensures letters are placed on board.
     */
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


    /**
     * Puts tiles vertically,if it meets another tile,it jumps through it and continues.
     *
     * @param row
     * @param col
     * @param letters
     * @requires letters != null, row,col = 0-14,tempBoard != null.
     * @ensures letters are placed on board.
     */
    private void setMoveVer(int row, int col, Tile[] letters) {
        int count = letters.length + row;
        int counter = 0;
        for (int i = row; i < count + 1; i++) {
            if (counter < letters.length) {
                if (isEmptyField(i, col)) {
                    setField(i, col, letters[counter]);
                    counter++;
                }
            }

        }
    }

    /**
     * resets board to begining state by calling multiple methods.
     *
     * @ensures board is rested to beginning state.
     */
    public ArrayList<ArrayList<Tile>> reset() {
        ArrayList<ArrayList<Tile>> result = new ArrayList<ArrayList<Tile>>();
        emptyBoard();
        fillBag();
        resetSpecialTiles();
        result.add(getRack());
        result.add(getRack());
        return result;

    }

    /**
     * Creates new rack from letterBag
     *
     * @return ArrayList<Tile> of new rack
     * @ensure new rack is made
     * @requires letrbag.size() != 0
     */
    public ArrayList<Tile> getRack() {
        ArrayList<Tile> result = new ArrayList<Tile>();
        if (letterBag.size() > 6) {
            for (int i = 0; i < 7; i++) {
                result.add(letterBag.get(i));
                letterBag.remove(i);
            }
        } else {
            for (int i = 0; i < letterBag.size(); i++) {
                result.add(letterBag.get(i));
                letterBag.remove(i);
            }
        }
        return result;
    }


    /**
     * Removes specified tiles from rack and then fills it up, so it has at least 7 letters.
     *
     * @param tilesRem
     * @param oldRack
     * @return ArrayList<Tile> filled rack
     * @ensures rack is refreshed
     * @requires tilesRem, oldRack, letterBag != null
     */
    public ArrayList<Tile> removeFromRackAndFill(Tile[] tilesRem, ArrayList<Tile> oldRack) {
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


    /**
     * Checks if field is empty
     *
     * @param row
     * @param col
     * @return true if field is empty,false otherwise.
     * @requires row, col = 0-14
     */
    public boolean isEmptyField(int row, int col) {
        return this.field[row][col].equals(Tile.EMPTY);
    }

    /**
     * Checks if board is empty
     *
     * @return true if board is empty,false otherwise
     */
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

    /**
     * Makes board empty.
     *
     * @ensures board is empty
     */
    private void emptyBoard() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                this.field[i][j] = Tile.EMPTY;
            }
        }
    }

    /**
     * Resets all special tile coordinates.
     *
     * @ensures special tile coordinates are full.
     */
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

    /**
     * returns index of row and col
     *
     * @param row
     * @param col
     * @return index of coordinates
     */
    public int index(int row, int col) {
        return row * DIM + col;
    }


    /**
     * Overides to string to represent board better,only used for testing !
     *
     * @return String of board
     */
    @Override
    public String toString() {
        String LINE = "---+---+---+---+---+---+---+---+---+---+---+---+---+---+---";
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


    /**
     * Converts tile array into one string,word.
     *
     * @param letters
     * @return String of letters
     * @requires letters != null
     */
    public String tileToString(Tile[] letters) {
        String result = "";
        for (int i = 0; i < letters.length; i++) {
            result = result + letters[i].toString();
        }
        return result;
    }

    /**
     * Converts tile list into one string,word.
     *
     * @param letters
     * @return String of letters
     * @requires letters != null
     */
    public String tileToString(ArrayList<Tile> letters) {
        String result = "";
        for (int i = 0; i < letters.size(); i++) {
            result = result + letters.get(i).toString();
        }
        return result;
    }


    /**
     * Converts String into Tile array.
     *
     * @param letters
     * @return Tile[]
     * @requires letters != null
     */
    public Tile[] stringToTile(String[] letters) {
        Tile[] result = new Tile[letters.length];
        for (int i = 0; i < letters.length; i++) {
            for (Tile tile : allEnums) {
                if (letters[i].equalsIgnoreCase(tile.toString())) {
                    result[i] = tile;
                }
            }
        }
        return result;
    }


}
