package ss.lr.Client.controller;

import ss.lr.Client.view.ClientGUI;
import ss.lr.Exceptions.ExitProgram;
import ss.lr.Exceptions.ProtocolException;
import ss.lr.Exceptions.ServerUnavailableException;
import ss.lr.Protocols.ClientProtocol;
import ss.lr.Protocols.ProtocolMessages;
import ss.lr.Client.model.ClientModel;
import ss.lr.Client.view.ClientTUI;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private Socket serverSock;
    private BufferedReader in;
    private BufferedWriter out;
    private ClientTUI view;
    private boolean connected;
    public ArrayList<String> tiles;
    ClientProtocol model;
    MyThreadWork work;
    MyThreadRead read;
    private String name;
    String[] connectionInfo;
    boolean gotInfo = false;
    ClientGUI gui;


    public Client() {
        view = new ClientTUI();
        tiles = new ArrayList<String>();
        gui = new ClientGUI("Sccrable",this);
        model = new ClientModel(this,name,gui);
        //connectionInfo = new String[3];
    }


    //add skip with empty SWAP.
    public void start() throws ServerUnavailableException {
            //createConnection();
            //model.handleHello();

        //execute();
        //working();
    }

    public void setConnectionInfo(String[] info){
        this.connectionInfo = info;
    }
    public void setGotInfo(Boolean state){
        this.gotInfo = state;
    }

    public void execute() throws ServerUnavailableException {
        work = new MyThreadWork();
        read = new MyThreadRead();
        work.start();
        read.start();



    }

    public String[] proccesInput(String input){
        //view.showMessage("Input before: " + input);
        String[] res = input.split(ProtocolMessages.DELIMITER);
        res[res.length-1] = res[res.length-1].replaceAll(ProtocolMessages.EOT,"");
        //view.showMessage("Input after size: " + res.length +"  " + res.toString());
        return res;
    }

    //-----View To model.

    public void doReady(){
        try {
            model.doReady();
        } catch (ServerUnavailableException e) {
            e.printStackTrace();
        }
    }



    //Should also liste to server!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//private boolean working = true;
    //add skip and exit
    public void working() throws ServerUnavailableException {
        //view.printHelpMenu();
        while(serverSock != null ){
            String ans = view.getString("Please input command: ");
            String[] command = ans.split(" ");
            String strCom = "";
            if(command.length != 0) {
                switch (command[0].toLowerCase()) {
                    case "m":
                        if (command.length == 5) {
                            try {
                                model.doMove(command);
                            } catch (ServerUnavailableException e) {
                                throw new ServerUnavailableException("Could not read from server.");
                            }
                        }
                        break;
                    case "s":
                        model.doSwap(command);
                        break;
                    case "q":
                        model.doExit();
                        break;
                    case "r":
                        model.doReady();
                        break;
                    case "h":
                        view.printHelpMenu();
                        break;
                    case "e":
                        closeConnection();
                        System.exit(0);
                        break;
                    case "skip":
                        model.doSwap();
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
            int port = 0;
            InetAddress addr = null;
            try {
                addr = InetAddress.getByName(connectionInfo[0]);
                port = Integer.parseInt(connectionInfo[1]);
                name = connectionInfo[2];
                model.setName(name);
                gui.showMessage("Connecting to " +addr+ ":" +port+ ".....");
                serverSock = new Socket(addr,port);
                in = new BufferedReader(new InputStreamReader(serverSock.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(serverSock.getOutputStream()));
                connected = true;//Not needed I thin
                gui.setConnectionYes();//Not needed I think
                model.handleHello();
                execute();

            } catch (IOException e) {
                gui.showMessage("ERROR: Couldn't connect to: " + addr + ":" + port);
                serverSock = null;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (ServerUnavailableException e) {
                e.printStackTrace();
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
                //view.showMessage("Received: " + ans);
                if(ans.equals(null)){
                    throw new ServerUnavailableException("Could not read from server.");
                }
                //view.showMessage("Msg: " +ans);
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
                view.showMessage("After: " + sb.toString());
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
        view.showMessage("Closed connection!");
        try {
            in.close();
            out.close();
            serverSock.close();
            connected = false;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args) throws ServerUnavailableException {
        (new Client()).start();
    }

//    @Override
//    public void run() {
//        while(connected){
//            try {
//               // System.out.println( "In run too early");
//                model.handleResponse(proccesInput(readLineFromServer()))
//            } catch (ServerUnavailableException e) {
//
//            }
//        }
//
//    }

    public class MyThreadWork extends Thread {

        public void run(){
            try {
                working();
            } catch (ServerUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    public class MyThreadRead extends Thread {//proccesInput(readLineFromServer()
//Verify prbl evrything here
        public void run(){
            while(connected){
                String[] message = new String[0];
                try {
                    message = proccesInput(readLineFromServer());
                } catch (ServerUnavailableException e) {
                    e.printStackTrace();
                }
                if(!message.equals(null)){
                        switch (message[0]){
                            case ProtocolMessages.ERROR:
                                try {
                                    model.handleError(message[1]);
                                } catch (ServerUnavailableException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case ProtocolMessages.GAMESTART:
                                // view.showMessage("Start!?");
                                gui.setPlayerNames(model.handleGameStart(message));
                                break;
                            case ProtocolMessages.TILES:
                                 gui.setTiles(model.handleTiles(message[1]));
                                break;
                            case ProtocolMessages.CURRENT:
                                    gui.setCurrent(message[1]);
                                break;
                            case ProtocolMessages.UPDATE:
                                String[] info = model.handleUpdate(message);
                               gui.createBoard(message[1],message[2],message[3],message[4],message[5]);
                                break;
                            case ProtocolMessages.GAMEOVER:
                                model.handleGameOver(message);
                        }
                    }

                }
            }
        }
}

