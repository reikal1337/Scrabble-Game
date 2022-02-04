package ss.lr.server.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.lr.server.model.Board;
import ss.lr.server.model.Tile;
import utils.WordChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/***
 Board tests.
 @author Lukas Reika s2596237.
 */

public class BoardTests {
    private Board board;

    @BeforeEach
    public void setUp() {
        WordChecker checker = new WordChecker();
        board = new Board(checker);
    }

    @Test
    public void testBoardEpmtyAtStart() {
        assertTrue(board.isEmptyBoard());
        board.setField(2, 3, Tile.A);
        assertFalse(board.isEmptyBoard());
    }

    @Test
    public void testFillBag() {
        board.fillBag();
        assertEquals(ocurrance(board.letterBag, Tile.A), 9);
        assertEquals(ocurrance(board.letterBag, Tile.B), 2);
        assertEquals(ocurrance(board.letterBag, Tile.C), 2);
        assertEquals(ocurrance(board.letterBag, Tile.D), 4);
        assertEquals(ocurrance(board.letterBag, Tile.E), 12);
        assertEquals(ocurrance(board.letterBag, Tile.F), 2);
        assertEquals(ocurrance(board.letterBag, Tile.G), 2);
        assertEquals(ocurrance(board.letterBag, Tile.H), 2);
        assertEquals(ocurrance(board.letterBag, Tile.I), 8);
        assertEquals(ocurrance(board.letterBag, Tile.J), 2);
        assertEquals(ocurrance(board.letterBag, Tile.K), 2);
        assertEquals(ocurrance(board.letterBag, Tile.L), 4);
        assertEquals(ocurrance(board.letterBag, Tile.M), 2);
        assertEquals(ocurrance(board.letterBag, Tile.N), 6);
        assertEquals(ocurrance(board.letterBag, Tile.O), 8);
        assertEquals(ocurrance(board.letterBag, Tile.P), 2);
        assertEquals(ocurrance(board.letterBag, Tile.Q), 1);
        assertEquals(ocurrance(board.letterBag, Tile.R), 6);
        assertEquals(ocurrance(board.letterBag, Tile.S), 4);
        assertEquals(ocurrance(board.letterBag, Tile.T), 6);
        assertEquals(ocurrance(board.letterBag, Tile.U), 4);
        assertEquals(ocurrance(board.letterBag, Tile.V), 2);
        assertEquals(ocurrance(board.letterBag, Tile.W), 2);
        assertEquals(ocurrance(board.letterBag, Tile.X), 1);
        assertEquals(ocurrance(board.letterBag, Tile.Y), 2);
        assertEquals(ocurrance(board.letterBag, Tile.Z), 1);
        assertEquals(ocurrance(board.letterBag, Tile.BLANK), 2);
    }

    @Test
    public void testIsField() {
        assertTrue(board.isField(0, 14));
        assertTrue(board.isField(5, 0));
        assertTrue(board.isField(13, 13));
        assertFalse(board.isField(3, 15));
        assertFalse(board.isField(-1, 12));
        assertFalse(board.isField(100, 4));
    }

    @Test
    public void testSetGetField() {
        board.setField(5, 5, Tile.E);
        board.setField(2, 3, Tile.A);
        assertEquals(board.getField(5, 5), Tile.E);
        assertEquals(board.getField(2, 3), Tile.A);
        assertEquals(board.getField(1, 8), Tile.EMPTY);
        assertEquals(board.getField(7, 7), Tile.EMPTY);
        assertEquals(board.getField(12, 3), Tile.EMPTY);

    }

    @Test
    public void testSetMoveVertically() {
        Tile[] testTile = {Tile.L, Tile.I, Tile.T, Tile.H};
        board.setMove(7, 7, testTile, "ver");
        assertEquals(board.getField(7, 7), Tile.L);
        assertEquals(board.getField(8, 7), Tile.I);
        assertEquals(board.getField(9, 7), Tile.T);
        assertEquals(board.getField(10, 7), Tile.H);
    }

    @Test
    public void testSetMoveHorizontally() {
        Tile[] testTile = {Tile.L, Tile.I, Tile.T, Tile.H};
        board.setMove(7, 7, testTile, "hor");
        assertEquals(board.getField(7, 7), Tile.L);
        assertEquals(board.getField(7, 8), Tile.I);
        assertEquals(board.getField(7, 9), Tile.T);
        assertEquals(board.getField(7, 10), Tile.H);
    }


    //--------------------------------------------
    @Test
    public void testBoardCopy() {
        Tile[] testTile = {Tile.L, Tile.I, Tile.T, Tile.H};
        board.setMove(7, 7, testTile, "hor");
        Board copy = board.boardCopy();
        assertEquals(copy.getField(7, 7), Tile.L);
        assertEquals(copy.getField(7, 8), Tile.I);
        assertEquals(copy.getField(7, 9), Tile.T);
        assertEquals(copy.getField(7, 10), Tile.H);
    }

