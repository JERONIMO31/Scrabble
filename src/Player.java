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
        myScore = 0;
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

    public int numInHand(char c){
        int num = 0;
        for (Tile tile : hand){
            if (tile.equals(c)){
                num++;
            }
        }
        return num;
    }

    public Tile popTile (char c) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getTileChar() == c) {
                return hand.remove(i);
            }
        }
        return null;
    }

    public void addToScore(int score) {
        this.myScore += score;
    }

    public int getScore() {
        return this.myScore;
    }

    public String getName() {
        return this.name;
    }

    public String getHand() {
        String handStr = "";
        for (Tile tile : hand) {
            handStr += "[" + tile.getTileChar() + "]";
        }
        return handStr;
    }
}
