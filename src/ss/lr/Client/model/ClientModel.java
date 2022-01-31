package ss.lr.Client.model;

import ss.lr.Client.view.ClientGUI;
import ss.lr.Exceptions.ProtocolException;
import ss.lr.Exceptions.ServerUnavailableException;
import ss.lr.Protocols.ClientProtocol;
import ss.lr.Protocols.ProtocolMessages;
import ss.lr.Client.controller.Client;
import ss.lr.Client.view.ClientTUI;

public class ClientModel implements ClientProtocol {
    private ClientTUI view;;
    private ClientGUI gui;
    private Client controller;
    private String name;
    private boolean playing ;

    public ClientModel(Client controller,String name,ClientGUI gui){
        this.controller = controller;
        view =new ClientTUI();
        this.name = name;
        this.gui = gui;
    }
    public void setName(String name){
        this.name = name;
    }



    @Override
    public void handleHello() throws ServerUnavailableException, ProtocolException {
        try {
            controller.sendMessage(ProtocolMessages.JOIN+ProtocolMessages.DELIMITER+name+ProtocolMessages.EOT);
        } catch (ServerUnavailableException e) {
            throw new ServerUnavailableException("Could not write from server.");
        }
        try {
            String orgMsg = controller.readLineFromServer();
            //view.showMessage("Wtf? " + orgMsg);
            String[] ans = orgMsg.split(ProtocolMessages.DELIMITER);
            ans[ans.length-1] = ans[ans.length-1].replaceAll(ProtocolMessages.EOT,"");
            if(!ans[0].equals(ProtocolMessages.WELCOME)){
                throw new ProtocolException("Incorrect handshake!!");
            }else{
                if(ans[1].equals(name)){
                    gui.showMessage("Connection established!");
                }
            }
        } catch (ServerUnavailableException e) {
            throw new ServerUnavailableException("Could not write from server.");
        }
    }
    public String[] proccesInput(String input){
        String[] res = input.split(ProtocolMessages.DELIMITER);
        res[res.length-1] = res[res.length-1].replaceAll(ProtocolMessages.EOT,"");
        return res;
    }

    //Check for turn;
    @Override
    public void doMove(String[] move) throws ServerUnavailableException {//m row col letters direction
        try {
            controller.sendMessage(ProtocolMessages.MOVE+ProtocolMessages.DELIMITER+move[0]+ProtocolMessages.DELIMITER+
                    move[1]+ProtocolMessages.DELIMITER+move[2]+ProtocolMessages.DELIMITER+move[3]+
                    ProtocolMessages.EOT);

            //Should move where it listens. handleResponse(proccesInput(readLineFromServer()));
        } catch (ServerUnavailableException e) {
            throw new ServerUnavailableException("Could not read from server.");
        }

    }

    @Override
    public void doSwap(String[] letters) throws ServerUnavailableException {
        try {
            controller.sendMessage(ProtocolMessages.SWAP+ProtocolMessages.DELIMITER+letters[1]+
                    ProtocolMessages.EOT);

            //Should move where it listens. handleResponse(proccesInput(readLineFromServer()));
        } catch (ServerUnavailableException e) {
            throw new ServerUnavailableException("Could not read from server.");
        }

    }
    @Override
    public void doSwap() throws ServerUnavailableException {
        try {
            controller.sendMessage(ProtocolMessages.SWAP+ProtocolMessages.EOT);

            //Should move where it listens. handleResponse(proccesInput(readLineFromServer()));
        } catch (ServerUnavailableException e) {
            throw new ServerUnavailableException("Could not read from server.");
        }

    }

    @Override
    public void handleQuit() throws ServerUnavailableException {
        controller.sendMessage(ProtocolMessages.QUIT+ProtocolMessages.EOT);
        controller.closeConnection();
        controller.start();

    }

    @Override
    public void doReady() throws ServerUnavailableException {
        try {
            //System.out.println("Works!!");
            controller.sendMessage(ProtocolMessages.READY+ProtocolMessages.EOT);

            //Read somewere else handleResponse(proccesInput(readLineFromServer()));
        } catch (ServerUnavailableException e) {
            throw new ServerUnavailableException("Could not read from server.");
        }


    }

    public void doChat(String message) throws ServerUnavailableException {
        controller.sendMessage(ProtocolMessages.CHAT+ProtocolMessages.DELIMITER+message+ProtocolMessages.EOT);
    }


    @Override
    public void doExit() throws ServerUnavailableException {
        controller.sendMessage(ProtocolMessages.QUIT+ProtocolMessages.EOT);
        controller. closeConnection();
        view.showMessage("Closing the program!");
        System.exit(0);

    };

    @Override
    public String handleError(String error) throws ServerUnavailableException {
        switch (error) {
            case "0":
                return "Error: Not your turn!";
            case "1":
                return "Error:  invalid move";
            case "2":
                return "Error: invalid swap";
            case "3":
                return "Error: bag empty";
            case "4":
                return "Error: server unavailable";
            case "5":
                return "Error: unknown command";
            case "6":
                return "Error: flag not supported";
            case "7":
                return "Error: invalid word";
        }
        return null;
    }

    @Override
    public String[] handleGameStart(String[] names) {
        playing = true;
        return names;
    }

    @Override
    public String handleTiles(String tiles) {
        String res = "";
        String[] newTiles = tiles.split("");
        for(String tile : newTiles){
            res = res+ " " + tile;
        }
        return res;
    }

    @Override
    public void handleCurrent(String name) throws ServerUnavailableException {

    }

    @Override
    public String[] handleUpdate(String[] update) {//2,3 names,4,5 scores;
        //view.showMessage("Size: " + update.length);
//        view.printBoard(update[1]);
//        view.showMessage(update[2] + " score: " + update[4] +
//                "\n" + update[3] + " score: " + update[5]);


        return update;

    }


    //GAMEOVER;<endType>;<names>;<points>!
    //Endtypes:
    //WINNER, a player wins
    //DRAW, tie for first place
    //STOP, a player quits or anything else

    @Override
    public String[] handleGameOver(String[] over) {//Not sure if should ask for next game;
        String result = "";
        int firstScore = Integer.parseInt(over[4]);
        int secondScore = Integer.parseInt(over[5]);
        if(over[1].equals("WINNER")){
            if(firstScore>secondScore){
                result ="Game end! Winner is: " + over[2];
            }else{
                result = "Game end! Winner is: " + over[3];
            }
        }else if(over[1].equals("DRAW")){
            result = "Game end! Draw!";
        }else if(over[1].equals("DRAW")){
            result = "Game end! Player left!";
        }
        String[] fullResult = {result,String.valueOf(firstScore),String.valueOf(secondScore)};
        return fullResult;
    }



}
