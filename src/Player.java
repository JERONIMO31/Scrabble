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
        makeHand();
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
        while (value > 0 || bag.getTileCount() > 0) {
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
        String handStr = "";
        for (Tile tile : hand) {
            handStr += "[" + tile.getTileChar() + "]";
        }
        return handStr;
    }
}
