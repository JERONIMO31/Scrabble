package src;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ScrabbleController {
    private final ScrabbleModel model;
    private final ScrabbleView view;
    private Scanner reader;

    public ScrabbleController(ScrabbleModel model, ScrabbleView view) {
        if (model == null || view == null) {
            throw new IllegalArgumentException("Model or View is null");
        }

        this.model = model;
        this.view = view;
        reader = new Scanner(System.in);

    }

    public void getCommand() {
        boolean success = false;
        while (!success) {
            String inputLine;
            String word1;
            String word2;
            String word3;
            String word4;
            int x;
            int y;
            char ch3;
            System.out.print(" > ");

            inputLine = reader.nextLine();

            // Find up to four words on the line.
            Scanner tokenizer = new Scanner(inputLine);
            if (tokenizer.hasNext()) {
                word1 = tokenizer.next();      // get first word
                if (word1.equals("quit")) {
                    model.quit();
                    return;
                } else if (word1.equals("skip")) {
                    return;
                } else if (word1.equals("reset")) {
                    model.resetGame();
                    return;
                } else if (word1.equals("help")) {
                    view.showHelp();
                    getCommand();
                    return;
                } else {
                    try {
                        x = Integer.parseInt(word1);
                    } catch (Exception _){
                        System.out.println("Invalid command!");
                        getCommand();
                        return;
                    }
                }

                try {
                    word2 = tokenizer.next();      // get second word
                    y = Integer.parseInt(word2);
                } catch (Exception _){
                    System.out.println("Invalid command!");
                    getCommand();
                    return;
                }

                try {
                    word3 = tokenizer.next().toUpperCase();      // get third word
                    ch3 = word3.charAt(0);
                } catch (Exception _){
                    System.out.println("Invalid command!");
                    getCommand();
                    return;
                }
                if (!word3.equals("D") && !word3.equals("R")) {
                    System.out.println("Invalid command!");
                    getCommand();
                    return;
                }

                try {
                    word4 = tokenizer.next();      // get fourth word
                } catch (Exception _){
                    System.out.println("Invalid command!");
                    getCommand();
                    return;
                }

                success = model.makeMove(x, y, ch3, word4);
                if (!success){System.out.println("Invalid move!");}
            }
        }
    }

    public void startGame() {
        System.out.print("How many players? > ");
        int numberOfPlayers = reader.nextInt();

        reader.nextLine();

        if (numberOfPlayers <= 0 || numberOfPlayers > 4) {
            throw new IllegalArgumentException("Number of players must be greater than 0 but less then 4");
        }

        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.print("Enter player " + (i+1) + "'s name > ");
            String name = reader.nextLine();
            model.addPlayer(name);
        }

        model.play();
    }

}
