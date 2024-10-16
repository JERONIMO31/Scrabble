package src;
import java.util.*;

public class Player {
    private final String name;
    private List<Tile> hand;
    private int myScore;
    private static Bag bag;

    public Player(String name, Bag bag) {
        this.name = name;
        this.bag = bag;

    }

    private void makeHand() {

    }

    private void refillHand() {
        int value = 7 - hand.size();
        while (value > 0 || bag.getTileCount() > 0) {
            hand.add(bag.drawTile());
            value--;
        }
    }
}
