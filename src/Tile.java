import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.*;
public class Tile {
    private final char tileChar;

    public Tile(char tile) {
        this.tileChar = Character.toUpperCase(tile);
    }

    public char getTileChar() {
        return this.tileChar;
    }

    public static int getTileScore(char tile) {
        if (tile == 'a'||tile == 'e'||tile == 'i'||tile == 'o'||tile == 'u'||tile == 'l'||tile == 'n'||tile == 's'||tile == 't'||tile == 'r') {
            return 1;
        }
        else if (tile == 'd'||tile == 'g') {
            return 2;
        }
        else if (tile == 'b'||tile == 'c'||tile == 'm'||tile == 'p') {
            return 3;
        }
        else if (tile == 'f'||tile == 'h'||tile == 'v'||tile == 'w'||tile == 'y') {
            return 4;
        }
        else if (tile == 'k') {
            return 5;
        }
        else if (tile == 'j'||tile == 'x') {
            return 8;
        }
        else if (tile == 'q'||tile == 'z') {
            return 10;
        }
        else {
            System.out.println(tile);
            throw new IllegalArgumentException("That is not a valid tile");
        }
    }

    public boolean equals(char letter) {
        return Character.toUpperCase(letter) == Character.toUpperCase(this.tileChar);
    }
}
