package ss.lr.protocols;

import ss.lr.exceptions.ProtocolException;
import ss.lr.exceptions.ServerUnavailableException;


public interface ClientProtocol {

    /***
     Clients protocols interface.
     @author reikal951@gmail.com.
     */
    String handleHello() throws ServerUnavailableException, ProtocolException;

    void doMove(String[] move) throws ServerUnavailableException;

    void doSwap(String letters) throws ServerUnavailableException;

    void doSwap() throws ServerUnavailableException;

    void handleQuit() throws ServerUnavailableException;

    void doReady() throws ServerUnavailableException;

    void doExit() throws ServerUnavailableException;

    void doDissconect() throws ServerUnavailableException;

    String handleError(String error);

    String[] handleGameStart(String[] names);

    String handleTiles(String tiles);

    void handleCurrent(String name) throws ServerUnavailableException;


    String[] handleGameOver(String[] over);

    void setName(String name);

    void doChat(String message) throws ServerUnavailableException;

}
