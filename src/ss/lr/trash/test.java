package ss.lr.trash;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class test {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList<>();
        list.add("0");
        System.out.println(list.contains("0"));


        int nr = 0;
        for(int i=0;i<10;i++){
            nr = i;
            if(i==5){
                break;
            }
        }
        //System.out.println(nr);



    }

}
