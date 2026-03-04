package gui;
import java.awt.Color;
import java.util.ArrayList;

public class ColorPalette {
    
    // all palette hex codes in one place. each row is one palette, each column is a player color (Red, Blue, Green, Yellow)
    private String[][] palettes = {
        {"#FF0000", "#0000FF", "#00FF00", "#FFFF00"},  // Normal
        {"#E1AD01", "#006592", "#707070", "#F8E71C"},  // Deuteranopia
        {"#9B870C", "#0052A5", "#585858", "#F5E027"},  // Protanopia
        {"#F31D64", "#006E72", "#33636B", "#D8D8D8"},  // Tritanopia
        {"#A0A0A0", "#606060", "#404040", "#D0D0D0"}   // Greyscale
    };

    // the names in the same order as the rows above so we can match them
    private String[] paletteNames = {"Normal", "Deuteranopia", "Protanopia", "Tritanopia", "Greyscale"};

    private ArrayList<Color> currentPalette = new ArrayList<>(); // stores the current active colors

    // finds the palette by name and converts the hex codes into real Java colors
    public void changePalette(String name) {
        currentPalette.clear(); // wipe the old colors first
        for (int i = 0; i < paletteNames.length; i++) {
            if (paletteNames[i].equals(name)) {
                for (String hex : palettes[i]) {
                    currentPalette.add(Color.decode(hex));
                }
            }
        }
    }

    // returns the whole list of colors for the current palette
    public ArrayList<Color> getCurrentPalette() {
        return currentPalette;
    }

    // returns a single player's color by index (0 = player 1, 1 = player 2, etc.)
    public Color getColor(int index) {
        return currentPalette.get(index);
    }

    // returns the hex codes for a given palette name so OptionsMenu can display the squares
    public String[] getHexCodes(String name) {
        for (int i = 0; i < paletteNames.length; i++) {
            if (paletteNames[i].equals(name)) {
                return palettes[i];
            }
        }
        return palettes[0]; // default to Normal if nothing matches
    }

    // returns all palette names so OptionsMenu can build the rows
    public String[] getPaletteNames() {
        return paletteNames;
    }
}
