

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * 
 * @author Group7
 */
public class BlokusGame extends JFrame implements ActionListener
{
	// GUI components
	private JPanel topPanel, bottomPanel, leftPanel;
	private JLabel statusLabel;
	private JButton newGameButton;
	private BoardSquare[][] boardSquares;
	private JButton[] pieceButtons;
	private int boardSize;
	
	// Game state
	private int currentPlayer;
	private boolean gameActive;
	private boolean[] hasPlacedFirstPiece;
	private boolean[][] piecesUsed;
	private int[][] startingCorners;
	
	// Player names
	private String[] playerNames;
	
	// Player inner class, integrated directly
	public static class Player {
		private int playerNumber;
		private String playerName;
		
		public Player(int playerNumber, String playerName) {
			this.playerNumber = playerNumber;
			this.playerName = playerName;
		}
		
		public int getPlayerNumber() { return playerNumber; }
		public String getPlayerName() { return playerName; }
		public void setPlayerName(String name) { this.playerName = name; }
	}
	
	// Current selection
	private int selectedPieceIndex;
	private String selectedPieceType;
	private int pieceRotation;
	
	// Preview
	private int previewRow = -1;
	private int previewCol = -1; // SAYING ITS NOT USED
	
	// Piece data
	private static final String[] PIECE_IDS = {
		"I1", "I2", "I3", "V3", "I4", "O", "T", "L4", "Z4",
		"F", "I5", "L", "N", "P", "T5", "U", "V", "W", "X", "Y", "Z"
	};
	
	private static final String[] PIECE_NAMES = {
		"■ (1sq)", "■■ (2sq)", "■■■ (3sq)", "■■(L) (3sq)",
		"■■■■ (4sq)", "■■(2x2) (4sq)", "■■■(T) (4sq)", "■(L) (4sq)", "■■(Z) (4sq)",
		"F (5sq)", "■■■■■ (5sq)", "L (5sq)", "N (5sq)", "P (5sq)",
		"T (5sq)", "U (5sq)", "V (5sq)", "W (5sq)", "X (5sq)", 
		"Y (5sq)", "Z (5sq)"
	};
	
	public BlokusGame(int boardSize)
	{
		this(boardSize, new String[]{"Player 1", "Player 2", "Player 3", "Player 4"});
	}
	
	// Constructor with custom player names
	public BlokusGame(int boardSize, String[] playerNames)
	{
		this.boardSize = boardSize;
		
		// Set player names
		if (playerNames != null && playerNames.length == 4) {
			this.playerNames = playerNames;
		} else {
			this.playerNames = new String[]{"Player 1", "Player 2", "Player 3", "Player 4"};
		}
		
		
		currentPlayer = 1;
		gameActive = false;
		hasPlacedFirstPiece = new boolean[4];
		piecesUsed = new boolean[4][21];
		selectedPieceIndex = 0;
		selectedPieceType = PIECE_IDS[0];
		pieceRotation = 0;
		
		startingCorners = new int[4][2];
		startingCorners[0] = new int[]{0, 0};
		startingCorners[1] = new int[]{0, boardSize-1};
		startingCorners[2] = new int[]{boardSize-1, boardSize-1};
		startingCorners[3] = new int[]{boardSize-1, 0};
		
		GameUI.setGameWindow(this);
		setupGUI();
		
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Blokus");
		setResizable(false);
		setVisible(true);
	}
	
