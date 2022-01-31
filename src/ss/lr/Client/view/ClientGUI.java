package ss.lr.Client.view;

import ss.lr.Client.controller.Client;
import ss.lr.Exceptions.ExitProgram;
import ss.lr.Local.model.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

public class ClientGUI extends JFrame {
    private JPanel mainPanel;
    private JLabel IpLabel;
    private JLabel tilesLable;
    private JButton swapButton;
    private JButton connectButton;
    private JButton dcButton;
    private JButton readyButton;
    private JButton turnButton;
    private JTextField swapField;
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
    private JList boardList;
    private JButton ipButton;
    private JFrame frame;
    private Board board;
    private String row;
    private String col;
    private ActionListener tileListener;
    String[] connectionInfo;
    Client client;
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
        this.client = clnt;
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


        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!ipField.getText().isEmpty() && !portField.getText().isEmpty() && !nameField.getText().isEmpty()){
                    String ip = ipField.getText();
                    String port = portField.getText();
                    String name = nameField.getText();
                    connectionInfo = new String[]{ip, port, name};
                    //System.out.println("Ip: " + ip + " port: " +port+ " name: " + name);
                    client.setConnectionInfo(connectionInfo);
                    try {
                        client.createConnection();
                    } catch (ExitProgram ex) {
                        ex.printStackTrace();
                    }
                }
                //System.out.println("Wtf??");
            }
        });
        readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.doReady();
            }
        });
    }

    private String[] getBoard(String board){
        //System.out.println(board);

        String[] letters = board.split(",");

        return letters;
    }

    private void setScore(String name1,String name2,String score1,String score2){
        player1Field.setText(name1);
        player2Field.setText(name2);
        play1Score.setText(score1);
        play2Score.setText(score2);
    }

    public void setTiles(String tiles){
        tilesField.setText(tiles);
    }

    public void setCurrent(String name){
        currentField.setText(name);
    }

    public void createBoard(String board,String name1,String name2,String score1,String score2){
        boardPanel.setLayout(new GridLayout(15, 15));
        String[] letters = getBoard(board);
        setScore(name1,name2,score1,score2);
        System.out.println("Size: " + letters.length +  " " + letters.toString());
            for(int i=0; i<15; i++) {
                for(int j=0; j<15; j++) {
                    int coord = index(i,j);
                    JButton button= new JButton();
                    //button.putClientProperty("location" , new Point(i,j));
                    button.setActionCommand(String.valueOf(coord));
                    //button.setPreferredSize(new Dimension(5, 5));
                    button.addActionListener(tileListener);
                    button.setText(letters[index(i,j)]);
                    //button.setSize(5,5);
                    boardPanel.add(button);
                }
            }
            boardPanel.revalidate();
            boardPanel.repaint();
    }
    public void createBoard(){
        boardPanel.setLayout(new GridLayout(15, 15));
        for(int i=0; i<15; i++) {
            for(int j=0; j<15; j++) {
                int coord = index(i,j);
                JButton button= new JButton();
                //button.putClientProperty("location" , new Point(i,j));
                //button.setActionCommand(String.valueOf(coord));
                //button.setPreferredSize(new Dimension(5, 5));
                button.addActionListener(tileListener);
                button.setText("-");
                button.setSize(5,5);
                if(DarkRedX3.contains(coord)){
                    button.setBackground(Color.RED);
                }else if (PaleRedX2.contains(coord)){
                    button.setBackground(Color.PINK);
                }else if(DarkBlueX3.contains(coord)){
                    button.setBackground(Color.BLUE);
                    button.setForeground(Color.GRAY);
                }else if(PaleBlueX2.contains(coord)){
                    button.setBackground(Color.CYAN);
                }
                boardPanel.add(button);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
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
        int[] coords = coordinates(Integer.parseInt(index));
        this.row = String.valueOf(coords[0]);
        this.col = String.valueOf(coords[1]);

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

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }
    //----------------------------

    //-----ActionListiners--------;

    public void tileListiner(){
        tileListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String index = e.getActionCommand();
                setCoords(index);
            }
        };
    }


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







    public void setupGUI(){
        //setUpBoardListiner();
        //createBoard(board);
        specialTiles();
        createBoard();
        frame.add(mainPanel);
        redirectSystemStreams();
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        System.out.println("Testing lollll");
    }

    //--------Controller to GUI
    public void setPlayerNames(String[] names){
        player1Field.setText(names[0]);
        player2Field.setText(names[1]);
        showMessage("Game has started!");
    }



    //--------GUI to controller methods...


    public void setConnectionYes(){
        connectionLable.setText("Connected ?: Yes");
        connectionLable.setForeground(Color.GREEN);
        dcButton.setEnabled(true);
        connectButton.setEnabled(false);
        readyButton.setEnabled(true);
    }

    public void setConnectionNo(){
        connectionLable.setText("Connected ?: No");
        connectionLable.setForeground(Color.RED);
        dcButton.setEnabled(false);
        connectButton.setEnabled(true);
    }

    public void showMessage(String message){
        System.out.println(message);
    }



}
