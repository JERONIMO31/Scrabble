
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bag {
    private static List<Tile> tiles;

    public Bag() {
        tiles = new ArrayList<>();
        this.initializeTiles();
        Collections.shuffle(tiles);
    }
    public Bag(int a){
        tiles = new ArrayList<>();
        this.initializeTiles();
        tiles.add(new Tile('h'));
        tiles.add(new Tile('e'));
        tiles.add(new Tile('l'));
        tiles.add(new Tile('l'));
        tiles.add(new Tile('o'));
        tiles.add(new Tile('b'));
        tiles.add(new Tile('t'));
    }

    private void initializeTiles() {
        this.addTiles('A', 9);
        this.addTiles('B', 2);
        this.addTiles('C', 2);
        this.addTiles('D', 4);
        this.addTiles('E', 12);
        this.addTiles('F', 2);
        this.addTiles('G', 3);
        this.addTiles('H', 2);
        this.addTiles('I', 9);
        this.addTiles('J', 1);
        this.addTiles('K', 1);
        this.addTiles('L', 4);
        this.addTiles('M', 2);
        this.addTiles('N', 6);
        this.addTiles('O', 8);
        this.addTiles('P', 2);
        this.addTiles('Q', 1);
        this.addTiles('R', 6);
        this.addTiles('S', 4);
        this.addTiles('T', 6);
        this.addTiles('U', 4);
        this.addTiles('V', 2);
        this.addTiles('W', 2);
        this.addTiles('X', 1);
        this.addTiles('Y', 2);
        this.addTiles('Z', 1);
    }

    private void addTiles(char letter, int count) {
        for(int i = 0; i < count; ++i) {
            tiles.add(new Tile(letter));
        }

    }

    public Tile drawTile() {
        if (tiles.isEmpty()) {
            throw new IllegalStateException("No tiles left in the bag");
        } else {
            return (Tile)tiles.removeLast();
        }
    }

    public int getTileCount() {
        return tiles.size();
    }

    public boolean ItContains(String word) {
        for(int i = 0; i < word.length(); ++i) {
            if (!tiles.contains(word.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
