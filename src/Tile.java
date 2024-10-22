package src;
/**
 * The Tile class represents a single Scrabble tile with a specific character.
 */
public class Tile {
    private final char tileChar;
    /**
     * Constructs a Tile with the specified character. The character is automatically
     * converted to uppercase.
     *
     * @param tile The character to be represented by this tile.
     */
    public Tile(char tile) {
        this.tileChar = Character.toUpperCase(tile);
    }
    /**
     * Returns the character represented by this tile.
     *
     * @return The character of this tile.
     */
    public char getTileChar() {
        return this.tileChar;
    }
    /**
     * Compares this tile with a character for equality.
     *
     * @param letter The character to compare to.
     * @return true if the tile's character matches the given character, false otherwise.
     */
    public boolean equals(char letter) {
        return Character.toUpperCase(letter) == Character.toUpperCase(this.tileChar);
    }
}
