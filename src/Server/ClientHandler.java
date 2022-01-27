package Server;

import Exceptions.ServerUnavailableException;
import Protocol.ProtocolMessages;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    protected BufferedReader in;
    protected BufferedWriter out;
    private Socket sock;

    /** The connected HotelServer */
    protected Server server;

    /** Name of this ClientHandler */
    protected String name;


    /**
     * Constructs a new HotelClientHandler. Opens the In- and OutputStreams.
     *
     * @param sock The client socket
     * @param srv  The connected server
     * @param name The name of this ClientHandler
     */
    public ClientHandler(Socket sock,Server srv, String name) {
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
            e.printStackTrace();
        }
    }


    public String getName(){
        return this.name;
    }


    @Override
    public void run() {
        String recMessage;
        try {
            recMessage = in.readLine();
            while(!recMessage.equals(null)){
                System.out.println("> [" + name + "] Incoming: " + recMessage);
                handleCommand(recMessage);
                recMessage = in.readLine();
            }
            shutdown();
        } catch (IOException e) {
            shutdown();
        } catch (ServerUnavailableException e) {
            e.printStackTrace();
        }
    }
    public synchronized void sendMessage(String message) throws ServerUnavailableException {
        if(out != null){
            System.out.println("< ["+getName()+"]"+ " sending: " + message );
            try {
                out.write(message);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new ServerUnavailableException("Could not write to client.");
            }

        }else{
            throw new ServerUnavailableException("Could not write to client.");
        }
    }


    public String[] proccesInput(String input){
        String[] res = input.split(ProtocolMessages.DELIMITER);
        res[res.length-1] = res[res.length-1].replaceAll(ProtocolMessages.EOT,"");
        return res;
    }

    private void handleCommand(String message) throws ServerUnavailableException {
        String[] com = proccesInput(message);
        //System.out.println("Wtf handler: "+com.toString() );
        switch (com[0].toUpperCase()){
            case (ProtocolMessages.JOIN):
                this.name = com[1];
                server.handelHello(com[1]);
                break;
            case(ProtocolMessages.MOVE):
                server.handleMove(com,name);
                break;
            case(ProtocolMessages.SWAP):
                server.handleSwap(com,name);
            case(ProtocolMessages.QUIT):
                server.handleQuit(name);
            case(ProtocolMessages.READY):
                server.handleReady(this);

        }
    }

    protected void shutdown(){
        System.out.println("> [" + name + "] Shutting down.");
        try {
            in.close();
            out.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(server.getRdyClients().contains(this)){
            server.removeRdyClient(this);
        }
        server.removeClient(this);
    }
    }