	private void setupGUI()
	{
		topPanel = new JPanel(new FlowLayout());
		leftPanel = new JPanel(new GridLayout(21, 1, 2, 2));
		leftPanel.setPreferredSize(new Dimension(150, 600));
		bottomPanel = new JPanel(new GridLayout(boardSize, boardSize, 1, 1));
		
		// core buttons placed on top
		newGameButton = new JButton("New Game");
		JButton rotateButton = new JButton("Rotate");
		statusLabel = new JLabel("Click 'New Game' to start!");
		
		newGameButton.addActionListener(this);
		rotateButton.addActionListener(e -> rotatePiece());
		
		topPanel.add(newGameButton);
		topPanel.add(rotateButton);
		
		// Options button for OptionsMenu (hints and Color Mode)
		try {
			Class.forName("OptionsMenu");
			JButton optionsButton = new JButton("Options");
			optionsButton.addActionListener(e -> { // HALLE CHANGED THIS FOR NEW COLORPALETTE CLASS
    			new OptionsMenu();
			});
			topPanel.add(optionsButton);
		} catch (ClassNotFoundException e) {
			System.out.println("OptionsMenu not found.");
		}
		
		topPanel.add(statusLabel);
		
		pieceButtons = new JButton[21];
		for (int i = 0; i < 21; i++)
		{
			final int idx = i;
			pieceButtons[i] = new JButton(PIECE_NAMES[i]);
			pieceButtons[i].addActionListener(e -> selectPiece(idx));
			pieceButtons[i].setEnabled(false);
			leftPanel.add(pieceButtons[i]);
		}
		
		boardSquares = new BoardSquare[boardSize][boardSize];
		for (int row = 0; row < boardSize; row++)
		{
			for (int col = 0; col < boardSize; col++)
			{
				boardSquares[row][col] = new BoardSquare(row, col);
				boardSquares[row][col].setPreferredSize(new Dimension(30, 30));
				boardSquares[row][col].setEmpty();
				boardSquares[row][col].setEnabled(false);
				boardSquares[row][col].addActionListener(this);
				
				final int r = row, c = col;
				boardSquares[row][col].addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent e) { showPreview(r, c); }
					public void mouseExited(MouseEvent e) { clearPreview(); }
				});
				
