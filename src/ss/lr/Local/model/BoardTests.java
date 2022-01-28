package ss.lr.Local.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

;

public class BoardTests {
    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }
    @Test
    public void testBoardEpmtyAtStart(){
        assertTrue(board.isEmptyBoard());
    }

    @Test
    public void testFillBag(){
        board.fillBag();
        assertEquals(ocurrance(board.letterBag,Tile.A),9);
        assertEquals(ocurrance(board.letterBag,Tile.B),2);
        assertEquals(ocurrance(board.letterBag,Tile.C),2);
        assertEquals(ocurrance(board.letterBag,Tile.D),4);
        assertEquals(ocurrance(board.letterBag,Tile.E),12);
        assertEquals(ocurrance(board.letterBag,Tile.F),2);
        assertEquals(ocurrance(board.letterBag,Tile.G),2);
        assertEquals(ocurrance(board.letterBag,Tile.H),2);
        assertEquals(ocurrance(board.letterBag,Tile.I),8);
        assertEquals(ocurrance(board.letterBag,Tile.J),2);
        assertEquals(ocurrance(board.letterBag,Tile.K),2);
        assertEquals(ocurrance(board.letterBag,Tile.L),4);
        assertEquals(ocurrance(board.letterBag,Tile.M),2);
        assertEquals(ocurrance(board.letterBag,Tile.N),6);
        assertEquals(ocurrance(board.letterBag,Tile.O),8);
        assertEquals(ocurrance(board.letterBag,Tile.P),2);
        assertEquals(ocurrance(board.letterBag,Tile.Q),1);
        assertEquals(ocurrance(board.letterBag,Tile.R),6);
        assertEquals(ocurrance(board.letterBag,Tile.S),4);
        assertEquals(ocurrance(board.letterBag,Tile.T),6);
        assertEquals(ocurrance(board.letterBag,Tile.U),4);
        assertEquals(ocurrance(board.letterBag,Tile.V),2);
        assertEquals(ocurrance(board.letterBag,Tile.W),2);
        assertEquals(ocurrance(board.letterBag,Tile.X),1);
        assertEquals(ocurrance(board.letterBag,Tile.Y),2);
        assertEquals(ocurrance(board.letterBag,Tile.Z),1);
        assertEquals(ocurrance(board.letterBag,Tile.BLANK),2);
    }

    @Test
    public void testIsField(){
        assertTrue(board.isField(0,14));
        assertTrue(board.isField(5,0));
        assertTrue(board.isField(13,13));
        assertFalse(board.isField(3,15));
        assertFalse(board.isField(-1,12));
        assertFalse(board.isField(100,4));
    }

    @Test
    public void testSetGetField(){
        board.setField(5,5,Tile.E);
        board.setField(2,3,Tile.A);
        assertEquals(board.getField(5,5),Tile.E);
        assertEquals(board.getField(2,3),Tile.A);
        assertEquals(board.getField(1,8),Tile.EMPTY);
        assertEquals(board.getField(7,7),Tile.EMPTY);
        assertEquals(board.getField(12,3),Tile.EMPTY);

    }

    @Test
    public void testSetMoveVertically(){
        Tile[] testTile = {Tile.L,Tile.I,Tile.T,Tile.H};
        board.setMove(7,7,testTile,"ver");
        assertEquals(board.getField(7,7),Tile.L);
        assertEquals(board.getField(8,7),Tile.I);
        assertEquals(board.getField(9,7),Tile.T);
        assertEquals(board.getField(10,7),Tile.H);
    }

    @Test
    public void testSetMoveHorizontally(){
        Tile[] testTile = {Tile.L,Tile.I,Tile.T,Tile.H};
        board.setMove(7,7,testTile,"hor");
        assertEquals(board.getField(7,7),Tile.L);
        assertEquals(board.getField(7,8),Tile.I);
        assertEquals(board.getField(7,9),Tile.T);
        assertEquals(board.getField(7,10),Tile.H);
    }


    //--------------------------------------------
    @Test
    public void testBoardCopy(){
        Tile[] testTile = {Tile.L,Tile.I,Tile.T,Tile.H};
        board.setMove(7,7,testTile,"hor");
        Board copy = board.boardCopy();
        assertEquals(copy.getField(7,7),Tile.L);
        assertEquals(copy.getField(7,8),Tile.I);
        assertEquals(copy.getField(7,9),Tile.T);
        assertEquals(copy.getField(7,10),Tile.H);
    }

    @Test
    public void testCheckIfFirstMoveLegal(){
        Tile[] testTile = {Tile.L,Tile.I,Tile.T,Tile.H};
        //board.setMove(7,7,testTile,"hor");
        assertNotNull(board.checkIfFirstMoveLegal(7,7,testTile,"ver"));
        assertNotNull(board.checkIfFirstMoveLegal(7,7,testTile,"hor"));

        assertEquals(board.checkIfFirstMoveLegal(8,8,testTile,"hor"),0);
        assertEquals(board.checkIfFirstMoveLegal(8,8,testTile,"ver"),0);
    }

    @Test
    public void testCheckIfMoveLegal(){
        Tile[] testTile = {Tile.L,Tile.I,Tile.T,Tile.H};
        board.setMove(7,7,testTile,"hor");
        Tile[] testTile2 = {Tile.L,Tile.T,Tile.H};
        assertNotNull(board.checkIfMoveLegal(6,8,testTile2,"ver"));
        Tile[] testTile3 = {Tile.L,Tile.I,Tile.H};
        assertNotNull(board.checkIfMoveLegal(8,6,testTile3,"hor"));

        assertEquals(board.checkIfMoveLegal(14,14,testTile,"hor"),0);
        assertEquals(board.checkIfMoveLegal(7,7,testTile,"hor"),0);
    }

    //double check..;
    @Test
    public void testAroundEmpty(){
        Tile[] testTile = {Tile.L,Tile.I,Tile.T,Tile.H};
        board.setMove(7,7,testTile,"hor");
        Tile[] testTile2 = {Tile.L,Tile.T,Tile.H};
        board.setMove(6,8,testTile,"ver");

        assertTrue(board.topEmpty(7,7,board));
        assertFalse(board.topEmpty(7,8,board));

        assertTrue(board.bottomEmpty(6,8,board));
        assertFalse(board.bottomEmpty(7,8,board));

        assertTrue(board.leftEmpty(7,7,board));
        assertFalse(board.leftEmpty(7,8,board));

        assertTrue(board.rightEmpty(7,10,board));
        assertFalse(board.rightEmpty(7,8,board));
    }

    @Test
    public void testReset(){
        Tile[] testTile = {Tile.L,Tile.I,Tile.T,Tile.H};
        board.setMove(7,7,testTile,"hor");
        assertFalse(board.isEmptyBoard());
        board.reset();
        assertTrue(board.isEmptyBoard());
    }

    @Test
    public void testTileToString(){
        Tile[] testTile = {Tile.L,Tile.I,Tile.T,Tile.H};
        String word = board.tileToString(testTile);
        assertEquals(word.toLowerCase(),"lith");
    }

    @Test
    public void stringToTile(){
        Tile[] goodTile = {Tile.L,Tile.I,Tile.T,Tile.H};
        String[] word = "lith".split("");
        Tile[] testTile = board.stringToTile(word);
       // System.out.println(goodTile.toString()+"\n"+  testTile.toString() );
        assertNotNull(testTile);
    }






    public int ocurrance(ArrayList<Tile> list, Tile item){
        int counter = 0;
        for(Tile tile: list){
            if(tile.equals(item)){
                counter++;
            }
        }return counter;

    }
}
