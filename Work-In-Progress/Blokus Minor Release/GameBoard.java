import java.awt.*;

import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import java.util.List;
 

public class GameBoard {
    
    JFrame frame = new JFrame();
    JPanel panel1, panel2, panel3, panel4, panel5;
    JButton buttons[][] = new JButton[20][20];
    JLabel topField, topField2, score, pieces;
    JButton rotateButton, helpButton, passButton, confirmMove, options, exit;


    public GameBoard(){
        
        frame.setSize(1000,700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        //top panel with hints and turn info
        panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(1000, 50));
        panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel1.setBackground(Color.GRAY);
        //panel1.setForeground(new Color(0));
        frame.add(panel1, BorderLayout.NORTH);
        
        // center panel with the game board and the right side panel with buttons
        JPanel middleSection = new JPanel(new BorderLayout());
        panel2 = new JPanel();
        panel2.setLayout(new GridLayout(20,20));
        panel2.setBackground(Color.GRAY);
        
        //panel2 and panel3 are added to the middle section so they can be next to each other, then the middle section is added to the frame    
        middleSection.add(panel2, BorderLayout.CENTER);

        for (int i=0; i<20; i++){ // creates the 20x20 grid of buttons for the game board
                for(int j=0; j<20; j++){
                    JButton button = new JButton();
                    button.setPreferredSize(new Dimension(25,25));
                    button.setBackground(Color.black);
                    button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    button.setBorder(new LineBorder(Color.WHITE,1));
                    buttons[i][j]= button;
                    panel2.add(button);
                    
                }
            }


        
        panel3 = new JPanel();
        panel3.setBackground(Color.WHITE);
        panel3.setPreferredSize(new Dimension(150,0));
        panel3.setLayout(new FlowLayout());
        middleSection.add(panel3, BorderLayout.EAST);

        frame.add(middleSection, BorderLayout.CENTER);
        
        // bottom panel with the pieces and score
        JPanel bottomSection = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel4.setBackground(Color.GRAY);
        gbc.gridy = 0;
        gbc.weighty= 0.0;
        bottomSection.add(panel4, gbc);

        panel5 = new JPanel();
        panel5.setBackground(Color.GRAY);
        panel5.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
        JScrollPane scrollPane = new JScrollPane(panel5);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        gbc.gridy = 1;
        gbc.weighty= 1.0;
        bottomSection.add(scrollPane, gbc);

        bottomSection.setPreferredSize(new Dimension(1100, 200));
        frame.add(bottomSection, BorderLayout.SOUTH);
        //frame.setVisible(true);
        
        // initializing the labels and buttons and adding them to the panels
        topField = new JLabel();
        topField.setPreferredSize(new Dimension(400,50));
        topField.setBackground(Color.WHITE);
        topField.setForeground(Color.red);
        topField.setFont(new Font("Consolas", Font.BOLD, 16));
        
        topField.setText("Hint: Place a piece touching a corner");
        topField.setOpaque(true);
        
        panel1.add(topField);
        
        topField2 = new JLabel();
        topField2.setPreferredSize(new Dimension(400,50));
        topField2.setBackground(Color.BLUE);
        topField2.setOpaque(true);
        topField2.setForeground(Color.BLACK);
        topField2.setFont(new Font("Consolas", Font.BOLD, 16));
        topField2.setText("Turn: Player 1's First Turn");
        
        panel1.add(topField2);

        pieces = new JLabel("Play pieces goes here");
        
        pieces.setPreferredSize(new Dimension(400,50));
        panel5.add(pieces);

        rotateButton = new JButton("Rotate");
        rotateButton.setPreferredSize(new Dimension(100,50));
        
    
        helpButton = new JButton("Help");
        helpButton.setPreferredSize(new Dimension(100,50));
        passButton = new JButton("Pass");
        passButton.setPreferredSize(new Dimension(100,50));
        confirmMove = new JButton("Confirm Move");
        confirmMove.setPreferredSize(new Dimension(100,50));
        options = new JButton("Options");
        options.setPreferredSize(new Dimension(100,50));
        exit = new JButton("Exit");
        exit.setPreferredSize(new Dimension(100,50));

        
        score = new JLabel("Score: Player 1: 0, Player 2: 0");
        score.setPreferredSize(new Dimension(400,20));

        panel3.add(rotateButton);
        panel3.add(confirmMove);
        panel3.add(helpButton);
        panel3.add(passButton);
        panel3.add(options);
        panel3.add(exit);
        
        panel4.add(score);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    

    }
        
    
    public void setListeners(ActionListener boardList, ActionListener controlList){
        
        for (int i=0; i<20; i++){
            for(int j=0; j<20; j++){
                buttons[i][j].addActionListener(boardList);
            }
        }
        
        options.addActionListener(controlList);
        exit.addActionListener(controlList);    
    }
    public Point getButtonPos(JButton button){
        for (int i=0; i<20; i++){
            for(int j=0; j<20; j++){
                if (buttons[i][j] == button){
                    return new Point(i,j);
                }
            }
        }
        return null;
    }
    public void setTurnInfo(int i){
        topField2.setText("Turn: Player "+String.valueOf(i)+ "'s Turn");
    }
    public void setHint(String hint){
        topField.setText("Piece Selected: "+hint);
    }

    public void updatePiecePanel(List<PlayPieces.GamePiece> pieces, ActionListener listener,int indx, Color pColor){

        panel5.removeAll();
        for (int i=0; i<pieces.size(); i++){
            PlayPieces.GamePiece piece = pieces.get(i);
            JButton a = new JButton();
            a.setLayout(new GridLayout(5,5));
            a.setPreferredSize(new Dimension(80,80));
            
            if (i == indx) {
            a.setBackground(Color.LIGHT_GRAY);
            a.setBorder(BorderFactory.createLineBorder(pColor, 3));
        } else {
            a.setBackground(Color.WHITE);
            a.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                JPanel cell = new JPanel();
            
                cell.setBackground(piece.isAt(col, row) ? pColor : a.getBackground());
                a.add(cell);
            }
        }

            a.setActionCommand(String.valueOf(i));
            a.addActionListener(listener);
            panel5.add(a);
        }
        panel5.revalidate();
        panel5.repaint();
    }

    
    public void drawGhost(PlayPieces.GamePiece piece, Point pivot, Color pColor, int[][] boardState) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                int state = boardState[i][j];
                if (state == 0) {
                buttons[i][j].setBackground(Color.BLACK);
            } 
        }
    }
    if (piece == null || pivot == null) return;

    Color ghostColor = new Color(pColor.getRed() , pColor.getGreen(), pColor.getBlue(), 120);

    for (Point p: piece.coordinates){
        int x = pivot.x + p.x, y = pivot.y + p.y;

        if (x>=0 && x<20 && y>=0 && y<20){
            if(boardState[x][y]==0){
                buttons[x][y].setBackground(ghostColor);
            }
        }
    
    }
    }


        
}