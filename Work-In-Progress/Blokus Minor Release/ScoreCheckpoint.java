import java.io.*;

/**
 * Saves game data to disk just before scoring begins.
 * If the app crashes mid-scoring the file stays on disk.
 * On next launch, BlokusGame detects it and resumes scoring.
 *
 * ScoringSystem.java is not touched.
 * @author Group7
 */
public class ScoreCheckpoint
{
    private static final String FILE = "score_checkpoint.dat";

    /** Call this immediately before ScoringSystem.showResults(). */
    public static void save(boolean[][] piecesUsed,
                            boolean[]   lastPieceWasMono,
                            String[]    playerNames)
    {
        try (ObjectOutputStream out =
                 new ObjectOutputStream(new FileOutputStream(FILE)))
        {
            out.writeObject(piecesUsed);
            out.writeObject(lastPieceWasMono);
            out.writeObject(playerNames);
        }
        catch (IOException e)
        {
            System.err.println("ScoreCheckpoint: could not save (" + e.getMessage() + ")");
        }
    }

    /** Call this immediately after ScoringSystem.showResults() returns normally. */
    public static void delete()
    {
        new File(FILE).delete();
    }

    /**
     * Call once at startup (after the window is visible).
     * If a leftover checkpoint exists the player crashed mid-scoring last time,
     * so we reload the data and run the score dialog again.
     */
    public static void resumeIfPresent(BlokusGame parent)
    {
        File f = new File(FILE);
        if (!f.exists()) return;

        try (ObjectInputStream in =
                 new ObjectInputStream(new FileInputStream(f)))
        {
            boolean[][] piecesUsed      = (boolean[][]) in.readObject();
            boolean[]   lastPieceWasMono = (boolean[])  in.readObject();
            String[]    playerNames      = (String[])    in.readObject();

            // Delete before showing results so a second crash doesn't loop forever
            f.delete();

            javax.swing.JOptionPane.showMessageDialog(parent,
                "The app crashed during scoring last session.\n" +
                "Resuming score display now.",
                "Recovered", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            ScoringSystem.showResults(piecesUsed, lastPieceWasMono, playerNames, parent);
        }
        catch (Exception e)
        {
            System.err.println("ScoreCheckpoint: corrupt file, deleting (" + e.getMessage() + ")");
            f.delete();
        }
    }
}
