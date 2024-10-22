// Source code is decompiled from a .class file using FernFlower decompiler.
package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
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
    private static boolean first;
    private static HashSet<String> wordSet;
    /**
     * Constructs a new ScrabbleModel, initializing the board, bag of tiles, players list,
     * and setting up the view and controller.
     */
    public ScrabbleModel() throws FileNotFoundException {
        board = new Board();
        bag = new Bag();
        players = new ArrayList();
        this.view = new ScrabbleView();
        this.controller = new ScrabbleController(this, this.view);
        wordSet = new HashSet();
        this.loadWordsFromFile();
    }

    private void loadWordsFromFile() throws FileNotFoundException {
        File file = new File("scrabble.txt");
        Scanner scanner = new Scanner(file);

        while(scanner.hasNextLine()) {
            wordSet.add(scanner.nextLine().trim());
        }

        scanner.close();
    }

    public boolean containsWord(String word) {
        return wordSet.contains(word.toLowerCase());
    }

    private int adjustCoordinate(int x, int y, int target, boolean isX) {
        while(true) {
            if (board.isEmpty(x, y)) {
                if (isX) {
                    if (x != target) {
                        break;
                    }
                } else if (y != target) {
                    break;
                }
            }

            if ((isX ? x : y) <= 1) {
                break;
            }

            if (isX) {
                --x;
            } else {
                --y;
            }
        }

        return isX ? x + 1 : y + 1;
    }
    /**
     * Adds a player to the game.
     *
     * @param name The name of the player to add.
     */
    public void addPlayer(String name) {
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
    public boolean makeMove(int x, int y, char direction, String word) {
        HashMap<Character, Integer> charMap = new HashMap();
        boolean connected = false;
        int[][] connections = new int[word.length()][2];
        int paircount = 0;
        int[][] directions;
        if (direction == 'D') {
            directions = new int[][]{{1, 0}, {-1, 0}};
        } else {
            directions = new int[][]{{0, 1}, {0, -1}};
        }

        if (!this.containsWord(word)) {
            return false;
        } else {
            int xIndex;
            int yIndex;
            int i;
            int newX;
            // Iterate over each character in the word to check if the move is valid.
            for(i = 0; i < word.length(); ++i) {
                newX = word.charAt(i);
                // Determine the x and y coordinates of the tile based on the direction of the word.
                if (direction == 'D') { // 'D' means down (vertical)
                    yIndex = y + i;
                    xIndex = x;
                } else {// 'R' means right (horizontal)
                    yIndex = y;
                    xIndex = x + i;
                }
                // If the tile space is empty, track how many tiles of this character are needed from the player's hand.
                if (board.isEmpty(xIndex, yIndex)) {
                    if (charMap.containsKey(Character.valueOf((char)newX))) {
                        charMap.put(Character.valueOf((char)newX), (Integer)charMap.get(Character.valueOf((char)newX)) + 1);
                    } else {
                        charMap.put(Character.valueOf((char)newX), 1);
                    }
                } else {
                    // If the space is not empty, check if the existing tile matches the current character in the word.
                    if (!board.getTile(xIndex, yIndex).equals((char)newX)) {
                        return false;
                    }

                    connected = true;
                }

                int[][] var14 = directions;
                int var15 = directions.length;

                for(int var16 = 0; var16 < var15; ++var16) {
                    int[] ints = var14[var16];
                    newX = xIndex + ints[0];
                    int newY = yIndex + ints[1];
                    if (newX >= 1 && newX <= 15 && newY >= 1 && newY <= 15 && !board.isEmpty(newX, newY)) {
                        connected = true;
                        connections[paircount][0] = newX;
                        connections[paircount][1] = newY;
                        break;
                    }
                }

                ++paircount;
            }

            for(i = 0; i < connections.length; ++i) {
                if (connections[i][0] != 0) {
                    if (direction == 'D') {
                        connections[i][0] = this.adjustCoordinate(connections[i][0], connections[i][1], x, true);
                    } else {
                        connections[i][1] = this.adjustCoordinate(connections[i][0], connections[i][1], y, false);
                    }
                }
            }

            for(i = 0; i < connections.length; ++i) {
                if (connections[i][0] != 0) {
                    newX = connections[i][0];
                    int newY = connections[i][1];
                    StringBuilder wordCon = new StringBuilder();

                    while(direction == 'D' && (!board.isEmpty(newX, newY) || newX == x) || direction != 'D' && (!board.isEmpty(newX, newY) || newY == y)) {
                        if ((direction != 'D' || newX == x) && (direction == 'D' || newY == y)) {
                            if (direction == 'D' && newY + 1 == 16 || direction != 'D' && newX + 1 == 16) {
                                break;
                            }

                            wordCon.append(word.charAt(i));
                        } else {
                            char tileChar = board.getTile(newX, newY).getTileChar();
                            wordCon.append(tileChar);
                        }

                        if (direction == 'D') {
                            ++newX;
                        } else {
                            ++newY;
                        }
                    }

                    if (!this.containsWord(wordCon.toString())) {
                        return false;
                    }
                }
            }

            if (!connected && !first) {
                return false;
            } else {
                Iterator var20 = charMap.keySet().iterator();

                char c;
                while(var20.hasNext()) {
                    c = (Character)var20.next();
                    if ((Integer)charMap.get(c) > ((Player)players.get(currentPlayerIndex)).numInHand(c)) {
                        return false;
                    }
                }

                if (first) {
                    if (direction == 'D') {
                        if (x != 8 || y > 8 || 8 > y + word.length()) {
                            return false;
                        }
                    } else if (y != 8 || x > 8 || 8 > x + word.length()) {
                        return false;
                    }

                    first = false;
                }
                // Place the tiles on the board.
                for(i = 0; i < word.length(); ++i) {
                    c = word.charAt(i);
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
                        board.addLetter(xIndex, yIndex, ((Player)players.get(currentPlayerIndex)).Indextile(c));
                        ((Player)players.get(currentPlayerIndex)).UpdateScore(c);
                        ((Player)players.get(currentPlayerIndex)).refillHand();
                    }
                }
                // If all tiles are placed successfully

                return true;
            }
        }
    }
    /**
     * Ends the game, displays the final board state, and marks the game as finished.
     */
    public void quit() {
        this.view.showEnd(board, players);
        finished = true;
    }
    /**
     * Resets the game by creating a new bag of tiles, resetting the board,
     * and reinitializing all players. It then starts a new game.
     */
    public void resetGame() {
        bag = new Bag();
        List<Player> holder = new ArrayList();
        Iterator var2 = players.iterator();

        while(var2.hasNext()) {
            Player player = (Player)var2.next();
            holder.add(new Player(player.getName(), bag));
        }

        players = holder; // Save the players
        board = new Board();
        this.quit();
        this.play();//Restart
    }
    /**
     * Starts and runs the Scrabble game loop. The game continues until it is marked as finished.
     */
    public void play() {
        finished = false;
        first = true;
        currentPlayerIndex = 0;
        boolean firstmove = false;
        // Gameplay loop
        while(!finished) {
            if (((Player)players.get(currentPlayerIndex)).Handsize() != 0) {
                this.view.showBoard(board, (Player)players.get(currentPlayerIndex), bag, players);
                if (!firstmove) {
                    this.view.showHelp();
                    firstmove = true;
                }

                this.controller.getCommand();
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        ScrabbleModel game = new ScrabbleModel();
        game.controller.startGame();
    }
}
