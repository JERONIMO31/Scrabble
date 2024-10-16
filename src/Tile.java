package src;

public class Tile {
    private final char tileChar;

    public Tile(char tile) {
        this.tileChar = Character.toUpperCase(tile);
    }

    public Tile() {
        this.tileChar = 0;
    }

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

    public char getTileChar() {
        return tileChar;
    }

    public boolean isEmpty() {
        return tileChar == 0;
    }

    public boolean equals(Tile tile) {
        return Character.toUpperCase(tile.getTileChar()) == Character.toUpperCase(this.tileChar);
    }
}
