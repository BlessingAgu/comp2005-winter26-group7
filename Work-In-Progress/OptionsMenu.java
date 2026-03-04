package gui;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OptionsMenu extends JFrame {

	private String currentPalette = "Normal"; // tracking the current selected palette
	private JCheckBox hintBox; // moved this up here so the 'Apply' button can check if it's ticked
	private ColorPalette colorPalette = new ColorPalette(); // stores the current palette colors so other classes can access them

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
        // asking ColorPalette for the names and hex codes instead of storing them here
        for (String name : colorPalette.getPaletteNames()) {
            paletteFrame.add(createPaletteRow(name, colorPalette.getHexCodes(name)));
        }

        
        // button at the end of the color palette rows to confirm selection
        JButton applyBtn = new JButton("Apply Settings");
        applyBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        applyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        applyBtn.addActionListener(e -> {

            // tell ColorPalette which palette was selected, it handles the hex codes itself now
            colorPalette.changePalette(currentPalette);

            // CONNECTS TO TEAM CODE (other classes)
            
            // GameActions.toggleHints(hintBox.isSelected());
            // GameActions.colorMode(currentPalette);
            // GameUI.updateButtonColours();
            
            // FOR FUTURE ITERATION
            // GameData.saveGame();

            JOptionPane.showMessageDialog(paletteFrame, "Settings Applied!");
            paletteFrame.dispose(); // close the color window, but keep the main options window open
        });
        
        paletteFrame.add(Box.createRigidArea(new Dimension(0, 10))); // just a bit of spacing so it's not touching the last row
        paletteFrame.add(applyBtn);

        paletteFrame.setLocationRelativeTo(this); // center this new window on top of the first one
        paletteFrame.setVisible(true);
    }

    // this creates a single row with the 4 squares of colors and the name button
    private JPanel createPaletteRow(String name, String[] colors) {
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
                if (c instanceof JPanel && ((JPanel) c).getName() != null) {
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

    // returns the ColorPalette object so other classes like SetupAGame can read the current colors
    public ColorPalette getColorPalette() {
        return colorPalette;
    }
    

}
