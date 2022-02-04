package ss.lr.server.view;

import ss.lr.server.model.Board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/***
 Simple servers TUI.
 @author Lukas Reika s2596237.
 */

public class ServerTUI implements ServerView {

    private static final int DIM = 15;
    private static final String RED_BOLD = "\033[1;31m";
    private static final String RESET = "\033[0m";
    private final PrintWriter console;
    private final BufferedReader consoleReader;

    public ServerTUI() {
        console = new PrintWriter(System.out, true);
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            console.println(message);
        }
    }

    public void showError(String message) {
        if (message != null) {
            console.println(RED_BOLD + message + RESET);
        }
    }

    @Override
    public String getString(String question) {
        String result = "";
        if (!question.equals(null)) {
            console.println(question);
            try {
                result = consoleReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public int getInt(String question) {
        int res = 0;
        boolean wrong = true;
        while (wrong) {
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
        while (wrong) {
            String ans = getString(question);
            if (ans.equalsIgnoreCase("yes")) {
                wrong = false;
                return true;
            } else if (ans.equalsIgnoreCase("no")) {
                wrong = false;
                return false;
            }
        }
        return false;
    }

    @Override
    public String getBoard(Board board) {
        String printBoard = "";
        for (int i = 0; i < DIM; i++) {
            String row = "";
            for (int j = 0; j < DIM; j++) {
                row = row + board.getField(i, j);
                if (j < DIM - 1) {
                    row = row + ",";
                }
            }
            printBoard = printBoard + row;
            if (i < DIM - 1) {
                printBoard = printBoard + ",";
            }
        }
        return printBoard;
    }

    public boolean stringIsInt(String word) {
        return word.matches("-?\\d+");
    }

    public boolean stringIsLetters(String word) {
        return word.matches("[a-zA-Z]+");
    }

}