    @Test
    public void testCheckIfFirstMoveLegal() {
        Tile[] testTile = {Tile.L, Tile.I, Tile.T, Tile.H};
        //board.setMove(7,7,testTile,"hor");
        assertNotNull(board.checkIfFirstMoveLegal(7, 7, testTile, "ver"));
        assertNotNull(board.checkIfFirstMoveLegal(7, 7, testTile, "hor"));

        assertEquals(board.checkIfFirstMoveLegal(8, 8, testTile, "hor"), 0);
        assertEquals(board.checkIfFirstMoveLegal(8, 8, testTile, "ver"), 0);
    }

    @Test
    public void testCheckIfMoveLegal() {
        Tile[] testTile = {Tile.L, Tile.I, Tile.T, Tile.H};
        board.setMove(7, 7, testTile, "hor");
        Tile[] testTile2 = {Tile.L, Tile.T, Tile.H};
        assertNotNull(board.checkIfMoveLegal(6, 8, testTile2, "ver"));
        Tile[] testTile3 = {Tile.L, Tile.I, Tile.H};
        assertNotNull(board.checkIfMoveLegal(8, 6, testTile3, "hor"));

        assertEquals(board.checkIfMoveLegal(14, 14, testTile, "hor"), -1);
        assertEquals(board.checkIfMoveLegal(7, 7, testTile, "hor"), -1);
    }

    //double check..;
    @Test
    public void testAroundEmpty() {
        Tile[] testTile = {Tile.L, Tile.I, Tile.T, Tile.H};
        board.setMove(7, 7, testTile, "hor");
        Tile[] testTile2 = {Tile.L, Tile.T, Tile.H};
        board.setMove(6, 8, testTile, "ver");

        assertTrue(board.topEmpty(7, 7, board));
        assertFalse(board.topEmpty(7, 8, board));

        assertTrue(board.bottomEmpty(7, 7, board));
        assertFalse(board.bottomEmpty(7, 8, board));

        assertTrue(board.leftEmpty(7, 7, board));
        assertFalse(board.leftEmpty(7, 8, board));

        assertTrue(board.rightEmpty(7, 10, board));
        assertFalse(board.rightEmpty(7, 8, board));
    }

    @Test
    public void testReset() {
        Tile[] testTile = {Tile.L, Tile.I, Tile.T, Tile.H};
        board.setMove(7, 7, testTile, "hor");
        assertFalse(board.isEmptyBoard());
        board.reset();
        assertTrue(board.isEmptyBoard());
    }

    @Test
    public void testTileToString() {
        Tile[] testTile = {Tile.L, Tile.I, Tile.T, Tile.H};
        String word = board.tileToString(testTile);
        assertEquals(word.toLowerCase(), "lith");

        ArrayList<Tile> listTiles = new ArrayList<>(Arrays.asList(testTile));
        String listWord = board.tileToString(listTiles);
        assertEquals(listWord.toLowerCase(), "lith");
    }

    @Test
    public void testStringToTile() {
        String[] word = "lith".split("");
        Tile[] testTile = board.stringToTile(word);
        assertNotNull(testTile);
    }


    @Test
    public void testGetBag() {
        ArrayList<Tile> testBag = new ArrayList<Tile>();
        Collections.addAll(testBag, Tile.A, Tile.A, Tile.A, Tile.A, Tile.A, Tile.A, Tile.A, Tile.A, Tile.A, Tile.B,
                Tile.B, Tile.C, Tile.C, Tile.D, Tile.D, Tile.D, Tile.D, Tile.E, Tile.E, Tile.E, Tile.E, Tile.E, Tile.E
                , Tile.E, Tile.E, Tile.E, Tile.E, Tile.E, Tile.E, Tile.F, Tile.F, Tile.G, Tile.G, Tile.H, Tile.H, Tile.I
                , Tile.I, Tile.I, Tile.I, Tile.I, Tile.I, Tile.I, Tile.I, Tile.J, Tile.J, Tile.K, Tile.K, Tile.L, Tile.L
                , Tile.L, Tile.L, Tile.M, Tile.M, Tile.N, Tile.N, Tile.N, Tile.N, Tile.N, Tile.N, Tile.O
                , Tile.O, Tile.O, Tile.O, Tile.O, Tile.O, Tile.O, Tile.O, Tile.P, Tile.P, Tile.Q, Tile.R
                , Tile.R, Tile.R, Tile.R, Tile.R, Tile.R, Tile.S, Tile.S, Tile.S, Tile.S, Tile.T, Tile.T
                , Tile.T, Tile.T, Tile.T, Tile.T, Tile.U, Tile.U, Tile.U, Tile.U, Tile.V, Tile.V, Tile.W
                , Tile.W, Tile.X, Tile.Y, Tile.Y, Tile.Z, Tile.BLANK, Tile.BLANK);
        ArrayList<Tile> boardBag = board.getBag();
        assertTrue(testBag.containsAll(boardBag));
    }

