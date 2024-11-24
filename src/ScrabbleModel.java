import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ScrabbleModel {
    private static List<Player> players;
    private static Board board;
    private static Bag bag;
    private final ScrabbleView view;
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
     * Constructor for ScrabbleModel with no view for test cases.
     * Initializes the board, bag, players list, and loads valid words from a file.
     */
    public ScrabbleModel() {
        board = new Board();
        bag = new Bag(0);
        players = new ArrayList<>();
        this.view = null;
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
        Scanner scanner;
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
    public boolean isWord(List<Tile> word) {
        StringBuilder stringWord = new StringBuilder();
        for (Tile tile : word){
            stringWord.append(tile.getTileChar());
        }
        return wordSet.contains(stringWord.toString().toLowerCase());
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
    public boolean isPossible(int x, int y, char direction, List<Tile> word) {
        HashMap<Character, Integer> charMap = new HashMap<>();
        int xIndex;
        int yIndex;

        // If it's not a valid word, return false
        if (!(isWord(word))) {
            return false;
        }

        boolean isTouching = false; // Used to ensure that at least one tile is touching the existing placed tiles

        // Iterate over the characters in the word and check the placement on the board
        for (int i = 0; i < word.size(); i++) {
            Tile c = word.get(i);
            if (direction == 'D') { // Moving down
                yIndex = y + i;
                xIndex = x;
            } else { // Moving right
                yIndex = y;
                xIndex = x + i;
            }
            if (board.isEmpty(xIndex, yIndex)) {
                // Count the occurrences of each character that need to be placed
                charMap.put(c.getTileChar(), charMap.getOrDefault(c.getTileChar(), 0) + 1);

                // Check adjacent tiles
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
            for (int i = 0; i < word.size(); i++) {
                if (direction == 'D') {
                    yIndex = y + i;
                } else {
                    xIndex = x + i;
                }
                if (xIndex == 7 && yIndex == 7) {
                    firstMove = false;
                    return true;
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
    public boolean isValid(int x, int y, char direction, List<Tile> word) {
        // Check if the word can be placed
        if (!isPossible(x, y, direction, word)) {
            return false;
        }

        // Validate all affected words on the board
        List<List<Tile>> effectedWords = getEffectedWords(x, y, direction, word);
        for (List<Tile> effectedWord : effectedWords) {
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
    private List<List<Tile>> getEffectedWords(int x, int y, char direction, List<Tile> word) {
        int xIndex;
        int yIndex;
        List<List<Tile>> wordList = new ArrayList<>();

        // For each character in the word, find new words formed by adjacent tiles
        for (int i = 0; i < word.size(); i++) {
            Tile c = word.get(i);
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
    private List<Tile> getWord(int x, int y, char direction, Tile c) {
        int xIndex;
        int yIndex;
        List<Tile> word = new ArrayList<>();
        word.add(c);

        // Search for adjacent tiles to complete the word in the specified direction
        if (direction == 'D') {
            yIndex = y + 1; // Search downward
            xIndex = x;
            while (!board.isEmpty(xIndex, yIndex)) {
                // Add tiles below the starting position to the word
                word.add(board.getTile(xIndex, yIndex));
                yIndex++;
            }
            yIndex = y - 1; // Search upward
            while (!board.isEmpty(xIndex, yIndex)) {
                // Add tiles above the starting position to the word
                word.addFirst(board.getTile(xIndex, yIndex));
                yIndex--;
            }
        } else {
            yIndex = y;
            xIndex = x + 1; // Search to the right
            while (!board.isEmpty(xIndex, yIndex)) {
                // Add tiles to the right of the starting position to the word
                word.add(board.getTile(xIndex, yIndex));
                xIndex++;
            }
            xIndex = x - 1; // Search to the left
            while (!board.isEmpty(xIndex, yIndex)) {
                // Add tiles to the left of the starting position to the word
                word.addFirst(board.getTile(xIndex, yIndex));
                xIndex--;
            }
        }

        return word;
    }

    /**
     * Scores a word and updates the current player's score.
     * The score is calculated based on the tiles in the word, an additional base score,
     * and a multiplier applied to the total score.
     *
     * @param word The word to score, represented as a list of tiles.
     * @param multiplier The multiplier to apply to the word's total score (e.g., for double/triple word scores).
     * @param additionalScore An additional base score to include in the calculation (e.g., bonus points).
     */
    private void scoreWord(List<Tile> word, int multiplier, int additionalScore) {
        int score = additionalScore;
        for (Tile c : word) {
            score += Tile.getTileScore(c);
        }
        if (word.size() > 1){
        getCurrentPlayer().updateScore(score*multiplier);}
    }

    /**
     * Updates the current player's score by adding a specified value.
     *
     * @param score The value to add to the current player's total score.
     */
    private void updatePlayerScore(int score){
        getCurrentPlayer().updateScore(score);
    }

    /**
     * Makes a move by placing a word on the board if it is valid.
     * @param x the starting x-coordinate of the word.
     * @param y the starting y-coordinate of the word.
     * @param direction the direction to place the word ('D' for down, 'R' for right).
     * @param word the word to be placed.
     * @return true if the move is successful, false otherwise.
     */
    public boolean makeMove(int x, int y, char direction, List<Tile> word) {

        int xIndex;
        int yIndex;

        // Determine the opposite direction for scoring perpendicular words
        char oppositeDirection = direction == 'D' ? 'R': 'D';

        int multiplier = 1;
        int wordScore = 0;

        // Check if the move is valid
        if (!isValid(x, y, direction, word)) {
            return false;
        }

        // Place the word on the board and update the player's hand
        for (int i = 0; i < word.size(); i++) {
            Tile c = word.get(i); // Current tile to place
            if (direction == 'D') {
                yIndex = y + i; // Move down for vertical placement
                xIndex = x;
            } else {
                yIndex = y;
                xIndex = x + i; // Move right for horizontal placement
            }
            if (board.isEmpty(xIndex, yIndex)) {
                // if empty, place the tile
                Tile tile = getCurrentPlayer().popTile(c);
                board.addLetter(xIndex, yIndex, tile);
                getCurrentPlayer().refillHand();

                // Check for special score multipliers at the location
                String m = board.getMultiplier(xIndex, yIndex);
                if (!m.equals("normal")){
                    switch (m){
                        // Apply scoring rules based on multiplier type
                        case "DL" -> {
                            wordScore += 2 * Tile.getTileScore(tile);
                            scoreWord(getWord(xIndex, yIndex, oppositeDirection, tile), 1, Tile.getTileScore(tile));
                        }
                        case "TL" -> {
                            wordScore += 3 * Tile.getTileScore(tile);
                            scoreWord(getWord(xIndex, yIndex, oppositeDirection, tile), 1, 2 * Tile.getTileScore(tile));
                        }
                        case "DW" -> {
                            multiplier = multiplier * 2;
                            wordScore += Tile.getTileScore(tile);
                            scoreWord(getWord(xIndex, yIndex, oppositeDirection, tile), 2, 0);
                        }
                        case "TW" -> {
                            multiplier = multiplier * 3;
                            wordScore += Tile.getTileScore(tile);
                            scoreWord(getWord(xIndex, yIndex, oppositeDirection, tile), 3, 0);
                        }
                    }
                } else {
                    // No special multiplier; add the tile's base score
                    wordScore += Tile.getTileScore(tile);
                    scoreWord(getWord(xIndex, yIndex, oppositeDirection, tile), 1, 0);
                }
            } else {
                // The board cell is already occupied; add the score of the existing tile
                wordScore += Tile.getTileScore(board.getTile(xIndex, yIndex));
            }
        }

        // Update the player's score and switch to the next player

        updatePlayerScore(wordScore*multiplier);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        if (view != null) {
            view.updateView();}
        return true;
    }

    /**
     * Updates currentPlayerIndex
     */
    public void skip(){
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        if (view != null) {
            view.updateView();
        }
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
        if (view != null) {
            this.view.showEnd();
        }
        bag = new Bag();
        firstMove = true;

        // Reset the players while retaining their names
        List<Player> holder = new ArrayList<>();
        for (Player player : players) {
            holder.add(new Player(player.getName(), bag));
        }
        players = holder;
        board = new Board();
        if (view != null) {
            view.updateView();
        }
    }

}
