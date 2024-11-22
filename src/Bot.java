import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class Bot {
    private Random rand;
    private int num = 0;
    public Player ai;
    public List<String> words;
    public List<String> moves;
    public List<Character> bag;
    public Bot( Bag bag) {
        this.ai = new Player(String.valueOf(num),bag);
        num += 1;
    }
    private void loadWordsFromFile() {
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
    public void playBot(char difficulty, List<Character> affectedWords ){
        String mov;
        loadWordsFromFile();
        bag = ai.getBag().getBagOfChar();
        for (String s : words){// loops through the dictionary to isolate each string
            boolean affected = false;
            int length = 0;
            String tempS = "";
            if(affectedWords.size() != 0){// checks that if it isnt the start of the game that the potential moves all have at least one character thats one the board
                length += 1;
                for(char temp: affectedWords){
                    if(s.indexOf(temp) != -1){
                        affected = true;
                    }
                }
            }
            if (affected == true){
                continue;
            }
            if (s.length()> 7+length){// elimantes any words that are too big for the and size plus 1 character if it isnt the start
                continue;
            }
            tempS= s;
            for(int i = 0; i<=7 ; i++){// goes through each tile

                if(tempS.indexOf(bag.get(i)) != -1){// verifies the tiles exists in the string
                    length += 1;
                    tempS = tempS.replaceFirst(String.valueOf(bag.get(i)),"");// replaces the charcter just found so we can find repeat character
                    if(s.length()== length){// verfies if the enough tiles have matched to a character in s to be able to make the word
                        moves.add(s);
                        break;
                    }
                }
            }
            mov = moves.get(rand.nextInt(moves.size()));


        }

    }
}
