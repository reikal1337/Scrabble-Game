package ss.lr.Client.view;

import ss.lr.Client.controller.Client;
import ss.lr.Exceptions.ExitProgram;
import ss.lr.Exceptions.ServerUnavailableException;
import ss.lr.Local.model.Board;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class ClientGUI extends JFrame {
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
    private JList boardList;
    private JButton ipButton;
    private JFrame frame;
    private Board board;
    private String row;
    private String col;
    private ActionListener tileListener;
    String[] connectionInfo;
    Client controller;
    //all dark red tile coordinates.
    private static ArrayList<Integer> DarkRedX3;//8tiles
    //all pale red tile coordinates.
    private static ArrayList<Integer> PaleRedX2;//16tiles
    //all dark blue tile coordinates.
    private static ArrayList<Integer> DarkBlueX3;//12iles
    //all pale blue tile coordinates.
    private static ArrayList<Integer> PaleBlueX2;//24tiles


    private void specialTiles() {
        //0 0, 0 7, 0 14, 7 0, 7 14, 14 0, 14 7, 14 14 -8
        DarkRedX3 = new ArrayList<Integer>();
        Collections.addAll(DarkRedX3, index(0, 0), index(0, 7), index(0, 14),
                index(7, 0), index(7, 14), index(14, 0), index(14, 7),
                index(14, 14));


        //1 1, 1 13, 2 2, 2 12, 3 3, 3 11, 4 4, 4 10, 10 4, 10 10, 11 3, 11 11, 12 2, 12 12, 13 1, 13 13 -16
        PaleRedX2 = new ArrayList<Integer>();
        Collections.addAll(PaleRedX2, index(1, 1), index(1, 13), index(2, 2)
                , index(2, 12), index(3, 3), index(3, 11), index(4, 4)
                , index(4, 10), index(10, 4), index(10, 10), index(11, 3)
                , index(11, 11), index(12, 2), index(12, 12), index(13, 1)
                , index(13, 13));


        //1 5, 1 9, 5 1, 5 5, 5 9, 5 13, 9 1, 9 5, 9 9, 9 13, 13 5, 13 9; -12
        DarkBlueX3 = new ArrayList<Integer>();
        Collections.addAll(DarkBlueX3, index(1, 5), index(1, 9), index(5, 1)
                , index(5, 5), index(5, 9), index(5, 13), index(9, 1)
                , index(9, 5), index(9, 9), index(9, 13), index(13, 5)
                , index(13, 9));

        //0 3, 0 11, 2 6, 2 8, 3 0, 3 7, 3 14, 6 2, 6 6, 6 8, 6 12, 7 3, 7 11, 8 2, 8 6,
        // 8 8, 8 12, 11 0, 11 7, 11 14, 12 6, 12 8, 14 3, 14 11 -24
        PaleBlueX2 = new ArrayList<Integer>();
        Collections.addAll(PaleBlueX2, index(0, 3), index(0, 11), index(2, 6)
                , index(2, 8), index(3, 0), index(3, 7), index(3, 14)
                , index(6, 2), index(6, 6), index(6, 8), index(6, 12)
                , index(7, 3), index(7, 11), index(8, 2), index(8, 6)
                , index(8, 8), index(8, 12), index(11, 0), index(11, 7)
                , index(11, 14), index(12, 6), index(12, 8), index(14, 3)
                , index(14, 11));
    }




    public ClientGUI(String title,Client clnt) {
        super(title);
        frame = new JFrame();
        row = "";
        col = "";
        this.controller = clnt;
        specialTiles();
        setupGUI();
        //frame.add(mainPanel);


//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setContentPane(mainPanel);
        //createBoard(board);
        //this.add(boardPanel);
        //this.pack();


//        ipButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //Grab the text
//                //Replace with string"Worked"
//                System.out.println(ipField.getText());
//                if(ipField.getText().equals("localhost")){
//                    ipField.setText("Worked");
//                }
//            }
//        });
    }

    private String[] getBoard(String board){
        //System.out.println(board);

        String[] letters = board.split(",");

        return letters;
    }

    public void setScore(String score1,String score2){
        play1Score.setText(score1);
        play2Score.setText(score2);
    }

    private void setNames(String name1,String name2){
        player1Field.setText(name1);
        player2Field.setText(name2);
    }

    public void setTiles(String tiles){
        tilesField.setText(tiles);
    }

    public void setCurrent(String name){
        currentField.setText(name);
    }

    public void createBoard(String board,String name1,String name2,String score1,String score2){
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(15, 15));
        String[] letters = getBoard(board);
        setNames(name1,name2);
        setScore(score1,score2);
        //System.out.println("Size: " + letters.length +  " " + letters.toString());
            for(int i=0; i<15; i++) {
                for(int j=0; j<15; j++) {
                    int coord = index(i,j);
                    String tile = letters[index(i,j)];
                    JButton button= new JButton(tile);
                    //button.putClientProperty("location" , new Point(i,j));
                    button.setActionCommand(String.valueOf(coord));
                    //button.setPreferredSize(new Dimension(5, 5));
                    button.addActionListener(tileListener);
                    //button.setSize(5,5);
                    if(tile.equals("-")){
                        if(DarkRedX3.contains(coord)){
                            button.setBackground(Color.RED);
                        }else if (PaleRedX2.contains(coord)){
                            button.setBackground(Color.PINK);
                        }else if(DarkBlueX3.contains(coord)){
                            button.setBackground(Color.BLUE);
                            button.setForeground(Color.GRAY);
                        }else if(PaleBlueX2.contains(coord)){
                            button.setBackground(Color.CYAN);
                        }else if(coord == index(7,7)){
                            button.setBackground(Color.BLACK);
                            //showMessage("trying");
                        }
                    }
                    boardPanel.add(button);
                }
            }
        frame.revalidate();
//        frame.repaint();
//        frame.pack();
    }

    //for testing
    public void createBoard(){
        boardPanel.setLayout(new GridLayout(15, 15));
        for(int i=0; i<15; i++) {
            for(int j=0; j<15; j++) {
                int coord = index(i,j);
                JButton button= new JButton();
                //button.putClientProperty("location" , new Point(i,j));
                button.setActionCommand(String.valueOf(coord));
                //button.setPreferredSize(new Dimension(5, 5));
                button.addActionListener(tileListener);
                button.setText("-");
                //button.setSize(5,10);
                if(DarkRedX3.contains(coord)){
                    button.setBackground(Color.RED);
                }else if (PaleRedX2.contains(coord)){
                    button.setBackground(Color.PINK);
                }else if(DarkBlueX3.contains(coord)){
                    button.setBackground(Color.BLUE);
                    button.setForeground(Color.GRAY);
                }else if(PaleBlueX2.contains(coord)){
                    button.setBackground(Color.CYAN);
                }else if(coord == index(7,7)){
                    Icon starIcon = new ImageIcon("C:\\Users\\reika\\IdeaProjects\\Mod2Project\\src\\Utils\\star.png");
                    button.setIcon(starIcon);
                    showMessage("trying");
                }
                boardPanel.add(button);
            }
        }
        frame.revalidate();
        //frame.repaint();
        //frame.pack();
    }

    public int index(int row, int col) {
        return row * 15 + col;
    }
    private static int[] coordinates(int index) {
        int row = index / 15;
        int col = index % 15;
        int[] result = {row, col};
        return result;
    }

    private void setCoords(String index){
        if(!index.contains("-")){
            int[] coords = coordinates(Integer.parseInt(index));
            this.row = String.valueOf(coords[0]);
            this.col = String.valueOf(coords[1]);
            rowLable.setText("Row: " + coords[0]);
            colLable.setText("Col: " + coords[1]);
        }else{
            this.row = "-";
            this.col = "-";
            rowLable.setText("Row: -");
            colLable.setText("Col: -");
        }


    }

    //------Testing console...
    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                chatText.append(text);
            }
        });
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                updateTextArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        //System.setOut(new PrintStream(out, true));
        //System.setErr(new PrintStream(out, true));
    }
    //----------------------------


