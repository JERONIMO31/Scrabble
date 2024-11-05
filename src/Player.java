import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player {
    private final String name;
    private List<Tile> hand;
    private int myScore;
    private Bag bag;

    public Player(String name, Bag bag) {
        this.name = name;
        this.bag = bag;
        this.hand = new ArrayList();
        this.myScore = 0;
        this.makeHand();
    }

    private void makeHand() {
        for(int i = 0; i < 7; ++i) {
            this.hand.add(this.bag.drawTile());
        }

    }

    public void refillHand() {
        for(int value = 7 - this.hand.size(); value > 0 && this.bag.getTileCount() > 0; --value) {
            this.hand.add(this.bag.drawTile());
        }

    }

    public int numInHand(char c) {
        int num = 0;
        Iterator var3 = this.hand.iterator();

        while(var3.hasNext()) {
            Tile tile = (Tile)var3.next();
            if (tile.equals(c)) {
                ++num;
            }
        }

        return num;
    }

    public Tile popTile(char c) {
        for(int i = 0; i < this.hand.size(); ++i) {
            if (((Tile)this.hand.get(i)).equals(c)) {
                return (Tile)this.hand.remove(i);
            }
        }

        return null;
    }

    public Tile Indextile(char c) {
        for(int i = 0; i < this.hand.size() - 1; ++i) {
            if (((Tile)this.hand.get(i)).equals(c)) {
                return (Tile)this.hand.get(i);
            }
        }

        return null;
    }

    public void UpdateScore(char letter) {
        this.myScore += this.getTileScore(letter);
        this.popTile(letter);
        this.refillHand();
    }

    public int getScore() {
        return this.myScore;
    }

    public String getName() {
        return this.name;
    }

    public String getHandView() {
        String handStr = "Value:";

        Iterator var2;
        Tile tile;
        for(var2 = this.hand.iterator(); var2.hasNext(); handStr = handStr + "[" + this.getTileScore(tile.getTileChar()) + "]") {
            tile = (Tile)var2.next();
        }

        handStr = handStr + "\nTiles:";

        for(var2 = this.hand.iterator(); var2.hasNext(); handStr = handStr + "[" + tile.getTileChar() + "]") {
            tile = (Tile)var2.next();
        }

        return handStr;
    }

    public int getTileScore(char tile) {
        if (tile != 'A' && tile != 'E' && tile != 'I' && tile != 'O' && tile != 'U' && tile != 'L' && tile != 'N' && tile != 'S' && tile != 'T' && tile != 'R' && tile != 'a' && tile != 'e' && tile != 'i' && tile != 'o' && tile != 'u' && tile != 'l' && tile != 'n' && tile != 's' && tile != 't' && tile != 'r') {
            if (tile != 'D' && tile != 'G' && tile != 'd' && tile != 'g') {
                if (tile != 'B' && tile != 'C' && tile != 'M' && tile != 'P' && tile != 'b' && tile != 'c' && tile != 'm' && tile != 'p') {
                    if (tile != 'F' && tile != 'H' && tile != 'V' && tile != 'W' && tile != 'Y' && tile != 'f' && tile != 'h' && tile != 'v' && tile != 'w' && tile != 'y') {
                        if (tile != 'K' && tile != 'k') {
                            if (tile != 'J' && tile != 'X' && tile != 'j' && tile != 'x') {
                                if (tile != 'Q' && tile != 'Z' && tile != 'q' && tile != 'z') {
                                    throw new IllegalArgumentException("That is not a valid tile");
                                } else {
                                    return 10;
                                }
                            } else {
                                return 8;
                            }
                        } else {
                            return 5;
                        }
                    } else {
                        return 4;
                    }
                } else {
                    return 3;
                }
            } else {
                return 2;
            }
        } else {
            return 1;
        }
    }

    public int Handsize() {
        return this.hand.size();
    }
}
