package src;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Bag class represents a bag of tiles in a Scrabble game. It contains all the tiles
 * available in the game, shuffles them upon creation, and allows players to draw tiles
 * during gameplay.
 */
public class Bag {
    private static List<Tile> tiles;

    /**
     * Constructs a new Bag object and initializes the set of tiles with their respective
     * counts. The tiles are shuffled after initialization.
     */
    public Bag() {
        tiles = new ArrayList<>();
        initializeTiles();
        Collections.shuffle(tiles); // Shuffle the tiles when the bag is created
    }

    /**
     * Initializes the bag with the standard set of Scrabble tiles and their respective
     * counts. Each tile is added to the list of tiles based on its frequency.
     */
    private void initializeTiles() {
        addTiles('A', 9);
        addTiles('B', 2);
        addTiles('C', 2);
        addTiles('D', 4);
        addTiles('E', 12);
        addTiles('F', 2);
        addTiles('G', 3);
        addTiles('H', 2);
        addTiles('I', 9);
        addTiles('J', 1);
        addTiles('K', 1);
        addTiles('L', 4);
        addTiles('M', 2);
        addTiles('N', 6);
        addTiles('O', 8);
        addTiles('P', 2);
        addTiles('Q', 1);
        addTiles('R', 6);
        addTiles('S', 4);
        addTiles('T', 6);
        addTiles('U', 4);
        addTiles('V', 2);
        addTiles('W', 2);
        addTiles('X', 1);
        addTiles('Y', 2);
        addTiles('Z', 1);
    }

    /**
     * Adds a specified number of tiles with the given character to the bag.
     *
     * @param letter The character of the tile to add.
     * @param count  The number of tiles to add for this character.
     */
    private void addTiles(char letter, int count) {
        for (int i = 0; i < count; i++) {
            tiles.add(new Tile(letter));
        }
    }

    /**
     * Draws a tile from the top of the bag, and removes and returns the tile.
     *
     * @return The drawn Tile object.
     * @throws IllegalStateException if the bag is empty and no tiles can be drawn.
     */
    public Tile drawTile() {
        if (tiles.isEmpty()) {
            throw new IllegalStateException("No tiles left in the bag");
        }
        return tiles.removeLast(); // Draw from the top
    }

    /**
     * Returns the current number of tiles remaining in the bag.
     *
     * @return The number of tiles left in the bag.
     */
    public int getTileCount() {
        return tiles.size();
    }
}
