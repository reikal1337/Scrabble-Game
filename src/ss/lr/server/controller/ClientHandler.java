package ss.lr.server.controller;

import org.junit.jupiter.params.aggregator.ArgumentAccessException;
import ss.lr.exceptions.ClientUnavailableException;
import ss.lr.exceptions.ServerUnavailableException;
import ss.lr.protocols.ProtocolMessages;
import ss.lr.server.model.Server;

import java.io.*;
import java.net.Socket;

/***
 As soon as it is created it works as a controller for communication between server and client.
 @author reikal951@gmail.com.
 */

public class ClientHandler implements Runnable {
    private BufferedReader in;
    private BufferedWriter out;
    private Socket sock;

    private Server server;
    private String name;

    public ClientHandler(Socket sock, Server srv, String name) {
        try {
            in = new BufferedReader(
                    new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(
                    new OutputStreamWriter(sock.getOutputStream()));
            this.sock = sock;
            this.server = srv;
            this.name = name;

        } catch (IOException e) {
            shutdown();
        }
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void run() {
        String recMessage;
        try {
            recMessage = in.readLine();
            while (recMessage != null) {
                System.out.println("> [" + name + "] Incoming: " + recMessage);
                handleCommand(recMessage);
                recMessage = in.readLine();
            }
            shutdown();
        } catch (IOException e) {
            shutdown();
            try {
                server.doGameOver("STOP");
            } catch (ClientUnavailableException ex) {
                server.showError("Unable to establish connection with new client!");
            }
        } catch (ServerUnavailableException e) {
            server.showError("Unable to connect to server!");
        }
    }

    public synchronized void sendMessage(String message) throws ClientUnavailableException {
        if (message != null) {
            if (out != null) {
                System.out.println("< [" + getName() + "]" + " sending: " + message);
                try {
                    out.write(message);
                    out.newLine();
                    out.flush();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    throw new ClientUnavailableException("Could not write to client.");
                }

            } else {
                throw new ClientUnavailableException("Could not write to client.");
            }
        } else {
            throw new ArgumentAccessException("Message can't be null!");
        }
    }


    public String[] processInput(String input) {
        String[] res = input.split(ProtocolMessages.DELIMITER);
        res[res.length - 1] = res[res.length - 1].replaceAll(ProtocolMessages.EOT, "");
        return res;
    }

    private void handleCommand(String message) throws ServerUnavailableException {
        String[] com = processInput(message);
        try {
            switch (com[0].toUpperCase()) {
                case (ProtocolMessages.JOIN):
                    server.handelHello(com[1], this);
                    break;
                case (ProtocolMessages.MOVE):
                    server.handleMove(com, name);
                    break;
                case (ProtocolMessages.SWAP):
                    if (com.length != 1) {
                        server.handleSwap(com[1], name);
                    } else {
                        server.doSkip(name);
                    }

                    break;
                case (ProtocolMessages.QUIT):
                    server.handleQuit(name);
                    break;
                case (ProtocolMessages.READY):
                    server.handleReady(this);
                    break;
                case (ProtocolMessages.CHAT):
                    server.doChat(com[1], name);
            }
        } catch (ClientUnavailableException e) {
            server.showError("Unable to get connection to client!");

        }
    }

    public void shutdown() {
        System.out.println("> [" + name + "] Shutting down.");
        try {
            in.close();
            out.close();
            sock.close();
        } catch (IOException e) {
            server.showMessage("Unable to properly close connection!");
        }
        if (server.getRdyClients().contains(this)) {
            server.removeRdyClient(this);
        }
        server.removeClient(this);
    }
}

