# Blokus – Group 7

A digital implementation of the board game Blokus, built in Java using Swing.  
Supports 2–4 players (human or computer), save/load, colour-blind modes, and computer AI.

---

## How to Run

1. Make sure all `.java` files are in the same folder, along with `blokus.png`
2. Compile everything:
   ```
   javac *.java
   ```
3. Run the main class:
   ```
   java BlokusMain
   ```

---

## How to Play

1. From the **Main Menu**, click **Start Game**
2. On the **Setup Screen**, choose how many players (2–4) and whether each is Human or Computer
3. Click **Start Game** to begin

**On your turn:**
- Select a piece from the list on the left
- Use **Rotate** or **Flip** to orient it
- Hover over the board to preview where it will land
- Click a square to place it
- If you cannot place anything, click **Pass Turn**

**Blokus rules:**
- Your first piece must touch your assigned corner of the board
- Every piece after that must touch a corner of one of your own pieces (diagonal contact only — no edge-to-edge contact with your own colour)
- You cannot place pieces on top of or edge-adjacent to your own existing pieces
- The game ends when all players pass consecutively
- The player with the fewest unplaced squares wins

---

## File Overview

| File | What it does |
|---|---|
| `BlokusMain.java` | Entry point — launches the main menu |
| `MainMenuUi.java` | Main menu screen with Start, Load, Rules, and Exit buttons |
| `setUpGame.java` | Setup screen — choose number of players, types, and colours |
| `BlokusGame.java` | Core game logic: board, piece placement, turn management, win detection |
| `BoardSquare.java` | Represents one square on the 20x20 board |
| `BoardSquareColors.java` | Maps player numbers to colours using the active palette |
| `ColorPalette.java` | Stores all 5 colour palettes (normal + 4 accessibility modes) |
| `ComputerPlayer.java` | AI logic for Easy and Hard difficulty |
| `Player.java` | Represents a player (human or computer) |
| `GameActions.java` | Handles colour mode and hint settings from the options menu |
| `GameUI.java` | Connects the options menu changes to the live game board |
| `OptionsMenu.java` | In-game options: hints toggle and colour mode selector |
| `ScoringSystem.java` | Calculates and displays final scores, handles ties |
| `ScoreCheckpoint.java` | Saves scoring data so the app can recover if it crashes mid-scoring |
| `SaveLoadManager.java` | Saves and loads full game state to/from disk |
| `GameSaveState.java` | The serializable data object written to disk during save |

---

## Features

### 2–4 Player Support
- 2 players: each controls 2 colours on the same board
- 3 players: one player controls 2 colours
- 4 players: one colour each
- Colours can be reassigned per player in the setup screen before the game starts

### Computer AI
- **Easy**: tries the smallest available piece first, places it at a random valid position
- **Hard**: tries the largest available piece first, places it at a random valid position
- Both difficulty levels auto-pass if no valid move exists so the game always continues

### Save and Load
- Click **Save Game** at any time during a game to write progress to `blokus_save.dat`
- Click **Load Game** from the main menu or in-game to resume a saved session
- If the app crashes during scoring, `ScoreCheckpoint` saves the scoring data and automatically recovers it on the next launch

### Colour Vision Accessibility
Five colour palettes are available under **Options → Select Color Mode**:

| Mode | Description |
|---|---|
| Mode 1 | Default (Red, Blue, Green, Yellow) |
| Mode 2 | Deuteranopia-friendly |
| Mode 3 | Protanopia-friendly |
| Mode 4 | Tritanopia-friendly |
| Mode 5 | Greyscale |

Changing the palette updates the board live without restarting the game.

### Gameplay Hints
The **Options** menu includes a hints toggle. When enabled, the hint bar at the top of the screen provides guidance on what the current player should do next.

### Piece Preview
Hovering over any board square shows a preview of where the selected piece would land. A green tint means the placement is valid; a red tint means it is not.

### Scoring
At the end of the game, each player loses points equal to the number of squares in their unplaced pieces. Bonuses apply:
- **+15** if all 21 pieces were placed
- **+5** additionally if the last piece placed was the single square monomino

In 2 or 3 player games, each player's score is the sum of their colour scores combined.

---

## Design Notes

### Design Patterns Used

**Strategy Pattern** — `Player` is the base class. `ComputerPlayer` extends it and swaps in a different move strategy (`easyMove` vs `hardMove`) depending on the selected difficulty. The game calls `takeTurn()` without needing to know which strategy is active.

**Observer Pattern** — Java's `ActionListener` interface is used throughout. Board squares, piece buttons, and control buttons all notify `BlokusGame` when interacted with. The game responds to events without the UI components needing any knowledge of game logic.

**State Pattern** — `GameSaveState` captures a complete snapshot of the game at any point in time. The entire game can be fully restored from this object, which also enables the crash recovery feature.

**Facade Pattern** — `GameActions` and `GameUI` act as simple entry points for the options menu to affect the running game. Rather than the options menu reaching directly into `BlokusGame` or `BoardSquareColors`, it calls a single method and the facade handles propagating the change.

### Networking Considerations
The architecture is structured so that future networking support would be straightforward to add:

- `GameSaveState` is already `Serializable`, meaning it can be transmitted over a network socket just as easily as it is written to disk
- Game logic lives in `BlokusGame` independently from the UI, so a server could run the game headlessly and send state updates to remote clients
- `ComputerPlayer` takes its turn through a single `takeTurn()` call, which could be replaced by a network message without changing any surrounding code
- Turn management is centralised — `currentPlayer` advances in one place, making it straightforward to insert a network sync step between turns
