package ss.lr.server.model;

import ss.lr.exceptions.ClientUnavailableException;
import ss.lr.protocols.ProtocolMessages;
import ss.lr.protocols.ServerProtocol;
import ss.lr.server.controller.ClientHandler;
import ss.lr.server.view.ServerTUI;
import utils.WordChecker;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/***
 Server works as a model. It establishes first connection,but after that make a client handler which resides as a controller.
 Here new board is generated and game itself played.
 @author reikal951@gmail.com.
 */

public class Server implements Runnable, ServerProtocol {
    private ServerSocket serverSock;

    private final List<ClientHandler> clients;
    private final List<ClientHandler> readyClients;

    private int next_client_no;
    private final String ip;
    private boolean gameStarted = false;
    private boolean firstMove = true;
    private int skipCount;
    private final WordChecker check;
    private Board board;
    private final ClientPlayer[] players;
    private int current;

    private final ServerTUI view;

    public Server() {
        clients = new ArrayList<>();
        readyClients = new ArrayList<>();
        view = new ServerTUI();
        next_client_no = 1;
        players = new ClientPlayer[2];
        ip = "localhost";
        skipCount = 0;
        check = new WordChecker();
    }

    public static void main(String[] args) {
        Server server = new Server();
        new Thread(server).start();
    }

    public List<ClientHandler> getRdyClients() {
        return this.readyClients;
    }

    public void removeClient(ClientHandler client) {
        this.clients.remove(client);
    }

    public void removeRdyClient(ClientHandler client) {
        this.readyClients.remove(client);
    }

    public void showMessage(String message) {
        view.showMessage(message);
    }

    public void showError(String message) {
        view.showError(message);
    }

    @Override
    public void run() {
        boolean newSocket = true;
        while (newSocket) {
            try {
                setup();
                while (true) {
                    Socket sock = serverSock.accept();
                    String name = "Client "
                            + String.format("%02d", next_client_no++);
                    showMessage("New client [" + name + "] connected!");
                    ClientHandler handler =
                            new ClientHandler(sock, this, name);
                    new Thread(handler).start();
                    clients.add(handler);
                }
            } catch (IOException e) {
                newSocket = false;
                showError("Unable to establish connection with new client!");
            }
        }

    }

    private void setup() {
        serverSock = null;
        while (serverSock == null) {
            int port = view.getInt("Please enter server port: ");

            try {
                showMessage("Attempting to open a socket at 127.0.0.1 "
                        + "on port " + port + "...");
                serverSock = new ServerSocket(port, 0,
                        InetAddress.getByName(ip));
                showMessage("Server started with port: " + port);
            } catch (IOException e) {
                showError("ERROR: could not create a socket on "
                        + "127.0.0.1" + " and port " + port + ".");
                if (!view.getBoolean("Do you want to try again? yes/no")) {
                    System.exit(0);
                }
                serverSock = null;
            }
        }

    }

    // Game methods-------------------
    private void setupGame() {
        board = new Board(check);
        ClientPlayer first = new ClientPlayer(readyClients.get(0).getName(), board.getRack());
        ClientPlayer second = new ClientPlayer(readyClients.get(1).getName(), board.getRack());
        players[0] = first;
        players[1] = second;
        gameStarted = true;
        reset();
    }

    private boolean playAMove(String[] move) {
        if (!gameOver()) {
            if (current == 0) {
                players[current].makeMove(board, move);
                current++;
                return true;
            } else if (current == 1) {
                players[current].makeMove(board, move);
                current--;
                return true;
            }
        }
        calculateLastScores();
        return false;
    }

    private void calculateLastScores() {
        int player1Minus = 0;
        int player2Minus = 0;
        for (int i = 0; i < 2; i++) {
            ArrayList<Tile> tiles = players[i].getRack();
            if (tiles.size() != 0) {
                for (Tile tile : tiles) {
                    if (i == 0) {
                        player1Minus = player1Minus + tile.getValue();
                    } else {
                        player2Minus = player2Minus + tile.getValue();
                    }
                }
            }
        }

        if (player1Minus == 0) {
            players[0].addScore(player2Minus);
            players[1].addScore(-player1Minus);
        } else if (player2Minus == 0) {
            players[0].addScore(-player2Minus);
            players[1].addScore(player1Minus);
        } else {
            players[0].addScore(-player2Minus);
            players[1].addScore(-player1Minus);
        }
    }

    private boolean gameOver() {
        return skipCount == 3 && board.getBag().size() == 0;
    }

    private void reset() {
        current = 0;
        ArrayList<ArrayList<Tile>> racks = board.reset();
        players[0].setRack(racks.get(0));
        players[1].setRack(racks.get(1));
    }

