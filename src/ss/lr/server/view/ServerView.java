package ss.lr.server.view;

import ss.lr.server.model.Board;

/***
 Servers view interface.
 @author reikal951@gmail.com.
 */

public interface ServerView {

    /**
     * Writes the given message to standard output.
     *
     * @param message the message to write to the standard output.
     */
    void showMessage(String message);

    void showError(String message);

    /**
     * Prints the question and asks the user to input a String.
     *
     * @param question The question to show to the user
     * @return The user input as a String
     */
    String getString(String question);

    /**
     * Prints the question and asks the user to input an Integer.
     *
     * @param question The question to show to the user
     * @return The written Integer.
     */
    int getInt(String question);

    /**
     * Prints the question and asks the user for a yes/no answer.
     *
     * @param question The question to show to the user
     * @return The user input as boolean.
     */
    boolean getBoolean(String question);

    //get bag representation of board
    String getBoard(Board board);

}
