import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.*;

public class ScrabbleController implements ActionListener {
    private final ScrabbleModel model;
    private final ScrabbleView view;
    private Scanner reader;

    public ScrabbleController(ScrabbleModel model, ScrabbleView view) {
        if (model != null && view != null) {
            this.model = model;
            this.view = view;
            this.reader = new Scanner(System.in);
        } else {
            throw new IllegalArgumentException("Model or View is null");
        }

        // Add event listeners to view components
        // Add listeners for boardPanel buttons
        JButton[][] boardCells = view.getBoardCells();
        for (int i = 0; i < boardCells.length; i++) {
            for (int j = 0; j < boardCells[i].length; j++) {
                final int row = i;
                final int col = j;
                boardCells[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (model.hasSelectedTile()) {
                            model.placeTile(row, col, model.getSelectedTile());
                            view.updateBoard(row, col, model.getSelectedTile());
                            model.clearSelectedTile();
                        }
                    }
                });
            }
        }

        // Add listeners for playerHandPanel buttons
        JButton[] playerHandTiles = view.getHandTiles();
        for (int i = 0; i < playerHandTiles.length; i++) {
            final int tileIndex = i;
            playerHandTiles[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Tile selectedTile = model.selectTileFromRack(tileIndex);
                    view.highlightHandTile(tileIndex);
                }
            });
        }

        // Add listeners for controlPanel
        view.getPlayWordButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate the word, update the score, and reset the board and rack if valid
                if (model.validateWord()) {
                    model.updateScore();
                    view.updateScoreDisplay(model.getPlayerScore());
                    model.resetBoard();
                    view.resetBoardDisplay();
                    view.updateRack(model.getPlayerRack());
                }
                else {
                    JOptionPane.showMessageDialog(view.getFrame(), "Invalid word");
                }
            }
        });
    }

    public void getCommand() {
        boolean success = false;

        while(!success) {
            System.out.print(" > ");
            String inputLine = this.reader.nextLine();
            Scanner tokenizer = new Scanner(inputLine);
            if (tokenizer.hasNext()) {
                String word1 = tokenizer.next();
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
                } catch (Exception var14) {
                    System.out.println("Invalid command!");
                    this.getCommand();
                    return;
                }

                int y;
                String word3;
                try {
                    word3 = tokenizer.next();
                    y = Integer.parseInt(word3);
                } catch (Exception var13) {
                    System.out.println("Invalid command!");
                    this.getCommand();
                    return;
                }

                char ch3;
                try {
                    word3 = tokenizer.next().toUpperCase();
                    ch3 = word3.charAt(0);
                } catch (Exception var12) {
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
                    word4 = tokenizer.next();
                } catch (Exception var11) {
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

    public void startGame() {
        System.out.print("How many players? > ");
        int numberOfPlayers = this.reader.nextInt();
        this.reader.nextLine();
        if (numberOfPlayers > 0 && numberOfPlayers <= 4) {
            for(int i = 0; i < numberOfPlayers; ++i) {
                System.out.print("Enter player " + (i + 1) + "'s name > ");
                String name = this.reader.nextLine();
                this.model.addPlayer(name);
            }

            this.model.play();
        } else {
            throw new IllegalArgumentException("Number of players must be greater than 0 but less then 4");
        }
    }

}
