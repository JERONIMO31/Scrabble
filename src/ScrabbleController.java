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
            String word1 = null;
            String word2 = null;
            String word3 = null;
            String word4 = null;

            System.out.print("Enter where to move, the x and y coordinates, the direction the word will go, and the word 'x y D/R <word>' > ");

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
                } else if (word1.equals("help")) {
                    view.showHelp();
                    getCommand();
                    return;
                }

                if (tokenizer.hasNext()) {
                    word2 = tokenizer.next();      // get second word
                    if (tokenizer.hasNext()) {
                        word3 = tokenizer.next();      // get third word
                        if (!word3.equals("D") && !word3.equals("R")) {
                            System.out.print("The direction of the word must be D (down) or R (right)");
                            System.out.print("Enter the direction the word will go 'D/R' > ");
                            inputLine = reader.nextLine();
                            if (tokenizer.hasNext()) {
                                word3 = tokenizer.next();
                            }
                        }
                        if (tokenizer.hasNext()) {
                            word4 = tokenizer.next();      // get fourth word

                            word3 = word3.toUpperCase();
                            char ch3 = word3.charAt(0);
                            int x = Integer.parseInt(word1);
                            int y = Integer.parseInt(word2);

                            success = model.makeMove(x, y, ch3, word4);
                        }
                    }
                }
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
