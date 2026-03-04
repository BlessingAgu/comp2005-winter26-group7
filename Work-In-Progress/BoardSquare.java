import java.awt.Color;
import java.awt.Graphics;
import javax.swing.*;

/*
 *  BoardSquare - A single square on the Blokus board
 *  Based on GridSquare by mhatcher
 *  @author Wellington
 */
public class BoardSquare extends JButton
{
	private int row, col;     // Location of this square on board
	private String occupiedBy; // null, "BLUE", "YELLOW", "RED", or "GREEN"
	private Color currentColor; // ADDED: Store current color

	// Constructor takes the row and column coordinates
	public BoardSquare(int row, int col)
	{
		super();
		this.row = row;
		this.col = col;
		this.occupiedBy = null;
		this.currentColor = Color.WHITE; // ADDED
		
		this.setOpaque(true);
		this.setBorderPainted(true);
		this.setFocusPainted(false);
		this.setContentAreaFilled(true);
		this.setBackground(Color.WHITE);
	}
	
	// Show colors on the board
	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(currentColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}
	
	// Reset to empty white square
	public void setEmpty()
	{
		this.occupiedBy = null;
		this.currentColor = Color.WHITE; 
		setBackground(Color.WHITE);
		repaint(); 
	}
	
	// Set to Blue player color
	public void setBlue()
	{
		this.occupiedBy = "BLUE";
		this.currentColor = Color.BLUE; 
		setBackground(Color.BLUE);
		setForeground(Color.WHITE);
		repaint(); 
	}

	// Set to Yellow player color
	public void setYellow()
	{
		this.occupiedBy = "YELLOW";
		this.currentColor = Color.YELLOW; 
		setBackground(Color.YELLOW);
		setForeground(Color.BLACK);
		repaint(); 
	}

	// Set to Red player color
	public void setRed()
	{
		this.occupiedBy = "RED";
		this.currentColor = Color.RED; 
		setBackground(Color.RED);
		setForeground(Color.WHITE);
		repaint();
	}

	// Set to Green player color
	public void setGreen()
	{
		this.occupiedBy = "GREEN";
		this.currentColor = new Color(0, 200, 0); 
		setBackground(new Color(0, 200, 0));
		setForeground(Color.WHITE);
		repaint(); 
	}
	
	// Check if square is empty
	public boolean isEmpty()
	{
		return occupiedBy == null;
	}
	
	// Check if square has specific color
	public boolean hasColor(String color)
	{
		return color != null && color.equals(occupiedBy);
	}

	// Getters and setters
	public void setRow(int value)    { row = value; }
	public void setCol(int value)    { col = value; }
	public int getRow()              { return row; }
	public int getCol()              { return col; }
	public String getOccupiedBy()    { return occupiedBy; }
}