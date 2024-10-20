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
    public void UpdateScore(char letter){
        myScore += getTileScore(letter);
        popTile(letter);
        refillHand();
    }
    private void makeHand() {
        for (int i = 0; i < 7; i++) {
            hand.add(bag.drawTile());
        }
    }

    public void refillHand() {
        int value = 7 - hand.size();
        while (value > 0 && bag.getTileCount() > 0) {
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
            if (hand.get(i).equals(c)) {
                return hand.remove(i);
            }
        }
        return null;
    }
    public Tile Indextile(char c){
        for (int i = 0 ; i < hand.size()-1 ; i++){
            if (hand.get(i).equals(c)){
                return hand.get(i);
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

    public String getHandView() {
        String handStr = "Value:";
        for (Tile tile : hand){
            handStr += " " + getTileScore(tile.getTileChar())+ " ";
        }
        handStr += "\nTiles:";
        for (Tile tile : hand) {
            handStr += "[" + tile.getTileChar() + "]";
        }
        return handStr;
    }
    public int getTileScore(char tile) {
        if (tile == 'A'||tile == 'E'||tile == 'I'||tile == 'O'||tile == 'U'||tile == 'L'||tile == 'N'||tile == 'S'||tile == 'T'||tile == 'R'|| tile == 'a'||tile == 'e'||tile == 'i'||tile == 'o'||tile == 'u'||tile == 'l'||tile == 'n'||tile == 's'||tile == 't'||tile == 'r') {
            return 1;
        }
        else if (tile == 'D'||tile == 'G'||tile == 'd'||tile == 'g') {
            return 2;
        }
        else if (tile == 'B'||tile == 'C'||tile == 'M'||tile == 'P'||tile == 'b'||tile == 'c'||tile == 'm'||tile == 'p') {
            return 3;
        }
        else if (tile == 'F'||tile == 'H'||tile == 'V'||tile == 'W'||tile == 'Y'||tile == 'f'||tile == 'h'||tile == 'v'||tile == 'w'||tile == 'y') {
            return 4;
        }
        else if (tile == 'K'||tile == 'k') {
            return 5;
        }
        else if (tile == 'J'||tile == 'X'||tile == 'j'||tile == 'x') {
            return 8;
        }
        else if (tile == 'Q'||tile == 'Z'||tile == 'q'||tile == 'z') {
            return 10;
        }
        else {
            throw new IllegalArgumentException("That is not a valid tile");
        }
    }
}
