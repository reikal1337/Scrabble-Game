package ss.lr.client.model;

import ss.lr.client.controller.Client;
import ss.lr.client.controller.ModuleControllerIMessage;
import ss.lr.exceptions.ProtocolException;
import ss.lr.exceptions.ServerUnavailableException;
import ss.lr.protocols.ClientProtocol;
import ss.lr.protocols.ProtocolMessages;


/***
 This is where input is checked,messages to server are constructed,main logic of client.
 It is not much as everything is done on server.
 @author Lukas Reika s2596237.
 */


public class ClientModel implements ClientProtocol {
    private final Client controller;
    private String name;

    public ClientModel(Client controller, String name) {
        this.controller = controller;
        setName(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String handleHello() throws ServerUnavailableException, ProtocolException {

        controller.sendMessage(ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + name + ProtocolMessages.EOT);
        String orgMsg = controller.readLineFromServer();
        //view.showMessage("Wtf? " + orgMsg);
        String[] ans = orgMsg.split(ProtocolMessages.DELIMITER);
        ans[ans.length - 1] = ans[ans.length - 1].replaceAll(ProtocolMessages.EOT, "");
        if (!ans[0].equals(ProtocolMessages.WELCOME)) {
            if (!ans[0].equals(ProtocolMessages.ERROR)) {
                throw new ProtocolException("Incorrect handshake!!");
            } else {
                controller.handleGUIModel(ModuleControllerIMessage.ERROR + ModuleControllerIMessage.BREAK
                        + handleError(ans[1]));
            }
        } else {
            if (ans[1].equals(name)) {
                return "Connection established!";
            }
        }
        return null;
    }


    @Override
    public void doMove(String[] move) throws ServerUnavailableException {//m row col letters direction

        controller.sendMessage(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + move[0] + ProtocolMessages.DELIMITER +
                move[1] + ProtocolMessages.DELIMITER + move[2] + ProtocolMessages.DELIMITER + move[3] +
                ProtocolMessages.EOT);


    }

    @Override
    public void doSwap(String word) throws ServerUnavailableException {
        controller.sendMessage(ProtocolMessages.SWAP + ProtocolMessages.DELIMITER + word +
                ProtocolMessages.EOT);

    }

    @Override
    public void doSwap() throws ServerUnavailableException {
        controller.sendMessage(ProtocolMessages.SWAP + ProtocolMessages.EOT);
    }

    @Override
    public void handleQuit() throws ServerUnavailableException {
        controller.sendMessage(ProtocolMessages.QUIT + ProtocolMessages.EOT);
        controller.closeConnection();
        //controller.start();

    }

    @Override
    public void doReady() throws ServerUnavailableException {
        controller.sendMessage(ProtocolMessages.READY + ProtocolMessages.EOT);
    }

    @Override
    public void doExit() {
        System.exit(0);
    }

    public void doChat(String message) throws ServerUnavailableException {
        controller.sendMessage(ProtocolMessages.CHAT + ProtocolMessages.DELIMITER + message + ProtocolMessages.EOT);
    }


    @Override
    public void doDissconect() throws ServerUnavailableException {
        controller.sendMessage(ProtocolMessages.QUIT + ProtocolMessages.EOT);
        controller.closeConnection();

    }

    @Override
    public String handleError(String error) {
        switch (error) {
            case "0":
                return "Error: invalid name!";
            case "1":
                return "Error: invalid move!";
            case "2":
                return "Error: invalid swap!";
            case "3":
                return "Error: bag empty!";
            case "4":
                return "Error: server unavailable!";
            case "5":
                return "Error: unknown command!";
            case "6":
                return "Error: flag not supported!";
            case "7":
                return "Error: invalid word!";
            case "8":
                return "Error: not your turn!";
        }
        return null;
    }

    @Override
    public String[] handleGameStart(String[] names) {
        return names;
    }

    @Override
    public String handleTiles(String tiles) {
        String res = "";
        String[] newTiles = tiles.split("");
        for (String tile : newTiles) {
            res = res + " " + tile;
        }
        return res;
    }

    @Override
    public void handleCurrent(String name) {
    }

    @Override
    public String[] handleGameOver(String[] over) {
        String result = "";
        int firstScore = Integer.parseInt(over[4]);
        int secondScore = Integer.parseInt(over[5]);
        if (over[1].equals("WINNER")) {
            if (firstScore > secondScore) {
                result = "Game end! Winner is: " + over[2];
            } else {
                result = "Game end! Winner is: " + over[3];
            }
        } else if (over[1].equals("DRAW")) {
            result = "Game end! Draw!";
        } else if (over[1].equals("STOP")) {
            result = "Game end! Player left!";
        }
        String[] fullResult = {result, String.valueOf(firstScore), String.valueOf(secondScore)};
        return fullResult;
    }
}