    @Test
    public void testCheckMoveWithBlank() {
        Tile[] testTile = {Tile.BLANK, Tile.L, Tile.L};
        assertTrue(board.checkIfFirstMoveLegal(7, 7, testTile, "hor") > 0);
        board.setMove(7, 7, testTile, "hor");
        assertEquals(board.getField(7, 7), Tile.A);
        assertEquals(board.getField(7, 8), Tile.L);
        assertEquals(board.getField(7, 9), Tile.L);
    }

    @Test
    public void testRemoveFromRackAndFill() {
        Tile[] tilesRem = {Tile.BLANK, Tile.K, Tile.L, Tile.P, Tile.L,};
        ArrayList<Tile> oldRack = new ArrayList<Tile>(Arrays.asList(Tile.L, Tile.P, Tile.K));
        assertEquals(board.removeFromRackAndFill(tilesRem, oldRack).size(), 7);


    }

    @Test
    public void testIfMoveLegal() {
        Tile[] testTile = {Tile.L, Tile.I, Tile.T, Tile.H};
        assertEquals(14, board.checkIfFirstMoveLegal(7, 7, testTile, "hor"));
        board.setMove(7, 7, testTile, "hor");

        Tile[] testTile2 = {Tile.L, Tile.T, Tile.H};
        assertEquals(9, board.checkIfMoveLegal(6, 8, testTile2, "ver"));
        board.setMove(6, 8, testTile2, "ver");

        Tile[] testTile3 = {Tile.L, Tile.I, Tile.T};
        assertEquals(-1, board.checkIfMoveLegal(8, 5, testTile3, "hor"));
        assertEquals(9, board.checkIfMoveLegal(9, 5, testTile3, "hor"));
        board.setMove(9, 5, testTile3, "hor");

        Tile[] testTile4 = {Tile.E, Tile.Z, Tile.Z, Tile.A, Tile.S};
        assertEquals(26, board.checkIfMoveLegal(9, 5, testTile4, "ver"));
        board.setMove(9, 5, testTile4, "ver");
        //ZANZA
        Tile[] testTile5 = {Tile.A, Tile.N, Tile.Z, Tile.A};
        assertEquals(24, board.checkIfMoveLegal(11, 5, testTile5, "hor"));
        board.setMove(11, 5, testTile5, "hor");
        //ZEKS
        Tile[] testTile6 = {Tile.Z, Tile.E, Tile.K};
        assertEquals(18, board.checkIfMoveLegal(14, 2, testTile6, "hor"));
        board.setMove(14, 2, testTile6, "hor");
        //HIZZ
        Tile[] testTile7 = {Tile.I, Tile.Z, Tile.Z};
        assertEquals(50, board.checkIfMoveLegal(7, 10, testTile7, "ver"));
        board.setMove(7, 10, testTile7, "ver");
        //ZANZA
        Tile[] testTile8 = {Tile.A, Tile.N, Tile.Z, Tile.A};
        assertEquals(23, board.checkIfMoveLegal(10, 10, testTile8, "hor"));
        board.setMove(10, 10, testTile8, "hor");
        //ABUZZ
        Tile[] testTile9 = {Tile.B, Tile.U, Tile.Z, Tile.Z};
        assertEquals(84, board.checkIfMoveLegal(10, 14, testTile9, "ver"));
        board.setMove(10, 14, testTile9, "ver");
        //SCHNOZZ
        Tile[] testTile10 = {Tile.S, Tile.C, Tile.H, Tile.O, Tile.Z, Tile.Z};
        assertEquals(66, board.checkIfMoveLegal(7, 12, testTile10, "ver"));
        board.setMove(7, 12, testTile10, "ver");
        //TAN
        Tile[] testTile11 = {Tile.A};
        assertEquals(3, board.checkIfMoveLegal(9, 7, testTile11, "ver"));
        board.setMove(9, 7, testTile11, "ver");
        //SEXY
        Tile[] testTile12 = {Tile.E, Tile.X, Tile.Y};
        assertEquals(-1, board.checkIfMoveLegal(7, 12, testTile12, "hor"));
        board.setMove(7, 12, testTile12, "hor");
        //System.out.println(board.toString());
    }


    public int ocurrance(ArrayList<Tile> list, Tile item) {
        int counter = 0;
        for (Tile tile : list) {
            if (tile.equals(item)) {
                counter++;
            }
        }
        return counter;

    }
}