    private void skipTurn() {
        if (current == 0) {
            current++;
        } else if (current == 1) {
            current--;
        }
    }

    private String getGameOutcome() {
        if (players[0].getScore() == players[1].getScore()) {
            return "DRAW";
        } else if (players[0].getScore() > players[1].getScore() ||
                players[0].getScore() < players[1].getScore()) {
            return "WINNER";
        }
        return "STOP";
    }

    private ClientHandler getClientByName(String name) {
        for (ClientHandler hand : clients) {
            if (hand.getName().equals(name)) {
                return hand;
            }
        }
        return null;
    }

    private ClientHandler getClientByNameRdy(String name) {
        for (ClientHandler hand : readyClients) {
            if (hand.getName().equals(name)) {
                return hand;
            }
        }
        return null;
    }


    //-----------------

    private ClientPlayer getPlayerByName(String name) {
        for (ClientPlayer player : players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public String namesInServer() {
        StringBuilder result = new StringBuilder();
        for (ClientHandler client : clients) {
            result.append(client.getName()).append(" ");
        }
        return result.toString();
    }

    private boolean inPlayersRack(Tile[] tiles, ClientPlayer player) {
        ArrayList<Tile> oldRack = player.getRack();
        for (Tile tile : tiles) {
            if (!oldRack.contains(tile)) {
                return false;
            }
        }
        return true;
    }

    //-------Server methods----------
    private boolean validMoveInput(String[] move) {
        if (view.stringIsInt(move[1]) && view.stringIsInt(move[1])) {
            int row = Integer.parseInt(move[1]);
            int col = Integer.parseInt(move[2]);
            if (board.isField(row, col)) {
                if (view.stringIsLetters(move[3])) {
                    return move[4].equalsIgnoreCase("ver") || move[4].equalsIgnoreCase("hor");
                }
            }
        }
        return false;
    }

    @Override
    public void handelHello(String name, ClientHandler client) throws ClientUnavailableException {
        if (getClientByName(name) == null) {
            client.setName(name);
            client.sendMessage(ProtocolMessages.WELCOME + ProtocolMessages.DELIMITER
                    + name + ProtocolMessages.DELIMITER + namesInServer() + ProtocolMessages.EOT);
        } else {
            doError("0", client.getName());
        }
    }

    @Override
    public void handleMove(String[] commands, String name) throws ClientUnavailableException {
        int score;
        if (players[current].getName().equals(name)) {
            if (validMoveInput(commands)) {
                String[] move = {commands[1], commands[2], commands[3], commands[4]};
                int row = Integer.parseInt(commands[1]);
                int col = Integer.parseInt(commands[2]);
                Tile[] tiles = board.stringToTile(commands[3].split(""));
                String direction = commands[4];
                if (gameStarted) {
                    if (!firstMove) {
                        score = board.checkIfMoveLegal(row, col, tiles, direction);
                        if (score > 0) {
                            if (tiles.length == 7) {
                                score = score + 50;
                            }
                            players[current].addScore(score);
                            skipCount = 0;
                            if (!playAMove(move)) {
                                doGameOver(getGameOutcome());
                            }
                            afterMove(commands[3], name);
                        } else if (score == -1) {
                            skipTurn();
                            doCurrent();
                            doError("7", name);
                        } else {
                            doError("1", name);
                        }
                    } else {
                        score = board.checkIfFirstMoveLegal(row, col, tiles, direction);
                        if (score > 0) {
                            if (tiles.length == 7) {
                                score = score + 50;
                            }
                            players[current].addScore(score);
                            skipCount = 0;
                            firstMove = false;
                            playAMove(move);
                            afterMove(commands[3], name);
                        } else if (score == -1) {
                            skipTurn();
                            doCurrent();
                            doError("7", name);
                        } else {
                            doError("1", name);
                        }
                    }
                }
            } else {
                doError("5", name);
            }
        } else {
            doError("8", name);
        }
    }

    public void doSkip(String name) throws ClientUnavailableException {
        if (players[current].getName().equals(name)) {
            skipCount++;
            skipTurn();
            try {
                doCurrent();
            } catch (ClientUnavailableException e) {
                showError("Unable to reach Client!");
            }
        } else {
            try {
                doError("8", name);
            } catch (ClientUnavailableException e) {
                showError("Unable to reach Client!");
            }
        }
    }

    @Override
    public void handleSwap(String word, String name) throws ClientUnavailableException {
        if (players[current].getName().equals(name)) {
            if (board.getBag().size() != 0) {
                ClientPlayer player = getPlayerByName(name);
                String[] letters = word.split("");
                Tile[] replacedRack = board.stringToTile(letters);

                if (player != null && inPlayersRack(replacedRack, player)) {
                    ArrayList<Tile> oldRack = player.getRack();
                    ArrayList<Tile> newRack = board.removeFromRackAndFill(replacedRack, oldRack);
                    player.setRack(newRack);
                    skipTurn();
                    doCurrent();
                    doTiles(name);
                } else {
                    doError("2", name);
                }
            } else {
                doError("3", name);
            }
        } else {
            doError("8", name);
        }
    }

    private void refreshRack(String word, String name) throws ClientUnavailableException {
        if (board.getBag().size() != 0) {
            ClientPlayer player = getPlayerByName(name);
            String[] letters = word.split("");
            Tile[] replacedRack = board.stringToTile(letters);
            if (player != null && inPlayersRack(replacedRack, player)) {
                ArrayList<Tile> oldRack = player.getRack();
                ArrayList<Tile> newRack = board.removeFromRackAndFill(replacedRack, oldRack);
                player.setRack(newRack);
            } else {
                doError("2", name);
            }
        } else {
            doError("3", name);
        }
    }

    @Override
    public void handleQuit(String name) throws ClientUnavailableException {
        Objects.requireNonNull(getClientByName(name)).shutdown();
        doGameOver("STOP");
    }

    @Override
    public void handleReady(ClientHandler client) throws ClientUnavailableException {
        if (!readyClients.contains(client)) {
            readyClients.add(client);
        }
        if (readyClients.size() == 2) {
            doGameStart();
        }
    }

    @Override
    public void doError(String error, String name) throws ClientUnavailableException {
        Objects.requireNonNull(getClientByName(name)).sendMessage(ProtocolMessages.ERROR + ProtocolMessages.DELIMITER +
                error + ProtocolMessages.EOT);
    }

    @Override
    public void doGameStart() throws ClientUnavailableException {
        setupGame();
        for (ClientHandler client : readyClients) {
            client.sendMessage(ProtocolMessages.GAMESTART + ProtocolMessages.DELIMITER + players[0].getName() +
                    ProtocolMessages.DELIMITER + players[1].getName() + ProtocolMessages.EOT);
            doUpdate(client.getName());
            doTiles(client.getName());
            doCurrent();
        }
    }

    @Override
    public void doTiles(String name) throws ClientUnavailableException {
        ClientPlayer player = getPlayerByName(name);
        if (player != null) {
            ClientHandler client = getClientByNameRdy(name);
            if (client != null) {
                String rack = board.tileToString(player.getRack());
                client.sendMessage(ProtocolMessages.TILES + ProtocolMessages.DELIMITER + rack + ProtocolMessages.EOT);
            }
        }
    }

    @Override
    public void doCurrent() throws ClientUnavailableException {
        for (ClientHandler client : readyClients) {
            client.sendMessage(ProtocolMessages.CURRENT + ProtocolMessages.DELIMITER +
                    players[current].getName() + ProtocolMessages.EOT);
        }

    }

    public void doChat(String message, String name) throws ClientUnavailableException {
        for (ClientHandler client : clients) {
            if (!client.getName().equals(name)) {
                client.sendMessage(ProtocolMessages.CHAT + ProtocolMessages.DELIMITER + message + ProtocolMessages.EOT);
            }
        }

    }

    @Override
    public void doUpdate(String name) throws ClientUnavailableException {//name1,name2,score1,score2
        ClientHandler client = getClientByNameRdy(name);
        if (client != null) {
            client.sendMessage(ProtocolMessages.UPDATE + ProtocolMessages.DELIMITER + view.getBoard(board) + ProtocolMessages.DELIMITER + players[0].getName() +
                    ProtocolMessages.DELIMITER + players[1].getName() + ProtocolMessages.DELIMITER + players[0].getScore() +
                    ProtocolMessages.DELIMITER + players[1].getScore() + ProtocolMessages.EOT);
        }
    }

    @Override
    public void doGameOver(String endType) throws ClientUnavailableException {
        for (ClientHandler client : readyClients) {
            client.sendMessage(ProtocolMessages.GAMEOVER + ProtocolMessages.DELIMITER + endType + ProtocolMessages.DELIMITER + players[0].getName() +
                    ProtocolMessages.DELIMITER + players[1].getName() + ProtocolMessages.DELIMITER + players[0].getScore() +
                    ProtocolMessages.DELIMITER + players[1].getScore() + ProtocolMessages.EOT);
        }

    }

    @Override
    public void afterMove(String usedLetters, String name) throws ClientUnavailableException {
        refreshRack(usedLetters, name);
        for (ClientHandler client : readyClients) {
            doUpdate(client.getName());
            doTiles(client.getName());
        }
        doCurrent();
    }

}
