
import java.awt.Color;


/**
 * 
 * @author Group7
 */
public class BoardSquareColors {

    private static ColorPalette colorPalette = new ColorPalette();

    static {
    colorPalette.changePalette("Normal"); // load Normal palette by default
    }
    
    
    
    
     // Set the current color mode
     
    public static void setColorMode(String mode) {
    colorPalette.changePalette(mode);
    }
    
    
     //Get color for a specific player (1-4)
    
    public static Color getColorForPlayer(int playerNumber) {
    return colorPalette.getColor(playerNumber - 1);
    }
    
    
     //Get light version of color for preview
     
    public static Color getLightColorForPlayer(int playerNumber)
    {
        Color base = getColorForPlayer(playerNumber);
        
        // Make it lighter (add white)
        int r = Math.min(255, base.getRed() + 80);
        int g = Math.min(255, base.getGreen() + 80);
        int b = Math.min(255, base.getBlue() + 80);
        
        return new Color(r, g, b);
    }
    
}