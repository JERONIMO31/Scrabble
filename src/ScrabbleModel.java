package src;
import java.util.*;

/**
 * The ScrabbleModel class is the core game logic for a Scrabble game.
 * It maintains the state of the game, including the board, the bag of tiles,
 * the players, and controls the flow of the game.
 */
public class ScrabbleModel {
    private static List<Player> players;
    private static Board board;
    private static Bag bag;
    private ScrabbleView view;
    private ScrabbleController controller;
    private static int currentPlayerIndex;
    private static boolean finished;

    /**
     * Constructs a new ScrabbleModel, initializing the board, bag of tiles, players list,
     * and setting up the view and controller.
     */
    public ScrabbleModel(){
        board = new Board();
        bag = new Bag();
        players = new ArrayList<>();
        view = new ScrabbleView();
        controller = new ScrabbleController(this, view);
    }

    /**
     * Adds a player to the game.
     *
     * @param name The name of the player to add.
     */
    public void addPlayer(String name){
        players.add(new Player(name, bag));
    }

    /**
     * Attempts to make a move by placing a word on the board.
     *
     * @param x         The starting x-coordinate on the board.
     * @param y         The starting y-coordinate on the board.
     * @param direction The direction of the word ('D' for down, 'R' for right).
     * @param word      The word to be placed on the board.
     * @return true if the move is valid and successful, false otherwise.
     */
    public boolean makeMove(int x, int y, char direction, String word){
        HashMap<Character, Integer> charMap = new HashMap<>();
        int xIndex;
        int yIndex;

        // Iterate over each character in the word to check if the move is valid.
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            // Determine the x and y coordinates of the tile based on the direction of the word.
            if (direction == 'D'){ // 'D' means down (vertical)
                yIndex = y + i;
                xIndex = x;
            } else {               // 'R' means right (horizontal)
                yIndex = y;
                xIndex = x + i;
            }

            // If the tile space is empty, track how many tiles of this character are needed from the player's hand.
            if (board.isEmpty(xIndex, yIndex)){
                if (charMap.containsKey(c)) {
                    charMap.put(c, charMap.get(c) + 1);
                } else {
                    charMap.put(c, 1);
                }
            }
            // If the space is not empty, check if the existing tile matches the current character in the word.
            else if (!board.getTile(xIndex, yIndex).equals(c)) {
                return false; // If the existing tile does not match, the move is invalid.
            }
        }

        // Check if the player has enough tiles in their hand to complete the move.
        for (char c : charMap.keySet()){
            if (charMap.get(c) > players.get(currentPlayerIndex).numInHand(c)){
                return false; // If the player does not have enough tiles of a certain character, the move is invalid.
            }
        }

        // Place the tiles on the board.
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            // Determine the x and y coordinates again, same as before.
            if (direction == 'D') {
                yIndex = y + i;
                xIndex = x;
            } else {
                yIndex = y;
                xIndex = x + i;
            }
            // If the space is empty, place the tile from the player's hand onto the board.
            if (board.isEmpty(xIndex, yIndex)) {
                board.addLetter(xIndex, yIndex, players.get(currentPlayerIndex).popTile(c));
                players.get(currentPlayerIndex).refillHand();
            }
        }
        // If all tiles are placed successfully
        return true;
    }

    /**
     * Ends the game, displays the final board state, and marks the game as finished.
     */
    public void quit(){
        view.showEnd(board, players);
        finished = true;
    }

    /**
     * Resets the game by creating a new bag of tiles, resetting the board,
     * and reinitializing all players. It then starts a new game.
     */
    public void resetGame(){
        bag = new Bag();
        List<Player> holder = new ArrayList<>();
        for (Player player : players){
            holder.add(new Player(player.getName(), bag));
        }
        players = holder;         // Save the players
        board = new Board();
        quit();
        play();                   // Restart
    }

    /**
     * Starts and runs the Scrabble game loop. The game continues until it is marked as finished.
     */
    public void play(){
        finished = false;
        currentPlayerIndex = 0;

        // Gameplay loop
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
