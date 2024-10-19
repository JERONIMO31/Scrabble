package src;
import java.util.Scanner;
import java.util.StringTokenizer;

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
        if (model == null || view == null) {
            throw new IllegalArgumentException("Model or View is null");
        }

        this.model = model;
        this.view = view;
        reader = new Scanner(System.in);

    }

    /**
     * Reads and processes commands from the player. Commands can include placing a word on the board,
     * quitting, skipping the turn, resetting the game, or asking for help. If a command is invalid, it prompts
     * the user to re-enter the command.
     */
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
                word1 = tokenizer.next();      // get first word, either help, skip, reset, help, or the x coordinate
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
                    word2 = tokenizer.next();      // get second word, the y coordinate
                    y = Integer.parseInt(word2);
                } catch (Exception _){
                    System.out.println("Invalid command!");
                    getCommand();
                    return;
                }

                try {
                    word3 = tokenizer.next().toUpperCase();      // get third word, the direction (R or D)
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
                    word4 = tokenizer.next();      // get fourth word, the word to place
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

    /**
     * Starts the Scrabble game by prompting the user for the number of players and their names.
     * Initializes the players and begins the game loop.
     */
    public void startGame() {
        System.out.print("How many players? > ");
        int numberOfPlayers = reader.nextInt();

        reader.nextLine();

        if (numberOfPlayers <= 0 || numberOfPlayers > 4) {
            throw new IllegalArgumentException("Number of players must be greater than 0 but less then 4");
        }

        // Prompt for player names and add them to the model.
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.print("Enter player " + (i+1) + "'s name > ");
            String name = reader.nextLine();
            model.addPlayer(name);
        }

        // Start the game.
        model.play();
    }

}
