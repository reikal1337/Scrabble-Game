package ss.lr.protocols;

import ss.lr.exceptions.ServerUnavailableException;
import ss.lr.server.controller.ClientHandler;

public interface ServerProtocol {

    /**
     * Returns a String to be sent as a response to a Client JOIN request,
     * including the name of the hotel: WELCOME; <clientName>;<names>;<flags>*!
     *
     * @return String to be sent to client as a handshake response.
     */

    void handelHello(String name, ClientHandler client) throws ServerUnavailableException;

    //handels MOVE;<position>;<letters>;<direction>!
    public void handleMove(String[] commands,String name) throws ServerUnavailableException;

    //Handels SWAP; <letters>!

    void handleSwap(String word, String name) throws ServerUnavailableException;

    //Handles QUIT!
    public void handleQuit(String name) throws ServerUnavailableException;

    //Handles READY!
    //(optional#1, no lobby) READY;<Pnumber>!
    //(optional#2, lobby)
    public void handleReady(ClientHandler client) throws ServerUnavailableException;

    //ERROR; <errorType>!
    public void doError(String error,String name) throws ServerUnavailableException;

    //GAMESTART; <names>*!
    public void doGameStart() throws ServerUnavailableException;

    //TILES;<letters>!
    public void doTiles(String name) throws ServerUnavailableException;

    //CURRENT; <currentName>!
    public void doCurrent() throws ServerUnavailableException;

    //UPDATE;<boardrows>;<names>;<points>!
    public void doUpdate(String name) throws ServerUnavailableException;

    //GAMEOVER;<endType>;<names>;<points>!
    public void doGameOver(String endType) throws ServerUnavailableException;

    void afterMove(String usedLetters, String name) throws ServerUnavailableException;
}
