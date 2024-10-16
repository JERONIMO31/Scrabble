package src;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bag {
    private final List<Tile> tiles;

    public Bag() {
        tiles = new ArrayList<>();
        initializeTiles();
        Collections.shuffle(tiles); // Shuffle the tiles when the bag is created
    }

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

    // Method to add a specific number of tiles to the bag
    private void addTiles(char letter, int count) {
        for (int i = 0; i < count; i++) {
            tiles.add(new Tile(letter));
        }
    }

    // Method to draw a tile from the bag
    public Tile drawTile() {
        if (tiles.isEmpty()) {
            throw new IllegalStateException("No tiles left in the bag");
        }
        return tiles.removeLast(); // Draw from the top
    }

    // Method to get the current number of tiles in the bag
    public int getTileCount() {
        return tiles.size();
    }
}
