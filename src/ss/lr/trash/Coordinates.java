package ss.lr.trash;

import java.util.ArrayList;

public class Coordinates {
    private ArrayList<Integer> rows = new ArrayList<Integer>();
    private ArrayList<Integer> cols = new ArrayList<Integer>();


    public Coordinates() {

    }

    public Coordinates(int row, int col) {
        rows.add(row);
        cols.add(col);

    }

    public Coordinates(int[] rows, int[] cols) {
        addAll(rows,cols);
    }

    public ArrayList<Integer> getRows() {
        return this.rows;
    }

    public ArrayList<Integer> getCols() {
        return this.cols;
    }

    public void setBoth(int row, int col) {
        rows.add(row);
        cols.add(col);
    }

    public void addAll(int[] rows, int[] cols) {
        if (rows.length == cols.length) {
            for (int i = 0; i < rows.length; i++) {
                this.rows.add(rows[i]);
                this.cols.add(cols[i]);
            }
        } else {
            //To be implemented.Throw exception.
        }

    }

    private int index(int row,int col){
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i) == row && cols.get(i) == col) {
                return i;
            }
        }
        return -1;

    }

    public boolean contain(int row, int col) {
        if(index(row,col) != -1){
            return true;
        }return false;
    }
    public void remove(int row,int col){
        if(contain(row,col)){
            int index = index(row,col);
            rows.remove(index);
            cols.remove(index);

        }
    }

    public int[] get(int index){
        if(index > rows.size()){
            int[] result = {rows.get(index),cols.get(index)};
            return result;
        }return null;
    }


    public void clear(){
        this.rows.clear();
        this.cols.clear();
    }
    @Override
    public String toString(){
        return rows.toString() + cols.toString();
    }



}


