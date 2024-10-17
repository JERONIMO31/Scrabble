package src;
import java.util.*;

public class Player {
    private final String name;
    private List<Tile> hand;
    private int myScore;
    private Bag bag;

    public Player(String name, Bag bag) {
        this.name = name;
        this.bag = bag;
        this.hand = new ArrayList<>();
        makeHand();
    }

    private void makeHand() {
        for (int i = 0; i < 7; i++) {
            hand.add(bag.drawTile());
        }
    }

    private void refillHand() {
        int value = 7 - hand.size();
        while (value > 0 || bag.getTileCount() > 0) {
            hand.add(bag.drawTile());
            value--;
        }
    }

    public void addToScore(int score) {
        this.myScore += score;
    }

    public int getScore() {
        return this.myScore;
    }

    public List<Tile> getHand() {
        return this.hand;
    }

    public String getName() {
        return this.name;
    }

    pubic void placeTile() {

    }
}
