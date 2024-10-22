package src;
import java.util.*;
/**
 * The Player class represents a player in the Scrabble game.
 */
public class Player {
    private final String name;
    private List<Tile> hand;
    private int myScore;
    private Bag bag;
    /**
     * Constructs a Player with the specified name and initializes their hand with tiles
     * drawn from the game's tile bag.
     *
     * @param name The name of the player.
     * @param bag  The bag of tiles from which the player draws tiles.
     */
    public Player(String name, Bag bag) {
        this.name = name;
        this.bag = bag;
        this.hand = new ArrayList<>();
        myScore = 0;
        this.makeHand();
    }
    /**
     * Fills the player's hand with 7 tiles by drawing from the bag.
     * This is called when the player is created.
     */
    private void makeHand() {
        for (int i = 0; i < 7; i++) {
            hand.add(bag.drawTile());
        }
    }
    /**
     * Refills the player's hand up to 7 tiles by drawing from the bag.
     * If the bag is empty, nothing happens.
     */
    public void refillHand() {
        int value = 7 - hand.size();
        while (value > 0 && bag.getTileCount() > 0) {
            hand.add(bag.drawTile());
            value--;
        }
    }
    /**
     * Counts the number of tiles in the player's hand that match the specified character.
     *
     * @param c The character to count in the player's hand.
     * @return The number of tiles in the player's hand that match the character.
     */
    public int numInHand(char c){
        int num = 0;
        for (Tile tile : hand){
            if (tile.equals(c)){
                num++;
            }
        }
        return num;
    }
    /**
     * Removes and returns the first tile in the player's hand that matches the specified character.
     *
     * @param c The character of the tile to remove from the player's hand.
     * @return The tile that was removed, or null if no matching tile is found.
     */
    public Tile popTile (char c) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).equals(c)) {
                return hand.remove(i);
            }
        }
        return null;
    }

    /**
     * returns the tile that first matches the chracter given
     * @param c the character given
     * @return null if c doesnt match and the tile if it matches
     */
    public Tile Indextile(char c) {
        for(int i = 0; i < this.hand.size() - 1; ++i) {
            if (((Tile)this.hand.get(i)).equals(c)) {
                return (Tile)this.hand.get(i);
            }
        }

        return null;
    }

    /**
     * Updates the players score and replaces the tile that is used
     * @param letter the character matching the tile the player used
     */
    public void UpdateScore(char letter) {
        this.myScore += this.getTileScore(letter);
        this.popTile(letter);
        this.refillHand();
    }
    /**
     * Gets the current score of the player.
     *
     * @return The player's current score.
     */
    public int getScore() {
        return this.myScore;
    }
    /**
     * Gets the name of the player.
     *
     * @return The player's name.
     */
    public String getName() {
        return this.name;
    }
    /**
     * Provides a string representation of the player's hand.
     *
     * @return A string displaying the player's hand, with each tile enclosed in brackets.
     */
    public String getHandView() {
        String handStr = "Value:";
        for (Tile tile : hand) {
            handStr += "[" + getTileScore(tile.getTileChar()) + "]";
        }
        handStr += "\nTiles:";
        for (Tile tile : hand) {
            handStr += "[" + tile.getTileChar() + "]";
        }
        return handStr;
    }
    /**
     * Returns the score associated with a given Scrabble tile character based on
     * Scrabble scoring rules.
     *
     * @param tile The character of the tile to get the score for.
     * @return The score of the tile. Returns 1 for common letters (e.g., A, E, I),
     *         2 for D, G, 3 for B, C, M, P, etc., up to 10 for Q and Z.
     * @throws IllegalArgumentException If the tile character is not a valid Scrabble tile.
     */
    public int getTileScore(char tile) {
        if (tile != 'A' && tile != 'E' && tile != 'I' && tile != 'O' && tile != 'U' && tile != 'L' && tile != 'N' && tile != 'S' && tile != 'T' && tile != 'R' && tile != 'a' && tile != 'e' && tile != 'i' && tile != 'o' && tile != 'u' && tile != 'l' && tile != 'n' && tile != 's' && tile != 't' && tile != 'r') {
            if (tile != 'D' && tile != 'G' && tile != 'd' && tile != 'g') {
                if (tile != 'B' && tile != 'C' && tile != 'M' && tile != 'P' && tile != 'b' && tile != 'c' && tile != 'm' && tile != 'p') {
                    if (tile != 'F' && tile != 'H' && tile != 'V' && tile != 'W' && tile != 'Y' && tile != 'f' && tile != 'h' && tile != 'v' && tile != 'w' && tile != 'y') {
                        if (tile != 'K' && tile != 'k') {
                            if (tile != 'J' && tile != 'X' && tile != 'j' && tile != 'x') {
                                if (tile != 'Q' && tile != 'Z' && tile != 'q' && tile != 'z') {
                                    throw new IllegalArgumentException("That is not a valid tile");
                                } else {
                                    return 10;
                                }
                            } else {
                                return 8;
                            }
                        } else {
                            return 5;
                        }
                    } else {
                        return 4;
                    }
                } else {
                    return 3;
                }
            } else {
                return 2;
            }
        } else {
            return 1;
        }
    }

    public int Handsize() {
        return this.hand.size();
    }
}

