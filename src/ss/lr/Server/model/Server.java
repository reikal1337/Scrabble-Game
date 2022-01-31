package ss.lr.Server.model;

import ss.lr.Exceptions.ExitProgram;
import ss.lr.Exceptions.ServerUnavailableException;
import ss.lr.Local.model.Board;
import ss.lr.Local.model.Tile;
import ss.lr.Protocols.ProtocolMessages;
import ss.lr.Protocols.ServerProtocol;
import Utils.WordChecker;
import ss.lr.Server.controller.ClientHandler;
import ss.lr.Server.view.ServerTUI;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable, ServerProtocol {
    /** The ServerSocket of this HotelServer */
    private ServerSocket serverSock;

    /** List of HotelClientHandlers, one for each connected client */
    private List<ClientHandler> clients;
    private List<ClientHandler> readyClients;

    /** Next client number, increasing for every new connection */
    private int next_client_no;
    private String ip;
    private boolean gameStarted = false;
    boolean firstMove = true;

    //Games stuff
    private Board board;
    private ClientPlayer[] players;
    private int current;

    /** The view of this HotelServer */
    private ServerTUI view;
    //Create proper game over.

    public Server(){
        clients = new ArrayList<>();
        readyClients = new ArrayList<ClientHandler>();
        view = new ServerTUI();
        next_client_no = 1;
        players = new ClientPlayer[2];
        ip = "localhost";
    }

    public List<ClientHandler> getRdyClients(){
        return this.readyClients;
    }


    @Override
    public void run() {
        boolean newSocket = true;
        while(newSocket){
            try {
                setup();
                while(true){
                    Socket sock = serverSock.accept();
                    String name = "Client "
                            + String.format("%02d", next_client_no++);
                    view.showMessage("New client [" + name + "] connected!");
                    ClientHandler handler =
                            new ClientHandler(sock, this, name);
                    new Thread(handler).start();
                    clients.add(handler);
                }
            } catch (ExitProgram e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void setup() throws ExitProgram {
//        if(next_client_no >1){
//            setupGame();
//        }
        serverSock = null;
        while(serverSock == null){
            int port = view.getInt("Please enter server port: ");

            try {
                view.showMessage("Attempting to open a socket at 127.0.0.1 "
                        + "on port " + port + "...");
                serverSock = new ServerSocket(port, 0,
                        InetAddress.getByName(ip));
                view.showMessage("Server started with port: " + port);
            } catch (IOException e) {
                view.showMessage("ERROR: could not create a socket on "
                        + "127.0.0.1" + " and port " + port + ".");
                //e.printStackTrace();

                if (!view.getBoolean("Do you want to try again? yes/no")) {
                    throw new ExitProgram("Server is being closed!");
                }
                serverSock = null;
            }
        }

    }

    public String getNames(){
        String result = "";
        for(ClientHandler name: clients){
            result = result + name.getName() + " ";
        }
        return result;
    }

    //Game-------------------



    public void setupGame(){
        WordChecker check = new WordChecker();
        board= new Board(check);
        ClientPlayer first = new ClientPlayer(readyClients.get(0).getName(),board.getRack()) ;
        ClientPlayer second = new ClientPlayer(readyClients.get(1).getName(),board.getRack());
        players[0] = first;
        players[1] = second;
        gameStarted = true;
        reset();
    }

    private boolean playAMove(String[] move) {
        if(!gameOver()) {
            //update();
            if(current==0) {
                players[current].makeMove(board,move);
                current++;
                return true;
            }else if(current==1) {
                players[current].makeMove(board,move);
                current--;
                return true;
            }
        }return false;

    }
    private boolean gameOver() {
        if(players[0].getRack().isEmpty() || players[1].getRack().isEmpty()){
            return true;
        }return false;
    }

    private void reset() {
        ArrayList<ArrayList<Tile>> racks = new ArrayList<ArrayList<Tile>>();
        current = 0;
        racks =  board.reset();
        players[0].setRack(racks.get(0));
        players[1].setRack(racks.get(1));
    }
    private void skipTurn(){
        if(current==0){
            current++;
        }else if(current==1){
            current--;
        }
    }

    private String getGameOutcome() {
        if (players[0].getScore() == players[1].getScore()) {
            return "DRAW";
        } else if (players[0].getScore() > players[1].getScore() ||
                players[0].getScore() < players[1].getScore()) {
            return "WINNER";
        }return "STOP";


    }


    //---------------




    public void removeClient(ClientHandler client) {
        this.clients.remove(client);
    }
    public void removeRdyClient(ClientHandler client) {
        this.readyClients.remove(client);
    }

    public String namesInServer(){
        String result = "";
        for(ClientHandler client : clients){
            result = result + client.getName()+" ";
        }return  result;
    }

//-------Server methods----------
    @Override
    public String getHello() {
        return "Joining test message";
    }

    @Override
    public void handelHello(String name) throws ServerUnavailableException {
        //view.showMessage("Name:"+name+" size of list" + clients.size());

        getClinetByName(name).sendMessage(ProtocolMessages.WELCOME+ProtocolMessages.DELIMITER
                +name+ProtocolMessages.DELIMITER+namesInServer()+ProtocolMessages.EOT);


    }

    private boolean validMoveInput(String[] move){
        if(view.stringIsInt(move[1]) && view.stringIsInt(move[1])){
            int row = Integer.parseInt(move[1]);
            int col = Integer.parseInt(move[2]);
            if(board.isField(row,col)) {
                if (view.stringIsLetters(move[3])) {
                    if (move[4].toLowerCase().equals("ver") || move[4].toLowerCase().equals("hor")) {
                        return true;
                    }
                }
            }
        }return false;
    }

    public ClientHandler getClinetByName(String name) {
        for(ClientHandler hand : clients){
            if (hand.getName().equals(name)){
                return hand;
            }
        }return null;
    }

    public ClientHandler getClinetByNameRdy(String name) {
        for(ClientHandler hand : readyClients){
            if (hand.getName().equals(name)){
                return hand;
            }
        }return null;
    }

    public ClientPlayer getPlayerByName(String name) {
        for(ClientPlayer player : players){
            if (player.getName().equals(name)){
                return player;
            }
        }return null;
    }


    @Override
    public void handleMove(String[] commands,String name) throws ServerUnavailableException {
        int score = 0;
        if(players[current].getName().equals(name)) {
            if(validMoveInput(commands)) {
                String[] move = {commands[1],commands[2],commands[3],commands[4]};
                int row = Integer.parseInt(commands[1]);
                int col = Integer.parseInt(commands[2]);
                Tile[] tiles = board.stringToTile(commands[3].split(""));
                String direction = commands[4];
                if (gameStarted) {
                    if(!firstMove){
                        score = board.checkIfMoveLegal(row, col, tiles, direction);
                        if(score > 1){
                            players[current].addScore(score);
                            if(!playAMove(move)){
                                doGameOver(getGameOutcome());//wtf don't remember
                            }
                            afterMove(commands[3],name);
                        }else{
                            doError("1",name);
                        }
                    }else if (firstMove) {
                        score = board.checkIfFirstMoveLegal(row,col,tiles,direction);
                        if(score > 1){
                            players[current].addScore(score);
                            playAMove(move);
                            firstMove = false;
                            afterMove(commands[3],name);
                        }else{
                            doError("1",name);
                        }

                    }
                }
            }else{
                doError("5",name);
            }
        }else{//ERROR; <errorType>!
            doError("0",name);
        }
    }

    @Override
    public void handleSwap(String word,String name) throws ServerUnavailableException {
        ClientPlayer player = getPlayerByName(name);
        String[] letters = word.split("");
        if(letters.length != 0){
            if(view.stringIsLetters(word)){
                Tile[] replacedRack = board.stringToTile(letters);
                if(inPlayersRack(replacedRack,player)) {
                    ArrayList<Tile> oldRack = player.getRack();
                    ArrayList<Tile> newRack = board.removeFromRackAndFill(replacedRack, oldRack);
                    player.setRack(newRack);
                    doTiles(name);
                }else{
                    doError("2",name);
                }
            }else{
                doError("2",name);
            }
        }else{
            skipTurn();
            doCurrent();
        }
    }
    public boolean inPlayersRack(Tile[] tiles,ClientPlayer player){
        ArrayList<Tile> oldRack = player.getRack();
        for(Tile tile: tiles){
            if(!oldRack.contains(tile)){
                return false;
            }
        }return true;
    }

    @Override
    public void handleQuit(String name) throws ServerUnavailableException {
        getClinetByNameRdy(name).shutdown();
        doGameOver("STOP");
    }

    @Override
    public void handleReady(ClientHandler client) throws ServerUnavailableException {
        if(!readyClients.contains(client)){
            readyClients.add(client);
        }
        if(readyClients.size() == 2){//need to change ==2
            doGameStart();
        }


    }

    @Override
    public void doError(String error,String name) throws ServerUnavailableException {
        getClinetByName(name).sendMessage(ProtocolMessages.ERROR+ProtocolMessages.DELIMITER+
                error+ProtocolMessages.EOT);
    }

    @Override
    public void doGameStart() throws ServerUnavailableException {
        setupGame();
        for(ClientHandler client : readyClients){
            client.sendMessage(ProtocolMessages.GAMESTART+ProtocolMessages.DELIMITER+players[0].getName()+
                    ProtocolMessages.DELIMITER+players[1].getName()+ProtocolMessages.EOT);
                    doUpdate(client.getName());
                    doTiles(client.getName());
                    doCurrent();
        }
    }

    @Override
    public void doTiles(String name) throws ServerUnavailableException {
        ClientPlayer player = getPlayerByName(name);
        ClientHandler client = getClinetByNameRdy(name);
        String rack = board.tileToString(player.getRack());
        client.sendMessage(ProtocolMessages.TILES+ProtocolMessages.DELIMITER+rack+ProtocolMessages.EOT);
    }

    @Override
    public void doCurrent() throws ServerUnavailableException {
        for(ClientHandler client : readyClients) {
            client.sendMessage(ProtocolMessages.CURRENT + ProtocolMessages.DELIMITER +
                    players[current].getName() + ProtocolMessages.EOT);
        }

    }

    public void doChat(String message,String name){
        for(ClientHandler client : clients){
            if(!client.getName().equals(name)){
                try {
                    client.sendMessage(ProtocolMessages.CHAT+ProtocolMessages.DELIMITER+message+ProtocolMessages.EOT);
                } catch (ServerUnavailableException e) {
                    new ServerUnavailableException("unable to send messages!");
                }
            }
        }

    }

    //UPDATE;<boardrows>;<names>;<points>!
    @Override
    public void doUpdate(String name) throws ServerUnavailableException {//name1,name2,score1,score2
        ClientHandler client = getClinetByNameRdy(name);
        client.sendMessage(ProtocolMessages.UPDATE+ProtocolMessages.DELIMITER+view.getBoard(board)+ProtocolMessages.DELIMITER+players[0].getName()+
                ProtocolMessages.DELIMITER+players[1].getName()+ProtocolMessages.DELIMITER+players[0].getScore()+
                ProtocolMessages.DELIMITER+players[1].getScore()+ProtocolMessages.EOT);
    }
    //GAMEOVER;<endType>;<names>;<points>!s
    @Override
    public void doGameOver(String endType) throws ServerUnavailableException {
        for(ClientHandler client : readyClients){
            client.sendMessage(ProtocolMessages.GAMEOVER+ProtocolMessages.DELIMITER+endType+ProtocolMessages.DELIMITER+players[0].getName()+
                    ProtocolMessages.DELIMITER+players[1].getName()+ProtocolMessages.DELIMITER+players[0].getScore()+
                    ProtocolMessages.DELIMITER+players[1].getScore()+ProtocolMessages.EOT);
        }

    }

    @Override
    public void afterMove(String usedLetters,String name) throws ServerUnavailableException {
        handleSwap(usedLetters,name);
        for(ClientHandler client : readyClients){
            doUpdate(client.getName());
            if(!client.getName().equals(name)){
                doTiles(client.getName());
            }
        }
        doCurrent();
    }
    public static void main(String[] args) {
        Server server = new Server();
        new Thread(server).start();
    }


}
