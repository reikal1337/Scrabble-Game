package ss.lr.client.view;

import ss.lr.client.controller.Client;
import ss.lr.client.controller.ClientControllerIMessage;
import ss.lr.server.model.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

/***
 This is view part of client,it handles all view and represents incoming data to GUI.
 It also validates input by disabling and enabling buttons.
 @author Lukas Reika s2596237.
 */

public class ClientGUI extends JFrame {
    private final JFrame frame;
    private final Client controller;
    private JPanel mainPanel;
    private JLabel IpLabel;
    private JLabel tilesLable;
    private JButton swapButton;
    private JButton connectButton;
    private JButton dcButton;
    private JButton readyButton;
    private JButton skipButton;
    private JTextField inputField;
    private JTextField ipField;
    private JTextField portField;
    private JTextField nameField;
    private JLabel portLable;
    private JLabel nameLable;
    private JTextField player1Field;
    private JTextField player2Field;
    private JTextField currentField;
    private JTextField tilesField;
    private JRadioButton horRadioButton;
    private JRadioButton verRadioButton;
    private JTextField chatField;
    private JPanel gamePanel;
    private JPanel menuPanel;
    private JPanel chatPanel;
    private JPanel boardPanel;
    private JTextArea chatText;
    private JButton moveButton;
    private JButton exitButton;
    private JLabel connectionLable;
    private JLabel play1Score;
    private JLabel play2Score;
    private JLabel rowLable;
    private JLabel colLable;
    private JButton infoButton;
    private JList boardList;
    private JButton ipButton;
    private Board board;
    private String row;
    private String col;
    private ActionListener tileListener;
    //all dark red tile coordinates.
    private ArrayList<Integer> DarkRedX3;//8tiles
    //all pale red tile coordinates.
    private ArrayList<Integer> PaleRedX2;//16tiles
    //all dark blue tile coordinates.
    private ArrayList<Integer> DarkBlueX3;//12iles
    //all pale blue tile coordinates.
    private ArrayList<Integer> PaleBlueX2;//24tiles

