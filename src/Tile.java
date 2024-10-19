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
     * Returns the score associated with a given Scrabble tile character based on
     * Scrabble scoring rules.
     *
     * @param tile The character of the tile to get the score for.
     * @return The score of the tile. Returns 1 for common letters (e.g., A, E, I),
     *         2 for D, G, 3 for B, C, M, P, etc., up to 10 for Q and Z.
     * @throws IllegalArgumentException If the tile character is not a valid Scrabble tile.
     */
    public int getTileScore(char tile) {
        if (tile == 'A'||tile == 'E'||tile == 'I'||tile == 'O'||tile == 'U'||tile == 'L'||tile == 'N'||tile == 'S'||tile == 'T'||tile == 'R') {
            return 1;
        }
        else if (tile == 'D'||tile == 'G') {
            return 2;
        }
        else if (tile == 'B'||tile == 'C'||tile == 'M'||tile == 'P') {
            return 3;
        }
        else if (tile == 'F'||tile == 'H'||tile == 'V'||tile == 'W'||tile == 'Y') {
            return 4;
        }
        else if (tile == 'K') {
            return 5;
        }
        else if (tile == 'J'||tile == 'X') {
            return 8;
        }
        else if (tile == 'Q'||tile == 'Z') {
            return 10;
        }
        else {
            throw new IllegalArgumentException("That is not a valid tile");
        }
    }

    /**
     * Returns the character represented by this tile.
     *
     * @return The character of this tile.
     */
    public char getTileChar() {
        return tileChar;
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
