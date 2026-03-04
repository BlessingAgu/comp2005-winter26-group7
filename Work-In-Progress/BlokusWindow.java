import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 *  BlokusWindow - Shows piece shapes visually!
 *  Based on WindowDemo by mhatcher
 *  
 *  @author Wellington
 */
public class BlokusWindow extends JFrame implements ActionListener
{
	// GUI components
	private JPanel topPanel, bottomPanel, leftPanel;
	private JLabel statusLabel;
	private JButton newGameButton;
	private BoardSquare[][] boardSquares;
	private JButton[] pieceButtons;
	private int boardSize;
	
	// Game state
	private String currentPlayer;
	private String[] playerColors;
	private int currentPlayerIndex;
	private boolean gameActive;
	private boolean[] hasPlacedFirstPiece;
	
	// Piece tracking, each player has 21 pieces
	private boolean[][] piecesUsed;
	private String[] pieceIds = {
		"I1", "I2", "I3", "V3", "I4", "O", "T", "L4", "Z4",
		"F", "I5", "L", "N", "P", "T5", "U", "V", "W", "X", "Y", "Z"
	};
	private String[] pieceNames = {
		"■ (1sq)", "■■ (2sq)", "■■■ (3sq)", "■■(L) (3sq)",
		"■■■■ (4sq)", "■■(2x2) (4sq)", "■■■(T) (4sq)", "■(L) (4sq)", "■■(Z) (4sq)",
		"F (5sq)", "■■■■■ (5sq)", "L (5sq)", "N (5sq)", "P (5sq)",
		"T (5sq)", "U (5sq)", "V (5sq)", "W (5sq)", "X (5sq)", 
		"Y (5sq)", "Z (5sq)"
	};
	
	// Current selection
	private String selectedPieceType;
	private int pieceRotation;
	
	// Preview state
	private int previewRow = -1;
	private int previewCol = -1;
	
	// Starting corners
	private int[][] startingCorners;
	