    public ClientGUI(String title, Client client) {
        super(title);
        frame = new JFrame();
        row = "";
        col = "";
        this.controller = client;
        specialTiles();
        setUpActionListeners();
        frame.add(mainPanel);
        redirectSystemStreams();
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static int[] coordinates(int index) {
        int row = index / 15;
        int col = index % 15;
        return new int[]{row, col};
    }

    private String[] getBoard(String board) {
        return board.split(",");
    }

    public void setScore(String score1, String score2) {
        play1Score.setText(score1);
        play2Score.setText(score2);
    }

    private void setNames(String name1, String name2) {
        player1Field.setText(name1);
        player2Field.setText(name2);
    }

    public void setTiles(String tiles) {
        tilesField.setText(tiles);
    }

    public void setCurrent(String name) {
        currentField.setText(name);
    }

    private void setPlayerNames(String[] names) {
        player1Field.setText(names[0]);
        player2Field.setText(names[1]);
    }

    public void setYourTurn() {
        moveButton.setEnabled(true);
        swapButton.setEnabled(true);
        inputField.setEnabled(true);
        skipButton.setEnabled(true);

    }

    public void gameStart(String[] names) {
        setPlayerNames(names);
        readyButton.setEnabled(false);
        showMessage("Game has started!");
    }

    public void setNotYourTurn() {
        moveButton.setEnabled(false);
        swapButton.setEnabled(false);
        inputField.setEnabled(false);
        skipButton.setEnabled(false);
    }

    public void setEndGame() {
        setNotYourTurn();
        readyButton.setEnabled(true);
    }

    public void setConnectionYes() {
        connectionLable.setText("Connected ?: Yes");
        connectionLable.setForeground(Color.GREEN);
        dcButton.setEnabled(true);
        connectButton.setEnabled(false);
        readyButton.setEnabled(true);
        ipField.setEnabled(false);
        portField.setEnabled(false);
        nameField.setEnabled(false);
        chatField.setEnabled(true);
    }

    public void setConnectionNo() {
        connectionLable.setText("Connected ?: No");
        connectionLable.setForeground(Color.RED);
        dcButton.setEnabled(false);
        connectButton.setEnabled(true);
        readyButton.setEnabled(false);
        ipField.setEnabled(true);
        portField.setEnabled(true);
        nameField.setEnabled(true);
        chatField.setEnabled(false);
    }

    private void specialTiles() {
        //0 0, 0 7, 0 14, 7 0, 7 14, 14 0, 14 7, 14 14 -8
        DarkRedX3 = new ArrayList<>();
        Collections.addAll(DarkRedX3, index(0, 0), index(0, 7), index(0, 14),
                index(7, 0), index(7, 14), index(14, 0), index(14, 7),
                index(14, 14));


        //1 1, 1 13, 2 2, 2 12, 3 3, 3 11, 4 4, 4 10, 10 4, 10 10, 11 3, 11 11, 12 2, 12 12, 13 1, 13 13 -16
        PaleRedX2 = new ArrayList<>();
        Collections.addAll(PaleRedX2, index(1, 1), index(1, 13), index(2, 2)
                , index(2, 12), index(3, 3), index(3, 11), index(4, 4)
                , index(4, 10), index(10, 4), index(10, 10), index(11, 3)
                , index(11, 11), index(12, 2), index(12, 12), index(13, 1)
                , index(13, 13));


        //1 5, 1 9, 5 1, 5 5, 5 9, 5 13, 9 1, 9 5, 9 9, 9 13, 13 5, 13 9; -12
        DarkBlueX3 = new ArrayList<>();
        Collections.addAll(DarkBlueX3, index(1, 5), index(1, 9), index(5, 1)
                , index(5, 5), index(5, 9), index(5, 13), index(9, 1)
                , index(9, 5), index(9, 9), index(9, 13), index(13, 5)
                , index(13, 9));

        //0 3, 0 11, 2 6, 2 8, 3 0, 3 7, 3 14, 6 2, 6 6, 6 8, 6 12, 7 3, 7 11, 8 2, 8 6,
        // 8 8, 8 12, 11 0, 11 7, 11 14, 12 6, 12 8, 14 3, 14 11 -24
        PaleBlueX2 = new ArrayList<>();
        Collections.addAll(PaleBlueX2, index(0, 3), index(0, 11), index(2, 6)
                , index(2, 8), index(3, 0), index(3, 7), index(3, 14)
                , index(6, 2), index(6, 6), index(6, 8), index(6, 12)
                , index(7, 3), index(7, 11), index(8, 2), index(8, 6)
                , index(8, 8), index(8, 12), index(11, 0), index(11, 7)
                , index(11, 14), index(12, 6), index(12, 8), index(14, 3)
                , index(14, 11));
    }

    public void createBoard(String board, String name1, String name2, String score1, String score2) {
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(15, 15));
        String[] letters = getBoard(board);
        setNames(name1, name2);
        setScore(score1, score2);
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                int coord = index(i, j);
                String tile = letters[index(i, j)];
                JButton button = new JButton(tile);
                button.setActionCommand(String.valueOf(coord));
                button.addActionListener(tileListener);
                if (tile.equals("-")) {
                    if (DarkRedX3.contains(coord)) {
                        button.setBackground(Color.RED);
                    } else if (PaleRedX2.contains(coord)) {
                        button.setBackground(Color.PINK);
                    } else if (DarkBlueX3.contains(coord)) {
                        button.setBackground(Color.BLUE);
                        button.setForeground(Color.GRAY);
                    } else if (PaleBlueX2.contains(coord)) {
                        button.setBackground(Color.CYAN);
                    } else if (coord == index(7, 7)) {
                        button.setBackground(Color.BLACK);
                    }
                }
                boardPanel.add(button);
            }
        }
        frame.revalidate();
    }

    private int index(int row, int col) {
        return row * 15 + col;
    }

    private void setCoords(String index) {
        if (!index.contains("-")) {
            int[] coords = coordinates(Integer.parseInt(index));
            this.row = String.valueOf(coords[0]);
            this.col = String.valueOf(coords[1]);
            rowLable.setText("Row: " + coords[0]);
            colLable.setText("Col: " + coords[1]);
        } else {
            this.row = "-";
            this.col = "-";
            rowLable.setText("Row: -");
            colLable.setText("Col: -");
        }


    }

    //----------Redirects console input to GUI
    private void updateConsole(String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                chatText.append(text);
            }
        });
    }

    private void redirectSystemStreams() {
        OutputStream outPut = new OutputStream() {

            @Override
            public void write(int b) {
                updateConsole(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) {
                updateConsole(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) {
                write(b, 0, b.length);
            }
        };
        System.setOut(new PrintStream(outPut, true));
        System.setErr(new PrintStream(outPut, true));
    }
//--------------------------------------------------------


    private void setUpActionListeners() {

        tileListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String index = e.getActionCommand();
                setCoords(index);
            }
        };

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ipField.getText().isEmpty() && !portField.getText().isEmpty() && !nameField.getText().isEmpty()) {
                    String ip = ipField.getText();
                    String port = portField.getText();
                    String name = nameField.getText();
                    if (name.matches("^[a-zA-Z0-9]*$") && name.length() <= 25 && name.length() >= 1) {
                        if (stringIsInt(port)) {
                            int portInt = Integer.parseInt(port);
                            if (portInt >= 0 && portInt <= 65535) {
                                showMessage("Connecting to " + ip + ":" + port + ".....");
                                controller.createConnection(ip, port, name);


                            } else {
                                showError("Error: port has to be 0-65535");
                            }
                        } else {
                            showError("Error: port has to be 0-65535");
                        }
                    } else {
                        showError("Error: Name can't have any special character and need to be between 1-25 length");
                    }
                } else {
                    showError("Error: Ip, port and name fields can't be empty!");
                }
            }
        });

        readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.handleGUIModel(ClientControllerIMessage.READY);
            }
        });

        moveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String direction = "";
                if (verRadioButton.isSelected()) {
                    direction = "ver";
                } else if (horRadioButton.isSelected()) {
                    direction = "hor";
                }
                if (allowedLetterInput()) {
                    String move = row + " " + col + " " + inputField.getText() + " " + direction;
                    setCoords("- -");
                    inputField.setText("");
                    controller.handleGUIModel(ClientControllerIMessage.MOVE + ClientControllerIMessage.BREAK
                            + move);
                } else {
                    showError("Error: You used letters that don't belong to you!");
                }

            }
        });

        chatField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = e.getActionCommand();
                String time = getTime();
                showChat(time + " " + controller.getName() + ": " + result);
                controller.handleGUIModel(ClientControllerIMessage.CHAT + ClientControllerIMessage.BREAK
                        + time + " " + controller.getName() + ": " + result);
                chatField.setText("");
            }
        });

        dcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.handleGUIModel(ClientControllerIMessage.DISCONNECT);
                setConnectionNo();

            }
        });

        skipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.handleGUIModel(ClientControllerIMessage.SKIP);
            }
        });
        swapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (allowedLetterInput()) {
                    controller.handleGUIModel(ClientControllerIMessage.SWAP + ClientControllerIMessage.BREAK
                            + inputField.getText());
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.handleGUIModel(ClientControllerIMessage.EXIT);
            }
        });

        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMessage("\nWelcome to scrabble game!\n" +
                        "Best Played on maximum screen!\n" +
                        "For information about functions hover over field or button.\n" +
                        "To make a play, chose a tile on which your word will start. Then input letters that you want to use," +
                        " chose direction hor/ver(horizontal or vertical) and press move button!\n" +
                        "Special tiles: \n" +
                        "Dark red “triple-word”.\n" +
                        "Pale red “double-word”.\n" +
                        "Start “center square”(Black), which also counts as double word.\n" +
                        "Dark blue “triple-letter”.\n" +
                        "Pale blue “double-letter”.\n" +
                        "\nFor more information how game is played visit: \n" +
                        "https://scrabble.hasbro.com/en-us/rules");
            }
        });

    }

    public String getTime() {
        LocalTime time = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return "[" + time.format(timeFormatter) + "]";
    }

    public boolean allowedLetterInput() {
        ArrayList<String> allowedLetters = new ArrayList<>();
        String[] allowedLettersArray = tilesField.getText().toLowerCase().split(" ");

        Collections.addAll(allowedLetters, allowedLettersArray);
        String[] inputLetters = inputField.getText().toLowerCase().split("");
        for (String letter : inputLetters) {
            if (allowedLetters.contains(letter)) {
                allowedLetters.remove(letter);
            } else {
                return false;
            }
        }
        return true;
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showChat(String message) {
        System.out.println(message);
    }

    public void showError(String message) {
        System.out.println(message);
    }

    private boolean stringIsInt(String word) {
        return word.matches("-?\\d+");
    }


}
