import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
 

public class GameBoard implements java.awt.event.ActionListener{
    
    JFrame frame = new JFrame();
    JPanel panel1, panel2, panel3, panel4, panel5;
    JButton buttons[][] = new JButton[20][20];
    JLabel topField, topField2, score, pieces;
    JButton rotateButton, helpButton, passButton, confirmMove, options;


    public GameBoard(){
        
        frame.setSize(1000,700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        

        panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(1000, 50));
        panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel1.setBackground(Color.GRAY);
        //panel1.setForeground(new Color(0));
        frame.add(panel1, BorderLayout.NORTH);

        JPanel middleSection = new JPanel(new BorderLayout());
        panel2 = new JPanel();
        panel2.setLayout(new GridLayout(20,20));
        panel2.setBackground(Color.GRAY);
        for (int i=0; i<20; i++){
                for(int j=0; j<20; j++){
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(25,25));
                button.setBackground(Color.black);
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                button.setBorder(new LineBorder(Color.WHITE,1));
                buttons[i][j]= button;
                panel2.add(button);
                buttons[i][j].addActionListener(this);
                }
            }
        middleSection.add(panel2, BorderLayout.CENTER);
        panel3 = new JPanel();
        panel3.setBackground(Color.WHITE);
        panel3.setPreferredSize(new Dimension(150,0));
        panel3.setLayout(new FlowLayout());
        middleSection.add(panel3, BorderLayout.EAST);

        frame.add(middleSection, BorderLayout.CENTER);

        JPanel bottomSection = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel4.setBackground(Color.GRAY);
        gbc.gridy = 0;
        gbc.weighty= 0.1;
        bottomSection.add(panel4, gbc);

        panel5 = new JPanel();
        panel5.setBackground(Color.GRAY);
        panel5.setLayout(new FlowLayout(FlowLayout.LEFT));
        gbc.gridy = 1;
        gbc.weighty= 0.9;
        bottomSection.add(panel5, gbc);

        bottomSection.setPreferredSize(new Dimension(1000, 200));
        frame.add(bottomSection, BorderLayout.SOUTH);
        frame.setVisible(true);
        
        topField = new JLabel();
        topField.setBackground(Color.WHITE);
        topField.setForeground(Color.gray);
        
        topField.setText("Hint: ,Place a piece touching a corner");
        topField.setOpaque(true);
        topField.setPreferredSize(new Dimension(400,50));
        panel1.add(topField);
        
        topField2 = new JLabel();
        topField2.setBackground(Color.WHITE);
        topField2.setForeground(Color.gray);
        
        topField2.setText("Turn: Player 1's First Turn");
        topField2.setOpaque(true);
        topField2.setPreferredSize(new Dimension(400,50));
        panel1.add(topField2);

        pieces = new JLabel("Play pieces goes here");
        
        pieces.setPreferredSize(new Dimension(400,50));
        panel5.add(pieces);

        rotateButton = new JButton("Rotate");
        rotateButton.setPreferredSize(new Dimension(100,50));

        helpButton = new JButton("Help");
        helpButton.setPreferredSize(new Dimension(100,50));
        helpButton.addActionListener(this);
        passButton = new JButton("Pass");
        passButton.setPreferredSize(new Dimension(100,50));
        confirmMove = new JButton("Confirm Move");
        confirmMove.setPreferredSize(new Dimension(100,50));
        options = new JButton("Options");
        options.setPreferredSize(new Dimension(100,50));
        options.addActionListener(this);
        score = new JLabel("Score: Player 1: 0, Player 2: 0");
        score.setPreferredSize(new Dimension(400,10));

        panel3.add(rotateButton);
        panel3.add(confirmMove);
        panel3.add(helpButton);
        panel3.add(passButton);
        panel3.add(options);
        
        panel4.add(score);

        frame.setVisible(true);
    

    }
        
    public void actionPerformed(java.awt.event.ActionEvent e){
        for (int i=0; i<20; i++){
            for(int j=0; j<20; j++){
                if(e.getSource()==buttons[i][j]){
                    buttons[i][j].setBackground(Color.white);
                }
            }
        }
        if (e.getSource()== options){
            OptionsMenu optionsMenu = new OptionsMenu();
            changePlayerColor(optionsMenu);
        }      

    }
    public void changePlayerColor(OptionsMenu optionsMenu) { // (HALLE) added in OptionsMenu

        JFrame colorPicker = new JFrame("color selection");
        colorPicker.setSize(400, 200);
        colorPicker.setLayout(new BoxLayout(colorPicker.getContentPane(), BoxLayout.Y_AXIS));

        // HALLE'S CODE FOR NEW ColorPalette CLASS

        Color[] colors = optionsMenu.getColorPalette().getCurrentPalette().toArray(new Color[0]);

        for (int i = 0; i < colors.length; i++) {
            JButton colorBtn = new JButton("");
            colorBtn.setBackground(colors[i]);
            colorPicker.add(colorBtn); // adds the button to the window
        }


    }

}
