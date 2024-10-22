package src;

import java.util.Scanner;
/**
 * The ScrabbleController class handles the input from the user and passes the necessary commands
 * to the ScrabbleModel. It controls the flow of the game by managing player actions, game resets,
 * and other commands.
 */
public class ScrabbleController {
    private final ScrabbleModel model;
    private final ScrabbleView view;
    private Scanner reader;
    /**
     * Constructs a ScrabbleController object that connects the model and view of the game.
     *
     * @param model The ScrabbleModel object that holds the game's data and logic.
     * @param view  The ScrabbleView object that handles the display and user interface.
     * @throws IllegalArgumentException if the model or view is null.
     */
    public ScrabbleController(ScrabbleModel model, ScrabbleView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            this.reader = new Scanner(System.in);
        } else {
            throw new IllegalArgumentException("Model or View is null");
        }
    }
    /**
     * Reads and processes commands from the player. Commands can include placing a word on the board,
     * quitting, skipping the turn, resetting the game, or asking for help. If a command is invalid, it prompts
     * the user to re-enter the command.
     */
    public void getCommand() {
        boolean success = false;

        while(!success) {
            System.out.print(" > ");
            String inputLine = this.reader.nextLine();
            // Find up to four words on the line.
            Scanner tokenizer = new Scanner(inputLine);
            if (tokenizer.hasNext()) {
                String word1 = tokenizer.next(); //get first word, either help, skip, reset, help, or the x coordinate
                if (word1.equals("quit")) {
                    this.model.quit();
                    return;
                }

                if (word1.equals("skip")) {
                    return;
                }

                if (word1.equals("reset")) {
                    this.model.resetGame();
                    return;
                }

                if (word1.equals("help")) {
                    this.view.showHelp();
                    this.getCommand();
                    return;
                }

                int x;
                try {
                    x = Integer.parseInt(word1);
                } catch (Exception var15) {
                    System.out.println("Invalid command!");
                    this.getCommand();
                    return;
                }

                int y;
                try {
                    String word2 = tokenizer.next(); // get second word, the y coordinate
                    y = Integer.parseInt(word2);
                } catch (Exception var14) {
                    System.out.println("Invalid command!");
                    this.getCommand();
                    return;
                }

                String word3;
                char ch3;
                try {
                    word3 = tokenizer.next().toUpperCase();// get third word, the direction (R or D)
                    ch3 = word3.charAt(0);
                } catch (Exception var13) {
                    System.out.println("Invalid command!");
                    this.getCommand();
                    return;
                }

                if (!word3.equals("D") && !word3.equals("R")) {
                    System.out.println("Invalid command!");
                    this.getCommand();
                    return;
                }

                String word4;
                try {
                    word4 = tokenizer.next();// get fourth word, the word to place
                } catch (Exception var12) {
                    System.out.println("Invalid command!");
                    this.getCommand();
                    return;
                }

                success = this.model.makeMove(x, y, ch3, word4);
                if (!success) {
                    System.out.println("Invalid move!");
                }
            }
        }

    }
    /**
     * Starts the Scrabble game by prompting the user for the number of players and their names.
     * Initializes the players and begins the game loop.
     */
    public void startGame() {
        System.out.print("How many players? > ");
        int numberOfPlayers = this.reader.nextInt();
        this.reader.nextLine();
        if (numberOfPlayers > 0 && numberOfPlayers <= 4) {
            // Prompt for player names and add them to the model.
            for(int i = 0; i < numberOfPlayers; ++i) {
                System.out.print("Enter player " + (i + 1) + "'s name > ");
                String name = this.reader.nextLine();
                this.model.addPlayer(name);
            }
            // Start the game.
            this.model.play();
        } else {
            throw new IllegalArgumentException("Number of players must be greater than 0 but less then 4");
        }
    }
}

