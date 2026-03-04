public class Player {

    private Colour playerColour;
    private String playerName;

    public Player(Colour playerColour, String playerName) {
        this.playerColour = playerColour;
        this.playerName = playerName;
    }

    public void setPlayerColour(Colour colour) {
        this.playerColour = colour;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public Colour getPlayerColour() {
        return playerColour;
    }

    public String getPlayerName() {
        return playerName;
    }
}