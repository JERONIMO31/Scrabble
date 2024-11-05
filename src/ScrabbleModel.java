import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

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

    public void addPlayer(String name) {
        players.add(new Player(name, bag));
    }

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
            for(i = 0; i < word.length(); ++i) {
                newX = word.charAt(i);
                if (direction == 'D') {
                    yIndex = y + i;
                    xIndex = x;
                } else {
                    yIndex = y;
                    xIndex = x + i;
                }

                if (board.isEmpty(xIndex, yIndex)) {
                    if (charMap.containsKey((char)newX)) {
                        charMap.put((char)newX, (Integer)charMap.get((char)newX) + 1);
                    } else {
                        charMap.put((char)newX, 1);
                    }
                } else {
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

                for(i = 0; i < word.length(); ++i) {
                    c = word.charAt(i);
                    if (direction == 'D') {
                        yIndex = y + i;
                        xIndex = x;
                    } else {
                        yIndex = y;
                        xIndex = x + i;
                    }

                    if (board.isEmpty(xIndex, yIndex)) {
                        board.addLetter(xIndex, yIndex, ((Player)players.get(currentPlayerIndex)).Indextile(c));
                        ((Player)players.get(currentPlayerIndex)).UpdateScore(c);
                        ((Player)players.get(currentPlayerIndex)).refillHand();
                    }
                }

                return true;
            }
        }
    }

    public void quit() {
        this.view.showEnd(board, players);
        finished = true;
    }

    public void resetGame() {
        bag = new Bag();
        List<Player> holder = new ArrayList();
        Iterator var2 = players.iterator();

        while(var2.hasNext()) {
            Player player = (Player)var2.next();
            holder.add(new Player(player.getName(), bag));
        }

        players = holder;
        board = new Board();
        this.quit();
        this.play();
    }

    public void play() {
        finished = false;
        first = true;
        currentPlayerIndex = 0;
        boolean firstmove = false;

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
