package ss.lr.Client.view;

import ss.lr.Exceptions.ExitProgram;
import ss.lr.Exceptions.ServerUnavailableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class ClientTUI implements ClientView {
    private PrintWriter console;
    private BufferedReader consoleReader;

    public ClientTUI(){
        console = new PrintWriter(System.out, true);
        consoleReader = new BufferedReader(new InputStreamReader(System.in));

    }


    @Override
    public void start() throws ServerUnavailableException {

    }

    @Override
    public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {

    }

    @Override
    public void showMessage(String message) {
        if(!message.equals(null)) {
            console.println(message);
        }

    }

    @Override
    public InetAddress getIp() {
        InetAddress ip = null;
        try {
            ip = InetAddress.getByName(getString("Please input ip to connect: "));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }


    @Override
    public String getString(String question) {
        String result = "";
        if(!question.equals(null)) {
            while (result.equals("")) {
                console.println(question);
                try {
                    result = consoleReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public int getInt(String question) {
        int res = 0;
        boolean wrong = true;
        while(wrong) {
            String ans = getString(question);
            if (stringIsInt(ans)) {
                res = Integer.parseInt(ans);
                wrong = false;
            }
        }
        return res;
    }

    @Override
    public boolean getBoolean(String question) {
        boolean wrong = true;
        while(wrong) {
            String ans = getString(question);
            if (ans.toLowerCase().equals("yes")) {
                wrong = false;
                return true;
            } else if (ans.toLowerCase().equals("no")) {
                wrong = false;
                return false;
            }
        }return false;
    }

    public void printBoard(String board){
        //showMessage("Test: " + board);
        String res = board.replaceAll(",","\n");
        showMessage(res);
    };

    @Override
    public void printHelpMenu() {
        showMessage("All your commands: \n"+
                "m - to make a move in this format:m row col letters direction\n"+
                "s - to swap letters in this format:s letters\n"+
                "q - to quite game in this format:q\n"+
                "r - to ready for a game in this format:r\n" +
                "h - to print this menu in this format:h\n" +
                "e - to exit the program in this format:e\n" +
                "skip - to skip turn in this format:skip");

    }

    private boolean stringIsInt(String word) {
        return word.matches("-?\\d+") ? true : false;
    }


}
