package gui;
import javax.swing.*;
import java.awt.*;

public class OptionsMenu extends JFrame {

    private String currentPalette = "Normal"; // tracking the current selected palette
    private JCheckBox hintBox; // moved this up here so the 'Apply' button can check if it's ticked

    public OptionsMenu() {
        // the first window the user sees after clicking Options
        setTitle("Blokus Options");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // using dispose so it doesn't close the whole game
        setLayout(new GridLayout(2, 1, 10, 10)); // splits the window into two big rows for Hints and the Color Mode button

        // HINT SECTION
        // creating the box for the hints using FlowLayout.CENTER so the checkbox doesn't hug the left side
        JPanel hintPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
        hintPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // black border around the hints box
        JLabel hintLabel = new JLabel("Enable Gameplay Hints");
        hintLabel.setFont(new Font("Arial", Font.PLAIN, 18)); 
        
        hintBox = new JCheckBox(); // initializing the checkbox so we can save its state later
        hintPanel.add(hintLabel);
        hintPanel.add(hintBox);
        add(hintPanel);

        // COLOR MODE BUTTON
        // this is the main button that triggers the second window
        JButton btnSelectColor = new JButton("Select Color Mode");
        btnSelectColor.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSelectColor.addActionListener(e -> openPaletteWindow()); // when clicked, run the method to open the color window
        add(btnSelectColor);

        setLocationRelativeTo(null); // puts it right in the middle of the screen
        setVisible(true);
    }
    
    // this opens the second window (when Select Color Mode is clicked) where the player can pick their favorite color palette
    private void openPaletteWindow() {
        JFrame paletteFrame = new JFrame("Choose Color Mode");
        paletteFrame.setSize(600, 750);
        paletteFrame.setLayout(new BoxLayout(paletteFrame.getContentPane(), BoxLayout.Y_AXIS)); // stack the rows on top of each other

        // adding each row: c1 is Red, c2 is Blue, c3 is Green, c4 is Yellow
        // specific Hex codes to match the accessibility modes exactly
        // NOTE FOR FUTURE ITERATION: making this its own class?
        paletteFrame.add(createPaletteRow("Normal", "#FF0000", "#0000FF", "#00FF00", "#FFFF00"));
        paletteFrame.add(createPaletteRow("Deuteranopia", "#E1AD01", "#006592", "#707070", "#F8E71C"));
        paletteFrame.add(createPaletteRow("Protanopia", "#9B870C", "#0052A5", "#585858", "#F5E027"));
        paletteFrame.add(createPaletteRow("Tritanopia", "#F31D64", "#006E72", "#33636B", "#D8D8D8"));
        paletteFrame.add(createPaletteRow("Greyscale", "#A0A0A0", "#606060", "#404040", "#D0D0D0"));
        
        // button at the end of the color palette rows to confirm selection
        JButton applyBtn = new JButton("Apply Settings");
        applyBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        applyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        applyBtn.addActionListener(e -> {
            
	        // CONNECTS TO TEAM CODE (other classes)
	        	
	        // this tells GameActions to toggle the hints based on what we clicked in the first window
	        GameActions.toggleHints(hintBox.isSelected()); 
	            
	        // this tells the game logic which color palette we want to use
	        GameActions.colorMode(currentPalette);
	            
	        // this is the main UI call that will change the 20x20 board colors
	        GameUI.updateButtonColours();
	            
	        // saving the settings so they stick when the game is loaded next time (for next iteration, not done yet)
	        // FOR FUTURE ITERATION
	        //GameData.saveGame(); 
	
	        // when button is clicked, pop up message to confirm user the settings have been applied
	        JOptionPane.showMessageDialog(paletteFrame, "Settings Applied!"); 
	        paletteFrame.dispose(); // close the color window, but keep the main options window open
        });
        
        paletteFrame.add(Box.createRigidArea(new Dimension(0, 10))); // just a bit of spacing so it's not touching the last row
        paletteFrame.add(applyBtn);

        paletteFrame.setLocationRelativeTo(this); // center this new window on top of the first one
        paletteFrame.setVisible(true);
    }
    
    // this creates a single row with the 4 squares of colors (and the name button just in case)
    private JPanel createPaletteRow(String name, String c1, String c2, String c3, String c4) {
        JPanel container = new JPanel(new BorderLayout());
        // container wraps the whole row so we can highlight the background light blue when selected
        container.setName(name); 
        
        // initializing the colors. activeBlue is a light blue highlight to show a color palette row is selected to the user (otherwise it is white)
        Color activeBlue = new Color(210, 235, 255); 
        container.setBackground(currentPalette.equals(name) ? activeBlue : Color.WHITE);
        container.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // the row that holds the squares, made transparent so the blue/white background shows through
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        row.setOpaque(false); 
        
        // making the squares of colors using the hex codes, big enough to be able to see
        // NEXT ITERATION: change to an ArrayList once ColourPalette class is made for flexibility
        String[] colors = {c1, c2, c3, c4};
        for (String hex : colors) {
            JPanel square = new JPanel();
            square.setPreferredSize(new Dimension(60, 60));
            square.setBackground(Color.decode(hex)); // converts the hex string into a real Java color
            square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            row.add(square);
        }

        JButton selectBtn = new JButton(name);
        selectBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        
        selectBtn.addActionListener(e -> {
            currentPalette = name; // updates the current palette to the new selected one
            
            // this loop goes through every row in the window and updates the colors instantly
            Container parent = container.getParent(); 
            for (Component c : parent.getComponents()) {
                if (c instanceof JPanel && ((JPanel)c).getName() != null) {
                    JPanel p = (JPanel) c;
                    if (name.equals(p.getName())) {
                        p.setBackground(activeBlue); // highlight the one we just clicked
                    } else {
                        p.setBackground(Color.WHITE); // make all the others white again
                    }
                }
            }
            parent.repaint(); // updates the screen because it was flickering without
        });
        
        row.add(selectBtn);
        container.add(row, BorderLayout.CENTER);
        return container;
    }

}
