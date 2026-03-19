
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import java.util.ArrayList;

public class TurnController {
    
    private GameBoard gameBoard;
    private int currentPlayerInd = 0;
    private int[][] boardState = new int[20][20]; 
    private List<List<PlayPieces.GamePiece>> allplayerPieces = new ArrayList<>();
    private PlayPieces.GamePiece selectedPiece= null;
    private int selectedPieceInd = -1;
    private Point lastHover = null;
    private Color[] pColors = {Color.BLUE, Color.YELLOW, Color.RED, Color.PINK};
    private boolean[] passed = new boolean[4];
    

    public TurnController(GameBoard gameBoard){

        this.gameBoard = gameBoard;
        for (int i=0; i<4; i++){
            List<PlayPieces.GamePiece> set = new ArrayList<>();
            for (Point[] d: PlayPieces.getPieces()) set.add(new PlayPieces.GamePiece(d, i+1));
            allplayerPieces.add(set);
        }
        setupBoardListeners();
        gameBoard.rotateButton.addActionListener(e-> rotateSelection());
        gameBoard.passButton.addActionListener(e -> { passed[currentPlayerInd] = true; nextTurn(); });
        gameBoard.confirmMove.addActionListener(e -> tryPlacePiece());
        updateUI();


    }
    private void setupBoardListeners(){
        for (int i=0; i<20; i++){
            for(int j=0; j<20; j++){
                final int r=i, c=j;
                gameBoard.buttons[i][j].addActionListener(e -> lastHover = new Point(r,c));
                gameBoard.buttons[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        lastHover = new Point(r,c);
                        if (selectedPiece != null) {
                            gameBoard.drawGhost(selectedPiece, lastHover , pColors[currentPlayerInd],boardState);
                        }
                    }
                });
            }
        }
    }
    private void rotateSelection(){
        if (selectedPiece != null){
            selectedPiece.rotateClockwise();
            updateUI();
        }
    }

    private void updateUI(){
        gameBoard.updatePiecePanel(allplayerPieces.get(currentPlayerInd),new PieceSelectListener(), selectedPieceInd,  pColors[currentPlayerInd]);
        gameBoard.setTurnInfo((currentPlayerInd + 1));
        updateScore();

    }
    private void updateScore(){
        StringBuilder score= new StringBuilder("Squares left: ");
        for (int i=0; i<4; i++){
            int count = 0;
            for (PlayPieces.GamePiece p: allplayerPieces.get(i)) count += p.coordinates.length;
            score.append("Player ").append(i+1).append(": ").append(count).append("  ");
        }
        gameBoard.score.setText(score.toString());
    }
    private class PieceSelectListener implements ActionListener{

        public void actionPerformed(ActionEvent e){
            selectedPieceInd = Integer.parseInt(e.getActionCommand());
            selectedPiece = allplayerPieces.get(currentPlayerInd).get(selectedPieceInd);
            gameBoard.setHint("Selected piece " + (selectedPieceInd + 1));
            updateUI();
        }
    }

    private boolean isLegalMove(PlayPieces.GamePiece piece, Point pos){
        boolean touchesOwn = false;
        boolean isFirstMove = true;
         for (int i=0; i<20; i++){
            for(int j=0; j<20; j++){
                if (boardState[i][j] == currentPlayerInd+1) isFirstMove = false;
            }
        }
        for (Point p: piece.coordinates){
            int x = pos.x + p.x, y = pos.y + p.y;
            if (x<0 || x>=20 || y<0 || y>=20 || boardState[x][y] != 0) return false;
            
            int[] dx = {1,-1,0,0}, dy = {0,0,1,-1};
            for (int i =0; i <4; i++){
                int ax = x + dx[i], ay = y + dy[i];
                if(ax>0 && ax<20 && ay >=0 && ay <20 && boardState[ax][ay]== currentPlayerInd +1) return false;

            }
            int[] cx = {1,1,-1,-1}, cy = {1,-1,1,-1};
            for (int i=0; i<4; i++){
                int ax = x + cx[i], ay = y + cy[i];
                if(ax>=0 && ax<20 && ay >=0 && ay <20 && boardState[ax][ay]== currentPlayerInd +1) touchesOwn = true;

            }
            if (isFirstMove && ((x==0 && y ==0) || (x==0 && y==19) || (x==19 && y==0) || (x==19 && y==19) )){
                touchesOwn = true;
            }
                
        }
        return touchesOwn;

    }
    private void nextTurn(){
        int start = currentPlayerInd;
        do {
            currentPlayerInd = (currentPlayerInd + 1) % 4;
            if (currentPlayerInd == start && passed[currentPlayerInd]) {declareWinner(); return;}
        } while (passed[currentPlayerInd]);
        updateUI();
    }
    private void declareWinner(){
        JOptionPane.showMessageDialog(gameBoard.frame, "Game Over!" + gameBoard.score.getText());
    }
    private void tryPlacePiece() {
        if (selectedPiece != null && lastHover != null && isLegalMove(selectedPiece, lastHover)) {
            for (Point rel : selectedPiece.coordinates) {
                int x = lastHover.x + rel.x;
                int y = lastHover.y + rel.y;
                boardState[x][y] = currentPlayerInd + 1;
                gameBoard.buttons[x][y].setBackground(pColors[currentPlayerInd]);
            }
            allplayerPieces.get(currentPlayerInd).remove(selectedPiece);
            selectedPiece = null;
            selectedPieceInd = -1;
            nextTurn();
        } else {
            gameBoard.setHint("Invalid move or no piece selected!");
        }
    }



}
