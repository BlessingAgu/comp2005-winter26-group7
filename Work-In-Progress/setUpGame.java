package setUpGame;
import java.awt.*;
import javax.swing.*;

import javax.swing.JPanel;

public class setUpGame extends JFrame{
	
	//player counter
	private int numberOfPlayers;
	
	
	public setUpGame(){
		int numberOfPlayers = 0; // keeping player count tracked
		
		setTitle("Game Set Up");
		setSize(800, 800);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new GridBagLayout()); //setting Layout of the UI to be able to modifly to fit the size of players allowed in
		GridBagConstraints gbc = new GridBagConstraints();
		
		
		
		//adding the Back and the start game buttons 
		JButton back = new JButton("Main Menu");
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(back, gbc);
		
		
		
		back.addActionListener(e -> {
			
			//mainMenu(); meant to return players to the main menu once it is implimented 
			
			
		});
		
		
		JButton start = new JButton("Start Game");
		gbc.gridx = 10;
		gbc.gridy = 0;
		add(start, gbc);
		
		start.addActionListener(e -> {
			
			//new BlokusGame = new BlokusGame //tie into the game board 
			
			
		
			
		});
		
		JButton player1Button = new JButton("Player 1");
		gbc.gridx = 0;
		gbc.gridy = 10;
		add(player1Button, gbc);
		
		JButton player2Button = new JButton("Player 2");
		gbc.gridx = 10;
		gbc.gridy = 10;
		add(player2Button, gbc);
		player2Button.setVisible(false);
		
		JButton player3Button = new JButton("Player 3");
		gbc.gridx = 0;
		gbc.gridy = 20;
		add(player3Button, gbc);
		player3Button.setVisible(false);
		
		JButton player4Button = new JButton("Player 4");
		gbc.gridx = 10;
		gbc.gridy = 20;
		add(player4Button, gbc);
		player4Button.setVisible(false);
		
		JButton addPlayerButton = new JButton("Add Player");
		gbc.gridx = 0;
		gbc.gridy = 30;
		add(addPlayerButton, gbc);
		
		JButton removePlayerButton = new JButton("Remove Player");
		gbc.gridx = 10;
		gbc.gridy = 30;
		add(removePlayerButton, gbc);
		
		JButton optionsButton = new JButton("Options");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 40;
		gbc.gridwidth = 50;
		gbc.ipady = 40;
		gbc.weightx = 0.0;
		add(optionsButton, gbc);
		
		optionsButton.addActionListener(e -> {
			//new OptionsMenu();
			
		});
		
        setLocationRelativeTo(null); // puts it right in the middle of the screen
        setVisible(true);
		
		
	}
	
	//public void addPlayer(){
	//	if (numberOfPlayers == 4) {
	//		tooManyPlayers();
	//	}
	//	else {
	//		if (numberOfPlayers == 0) {
	//			Player p1 = new Player();
	//			
	//			}
	//		else if (numberOfPlayers == 1) {
	//			Player p2 = new Player();
	//		}
	//		else if (numberOfPlayers == 2) {
	//			Player p3 = new Player();
	//		}
		//	else if (numberOfPlayers == 3) {
		//		Player p4 = new Player();
		//	}
	//	}
		
	//}
	
	public void changePlayerColor() {
		
		JFrame colorPicker = new JFrame("color selection");
		colorPicker.setSize(400, 200);
		colorPicker.setLayout(new BoxLayout(colorPicker.getContentPane(), BoxLayout.Y_AXIS));
		
		//creating the menu to pick color from
		JButton color1B = new JButton("");
		color1B.setBackground(Color.RED);
		
		color1B.addActionListener(e -> {
			//changePlayerColour()
			
		});
		
		JButton color2B = new JButton("");
		color2B.setBackground(Color.GREEN);
		
		color2B.addActionListener(e -> {
			//changePlayerColour()
			
		});
		
		JButton color3B = new JButton("");
		color3B.setBackground(Color.BLUE);
		
		color3B.addActionListener(e -> {
			//changePlayerColour()
			
		});
		
		JButton color4B = new JButton("");
		color4B.setBackground(Color.YELLOW);
		
		color4B.addActionListener(e -> {
			//changePlayerColour()
			
		});
		
		
	}
	
	public void playerTypeMenu() {
		JFrame playerType = new JFrame("Chose Player type"); // pops up a menu to chose a player type
		playerType.setSize(600, 750);
		playerType.setLayout(new BoxLayout(playerType.getContentPane(), BoxLayout.Y_AXIS));
		
		JButton compPlayer = new JButton("Computer Player");
		compPlayer.setFont(new Font("Arial", Font.PLAIN, 18));
		compPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		compPlayer.addActionListener(e -> {
			
			//playerInformation.addNewComputerPlayer() //ment for later itoration when computer player is fucntional 
			
			
		});
		
		JButton humanPlayer = new JButton("Human Player");
		humanPlayer.setFont(new Font("Arial", Font.PLAIN, 18));
		humanPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		humanPlayer.addActionListener(e -> {
			
			//playerInformation.addPlayer();
			//changePlayerColor();
			
		});
		
	}
	
	public void tooManyPlayers() { //convert to it's own class later for error handling
		
	}	
	
	public int getNumberOfPlayers() { //getter for number of players
		return numberOfPlayers;
	}

	public static void main(String[] args) {
		setUpGame test = new setUpGame();
	};

}