//    public void connectionListiner(){
//        connectButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(!ipField.getText().isEmpty() && !portField.getText().isEmpty() && !nameField.getText().isEmpty()){
//                    String ip = ipField.getText();
//                    String port = portField.getText();
//                    String name = nameField.getText();
//                    connectionInfo = new String[]{ip, port, name};
//                    client.setConnectionInfo(connectionInfo);
//                }
//                System.out.println("Wtf??");
//
//            }
//        });
//    }



    private void setUpActionListeners(){

        tileListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String index = e.getActionCommand();
               // showMessage("HAha " + index);
                setCoords(index);
            }
        };

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!ipField.getText().isEmpty() && !portField.getText().isEmpty() && !nameField.getText().isEmpty()){
                    String ip = ipField.getText();
                    String port = portField.getText();
                    String name = nameField.getText();
                    if (name.matches("^[a-zA-Z0-9]*$") && name.length() <= 25 && name.length() >= 1) {
                        if (stringIsInt(port)) {
                            int portInt = Integer.parseInt(port);
                            if (portInt >= 0 && portInt <= 65535) {
                                try {
                                    showMessage("Connecting to " + ip + ":" + port + ".....");
                                    controller.createConnection(ip, port, name);
                                } catch (ExitProgram ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                showError("Error: port has to be 0-65535");
                            }
                        }else{
                            showError("Error: port has to be 0-65535");
                        }
                    }else{
                        showError("Error: Name can't have any special character and need to be between 1-25 length");
                    }
                }else{
                    showError("Error: Ip, port and name fields can't be empty!");
                }
            }
        });

        readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.doReady();
            }
        });

        //Need to check input..Or not use model as it's not needed..
        moveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String direction = "";
                if(verRadioButton.isSelected()){
                    direction = "ver";
                }else if (horRadioButton.isSelected()){
                    direction = "hor";
                }
                if(allowedLetterInput()){
                    String move = row + " " + col + " " + inputField.getText() + " " + direction;
                    setCoords("- -");
                    inputField.setText("");
                    try {
                        controller.makeMove(move);
                    } catch (ServerUnavailableException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    showError("Error: You used letters that don't belong to you!");
                }

            }
        });

        chatField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = e.getActionCommand();
                String time = getTime();
                showChat(time + " "+ controller.getName()+": "+result);
                controller.doChat(time + " "+ controller.getName()+": "+result);
                chatField.setText("");
            }
        });

        dcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.doDissconect();
                setConnectionNo();

            }
        });

        skipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.doSkip();
            }
        });
        swapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(allowedLetterInput()){
                    controller.doSwap(inputField.getText());
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.doExit();
            }
        });

    }

    public String getTime(){
        LocalTime time = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return "["+time.format(timeFormatter)+"]";
    }

    public boolean allowedLetterInput(){
        ArrayList<String>allowedLetters = new ArrayList<String>();
        String[] allowedLettersArray = tilesField.getText().toLowerCase().split(" ");
        for(String letter: allowedLettersArray){
            allowedLetters.add(letter);
        }
        String[] inputLetters = inputField.getText().toLowerCase().split("");
        for(String letter : inputLetters){
            if(allowedLetters.contains(letter)){
                allowedLetters.remove(letter);
            }else{
            return false;
            }
        }return true;
    }

    //-----Formaters---

    //Can't enter nothing..
