package ss.lr.Client.view;

import javax.swing.*;

public class ClientGUI extends JFrame{
    private JPanel mainPanel;
    private JLabel IpLabel;
    private JRadioButton connectedRadioButton;
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
    private JButton ipButton;

    public ClientGUI(String title){
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
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

    public static void main(String[] args){
        JFrame frame = new ClientGUI("Client");
        frame.setVisible(true);
    }

}
