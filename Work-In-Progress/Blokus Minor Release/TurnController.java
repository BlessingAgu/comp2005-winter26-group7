
import java.awt.event.ActionListener;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.JButton;

public class TurnController {
    
    private GameBoard gameBoard;

    public TurnController(GameBoard gameBoard){

        this.gameBoard = gameBoard;
        this.gameBoard.setListeners(new BoardListener(), new PanelListener()); //polymorphism (midterm lesson)
    }

    private class BoardListener implements ActionListener{
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            
            JButton source = (JButton) e.getSource();
            Point gridLocation = gameBoard.getButtonPos(source);
            System.out.println("Button at (" + gridLocation.x + ", " + gridLocation.y+") clicked");
            
        }
    }

    private class PanelListener implements ActionListener{

        public void actionPerformed(ActionEvent e){

            String message = e.getActionCommand();

            if (message.equals("Options")){

                System.out.println("Options button clicked");
                gameBoard.setHint("HMM!! Why are you in options?");
                // we can implement options here
            }
            if(message.equals("Exit")){

                //GameBoard game =new GameBoard();
                //TurnController turnController = new TurnController(game);
                System.out.println("Exit button clicked");
                gameBoard.setHint("Exiting the game...");

            }

        }
    }
}