				bottomPanel.add(boardSquares[row][col]);
			}
		}
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(leftPanel, BorderLayout.WEST);
		getContentPane().add(bottomPanel, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof BoardSquare)
		{
			handleSquareClick((BoardSquare) e.getSource());
		}
		if (e.getSource().equals(newGameButton))
		{
			startNewGame();
		}
	}
	
	private void startNewGame()
	{
		for (int row = 0; row < boardSize; row++)
		{
			for (int col = 0; col < boardSize; col++)
			{
				boardSquares[row][col].setEmpty();
				boardSquares[row][col].setEnabled(true);
			}
		}
		
		currentPlayer = 1;
		gameActive = true;
		pieceRotation = 0;
		
		for (int i = 0; i < 4; i++)
		{
			hasPlacedFirstPiece[i] = false;
			for (int j = 0; j < 21; j++)
			{
				piecesUsed[i][j] = false;
			}
		}
		
		updatePieceButtons();
		statusLabel.setText(getPlayerDisplayName(currentPlayer) + " - Select piece");
	}
	
	// called by GameUI if color palettes are changed
	public void refreshBoardColors()
	{
		for (int row = 0; row < boardSize; row++)
		{
			for (int col = 0; col < boardSize; col++)
			{
				boardSquares[row][col].refreshColor();
			}
		}
	}
	
	// Get player display name
	private String getPlayerDisplayName(int playerNumber)
	{
		if (playerNumber >= 1 && playerNumber <= 4) {
			return playerNames[playerNumber - 1];
		}
		return "Player " + playerNumber;
	}
	
	// Set custom player name
	public void setPlayerName(int playerNumber, String name)
	{
		if (playerNumber >= 1 && playerNumber <= 4 && name != null && !name.isEmpty()) {
			playerNames[playerNumber - 1] = name;
		}
	}
	
	private void updatePieceButtons()
	{
		int playerIdx = currentPlayer - 1;
		for (int i = 0; i < 21; i++)
		{
			if (piecesUsed[playerIdx][i])
			{
				pieceButtons[i].setEnabled(false);
				pieceButtons[i].setText(PIECE_NAMES[i] + " ✗");
			}
			else
			{
				pieceButtons[i].setEnabled(true);
				pieceButtons[i].setText(PIECE_NAMES[i]);
			}
		}
	}
	
	private void selectPiece(int idx)
	{
		if (!gameActive) return;
		selectedPieceIndex = idx;
		selectedPieceType = PIECE_IDS[idx];
		statusLabel.setText(getPlayerDisplayName(currentPlayer) + " selected " + PIECE_NAMES[idx]);
	}
	
	private void rotatePiece()
	{
		pieceRotation = (pieceRotation + 1) % 4;
		statusLabel.setText(getPlayerDisplayName(currentPlayer) + " - Rotated " + (pieceRotation * 90) + "°");
	}
	
	private void showPreview(int row, int col)
	{
		if (!gameActive) return;
		
		// Don't show preview for already used pieces
		if (piecesUsed[currentPlayer - 1][selectedPieceIndex])
		{
			return;
		}
		
		clearPreview();
		previewRow = row;
		previewCol = col;
		
		int[][] shape = getPieceShape();
		boolean valid = canPlacePiece(row, col, shape);
		
		for (int[] sq : shape)
		{
			int r = row + sq[0], c = col + sq[1];
			if (r >= 0 && r < boardSize && c >= 0 && c < boardSize)
			{
				if (valid)
				{
					boardSquares[r][c].setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
					// Use color palette, otherwise use simple light colors
					Color previewColor = getPreviewColor(currentPlayer);
					boardSquares[r][c].setBackground(previewColor);
				}
				else
				{
					boardSquares[r][c].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
				}
			}
		}
	}
	
	// Gets preview color, uses BoardSquareColors if available
	private Color getPreviewColor(int player)
	{
		try {
			Class.forName("BoardSquareColors");
			return BoardSquareColors.getLightColorForPlayer(player);
		} catch (ClassNotFoundException e) {
			
			switch (player)
			{
				case 1: return new Color(255, 200, 200); // Light red
				case 2: return new Color(200, 200, 255); // Light blue
				case 3: return new Color(200, 255, 200); // Light green
				case 4: return new Color(255, 255, 200); // Light yellow
				default: return Color.WHITE;
			}
		}
	}
	
	private void clearPreview()
	{
		if (previewRow < 0) return;
		for (int row = 0; row < boardSize; row++)
		{
			for (int col = 0; col < boardSize; col++)
			{
				if (boardSquares[row][col].isEmpty())
				{
					boardSquares[row][col].setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
					boardSquares[row][col].setBackground(Color.WHITE);
				}
			}
		}
		previewRow = -1;
		previewCol = -1;
	}
	
	private void handleSquareClick(BoardSquare square)
	{
		if (!gameActive || !square.isEnabled()) return;
		
		int row = square.getRow(), col = square.getCol();
		
		// Check if piece has already been used by current player
		if (piecesUsed[currentPlayer - 1][selectedPieceIndex])
		{
			statusLabel.setText(getPlayerDisplayName(currentPlayer) + " - This piece already used! Select a different piece.");
			return;
		}
		
		int[][] shape = getPieceShape();
		
		if (!canPlacePiece(row, col, shape)) return;
		
		for (int[] sq : shape)
		{
			int r = row + sq[0], c = col + sq[1];
			boardSquares[r][c].setPlayer(currentPlayer);
			boardSquares[r][c].setEnabled(false);
		}
		
		piecesUsed[currentPlayer - 1][selectedPieceIndex] = true;
		hasPlacedFirstPiece[currentPlayer - 1] = true;
		pieceRotation = 0;
		clearPreview();
		
		currentPlayer = (currentPlayer % 4) + 1;
		updatePieceButtons();
		statusLabel.setText(getPlayerDisplayName(currentPlayer) + " - Select piece");
	}
	
	private boolean canPlacePiece(int row, int col, int[][] shape)
	{
		// Check bounds and overlap
		for (int[] sq : shape)
		{
			int r = row + sq[0], c = col + sq[1];
			if (r < 0 || r >= boardSize || c < 0 || c >= boardSize) return false;
			if (!boardSquares[r][c].isEmpty()) return false;
		}
		
		// First piece must cover corner
		if (!hasPlacedFirstPiece[currentPlayer - 1])
		{
			int[] corner = startingCorners[currentPlayer - 1];
			for (int[] sq : shape)
			{
				int r = row + sq[0], c = col + sq[1];
				if (r == corner[0] && c == corner[1]) return true;
			}
			return false;
		}
		
		// Must touch corner, not edge
		boolean touchesCorner = false;
		for (int[] sq : shape)
		{
			int r = row + sq[0], c = col + sq[1];
			if (checkDiagonal(r-1, c-1) || checkDiagonal(r-1, c+1) || 
			    checkDiagonal(r+1, c-1) || checkDiagonal(r+1, c+1))
			{
				touchesCorner = true;
			}
		}
		if (!touchesCorner) return false;
		
		// Check no edge contact
		for (int[] sq : shape)
		{
			int r = row + sq[0], c = col + sq[1];
			if (checkSide(r-1, c) || checkSide(r+1, c) || 
			    checkSide(r, c-1) || checkSide(r, c+1))
			{
				return false;
			}
		}
		return true;
	}
	
	private boolean checkDiagonal(int r, int c)
	{
		if (r < 0 || r >= boardSize || c < 0 || c >= boardSize) return false;
		return boardSquares[r][c].hasPlayer(currentPlayer);
	}
	
	private boolean checkSide(int r, int c)
	{
		if (r < 0 || r >= boardSize || c < 0 || c >= boardSize) return false;
		return boardSquares[r][c].hasPlayer(currentPlayer);
	}
	
	private int[][] getPieceShape()
	{
		int[][] base = getBaseShape();
		int[][] rotated = new int[base.length][2];
		
		for (int i = 0; i < base.length; i++)
		{
			int r = base[i][0], c = base[i][1];
			for (int rot = 0; rot < pieceRotation; rot++)
			{
				int newR = c, newC = -r;
				r = newR; c = newC;
			}
			rotated[i] = new int[]{r, c};
		}
		
		int minR = Integer.MAX_VALUE, minC = Integer.MAX_VALUE;
		for (int[] sq : rotated)
		{
			minR = Math.min(minR, sq[0]);
			minC = Math.min(minC, sq[1]);
		}
		for (int[] sq : rotated)
		{
			sq[0] -= minR;
			sq[1] -= minC;
		}
		return rotated;
	}
	
	private int[][] getBaseShape()
	{
		switch (selectedPieceType)
		{
			case "I1": return new int[][]{{0,0}};
			case "I2": return new int[][]{{0,0},{0,1}};
			case "I3": return new int[][]{{0,0},{0,1},{0,2}};
			case "V3": return new int[][]{{0,0},{0,1},{1,0}};
			case "I4": return new int[][]{{0,0},{0,1},{0,2},{0,3}};
			case "O": return new int[][]{{0,0},{0,1},{1,0},{1,1}};
			case "T": return new int[][]{{0,0},{0,1},{0,2},{1,1}};
			case "L4": return new int[][]{{0,0},{1,0},{2,0},{2,1}};
			case "Z4": return new int[][]{{0,0},{0,1},{1,1},{1,2}};
			case "F": return new int[][]{{0,1},{0,2},{1,0},{1,1},{2,1}};
			case "I5": return new int[][]{{0,0},{0,1},{0,2},{0,3},{0,4}};
			case "L": return new int[][]{{0,0},{1,0},{2,0},{3,0},{3,1}};
			case "N": return new int[][]{{0,1},{1,0},{1,1},{2,0},{3,0}};
			case "P": return new int[][]{{0,0},{0,1},{1,0},{1,1},{2,0}};
			case "T5": return new int[][]{{0,0},{0,1},{0,2},{1,1},{2,1}};
			case "U": return new int[][]{{0,0},{0,2},{1,0},{1,1},{1,2}};
			case "V": return new int[][]{{0,0},{1,0},{2,0},{2,1},{2,2}};
			case "W": return new int[][]{{0,0},{1,0},{1,1},{2,1},{2,2}};
			case "X": return new int[][]{{0,1},{1,0},{1,1},{1,2},{2,1}};
			case "Y": return new int[][]{{0,1},{1,0},{1,1},{2,1},{3,1}};
			case "Z": return new int[][]{{0,0},{0,1},{1,1},{2,1},{2,2}};
			default: return new int[][]{{0,0}};
		}
	}
	
}