//    private void portFormater(){
//        NumberFormat portFormat = NumberFormat.getIntegerInstance();
//        portFormat.setGroupingUsed(false);
//        NumberFormatter portFormatter = new NumberFormatter(portFormat);
//        portFormatter.setValueClass(Integer.class);
//        portFormatter.setMinimum(null);
//        portFormatter.setMaximum(65535);
//        portFormatter.setAllowsInvalid(false);
//        DefaultFormatterFactory portFactory = new DefaultFormatterFactory(portFormatter);
//        portField.setFormatterFactory(portFactory);
//    }
//
//    private void fieldFormaters(){
//        portFormater();
//    }

    //-----------------


    public void setupGUI(){
        setUpActionListeners();
        //fieldFormaters();
        //createBoard(board);
        //createBoard();
        frame.add(mainPanel);
        redirectSystemStreams();
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //System.out.println("Testi);
    }

    //--------Controller to GUI
    public void setPlayerNames(String[] names){
        player1Field.setText(names[0]);
        player2Field.setText(names[1]);
        showMessage("Game has started!");
    }

    public void setStartOfGame(String[] names){
        setPlayerNames(names);
        moveButton.setEnabled(true);
        swapButton.setEnabled(true);
        inputField.setEnabled(true);
        skipButton.setEnabled(true);


    }



    //--------GUI to controller methods...


    public void setConnectionYes(){
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

    public void setConnectionNo(){
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

    public void showMessage(String message){
        System.out.println(message);
    }
    public void showChat(String message){
        System.out.println(message);
    }
    public void showError(String message){
        System.out.println(message);
    }



//    public void showMessage(String message){
//        System.out.println("\033[1;37m"+message+"\u001B[0m");
//    }
//    public void showChat(String message){
//        System.out.println(message);
//    }
//    public void showError(String message){
//        System.out.println("\033[1;91m" + message + "\u001B[0m");
//    }

    private boolean stringIsInt(String word) {return word.matches("-?\\d+") ? true : false;
    }



}
