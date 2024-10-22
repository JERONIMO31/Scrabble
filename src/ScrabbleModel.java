package src;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ScrabbleModel {
    private static List<Player> players;
    private static Board board;
    private static Bag bag;
    private ScrabbleView view;
    private ScrabbleController controller;
    private static int currentPlayerIndex;
    private static boolean finished, first;
    private static HashSet<String> wordSet;

    public ScrabbleModel() throws FileNotFoundException {
        board = new Board();
        bag = new Bag();
        players = new ArrayList<>();
        view = new ScrabbleView();
        controller = new ScrabbleController(this, view);
        wordSet = new HashSet<>();
        loadWordsFromFile();
    }

    /**
     * loads the scrabble words list into a Hashset
     *
     */
    private void loadWordsFromFile() throws FileNotFoundException { //load the word list into a Hashset
        File file = new File("scrabble.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            wordSet.add(scanner.nextLine().trim());  // Add each word to the HashSet
        }
        scanner.close();
    }
    /**
     * checks to see if words is in Word list
     *
     * @param word Word to be checked validity
     * @return returns true if the word is on the word list
     */
    public boolean containsWord(String word) { //check if word is in set

        return wordSet.contains(word.toLowerCase());
    }

    /**
     * Adjust coordinates to the first letter of the connected word.
     *
     * @param x The initial x coordinate
     * @param y The initial y coordinate
     * @param target the x or y coordinate in which the word is connected to the other
     * @param isX Tells whether, or not to adjust x or y
     * @return returns the adjusted coordinate to the first value of the word
     */
    private int adjustCoordinate(int x, int y, int target, boolean isX) {
        while (!board.isEmpty(x, y) || (isX ? x == target : y == target)) {
            if ((isX ? x : y) > 1) {  // Ensure the coordinate stays valid (greater than 1)
                if (isX) x--;
                else y--;
            } else {
                break;  // Break to avoid infinite loop
            }
        }
        return isX ? x + 1 : y + 1;  // Increment the adjusted coordinate
    }


    public void addPlayer(String name){
        players.add(new Player(name, bag));
    }

    public boolean makeMove(int x, int y, char direction, String word){
        HashMap<Character, Integer> charMap = new HashMap<>();
        int xIndex;
        int yIndex;
        boolean connected = false; //variable to check if the word is connected to another
        int[][] directions; // directions for which way is need to check for other words
        int[][] connections = new int[word.length()][2];
        int paircount = 0; // variable to track amount added to connections


        if(direction == 'D') { //set directions for checking word attachments
            directions = new int[][] {{1, 0}, {-1, 0}};
        }
        else{
            directions = new int[][]{{0, 1},{0, -1}};
        }
        if (!containsWord(word)) {
            return false;
        }

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
            else{ //if not false and not empty meaning word is connected
                connected = true;
            }

            for (int[] ints : directions) { //check each letter to see if touching another word
                int newX = xIndex + ints[0];
                int newY = yIndex + ints[1];

                // Check bounds and if connected to something add to array
                if (newX >= 1 && newX <= 15 && newY >= 1 && newY <= 15 && !board.isEmpty(newX, newY)) {
                    connected = true;
                    connections[paircount][0] = newX;
                    connections[paircount][1] = newY;
                    break;  // No need to continue checking once connected
                }
            }
            paircount++;

        }

        for (int i = 0; i < connections.length; i++) {  // Loop through to find the top of each connected word
            if (connections[i][0] != 0) {
                // Update newX or newY based on the direction
                if (direction == 'D') {
                    connections[i][0] = adjustCoordinate(connections[i][0] , connections[i][1] , x, true);
                } else {
                    connections[i][1] = adjustCoordinate(connections[i][0] , connections[i][1] , y, false);
                }
            }
        }

        for (int i = 0; i < connections.length; i++) { // Loop to create the connected word from starting point and check validity
            if (connections[i][0] != 0) {
                int newX = connections[i][0];
                int newY = connections[i][1];
                StringBuilder wordCon = new StringBuilder();

                // Build the word directly in the main method
                while ((direction == 'D' && (!board.isEmpty(newX, newY) || (newX == x))) ||
                        (direction != 'D' && (!board.isEmpty(newX, newY) || (newY == y)))) {

                    if ((direction == 'D' && newX != x) || (direction != 'D' && newY != y)) {
                        char tileChar = board.getTile(newX, newY).getTileChar();
                        wordCon.append(tileChar);
                    } else if ((direction == 'D' && newY + 1 == 16) || (direction != 'D' && newX + 1 == 16)) {
                        break;
                    } else {
                        wordCon.append(word.charAt(i));
                    }

                    if (direction == 'D') {
                        newX++;
                    } else {
                        newY++;
                    }
                }

                // Check if the built word is valid
                if (!containsWord(wordCon.toString())) {
                    return false;
                }
            }
        }

        if(!connected && !first){ //returns false if move is not connected doesn't matter if first move
            return false;
        }

        for (char c : charMap.keySet()){
            if (charMap.get(c) > players.get(currentPlayerIndex).numInHand(c)){
                return false;
            }
        }

        if (first){ // if first move must touch middle tile
            if(direction == 'D'){
                if(!((x == 8)&&(y <= 8 && 8 <=y+word.length()))){ //checks to see if first move doesn't touch middle
                    return false;
                }
            }
            else{
                if(!((y == 8)&&(x <= 8 && 8 <= x+word.length()))){ //checks to see if first move doesn't touch middle
                    return false;
                }
            }
            first = false;
        } // end of first move checker

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
                players.get(currentPlayerIndex).refillHand();
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
        first = true; //bool to see if it is the first move made
        currentPlayerIndex = 0;
        while (!finished){
            view.showBoard(board, players.get(currentPlayerIndex), bag, players);
            controller.getCommand();
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        ScrabbleModel game = new ScrabbleModel();
        game.controller.startGame();
    }
}
