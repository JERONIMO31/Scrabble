import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Bot extends Player{
    private Random rand;
    private Board board;
    private Tile[][] Board;
    public List<String> words;
    public List<List<String>> moves;
    public List<Character> hand;

    /**
     * Constructor for Bot
     * @param bag is the tile bag, name is name of the bot
     *sets up the Bot
     */
    public Bot(String name, Bag bag){
        super(name,bag);
    }
    private void loadWordsFromFile() {
        words = new ArrayList<>();
        File file = new File("src/scrabble.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        while (scanner.hasNextLine()) {
            words.add(scanner.nextLine().trim()); // Add each word to the set after trimming whitespace
        }

        scanner.close();
    }
    public List<Character> characterList(List<List<Character>> characters){
        List<Character> newCharacter = new ArrayList<>();
        while (characters.size() < 15) {
            characters.add(new ArrayList<>(Collections.nCopies(15, null)));
        }
        for (List<Character> row : characters) {
            while (row.size() < 15) {
                row.add(null);
            }
        }
        for (int x = 0; x <= 14 ; x++){
            for (int y = 0; y <= 14; y++){
                newCharacter.add(characters.get(x).get(y));
            }
        }
        return newCharacter;
    }
    public List<Integer> findStartingCoordinates(List<List<Character>> characters,Character c, String s,char direction){
        List<Integer> positions = new ArrayList<Integer>();
        for (int x = 0; x<= 14; x++){
            for (int y = 0; y<= 14; y++){
                if(characters.get(x).get(y) == c){
                    for(char tempc : s.toCharArray()){
                        if(tempc == c){
                            break;
                        }
                        if(direction == 'D'){
                            x-= 1;
                        }
                        if(direction == 'H'){
                            y -=1;
                        }
                    }
                    positions.add(x);
                    positions.add(y);
                    break;
                }
                break;
            }
        }
        return positions;
    }
    public List<Integer> findCoordinates(List<List<Character>> characters,Character c){
        List<Integer> positions = new ArrayList<Integer>();
        for (int x = 0; x<= 14; x++){
            for (int y = 0; y<= 14; y++){
                if(characters.get(x).get(y) == c){
                    positions.add(x);
                    positions.add(y);
                }
            }
        }
        return positions;
    }

    public boolean areAllElementsNull(List<Character> list) {
        if (list == null || list.isEmpty()) {
            return true; // Treat null or empty list as "all elements null"
        }
        for (Object element : list) {
            if (element != null) {
                return false; // Found a non-null element
            }
        }
        return true; // All elements are null
    }

    public void playBot( ScrabbleModel model) {
        List<List<Character>> affectedWords = AffectedWords(model);
        List<Character> validcharacter = characterList(affectedWords);// list of character to put ai words on
        HashMap<String, Character> moveSet = new HashMap<>();
        Tile interactionTile = null;
        List<Tile> Tiles = new ArrayList<Tile>();
        String mov = null;
        hand = new ArrayList<>();
        int count = 0;
        loadWordsFromFile();
        boolean breaks;
        char direction = 'N';
        int positionx = 1;
        int positiony = 1;
        List<Integer> positions = new ArrayList<>();

        for (Tile temp : super.getHand()) {
            hand.add(temp.getTileChar());
            count += 1;
        }
        for (String s : words) {// loops through the dictionary to isolate each string
            breaks = false;
            int length = 0;
            if (!areAllElementsNull(validcharacter)) {
                for(char temp : s.toCharArray()){
                    if(validcharacter.contains(temp)){
                        length += 1;
                        break;
                    }
                }
            }
            String tempS = "";
            if (s.length() > 7 + length) {// elimantes any words that are too big for the and size plus 1 character if it isnt the start
                continue;
            }
            tempS = s;
            for (char tempc : tempS.toCharArray()) {// goes through each tile
                if (hand.contains(tempc)) {// verifies the tiles exists in the string
                    length += 1;
                    tempS = tempS.replaceFirst(String.valueOf(tempc), "");// replaces the charcter just found so we can find repeat character
                }
            }
            if (s.length() <= length && tempS.length() <= 1) {// verfies if the enough tiles have matched to a character in s to be able to make the word
                Tiles = new ArrayList<>();
                tempS = s;
                if (areAllElementsNull(validcharacter)) {
                    positions.add(0, 7);
                    positions.add(1, 7);
                    direction = 'H';
                    for (Tile tempTile : super.getHand()) {
                        if (s.contains(String.valueOf(tempTile.getTileChar()))) {
                            Tiles.add(tempTile);
                            mov = s.replaceFirst(String.valueOf(tempTile.getTileChar()), "");
                        }
                    }
                    model.makeMove(positions.get(0), positions.get(1), direction, Tiles);
                    break;
                } else {
                    for (char tempo :s.toCharArray()){
                        if(validcharacter.contains(tempo)){
                            breaks = true;
                            break;
                        }
                    }

                    if(!breaks|| s.length() == 1){
                        continue;
                    }
                    breaks = false;
                    while(validcharacter.remove(null)){
                    }
                    for (char temp : s.toCharArray()) {
                        if (validcharacter.contains(temp)) {
                            direction = getDirection(affectedWords, temp, model);
                            positions = findStartingCoordinates(affectedWords, temp, s, direction);
                            interactionTile = new Tile(temp);
                            tempS = tempS.replaceFirst(String.valueOf(interactionTile.getTileChar()), "");
                            Tiles.add(interactionTile);
                            break;
                        }
                    }
                    if (direction == 'N') {
                        continue;
                    }
                    for (Tile tempTile : super.getHand()) {
                        if (tempS.contains(String.valueOf(tempTile.getTileChar()))) {
                            Tiles.add(tempTile);
                            tempS = tempS.replaceFirst(String.valueOf(tempTile.getTileChar()), "");
                        }
                        if (model.isValid(positions.get(0), positions.get(1), direction, Tiles)) {
                            breaks = model.makeMove(positions.get(0), positions.get(1), direction, Tiles);
                            break;
                        }
                    }
                    if(breaks){
                        break;
                    }
                }

            }
        }

/**


        if (moves.isEmpty()) {
            model.skip();
        } else {
            while (mov == null) {
                positionx = rand.nextInt(14);
                positiony = rand.nextInt(14);
                mov = moves.get(positionx).get(positiony);
            }
            direction = moveSet.get(mov);
            for (Tile tempTile : super.getHand()) {
                if (mov.contains(String.valueOf(tempTile.getTileChar()))) {
                    Tiles.add(tempTile);
                    mov = mov.replaceFirst(String.valueOf(tempTile.getTileChar()), "");
                }
            }
            Tiles.add(interactionTile);
            model.makeMove(positionx, positiony, direction, Tiles);
        }
    }
 */
    }
    private List<List<Character>> AffectedWords(ScrabbleModel model) {
        List<List<Character>> Letter = new ArrayList<List<Character>>();
        board=  model.getBoard();
        Board = board.getBoard();
        for (int x = 0; x < 14; x++) {
            List<Character> innerList = new ArrayList<>(Collections.nCopies(15, null));
            Letter.add(innerList);
        }
        for (int y = 0; y <= 14; y++) {
            for (int x = 0 ; x <= 14; x++){
                int xtouching = 0;// checks how many tiles in the x and  y direction the current tile is in contact with.
                int ytouching = 0;
                if(Board[x][y] != null){
                    if(board.getTile(x+1,y) != null){// these statements check if the tiles to the left right up or down of the tile are filled
                        xtouching += 1;
                    }
                    if(board.getTile(x-1,y) != null){
                        xtouching += 1;
                    }
                    if(board.getTile(x,y+1) != null){
                        ytouching += 1;
                    }
                    if(board.getTile(x,y-1) != null){
                        ytouching += 1;
                    }
                    if(!(xtouching >= 1 && ytouching >= 1 ) ){
                        Letter.get(x).add(y,Character.valueOf(board.getTile(x,y).getTileChar()));
                    }
                }
            }
        }
        return Letter;
    }
   private char getDirection(List<List<Character>> letter , Character C,ScrabbleModel model){
       char Direction;
       List<Integer> position = findCoordinates(letter,C);
       board=  model.getBoard();
       Board = board.getBoard();
       int xtouching = 0;// checks how many tiles in the x and  y direction the current tile is in contact with.
       int ytouching = 0;
       if(board.getTile(position.get(0) +1,position.get(1)) != null){// these statements check if the tiles to the left right up or down of the tile are filled
           xtouching += 1;
       }
       if(board.getTile(position.get(0)-1,position.get(1)) != null){
           xtouching += 1;
       }
       if(board.getTile(position.get(0),position.get(1)+1) != null){
           ytouching += 1;
       }
       if(board.getTile(position.get(0),position.get(1)-1) != null){
           ytouching += 1;
       }
       if(xtouching == 0){
           Direction = 'H';
       }
       else{
           Direction = 'D';
       }
       return Direction;
   }
}
