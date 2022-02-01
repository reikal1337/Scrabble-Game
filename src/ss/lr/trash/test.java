package ss.lr.trash;

public class test {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {

        int nr = 0;
        for(int i=0;i<10;i++){
            nr = i;
            if(i==5){
                break;
            }
        }
        System.out.println(nr);



    }

}
