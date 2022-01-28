package ss.lr.Client.model;

import ss.lr.Exceptions.ProtocolException;
import ss.lr.Exceptions.ServerUnavailableException;
import ss.lr.Protocols.ClientProtocol;
import ss.lr.Protocols.ProtocolMessages;
import ss.lr.Client.controller.Client;
import ss.lr.Client.view.ClientTUI;

public class ClientModel implements ClientProtocol {
    private ClientTUI view;;
    private Client controller;
    private String name;
    private boolean playing ;

    public ClientModel(Client controller,String name){
        this.controller = controller;
        view =new ClientTUI();
        this.name = name;
    }
    public void setName(String name){
        this.name = name;
    }

    public void handleResponse(String[] message) throws ServerUnavailableException {
        if(!message.equals(null)){
            switch (message[0]){
                case ProtocolMessages.ERROR:
                    handleError(message[1]);
                    break;
                case ProtocolMessages.GAMESTART:
                   // view.showMessage("Start!?");
                    handleGameStart(message);
                    break;
                case ProtocolMessages.TILES:
                    handleTiles(message[1]);
                    break;
                case ProtocolMessages.CURRENT:
                    handleCurrent(message[1]);
                    break;
                case ProtocolMessages.UPDATE:
                    handleUpdate(message);
                    break;
                case ProtocolMessages.GAMEOVER:
                    handleGameOver(message);

            }

        }

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
                    view.showMessage("Connection established!");
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
            controller.sendMessage(ProtocolMessages.MOVE+ProtocolMessages.DELIMITER+move[1]+ProtocolMessages.DELIMITER+
                    move[2]+ProtocolMessages.DELIMITER+move[3]+ProtocolMessages.DELIMITER+move[4]+
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


    @Override
    public void doExit() throws ServerUnavailableException {
        controller.sendMessage(ProtocolMessages.QUIT+ProtocolMessages.EOT);
        controller. closeConnection();
        view.showMessage("Closing the program!");
        System.exit(0);

    };

    @Override
    public void handleError(String error) throws ServerUnavailableException {
        switch (error) {
            case "0":
                view.showMessage("Error: Not your turn!");
                controller.working();
                break;
            case "1":
                view.showMessage("Error:  invalid move");
                controller. working();
                break;
            case "2":
                view.showMessage("Error: invalid swap");
                controller. working();
                break;
            case "3":
                view.showMessage("Error: bag empty");
                controller.working();
                break;
            case "4":
                view.showMessage("Error: server unavailable");
                controller.working();
                break;
            case "5":
                view.showMessage("Error: unknown command");
                controller.working();
                break;
            case "6":
                view.showMessage("Error: flag not supported");
                controller.working();
                break;
            case "7":
                view.showMessage("Error: invalid word (Your turn gets skipped)");
                controller.working();
                break;
        }
    }

    @Override
    public void handleGameStart(String[] names) {
        playing = true;
        view.showMessage("Game has started with players: " + names[1]+" and "+ names[2]);
    }

    @Override
    public void handleTiles(String tiles) {
        controller.tiles.clear();
        String[] newTiles = tiles.split("");
        for(String tile : newTiles){
            controller.tiles.add(tile);
        }
        view.showMessage("Your new tiles:\n" + controller.tiles.toString());
    }

    @Override
    public void handleCurrent(String name) throws ServerUnavailableException {
        if(name.equals(this.name)){
            view.showMessage("It's your turn");
        }else {
            view.showMessage("It's other players turn!");
        }
    }

    @Override
    public void handleUpdate(String[] update) {//2,3 names,4,5 scores;
        //view.showMessage("Size: " + update.length);
        view.printBoard(update[1]);
        view.showMessage(update[2] + " score: " + update[4] +
                "\n" + update[3] + " score: " + update[5]);

    }


    //GAMEOVER;<endType>;<names>;<points>!
    //Endtypes:
    //WINNER, a player wins
    //DRAW, tie for first place
    //STOP, a player quits or anything else

    @Override
    public void handleGameOver(String[] over) {//Not sure if should ask for next game;
        int firstScore = Integer.parseInt(over[4]);
        int secondScore = Integer.parseInt(over[5]);
        if(over[1].equals("WINNER")){
            if(firstScore>secondScore){
                view.showMessage("Game end! Winner is: " + over[2]);
            }else{
                view.showMessage("Game end! Winner is: " + over[3]);
            }
        }else if(over[1].equals("DRAW")){
            view.showMessage("Game end! Draw!");
        }else if(over[1].equals("DRAW")){
            view.showMessage("Game end! Player left!");
        }

    }



}
