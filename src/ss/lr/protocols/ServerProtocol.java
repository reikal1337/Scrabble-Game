package ss.lr.protocols;

import ss.lr.exceptions.ClientUnavailableException;
import ss.lr.server.controller.ClientHandler;

public interface ServerProtocol {

    /***
     Server protocols interface.
     @author reikal951@gmail.com.
     */

    void handelHello(String name, ClientHandler client) throws ClientUnavailableException;

    void handleMove(String[] commands, String name) throws ClientUnavailableException;

    void handleSwap(String word, String name) throws ClientUnavailableException;

    void handleQuit(String name) throws ClientUnavailableException;

    void handleReady(ClientHandler client) throws ClientUnavailableException;

    void doError(String error, String name) throws ClientUnavailableException;

    void doGameStart() throws ClientUnavailableException;

    void doTiles(String name) throws ClientUnavailableException;

    void doCurrent() throws ClientUnavailableException;

    void doUpdate(String name) throws ClientUnavailableException;

    void doGameOver(String endType) throws ClientUnavailableException;

    void afterMove(String usedLetters, String name) throws ClientUnavailableException;
}