	public BlokusWindow(int boardSize, int notUsed)
	{
		this.boardSize = boardSize;

		// Initialize
		playerColors = new String[]{"BLUE", "YELLOW", "RED", "GREEN"};
		currentPlayerIndex = 0;
		currentPlayer = playerColors[0];
		gameActive = false;
		selectedPieceType = "I1";
		pieceRotation = 0;
		hasPlacedFirstPiece = new boolean[4];
		piecesUsed = new boolean[4][21];
		
		// Putting the players in specific corners 
		startingCorners = new int[4][2];
		startingCorners[0] = new int[]{0, 0};
		startingCorners[1] = new int[]{0, boardSize-1};
		startingCorners[2] = new int[]{boardSize-1, boardSize-1};
		startingCorners[3] = new int[]{boardSize-1, 0};
		
		// Create panels
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		
		// panel for piece buttons
		leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(21, 1, 2, 2));
		leftPanel.setPreferredSize(new Dimension(150, 600));
		
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(boardSize, boardSize, 1, 1));
		
		// Top panel
		newGameButton = new JButton("New Game");
		JButton rotateButton = new JButton("Rotate");
		statusLabel = new JLabel("Click 'New Game' to start!");
		newGameButton.addActionListener(this);
		rotateButton.addActionListener(e -> rotatePiece());
		
		topPanel.add(newGameButton);
		topPanel.add(rotateButton);
		topPanel.add(statusLabel);
		
		// Piece buttons
		pieceButtons = new JButton[21];
		for (int i = 0; i < 21; i++)
		{
			final int pieceIndex = i;
			pieceButtons[i] = new JButton(pieceNames[i]);
			pieceButtons[i].addActionListener(e -> selectPiece(pieceIndex));
			pieceButtons[i].setEnabled(false);
			leftPanel.add(pieceButtons[i]);
		}
		
		// Board squares
		boardSquares = new BoardSquare[boardSize][boardSize];
		for (int row = 0; row < boardSize; row++)
		{
			for (int col = 0; col < boardSize; col++)
			{
				boardSquares[row][col] = new BoardSquare(row, col);
				boardSquares[row][col].setPreferredSize(new Dimension(30, 30));
				boardSquares[row][col].setEmpty();
				boardSquares[row][col].setEnabled(false);
				boardSquares[row][col].setOpaque(true);
				boardSquares[row][col].setBorderPainted(true);
				boardSquares[row][col].addActionListener(this);
				
				// Add mouse listener for preview
				final int r = row;
				final int c = col;
				boardSquares[row][col].addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent e) {
						showPreview(r, c);
					}
					public void mouseExited(MouseEvent e) {
						clearPreview();
					}
				});
				
				bottomPanel.add(boardSquares[row][col]);
			}
		}
		
		// Add to frame
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(leftPanel, BorderLayout.WEST);
		getContentPane().add(bottomPanel, BorderLayout.CENTER);
		pack();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Blokus - See Piece Shapes!");
		setResizable(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent aevt)
	{
		Object selected = aevt.getSource();
		
		if (selected instanceof BoardSquare)
		{
			handleSquareClick((BoardSquare) selected);
		}
		
		if (selected.equals(newGameButton))
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
		
		currentPlayerIndex = 0;
		currentPlayer = playerColors[0];
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
		statusLabel.setText(currentPlayer + " - Select piece, hover over board to see shape");
	}
	
	private void updatePieceButtons()
	{
		for (int i = 0; i < 21; i++)
		{
			if (piecesUsed[currentPlayerIndex][i])
			{
				pieceButtons[i].setEnabled(false);
				pieceButtons[i].setText(pieceNames[i] + " ✗");
			}
			else
			{
				pieceButtons[i].setEnabled(true);
				pieceButtons[i].setText(pieceNames[i]);
			}
		}
	}
	
	private void selectPiece(int pieceIndex)
	{
		if (!gameActive) return;
		
		selectedPieceType = pieceIds[pieceIndex];
		statusLabel.setText(currentPlayer + " selected " + pieceNames[pieceIndex] + " - Hover to preview!");
	}
	
	// SHOW PIECE SHAPE AS PREVIEW!
	private void showPreview(int row, int col)
	{
		if (!gameActive || selectedPieceType == null) return;
		
		// Check if piece has already been used
		int pieceIndex = getPieceIndex(selectedPieceType);
		if (piecesUsed[currentPlayerIndex][pieceIndex])
		{
			return; // Don't show preview for used pieces
		}
		
		clearPreview();
		previewRow = row;
		previewCol = col;
		
		int[][] shape = getPieceShape();
		boolean isValid = canPlacePiece(row, col);
		
		// Show the piece shape with colored borders
		for (int[] square : shape)
		{
			int r = row + square[0];
			int c = col + square[1];
			
			if (r >= 0 && r < boardSize && c >= 0 && c < boardSize)
			{
				if (isValid)
				{
					// Green border for valid
					boardSquares[r][c].setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
					boardSquares[r][c].setBackground(getLightColor(currentPlayer));
				}
				else
				{
					// Red border for invalid
					boardSquares[r][c].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
				}
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
	
	private Color getLightColor(String player)
	{
		switch (player)
		{
			case "BLUE": return new Color(173, 216, 230);
			case "YELLOW": return new Color(255, 255, 200);
			case "RED": return new Color(255, 200, 200);
			case "GREEN": return new Color(200, 255, 200);
			default: return Color.WHITE;
		}
	}
	
	private void handleSquareClick(BoardSquare square)
	{
		if (!gameActive || !square.isEnabled()) return;

		int row = square.getRow();
		int col = square.getCol();
		
		// Check if piece has already been used by current player
		int pieceIndex = getPieceIndex(selectedPieceType);
		if (piecesUsed[currentPlayerIndex][pieceIndex])
		{
			statusLabel.setText(currentPlayer + " - This piece has already been used! Select a different piece.");
			return;
		}
		
		if (!canPlacePiece(row, col))
		{
			return;
		}
		
		// Place ALL squares of the piece!
		int[][] shape = getPieceShape();
		for (int[] sq : shape)
		{
			int r = row + sq[0];
			int c = col + sq[1];
			setSquareColor(boardSquares[r][c], currentPlayer);
			boardSquares[r][c].setEnabled(false);
		}
		
		// Mark piece as used
		piecesUsed[currentPlayerIndex][pieceIndex] = true;
		
		hasPlacedFirstPiece[currentPlayerIndex] = true;
		pieceRotation = 0;
		clearPreview();
		
		// Next player
		currentPlayerIndex = (currentPlayerIndex + 1) % 4;
		currentPlayer = playerColors[currentPlayerIndex];
		
		updatePieceButtons();
		statusLabel.setText(currentPlayer + " - Select piece");
	}
	
	private int getPieceIndex(String pieceId)
	{
		for (int i = 0; i < pieceIds.length; i++)
		{
			if (pieceIds[i].equals(pieceId))
			{
				return i;
			}
		}
		return 0;
	}
	
	private boolean canPlacePiece(int row, int col)
	{
		int[][] shape = getPieceShape();
		
		// Check bounds and overlap
		for (int[] square : shape)
		{
			int r = row + square[0];
			int c = col + square[1];
			
			if (r < 0 || r >= boardSize || c < 0 || c >= boardSize)
			{
				return false;
			}
			
			if (!boardSquares[r][c].isEmpty())
			{
				return false;
			}
		}
		
		// First piece: must cover corner
		if (!hasPlacedFirstPiece[currentPlayerIndex])
		{
			int[] corner = startingCorners[currentPlayerIndex];
			for (int[] square : shape)
			{
				int r = row + square[0];
				int c = col + square[1];
				
				if (r == corner[0] && c == corner[1])
				{
					return true;
				}
			}
			return false;
		}
		
		// Must touch corner, not edge
		boolean touchesCorner = false;
		
		for (int[] square : shape)
		{
			int r = row + square[0];
			int c = col + square[1];
			
			if (checkDiagonal(r-1, c-1, currentPlayer) ||
			    checkDiagonal(r-1, c+1, currentPlayer) ||
			    checkDiagonal(r+1, c-1, currentPlayer) ||
			    checkDiagonal(r+1, c+1, currentPlayer))
			{
				touchesCorner = true;
			}
		}
		
		if (!touchesCorner) return false;
		
		// Check no edge contact
		for (int[] square : shape)
		{
			int r = row + square[0];
			int c = col + square[1];
			
			if (checkSide(r-1, c, currentPlayer) ||
			    checkSide(r+1, c, currentPlayer) ||
			    checkSide(r, c-1, currentPlayer) ||
			    checkSide(r, c+1, currentPlayer))
			{
				return false;
			}
		}
		
		return true;
	}
	
	private boolean checkDiagonal(int r, int c, String player)
	{
		if (r < 0 || r >= boardSize || c < 0 || c >= boardSize) return false;
		return boardSquares[r][c].hasColor(player);
	}
	
	private boolean checkSide(int r, int c, String player)
	{
		if (r < 0 || r >= boardSize || c < 0 || c >= boardSize) return false;
		return boardSquares[r][c].hasColor(player);
	}
	
	private void setSquareColor(BoardSquare square, String player)
	{
		switch (player)
		{
			case "BLUE": square.setBlue(); break;
			case "YELLOW": square.setYellow(); break;
			case "RED": square.setRed(); break;
			case "GREEN": square.setGreen(); break;
		}
	}
	
	private void rotatePiece()
	{
		pieceRotation = (pieceRotation + 1) % 4;
		statusLabel.setText(currentPlayer + " - Rotated " + (pieceRotation * 90) + "°");
	}
	
	// ALL 21 PIECE SHAPES
	private int[][] getPieceShape()
	{
		int[][] baseShape;
		
		switch (selectedPieceType)
		{
			case "I1": baseShape = new int[][]{{0,0}}; break;
			case "I2": baseShape = new int[][]{{0,0},{0,1}}; break;
			case "I3": baseShape = new int[][]{{0,0},{0,1},{0,2}}; break;
			case "V3": baseShape = new int[][]{{0,0},{0,1},{1,0}}; break;
			case "I4": baseShape = new int[][]{{0,0},{0,1},{0,2},{0,3}}; break;
			case "O": baseShape = new int[][]{{0,0},{0,1},{1,0},{1,1}}; break;
			case "T": baseShape = new int[][]{{0,0},{0,1},{0,2},{1,1}}; break;
			case "L4": baseShape = new int[][]{{0,0},{1,0},{2,0},{2,1}}; break;
			case "Z4": baseShape = new int[][]{{0,0},{0,1},{1,1},{1,2}}; break;
			case "F": baseShape = new int[][]{{0,1},{0,2},{1,0},{1,1},{2,1}}; break;
			case "I5": baseShape = new int[][]{{0,0},{0,1},{0,2},{0,3},{0,4}}; break;
			case "L": baseShape = new int[][]{{0,0},{1,0},{2,0},{3,0},{3,1}}; break;
			case "N": baseShape = new int[][]{{0,1},{1,0},{1,1},{2,0},{3,0}}; break;
			case "P": baseShape = new int[][]{{0,0},{0,1},{1,0},{1,1},{2,0}}; break;
			case "T5": baseShape = new int[][]{{0,0},{0,1},{0,2},{1,1},{2,1}}; break;
			case "U": baseShape = new int[][]{{0,0},{0,2},{1,0},{1,1},{1,2}}; break;
			case "V": baseShape = new int[][]{{0,0},{1,0},{2,0},{2,1},{2,2}}; break;
			case "W": baseShape = new int[][]{{0,0},{1,0},{1,1},{2,1},{2,2}}; break;
			case "X": baseShape = new int[][]{{0,1},{1,0},{1,1},{1,2},{2,1}}; break;
			case "Y": baseShape = new int[][]{{0,1},{1,0},{1,1},{2,1},{3,1}}; break;
			case "Z": baseShape = new int[][]{{0,0},{0,1},{1,1},{2,1},{2,2}}; break;
			default: baseShape = new int[][]{{0,0}};
		}
		
		// Apply rotation
		int[][] rotated = new int[baseShape.length][2];
		for (int i = 0; i < baseShape.length; i++)
		{
			int r = baseShape[i][0];
			int c = baseShape[i][1];
			
			for (int rot = 0; rot < pieceRotation; rot++)
			{
				int newR = c;
				int newC = -r;
				r = newR;
				c = newC;
			}
			
			rotated[i] = new int[]{r, c};
		}
		
		// Normalize
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
}