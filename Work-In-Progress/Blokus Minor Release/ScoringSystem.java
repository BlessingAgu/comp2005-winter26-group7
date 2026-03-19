import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Handles end-game scoring for Blokus.
 * Pluggable: safe to add or remove without affecting other classes.
 * @author Group7
 */
public class ScoringSystem
{
    // Square count for each of the 21 pieces 
    private static final int[] PIECE_SIZES = {
        1, 2, 3, 3, 4, 4, 4, 4, 4,
        5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5
    };
    private static final int MONOMINO_INDEX = 0; // I1 at index 0

    // Calculate score per player
    public static int[] calculateScores(boolean[][] piecesUsed, boolean[] lastPieceWasMono)
    {
        if (piecesUsed == null || piecesUsed.length < 4)
            throw new IllegalArgumentException("Game data missing.");

        int[] scores = new int[4];
        for (int p = 0; p < 4; p++)
        {
            int unplaced = 0;
            boolean allPlaced = true;
            for (int i = 0; i < 21; i++)
            {
                if (!piecesUsed[p][i]) { unplaced += PIECE_SIZES[i]; allPlaced = false; }
            }
            scores[p] = -unplaced;
            if (allPlaced)
            {
                scores[p] += 15;
                if (lastPieceWasMono != null && lastPieceWasMono[p]) scores[p] += 5;
            }
        }
        return scores;
    }

    //Step 4: Determine winner(s); multiple = tie
    public static int[] determineWinners(int[] scores)
    {
        int max = Integer.MIN_VALUE;
        for (int s : scores) if (s > max) max = s;
        ArrayList<Integer> winners = new ArrayList<>();
        for (int i = 0; i < scores.length; i++) if (scores[i] == max) winners.add(i + 1);
        return winners.stream().mapToInt(x -> x).toArray();
    }

    
    public static void showResults(boolean[][] piecesUsed, boolean[] lastPieceWasMono,
                                   String[] playerNames, JFrame parent)
    {
        // Alt 2: error retrieving data
        int[] scores;
        try { scores = calculateScores(piecesUsed, lastPieceWasMono); }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(parent,
                "Scoring cannot be completed due to an error retrieving game data.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Alt 3: failure to display
        try { showScoreDialog(scores, determineWinners(scores), playerNames, parent); }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(parent,
                "Unable to display scores. The system encountered a problem.",
                "Display Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // display scores, announce winner/tie, new game / exit
    private static void showScoreDialog(int[] scores, int[] winners,
                                        String[] playerNames, JFrame parent)
    {
        JDialog dialog = new JDialog(parent, "Game Over - Final Scores", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // Score rows 
        JPanel scoresPanel = new JPanel(new GridLayout(4, 1, 4, 4));
        scoresPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        for (int p = 0; p < 4; p++)
        {
            Color bg = getPlayerColor(p + 1);
            Color fg = luminance(bg) > 0.5 ? Color.BLACK : Color.WHITE;

            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(bg);

            JLabel info = new JLabel("  " + name(playerNames, p) + ":  " + scores[p] + " pts");
            info.setFont(new Font("Arial", Font.PLAIN, 15));
            info.setBackground(bg); info.setForeground(fg); info.setOpaque(true);
            row.add(info, BorderLayout.CENTER);

            if (isWinner(p + 1, winners))
            {
                JLabel star = new JLabel("* WINNER  ", SwingConstants.RIGHT);
                star.setFont(new Font("Arial", Font.BOLD, 13));
                star.setBackground(bg); star.setForeground(fg); star.setOpaque(true);
                row.add(star, BorderLayout.EAST);
            }
            scoresPanel.add(row);
        }

        // Step 6: announce winner or tie (Alt 1)
        String announcement = winners.length > 1 ? buildTieMessage(winners, playerNames)
                                                  : name(playerNames, winners[0] - 1) + " wins!";
        JLabel label = new JLabel(announcement, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 17));
        label.setBorder(BorderFactory.createEmptyBorder(8, 10, 4, 10));

        // Step 7: new game / exit
        JButton newGame = new JButton("New Game");
        JButton exit    = new JButton("Exit");
        newGame.addActionListener(e -> { dialog.dispose(); ((BlokusGame) parent).startNewGame(); });
        exit.addActionListener(e -> System.exit(0));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        btns.add(newGame); btns.add(exit);

        JPanel south = new JPanel(new BorderLayout());
        south.add(label, BorderLayout.NORTH);
        south.add(btns, BorderLayout.SOUTH);

        dialog.add(scoresPanel, BorderLayout.CENTER);
        dialog.add(south, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(380, 260));
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

   

    private static String buildTieMessage(int[] winners, String[] names)
    {
        StringBuilder sb = new StringBuilder("Tie: ");
        for (int i = 0; i < winners.length; i++)
        { if (i > 0) sb.append(" & "); sb.append(name(names, winners[i] - 1)); }
        sb.append(" are joint winners!");
        return sb.toString();
    }

    private static String name(String[] names, int idx)
    {
        return (names != null && idx >= 0 && idx < names.length && names[idx] != null)
               ? names[idx] : "Player " + (idx + 1);
    }

    private static boolean isWinner(int p, int[] winners)
    { for (int w : winners) if (w == p) return true; return false; }

    // Colour-blind safe: delegates to active palette via BoardSquareColors
    private static Color getPlayerColor(int p)
    {
        try { Class.forName("BoardSquareColors"); return BoardSquareColors.getColorForPlayer(p); }
        catch (ClassNotFoundException e)
        {
            switch (p) {
                case 1: return Color.RED;  case 2: return Color.BLUE;
                case 3: return new Color(0,160,0); case 4: return new Color(180,160,0);
                default: return Color.GRAY;
            }
        }
    }

    private static double luminance(Color c)
    { return (0.299*c.getRed() + 0.587*c.getGreen() + 0.114*c.getBlue()) / 255.0; }
}
