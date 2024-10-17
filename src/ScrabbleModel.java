package src;
import java.util.*;

public class ScrabbleModel {
    private static List<Player> players;
    private static Board board;
    private static Bag bag;
    private ScrabbleView view;
    private ScrabbleController controller;
    private static int currentPlayerIndex;
    private static boolean finished;

    public ScrabbleModel(){
        board = new Board();
        bag = new Bag();
        players = new ArrayList<>();
        view = new ScrabbleView();
        controller = new ScrabbleController(this, view);
    }

    public void addPlayer(String name){
        players.add(new Player(name, bag));
    }

    public boolean makeMove(int x, int y, char direction, String word){
        HashMap<Character, Integer> charMap = new HashMap<>();
        int xIndex;
        int yIndex;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (direction == 'D'){
                yIndex = y + i;
                xIndex = x;
            } else {
                yIndex = y;
                xIndex = x + i;
            }
            if (board.isEmpty(xIndex, yIndex)){
                if (charMap.containsKey(c)) {
                    charMap.put(c, charMap.get(c) + 1);
                } else {
                    charMap.put(c, 1);
                }
            } else if (!board.getTile(xIndex, yIndex).equals(c)) {
                return false;
            }
        }
        for (char c : charMap.keySet()){
            if (charMap.get(c) > players.get(currentPlayerIndex).numInHand(c)){
                return false;
            }
        }
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (direction == 'D') {
                yIndex = y + i;
                xIndex = x;
            } else {
                yIndex = y;
                xIndex = x + i;
            }
            if (board.isEmpty(xIndex, yIndex)) {
                board.addLetter(xIndex, yIndex, players.get(currentPlayerIndex).popTile(c));
            }
        }
        return true;
    }

    public void quit(){
        view.showEnd(board, players);
        finished = true;
    }

    public void resetGame(){
        bag = new Bag();
        List<Player> holder = new ArrayList<>();
        for (Player player : players){
            holder.add(new Player(player.getName(), bag));
        }
        players = holder;
        board = new Board();
        quit();
        play();
    }

    public void play(){
        finished = false;
        currentPlayerIndex = 0;
        while (!finished){
            view.showBoard(board, players.get(currentPlayerIndex), bag, players);
            controller.getCommand();
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    public static void main(String[] args){
        ScrabbleModel game = new ScrabbleModel();
        game.controller.startGame();
    }
}
