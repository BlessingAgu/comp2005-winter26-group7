

/**
 * 
 * @author Group7
 */
public class GameUI
{
    private static BlokusGame gameWindow;
    
    
     //Set the game window reference
     //Call this when BlokusGame is created
     
    public static void setGameWindow(BlokusGame window)
    {
        gameWindow = window;
    }
    
    
     //Called by OptionsMenu when "Apply Settings" is clicked
     //Updates all board square colors based on selected palette
     
    public static void updateButtonColours()
    {
        if (gameWindow != null)
        {
            gameWindow.refreshBoardColors();
            System.out.println("Board colors updated!");
        }
        else
        {
            System.out.println("Warning: GameWindow not set yet");
        }
    }
}