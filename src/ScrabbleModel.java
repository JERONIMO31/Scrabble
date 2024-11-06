import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class ScrabbleModel {
    private static List<Player> players;
    private static Board board;
    private static Bag bag;
    private ScrabbleView view;
    private static int currentPlayerIndex;
    private static HashSet<String> wordSet;
    private static Boolean firstMove;

    /**
     * Constructor for ScrabbleModel.
     * Initializes the board, bag, players list, and loads valid words from a file.
     */
    public ScrabbleModel(ScrabbleView view) {
        board = new Board();
        bag = new Bag();
        players = new ArrayList<>();
        this.view = view;
        wordSet = new HashSet<>();
        this.loadWordsFromFile();
        firstMove = true;
        currentPlayerIndex = 0;
    }

    /**
     * Loads valid words from a file into a HashSet for fast lookup.
     * The words are expected to be in "src/scrabble.txt".
     */
    private void loadWordsFromFile() {
        File file = new File("src/scrabble.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        while (scanner.hasNextLine()) {
            wordSet.add(scanner.nextLine().trim()); // Add each word to the set after trimming whitespace
        }

        scanner.close();
    }

    /**
     * Checks if a given word exists in the set of valid Scrabble words.
     * @param word the word to be checked.
     * @return true if the word exists, false otherwise.
     */
    public boolean isWord(String word) {
        return wordSet.contains(word.toLowerCase());
    }

    /**
     * Adds a player to the game with the specified name.
     * @param name the name of the player to be added.
     */
    public void addPlayer(String name) {
        players.add(new Player(name, bag));
    }

    /**
     * Checks if a word can be placed on the board at the specified coordinates
     * in the given direction without violating any game rules.
     * @param x the starting x-coordinate of the word.
     * @param y the starting y-coordinate of the word.
     * @param direction the direction to place the word ('D' for down, 'R' for right).
     * @param word the word to be placed.
     * @return true if the word can be placed, false otherwise.
     */
    private boolean isPossible(int x, int y, char direction, String word) {
        HashMap<Character, Integer> charMap = new HashMap<>();
        int xIndex;
        int yIndex;

        // If it's not a valid word, return false
        if (!(isWord(word))) {
            return false;
        }

        Boolean isTouching = false; // Used to ensure that at least one tile is touching the existing placed tiles

        // Iterate over the characters in the word and check the placement on the board
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (direction == 'D') { // Moving down
                yIndex = y + i;
                xIndex = x;
            } else { // Moving right
                yIndex = y;
                xIndex = x + i;
            }
            if (board.isEmpty(xIndex, yIndex)) {
                // Count the occurrences of each character that need to be placed
                charMap.put(c, charMap.getOrDefault(c, 0) + 1);

                if (!board.isEmpty(xIndex + 1, yIndex) || !board.isEmpty(xIndex - 1, yIndex) ||
                        !board.isEmpty(xIndex, yIndex + 1)  || !board.isEmpty(xIndex, yIndex - 1) ){
                    isTouching = true;
                }

            } else if (!board.getTile(xIndex, yIndex).equals(c)) {
                return false;  // The board contains a different character at this position
            }
            else {isTouching = true;}
        }

        // Check if the player has enough of each character in their hand
        for (char c : charMap.keySet()) {
            if (charMap.get(c) > players.get(currentPlayerIndex).numInHand(c)) {
                return false;
            }
        }

        // Handle special case for the first move (must include the center tile)
        if (firstMove) {
            xIndex = x;
            yIndex = y;
            for (int i = 0; i < word.length(); i++) {
                if (xIndex == 7 && yIndex == 7) {
                    firstMove = false;
                    return true;
                }
                if (direction == 'D') {
                    yIndex = y + i;
                } else {
                    xIndex = x + i;
                }
            }
            return false;
        }
        return isTouching;
    }

    /**
     * Checks if a word is valid for placement on the board, considering game rules.
     * @param x the starting x-coordinate of the word.
     * @param y the starting y-coordinate of the word.
     * @param direction the direction to place the word ('D' for down, 'R' for right).
     * @param word the word to be placed.
     * @return true if the word can be placed, false otherwise.
     */
    private boolean isValid(int x, int y, char direction, String word) {
        // Check if the word can be placed
        if (!isPossible(x, y, direction, word)) {
            return false;
        }

        // Validate all affected words on the board
        List<String> effectedWords = getEffectedWords(x, y, direction, word);
        for (String effectedWord : effectedWords) {
            if (!isWord(effectedWord)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieves all words affected by placing a word on the board.
     * @param x the starting x-coordinate.
     * @param y the starting y-coordinate.
     * @param direction the direction of the word.
     * @param word the word being placed.
     * @return a list of affected words.
     */
    private List<String> getEffectedWords(int x, int y, char direction, String word) {
        int xIndex;
        int yIndex;
        List<String> wordList = new ArrayList<>();

        // For each character in the word, find new words formed by adjacent tiles
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (direction == 'D') {
                yIndex = y + i;
                xIndex = x;
                if (board.isEmpty(xIndex, yIndex)) {
                    wordList.add(getWord(xIndex, yIndex, 'R', c));
                }
            } else {
                yIndex = y;
                xIndex = x + i;
                if (board.isEmpty(xIndex, yIndex)) {
                    wordList.add(getWord(xIndex, yIndex, 'D', c));
                }
            }
        }
        return wordList;
    }

    /**
     * Retrieves the complete word formed in a given direction from the board.
     * @param x the x-coordinate of the starting position.
     * @param y the y-coordinate of the starting position.
     * @param direction the direction to search ('D' for down, 'R' for right).
     * @param c the character being added to the board.
     * @return the complete word as a string.
     */
    private String getWord(int x, int y, char direction, char c) {
        int xIndex;
        int yIndex;
        StringBuilder word = new StringBuilder(String.valueOf(c));

        // Search for adjacent tiles to complete the word in the specified direction
        if (direction == 'D') {
            yIndex = y + 1;
            xIndex = x;
            while (!board.isEmpty(xIndex, yIndex)) {
                word.append(board.getTile(xIndex, yIndex).getTileChar());
                yIndex++;
            }
            yIndex = y - 1;
            while (!board.isEmpty(xIndex, yIndex)) {
                word.insert(0, board.getTile(xIndex, yIndex).getTileChar());
                yIndex--;
            }
        } else {
            yIndex = y;
            xIndex = x + 1;
            while (!board.isEmpty(xIndex, yIndex)) {
                word.append(board.getTile(xIndex, yIndex).getTileChar());
                xIndex++;
            }
            xIndex = x - 1;
            while (!board.isEmpty(xIndex, yIndex)) {
                word.insert(0, board.getTile(xIndex, yIndex).getTileChar());
                xIndex--;
            }
        }
        return word.toString();
    }

    /**
     * Updates the current player's score based on the words they have formed.
     * @param effectedWords the list of words formed or affected.
     * @param word the main word that was placed.
     */
    private void updatePlayerScore(List<String> effectedWords, String word) {
        // Update score for each affected word
        for (String effectedWord : effectedWords) {
            for (char c : effectedWord.toCharArray()) {
                getCurrentPlayer().updateScore(c);
            }
        }
        // Update score for the main word
        for (char c : word.toCharArray()) {
            getCurrentPlayer().updateScore(c);
        }
    }

    /**
     * Makes a move by placing a word on the board if it is valid.
     * @param x the starting x-coordinate of the word.
     * @param y the starting y-coordinate of the word.
     * @param direction the direction to place the word ('D' for down, 'R' for right).
     * @param word the word to be placed.
     * @return true if the move is successful, false otherwise.
     */
    public boolean makeMove(int x, int y, char direction, String word) {
        word = word.toLowerCase();

        int xIndex;
        int yIndex;

        // Check if the move is valid
        if (!isValid(x, y, direction, word)) {
            return false;
        }

        // Place the word on the board and update the player's hand
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
                board.addLetter(xIndex, yIndex, getCurrentPlayer().popTile(c));
                getCurrentPlayer().refillHand();
            }
        }

        // Update the player's score and switch to the next player
        updatePlayerScore(getEffectedWords(x, y, direction, word), word);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        view.updateView();
        return true;
    }

    /**
     * Updates currentPlayerIndex
     */
    public void skip(){
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Retrieves the current player in the game.
     * @return the current player.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Retrieves the current board.
     * @return the current board.
     */
    public Board getBoard(){
        return board;
    }

    /**
     * Retrieves the player list.
     * @return the player list.
     */
    public static List<Player> getPlayers() {
        return players;
    }

    /**
     * Resets the game by reinitializing the board, bag, and players.
     */
    public void resetGame() {
        this.view.showEnd();
        bag = new Bag();
        firstMove = true;

        // Reset the players while retaining their names
        List<Player> holder = new ArrayList<>();
        for (Player player : players) {
            holder.add(new Player(player.getName(), bag));
        }
        players = holder;
        board = new Board();
        view.updateView();
    }

}
