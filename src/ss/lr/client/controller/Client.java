package ss.lr.client.controller;

import ss.lr.client.model.ClientModel;
import ss.lr.client.view.ClientGUI;
import ss.lr.exceptions.ProtocolException;
import ss.lr.exceptions.ServerUnavailableException;
import ss.lr.protocols.ClientProtocol;
import ss.lr.protocols.ProtocolMessages;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/***
 Client works as I controler,it connects to the server and manages connection between model,GUI and server.
 @author Lukas Reika s2596237.
 */


public class Client {

    private final ClientProtocol model;
    private final ClientGUI gui;
    private Socket serverSock;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean connected;
    private String name;


    public Client() {
        gui = new ClientGUI("Sccrable", this);
        model = new ClientModel(this, name);
    }

    public static void main(String[] args) {
        (new Client()).start();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void execute() throws ServerUnavailableException {
        MyThreadRead read = new MyThreadRead();
        read.start();
    }

    public String[] proccesInput(String input) {
        if (input != null) {
            String[] res = input.split(ProtocolMessages.DELIMITER);
            res[res.length - 1] = res[res.length - 1].replaceAll(ProtocolMessages.EOT, "");
            return res;
        }
        return null;
    }

    public void handleGUIModel(String command) {
        String[] info = command.split(ClientControllerIMessage.BREAK);
        try {
            switch (info[0]) {
                case ClientControllerIMessage.SWAP:
                    model.doSwap(info[1]);
                    break;
                case ClientControllerIMessage.SKIP:
                    model.doSwap();
                    break;
                case ClientControllerIMessage.EXIT:
                    if (out != null) {
                        model.doDissconect();
                    }
                    model.doExit();
                    break;
                case ClientControllerIMessage.DISCONNECT:
                    model.doDissconect();
                    break;
                case ClientControllerIMessage.ERROR:
                    gui.showError(info[1]);
                    break;
                case ClientControllerIMessage.READY:
                    model.doReady();
                    break;
                case ClientControllerIMessage.MOVE:
                    String[] move = info[1].split(" ");
                    gui.showMessage("Lol");
                    model.doMove(move);
                    break;
                case ClientControllerIMessage.CHAT:
                    model.doChat(info[1]);
                    break;
            }
        } catch (ServerUnavailableException e) {
            gui.showError("Unable to reach server!");
        }
    }

    public void createConnection(String ip, String sPort, String name) {
        clearConnection();
        int port = 0;
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(ip);
            port = Integer.parseInt(sPort);
            model.setName(name);
            setName(name);
            serverSock = new Socket(addr, port);
            in = new BufferedReader(new InputStreamReader(serverSock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(serverSock.getOutputStream()));
            connected = true;
            String connection = model.handleHello();
            if (connection != null) {
                gui.showMessage(connection);
                gui.setConnectionYes();
                execute();
            }
        } catch (ServerUnavailableException | UnknownHostException e) {
            gui.showMessage("ERROR: Couldn't connect to: " + addr + ":" + port);
        } catch (ProtocolException e) {
            gui.showMessage("Unable to do handshake!");
        } catch (IOException e) {
            gui.showMessage("ERROR: Couldn't connect to: " + addr + ":" + port);
        }
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
        if (out != null) {
            try {
                out.write(message);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new ServerUnavailableException("Could not write to server.");
            }
        } else {
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
                if (ans == null) {

                }
                return ans;
            } catch (IOException e) {
                closeConnection();
                gui.setConnectionNo();
            }

        } else {
            throw new ServerUnavailableException("Could not read from server.");
        }
        return null;
    }

    /**
     * Closes the connection by closing the In- and OutputStreams, as well as the
     * serverSocket.
     */
    public void closeConnection() {
        gui.showMessage("Closed connection!");
        try {
            connected = false;
            in.close();
            out.close();
            serverSock.close();
            //read.interrupt();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
    }

    public class MyThreadRead extends Thread {
        public void run() {
            while (connected) {
                String[] message = new String[0];
                try {
                    message = proccesInput(readLineFromServer());
                } catch (ServerUnavailableException e) {
                    gui.showError("Unable to red from server");
                }
                if (message != null) {
                    switch (message[0]) {
                        case ProtocolMessages.ERROR:
                            gui.showError(model.handleError(message[1]));
                            break;
                        case ProtocolMessages.GAMESTART:
                            gui.gameStart(model.handleGameStart(message));
                            break;
                        case ProtocolMessages.TILES:
                            gui.setTiles(model.handleTiles(message[1]));
                            break;
                        case ProtocolMessages.CURRENT:
                            gui.setCurrent(message[1]);
                            if (message[1].equals(Client.this.getName())) {
                                gui.setYourTurn();
                            } else {
                                gui.setNotYourTurn();
                            }
                            break;
                        case ProtocolMessages.UPDATE:
                            gui.createBoard(message[1], message[2], message[3], message[4], message[5]);
                            break;
                        case ProtocolMessages.GAMEOVER:
                            String[] result = model.handleGameOver(message);
                            gui.showMessage(result[0]);
                            gui.setScore(result[1], result[2]);
                            gui.setEndGame();
                            break;
                        case ProtocolMessages.CHAT: {
                            gui.showChat(message[1]);
                        }
                    }
                }

            }
        }
    }
}

