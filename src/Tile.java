
public class Tile {
    private final char tileChar;

    public Tile(char tile) {
        this.tileChar = Character.toUpperCase(tile);
    }

    public char getTileChar() {
        return this.tileChar;
    }

    public boolean equals(char letter) {
        return Character.toUpperCase(letter) == Character.toUpperCase(this.tileChar);
    }
}
