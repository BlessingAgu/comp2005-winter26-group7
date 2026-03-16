
/**
 * 
 * @author Group7
 */
public class GameActions
{
    private static boolean hintsEnabled = false;
    private static String currentColorMode = "Normal";
    
    
     //Called by OptionsMenu when Apply is clicked
     //Toggles hints on/off

     // FOR FUTURE ITERATION, INVOLVING OPTIONSMENU
     
    // public static void toggleHints(boolean enabled)
    // {
    //     hintsEnabled = enabled;
    //     System.out.println("Hints " + (enabled ? "enabled" : "disabled"));
        
    //     // FOR FUTURE ITERATION: show valid move hints on board
    //     // For now, just store the setting
    // }
    
    
     //Called by OptionsMenu when color mode is selected
     //Updates the current color palette
    
    public static void colorMode(String paletteName)
    {
        currentColorMode = paletteName;
        System.out.println("Color mode changed to: " + paletteName);
        
        // Update BoardSquare color mappings
        BoardSquareColors.setColorMode(paletteName);
    }
    
    
    // Get current hints status (for later iteration)
    
    public static boolean areHintsEnabled()
    {
        return hintsEnabled;
    }
    
    
     //Get current color mode
     
    public static String getColorMode()
    {
        return currentColorMode;
    }
}