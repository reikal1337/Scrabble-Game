package Client;

import Exceptions.ExitProgram;
import Exceptions.ProtocolException;
import Exceptions.ServerUnavailableException;
import Game.Tile;
import Protocol.ClientProtocol;
import Protocol.ProtocolMessages;
import Server.ClientHandler;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

public class Client implements ClientProtocol,Runnable {

    private Socket serverSock;
    private BufferedReader in;
    private BufferedWriter out;
    private ClientTUI view;
    private String name;
    private boolean playing ;
    private boolean connected;
    ArrayList<String> tiles;


    public Client() {
        view = new ClientTUI();
        tiles = new ArrayList<String>();
    }


    //add skip with empty SWAP.
    public void start() throws ServerUnavailableException {
        try {
            createConnection();
            handleHello();
        } catch (ExitProgram e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (ServerUnavailableException e) {
            e.printStackTrace();
        }
        working();

    }
    private void handleResponse(String[] message) throws ServerUnavailableException {
        if(!message.equals(null)){
            switch (message[0]){
                case ProtocolMessages.ERROR:
                    handleError(message[1]);
                    break;
                case ProtocolMessages.GAMESTART:
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


    //Should also liste to server!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//private boolean working = true;
    //add skip and exit
    public synchronized void working() throws ServerUnavailableException {
        view.printHelpMenu();
        while(serverSock != null){
            String ans = view.getString("Please input command: ");
            String[] command = ans.split(" ");
            String strCom = "";
            if(command.length != 0) {
                switch (command[0].toLowerCase()) {
                    case "m":
                        if (command.length == 5) {
                            try {
                                doMove(command);
                            } catch (ServerUnavailableException e) {
                                throw new ServerUnavailableException("Could not read from server.");
                            }
                        }
                        break;
                    case "s":
                        doSwap(command);
                        break;
                    case "q":
                        doExit();
                        break;
                    case "r":
                        doReady();
                        break;
                    case "h":
                        view.printHelpMenu();
                        break;
                    default:
                        view.showMessage("Command: '" + command[0] + "' is not correct!");
                }
            }else{
                view.showMessage("Command can't be empty!");
            }
        }

    }

    public void createConnection() throws ExitProgram {
        clearConnection();
        while(serverSock == null){
            InetAddress addr = view.getIp();
            int port = view.getInt("Please input port: ");
            try {
                name = view.getString("Please input your name: ");
                view.showMessage("Connecting to " +addr+ ":" +port+ ".....");
                serverSock = new Socket(addr,port);
                in = new BufferedReader(new InputStreamReader(serverSock.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(serverSock.getOutputStream()));
                Thread th = new Thread();
                connected = true;

                th.run();
            } catch (IOException e) {
                view.showMessage("ERROR: Couldn't connect to: " + addr + ":" + port);
                if (!view.getBoolean("Do you want to try again? y/n")) {
                    throw new ExitProgram("Server is being closed!");
                }
                serverSock = null;
            }
        }
        //view.showMessage("Connection lost!");
    }

    public void clearConnection() {
        serverSock = null;
        in = null;
        out = null;
        connected = false;
    }

    /**
     * Sends a message to the connected server, followed by a new line. The stream
     * is then flushed.
     *
     * @param message the message to write to the OutputStream.
     * @throws ServerUnavailableException if IO errors occur.
     */
    public synchronized void sendMessage(String message) throws ServerUnavailableException {
        if(out != null){
            try {
                out.write(message);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new ServerUnavailableException("Could not write to server.");
            }
        }else{
            throw new ServerUnavailableException("Could not write to server.");
        }
    }

    /**
     * Reads and returns one line from the server.
     *
     * @return the line sent by the server.
     * @throws ServerUnavailableException if IO errors occur.
     */
    public String readLineFromServer() throws ServerUnavailableException {
        if (in != null) {
            try {
                String ans = in.readLine();
                if(ans.equals(null)){
                    throw new ServerUnavailableException("Could not read from server.");
                }
                return ans;
            } catch (IOException e) {
                throw new ServerUnavailableException("Could not read from server.");
            }

        }else{
            throw new ServerUnavailableException("Could not read from server.");
        }
    }

    /**
     * Reads and returns multiple lines from the server until the end of the text is
     * indicated using a line containing ProtocolMessages.EOT.
     *
     * @return the concatenated lines sent by the server.
     * @throws ServerUnavailableException if IO errors occur.
     */
    public String readMultipleLinesFromServer() throws ServerUnavailableException {
        if (in != null) {
            try {
                // Read and return answer from Server
                StringBuilder sb = new StringBuilder();
                for (String line = in.readLine(); line != null
                        && !line.equals(ProtocolMessages.EOT); line = in.readLine()) {
                    sb.append(line + System.lineSeparator());
                }
                return sb.toString();
            } catch (IOException e) {
                throw new ServerUnavailableException("Could not read from server.");
            }
        } else {
            throw new ServerUnavailableException("Could not read from server.");
        }
    }

    /**
     * Closes the connection by closing the In- and OutputStreams, as well as the
     * serverSocket.
     */
    public void closeConnection() {
        view.showMessage("Closing the connection with the server...");
        try {
            in.close();
            out.close();
            serverSock.close();
            connected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void handleHello() throws ServerUnavailableException, ProtocolException {
        try {
            sendMessage(ProtocolMessages.JOIN+ProtocolMessages.DELIMITER+name+ProtocolMessages.EOT);
        } catch (ServerUnavailableException e) {
            throw new ServerUnavailableException("Could not write from server.");
        }
        try {
            String orgMsg = readLineFromServer();
            view.showMessage("Wtf? " + orgMsg);
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
            sendMessage(ProtocolMessages.MOVE+ProtocolMessages.DELIMITER+move[1]+ProtocolMessages.DELIMITER+
                    move[2]+ProtocolMessages.DELIMITER+move[3]+ProtocolMessages.DELIMITER+move[4]+
                    ProtocolMessages.EOT);

           //Should move where it listens. handleResponse(proccesInput(readLineFromServer()));
        } catch (ServerUnavailableException e) {
            throw new ServerUnavailableException("Could not read from server.");
        }

    }

    @Override
    public void doSwap(String[] letters) {

    }

    @Override
    public void handleQuit() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.QUIT+ProtocolMessages.EOT);
        closeConnection();
        start();

    }

    @Override
    public void doReady() throws ServerUnavailableException {
        try {
            sendMessage(ProtocolMessages.READY+ProtocolMessages.EOT);

           //Read somewere else handleResponse(proccesInput(readLineFromServer()));
        } catch (ServerUnavailableException e) {
            throw new ServerUnavailableException("Could not read from server.");
        }


    }


    @Override
    public void doExit() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.QUIT+ProtocolMessages.EOT);
        closeConnection();
        view.showMessage("Closing the program!");
        System.exit(0);

    };

    @Override
    public void handleError(String error) throws ServerUnavailableException {
        switch (error) {
            case "0":
                view.showMessage("Error: Not your turn!");
                working();
                break;
            case "1":
                view.showMessage("Error:  invalid move");
                working();
                break;
            case "2":
                view.showMessage("Error: invalid swap");
                working();
                break;
            case "3":
                view.showMessage("Error: bag empty");
                working();
                break;
            case "4":
                view.showMessage("Error: server unavailable");
                working();
                break;
            case "5":
                view.showMessage("Error: unknown command");
                working();
                break;
            case "6":
                view.showMessage("Error: flag not supported");
                working();
                break;
            case "7":
                view.showMessage("Error: invalid word (Your turn gets skipped)");
                working();
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
        this.tiles.clear();
        String[] newTiles = tiles.split("");
        for(String tile : newTiles){
            this.tiles.add(tile);
        }
        view.showMessage("Your new tiles:\n" + this.tiles.toString());
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
    public static void main(String[] args) throws ServerUnavailableException {
        (new Client()).start();
    }

    @Override
    public void run() {
        while(connected){
            try {
                System.out.println( "In run too early");
                handleResponse(proccesInput(readLineFromServer()));
            } catch (ServerUnavailableException e) {

            }
        }

    }
}
