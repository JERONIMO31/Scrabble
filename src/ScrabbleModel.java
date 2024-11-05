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
    private static int currentPlayerIndex;
    private static HashSet<String> wordSet;
    private static Boolean firstMove;

    public ScrabbleModel() throws FileNotFoundException {
        board = new Board();
        bag = new Bag();
        players = new ArrayList<>();
        this.view = new ScrabbleView();
        wordSet = new HashSet<>();
        this.loadWordsFromFile();
        firstMove = true;
    }

    private void loadWordsFromFile() throws FileNotFoundException {
        File file = new File("src/scrabble.txt");
        Scanner scanner = new Scanner(file);

        while(scanner.hasNextLine()) {
            wordSet.add(scanner.nextLine().trim());
        }

        scanner.close();
    }

    public boolean isWord(String word) {
        return wordSet.contains(word.toLowerCase());
    }

    public void addPlayer(String name) {
        players.add(new Player(name, bag));
    }

    private boolean isPossible(int x, int y, char direction, String word) {
        HashMap<Character, Integer> charMap = new HashMap<>();
        int xIndex;
        int yIndex;

        if (!(isWord(word))){
            return false;
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

        return true;
    }

    private boolean isValid(int x, int y, char direction, String word) {
        if (!isPossible(x, y, direction, word)){
            return false;
        }
        if (firstMove){
            int xIndex = x;
            int yIndex = y;
            for (int i = 0; i < word.length(); i++) {
                if (xIndex == 8 && yIndex == 8){
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
        List<String> effectedWords = getEffectedWords(x, y, direction, word);
        if (effectedWords.size() > 1){return false;}
        for (String effectedWord : effectedWords){
            if (!isWord(effectedWord)){
                return false;
            }
        }
        return true;
    }

    private List<String> getEffectedWords(int x, int y, char direction, String word) {
        int xIndex;
        int yIndex;
        List<String> wordList = new ArrayList<>();
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

    private String getWord(int x, int y, char direction, char c){
        int xIndex;
        int yIndex;
        StringBuilder word = new StringBuilder(String.valueOf(c));
        if (direction == 'D') {
            yIndex = y + 1;
            xIndex = x;
            while(!board.isEmpty(xIndex, yIndex)) {
                word.append(board.getTile(xIndex, yIndex).getTileChar());
                yIndex = yIndex + 1;
            }
            yIndex = y - 1;
            while(!board.isEmpty(xIndex, yIndex)) {
                word.insert(0, board.getTile(xIndex, yIndex).getTileChar());
                yIndex = yIndex - 1;
            }
        } else {
            yIndex = y;
            xIndex = x + 1;
            while(!board.isEmpty(xIndex, yIndex)) {
                word.append(board.getTile(xIndex, yIndex).getTileChar());
                xIndex = xIndex + 1;
            }
            xIndex = x - 1;
            while(!board.isEmpty(xIndex, yIndex)) {

                xIndex = xIndex - 1;
            }
        }
        return word.toString();
    }

    private void updatePlayerScore(List<String> effectedWords, String word){
        for (String effectedWord : effectedWords){
            for (char c : effectedWord.toCharArray()){
                getCurrentPlayer().updateScore(c);
            }
        }
        for (char c : word.toCharArray()){
            getCurrentPlayer().updateScore(c);
        }
    }

    public boolean makeMove(int x, int y, char direction, String word) {
        int xIndex;
        int yIndex;

        if (!isValid(x, y, direction, word)){
            return false;
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
                board.addLetter(xIndex, yIndex, getCurrentPlayer().popTile(c));
                getCurrentPlayer().refillHand();
            }
        }
        updatePlayerScore(getEffectedWords(x, y, direction, word), word);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        view.updateBoard();
        return true;
    }

    public Player getCurrentPlayer(){
        return players.get(currentPlayerIndex);
    }

    public void resetGame() {
        this.view.showEnd(board, players);
        bag = new Bag();
        firstMove = true;

        List<Player> holder = new ArrayList<>();

        for (Player player : players) {
            holder.add(new Player(player.getName(), bag));
        }

        players = holder;
        board = new Board();
        view.updateBoard();
    }

}
