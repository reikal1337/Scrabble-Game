package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Locale;

public class ServerTUI implements ServerView{


    /** The PrintWriter to write messages to */
    private PrintWriter console;
    private BufferedReader consoleReader;

    /**
     * Constructs a new HotelServerTUI. Initializes the console.
     */
    public ServerTUI() {
        console = new PrintWriter(System.out, true);
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void showMessage(String message) {
        if(!message.equals(null)) {
            console.println(message);
        }
    }

    @Override
    public String getString(String question) {
        String result = "";
        if(!question.equals(null)) {
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
    public boolean stringIsInt(String word) {
        return word.matches("-?\\d+") ? true : false;
    }

    public boolean stringIsLetters(String word) {
        return word.matches("[a-zA-Z]+") ? true : false;
    }

}
