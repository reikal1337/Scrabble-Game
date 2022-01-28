package ss.lr.Protocols;

import ss.lr.Exceptions.ProtocolException;
import ss.lr.Exceptions.ServerUnavailableException;


public interface ClientProtocol {

    /**
     * Returns a String to be sent as a response to a Client JOIN request,
     * including the name of the hotel: WELCOME; <clientName>;<names>;<flags>*!
     *
     * @return String to be sent to client as a handshake response.
     */
   // public String getHello();
    public void handleHello() throws ServerUnavailableException, ProtocolException;

    //handels MOVE;<position>;<letters>;<direction>!
    public void doMove(String[] move) throws ServerUnavailableException;

    //Handels SWAP; <letters>!
    public void doSwap(String[] letters) throws ServerUnavailableException;
    public void doSwap() throws ServerUnavailableException;


    //Handles QUIT!
    public void handleQuit() throws ServerUnavailableException;

    //Handles READY!
    //(optional#1, no lobby) READY;<Pnumber>!
    //(optional#2, lobby)
    public void doReady() throws ServerUnavailableException;

    public void doExit() throws ServerUnavailableException;

    //ERROR; <errorType>!
    public void handleError(String error)  throws ServerUnavailableException;

    //GAMESTART; <names>*!
    public void handleGameStart(String[] names);

    //TILES;<letters>!
    public void handleTiles(String tiles);

    //CURRENT; <currentName>!
    public void handleCurrent(String name) throws ServerUnavailableException;

    //UPDATE;<boardrows>;<names>;<points>!
    public void handleUpdate(String[] update);

    //GAMEOVER;<endType>;<names>;<points>!
    public void handleGameOver(String[] over);

    void setName(String name);

    public void handleResponse(String[] proccesInput)throws ServerUnavailableException;
}
