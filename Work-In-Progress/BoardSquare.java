package gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.*;

/**
 * BoardSquare - Works standalone or with BoardSquareColors
 * @author CWellington
 */
public class BoardSquare extends JButton
{
	private int row, col;
	private int occupiedBy; // 0 = empty, 1-4 = player number
	private Color currentColor;

	public BoardSquare(int row, int col)
	{
		super();
		this.row = row;
		this.col = col;
		this.occupiedBy = 0;
		this.currentColor = Color.WHITE;
		
		this.setOpaque(true);
		this.setBorderPainted(true);
		this.setFocusPainted(false);
		this.setContentAreaFilled(true);
		this.setBackground(Color.WHITE);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(currentColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}
	
	public void setEmpty()
	{
		this.occupiedBy = 0;
		this.currentColor = Color.WHITE;
		setBackground(Color.WHITE);
		repaint();
	}
	
	public void setPlayer(int playerNumber)
	{
		this.occupiedBy = playerNumber;
		
		// Try to use BoardSquareColors if available, otherwise use default colors
		try {
			Class.forName("gui.BoardSquareColors");
			this.currentColor = BoardSquareColors.getColorForPlayer(playerNumber);
		} catch (ClassNotFoundException e) {
			// BoardSquareColors not available - use default colors
			this.currentColor = getDefaultColor(playerNumber);
		}
		
		setBackground(currentColor);
		setForeground(Color.WHITE);
		repaint();
	}
	
	// Default colors if BoardSquareColors is not present
	private Color getDefaultColor(int playerNumber)
	{
		switch (playerNumber)
		{
			case 1: return Color.RED;
			case 2: return Color.BLUE;
			case 3: return Color.GREEN;
			case 4: return Color.YELLOW;
			default: return Color.WHITE;
		}
	}
	
	// OPTIONAL FEATURE: Refresh color when palette changes
	public void refreshColor()
	{
		if (occupiedBy > 0)
		{
			try {
				Class.forName("gui.BoardSquareColors");
				this.currentColor = BoardSquareColors.getColorForPlayer(occupiedBy);
				setBackground(currentColor);
				repaint();
			} catch (ClassNotFoundException e) {
				// BoardSquareColors not available - keep current color
			}
		}
	}
	
	public boolean isEmpty()
	{
		return occupiedBy == 0;
	}
	
	public boolean hasPlayer(int playerNumber)
	{
		return occupiedBy == playerNumber;
	}

	public void setRow(int value)    { row = value; }
	public void setCol(int value)    { col = value; }
	public int getRow()              { return row; }
	public int getCol()              { return col; }
	public int getOccupiedBy()       { return occupiedBy; }
}
