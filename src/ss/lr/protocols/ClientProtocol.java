package ss.lr.protocols;

import ss.lr.exceptions.ProtocolException;
import ss.lr.exceptions.ServerUnavailableException;


public interface ClientProtocol {

    /**
     * Returns a String to be sent as a response to a Client JOIN request,
     * including the name of the hotel: WELCOME; <clientName>;<names>;<flags>*!
     *
     * @return String to be sent to client as a handshake response.
     */
   // public String getHello();
    public String handleHello() throws ServerUnavailableException, ProtocolException;

    //handels MOVE;<position>;<letters>;<direction>!
    public void doMove(String[] move) throws ServerUnavailableException;

    //Handels SWAP; <letters>!
    public void doSwap(String letters) throws ServerUnavailableException;
    public void doSwap() throws ServerUnavailableException;


    //Handles QUIT!
    public void handleQuit() throws ServerUnavailableException;

    //Handles READY!
    //(optional#1, no lobby) READY;<Pnumber>!
    //(optional#2, lobby)
    public void doReady() throws ServerUnavailableException;

    public void doExit() throws ServerUnavailableException;

    void doDissconect() throws ServerUnavailableException;

    //ERROR; <errorType>!
    public String handleError(String error);

    //GAMESTART; <names>*!
    public String[] handleGameStart(String[] names);

    //TILES;<letters>!
    public String handleTiles(String tiles);

    //CURRENT; <currentName>!
    public void handleCurrent(String name) throws ServerUnavailableException;


    //GAMEOVER;<endType>;<names>;<points>!
    public String[] handleGameOver(String[] over);

    void setName(String name);

    void doChat(String message) throws ServerUnavailableException;

    //public void handleResponse(String[] proccesInput)throws ServerUnavailableException;
}
