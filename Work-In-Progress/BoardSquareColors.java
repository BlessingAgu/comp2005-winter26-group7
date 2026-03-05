package gui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * BoardSquareColors - Manages color palettes for accessibility
 * Stores the hex codes from OptionsMenu and provides Color objects
 * @author CWellington
 */
public class BoardSquareColors
{
    private static String currentMode = "Normal";
    
    // Color palettes matching OptionsMenu exactly
    // Format: Mode -> Player# -> Color
    private static Map<String, Color[]> palettes = new HashMap<>();
    
    static
    {
        // Normal: Red, Blue, Green, Yellow
        palettes.put("Normal", new Color[]{
            Color.decode("#FF0000"),  // Player 1 (was Blue, now Red per their code)
            Color.decode("#0000FF"),  // Player 2 (was Yellow, now Blue)
            Color.decode("#00FF00"),  // Player 3 (was Red, now Green)
            Color.decode("#FFFF00")   // Player 4 (was Green, now Yellow)
        });
        
        // Deuteranopia (red-green colorblind)
        palettes.put("Deuteranopia", new Color[]{
            Color.decode("#E1AD01"),
            Color.decode("#006592"),
            Color.decode("#707070"),
            Color.decode("#F8E71C")
        });
        
        // Protanopia (red colorblind)
        palettes.put("Protanopia", new Color[]{
            Color.decode("#9B870C"),
            Color.decode("#0052A5"),
            Color.decode("#585858"),
            Color.decode("#F5E027")
        });
        
        // Tritanopia (blue-yellow colorblind)
        palettes.put("Tritanopia", new Color[]{
            Color.decode("#F31D64"),
            Color.decode("#006E72"),
            Color.decode("#33636B"),
            Color.decode("#D8D8D8")
        });
        
        // Greyscale
        palettes.put("Greyscale", new Color[]{
            Color.decode("#A0A0A0"),
            Color.decode("#606060"),
            Color.decode("#404040"),
            Color.decode("#D0D0D0")
        });
    }
    
    /**
     * Set the current color mode
     * Called by GameActions.colorMode()
     */
    public static void setColorMode(String mode)
    {
        if (palettes.containsKey(mode))
        {
            currentMode = mode;
        }
        else
        {
            System.out.println("Warning: Unknown color mode '" + mode + "', using Normal");
            currentMode = "Normal";
        }
    }
    
    /**
     * Get color for a specific player (1-4)
     */
    public static Color getColorForPlayer(int playerNumber)
    {
        Color[] colors = palettes.get(currentMode);
        if (playerNumber >= 1 && playerNumber <= 4)
        {
            return colors[playerNumber - 1];
        }
        return Color.WHITE;  // Default for empty squares
    }
    
    /**
     * Get light version of color for preview
     */
    public static Color getLightColorForPlayer(int playerNumber)
    {
        Color base = getColorForPlayer(playerNumber);
        
        // Make it lighter (add white)
        int r = Math.min(255, base.getRed() + 80);
        int g = Math.min(255, base.getGreen() + 80);
        int b = Math.min(255, base.getBlue() + 80);
        
        return new Color(r, g, b);
    }
    
    /**
     * Get current mode name
     */
    public static String getCurrentMode()
    {
        return currentMode;
    }
}
