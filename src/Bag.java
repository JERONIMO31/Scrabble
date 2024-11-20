
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
        this.addTiles('a', 9);
        this.addTiles('b', 2);
        this.addTiles('c', 2);
        this.addTiles('d', 4);
        this.addTiles('e', 12);
        this.addTiles('f', 2);
        this.addTiles('g', 3);
        this.addTiles('h', 2);
        this.addTiles('i', 9);
        this.addTiles('j', 1);
        this.addTiles('k', 1);
        this.addTiles('l', 4);
        this.addTiles('m', 2);
        this.addTiles('n', 6);
        this.addTiles('o', 8);
        this.addTiles('p', 2);
        this.addTiles('q', 1);
        this.addTiles('r', 6);
        this.addTiles('s', 4);
        this.addTiles('t', 6);
        this.addTiles('u', 4);
        this.addTiles('v', 2);
        this.addTiles('w', 2);
        this.addTiles('x', 1);
        this.addTiles('y', 2);
        this.addTiles('z', 1);
        this.addTiles(' ', 100);
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
