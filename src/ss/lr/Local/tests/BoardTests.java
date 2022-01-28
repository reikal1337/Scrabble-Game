package ss.lr.Local.tests;

import ss.lr.Local.model.Board;
import ss.lr.Local.model.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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
    public void testsetMoveHorizontally(){
        Tile[] testTile = {Tile.L,Tile.I,Tile.T,Tile.H};
        board.setMove(7,7,testTile,"hor");
        assertEquals(board.getField(7,7),Tile.L);
        assertEquals(board.getField(7,8),Tile.I);
        assertEquals(board.getField(7,9),Tile.T);
        assertEquals(board.getField(7,10),Tile.H);
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
