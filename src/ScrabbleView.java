import javax.swing.*;
import java.util.List;
import java.awt.*;

public class ScrabbleView extends JFrame {
    ScrabbleModel model;
    ScrabbleController sc;
    private final JPanel boardPanel;
    private final JButton[][] boardCells;
    private final JPanel playerHandPanel;
    private final JButton[] handTiles;
    private final JTextField playerName;
    private final JPanel scorePanel;
    private final JLabel scoreLabel;
    private final JPanel controlPanel;
    private final JButton playWordButton, skipButton;
    private final JMenuItem resetGameItem, resetGameSPItem, helpItem;
    private String scoreStr;

    /**
     * Constructor for ScrabbleView.
     * Sets up the main JFrame, initializes game components, and connects the view with the model and controller.
     */
    public ScrabbleView() {
        // Initialize JFrame
        this.scoreStr = "";
        this.setTitle("Scrabble");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 1000);

        model = new ScrabbleModel(this);
        sc = new ScrabbleController(this.model, this);

        // Get number of players and add to model
        this.setPlayers();

        // Initialize boardPanel
        boardPanel = new JPanel(new GridLayout(15, 15));
        boardCells = new JButton[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                JButton boardCell = new JButton();
                boardCell.setPreferredSize(new Dimension(50, 50));
                boardCell.setActionCommand("B " + i + " " + j);
                boardCell.addActionListener(sc); // Set up board cell to respond to controller
                boardCells[i][j] = boardCell;
                boardCells[i][j].setBackground(Color.pink);
                boardPanel.add(boardCell);
            }
        }
        boardCells[7][7].setBackground(Color.yellow); // Highlight center tile for first move requirement

        // Initialize playerHandPanel
        playerHandPanel = new JPanel(new FlowLayout());
        handTiles = new JButton[7];
        playerName = new JTextField(model.getCurrentPlayer().getName() + "'s turn:");
        playerHandPanel.add(playerName);
        for (int i = 0; i < 7; i++) {
            handTiles[i] = new JButton(" ");
            handTiles[i].setPreferredSize(new Dimension(50, 50));
            handTiles[i].setActionCommand("H " + i);
            handTiles[i].addActionListener(sc); // Set up hand tile to respond to controller
            playerHandPanel.add(handTiles[i]);
        }

        // Initialize controlPanel and buttons
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        playWordButton = new JButton("Play Word");
        skipButton = new JButton("Play Word");

        playWordButton.setActionCommand("P");
        playWordButton.addActionListener(sc); // Play word action

        skipButton.setActionCommand("S");
        skipButton.addActionListener(sc); // Skip turn action

        controlPanel.add(skipButton);
        controlPanel.add(playWordButton);

        // Initialize scorePanel
        scorePanel = new JPanel();
        scoreStr = getScoreString();
        scoreLabel = new JLabel(scoreStr);
        scorePanel.add(scoreLabel);

        // Add gameMenu and items
        JMenu gameMenu = new JMenu("Game");
        resetGameSPItem = new JMenuItem("Restart game (with same players)");
        resetGameItem = new JMenuItem("New game (with new players)");
        helpItem = new JMenuItem("Help");

        resetGameSPItem.setActionCommand("HELP");
        resetGameSPItem.addActionListener(sc); // Help action

        resetGameSPItem.setActionCommand("RGSP");
        resetGameSPItem.addActionListener(sc); // Restart game with same players

        resetGameItem.setActionCommand("RGNP");
        resetGameItem.addActionListener(sc); // New game with new players

        JMenuBar menuBar = new JMenuBar();
        gameMenu.add(resetGameItem);
        gameMenu.add(resetGameSPItem);
        gameMenu.add(helpItem);

        menuBar.add(gameMenu);
        // Add menu bar to frame
        this.setJMenuBar(menuBar); // Add menu bar to frame

        // Add panels to the frame
        add(boardPanel, BorderLayout.CENTER);
        add(playerHandPanel, BorderLayout.SOUTH);
        add(scorePanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.EAST);

        pack(); // Adjust frame to fit components
        setLocationRelativeTo(null); // Center frame on screen
        setVisible(true);
        this.updateView(); // Initial update for view
    }

    /**
     * Prompts the user for the number of players and their names, adding them to the model.
     */
    public void setPlayers() {
        int playerNum = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter number of players (max 4 players): "));
        while (playerNum > 4 || playerNum < 1) {
            playerNum = Integer.parseInt(JOptionPane.showInputDialog(this, "Invalid number of players. Please state 1, 2, 3, or 4: "));
        }

        for (int i = 0; i < playerNum; i++) {
            String name = JOptionPane.showInputDialog(this, "Enter player " + (i + 1) + "'s name: ");
            model.addPlayer(name);
        }
    }

    /**
     * Generates a string with the current scores of all players.
     * @return String displaying each player's name and score.
     */
    private String getScoreString() {
        scoreStr = "Scores: ";
        for (Player player : this.model.getPlayers()) {
            scoreStr += (player.getName() + ": " + player.getScore() + " ");
        }
        return scoreStr;
    }

    /**
     * Updates the view to reflect the current state of the model.
     */
    public void updateView() {
        Board board = this.model.getBoard();

        // Restore the board
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (board.isEmpty(i, j)) {
                    boardCells[i][j].setText(" ");
                    boardCells[i][j].setEnabled(true);
                }
                else {
                    boardCells[i][j].setText(String.valueOf(board.getTile(i, j).getTileChar()));
                    boardCells[i][j].setEnabled(false);
                }
            }
        }

        // Restore the current players hand
        for (int i = 0; i < model.getCurrentPlayer().getHand().size(); i++) {
            handTiles[i].setText(String.valueOf(model.getCurrentPlayer().getHand().get(i).getTileChar()));
            handTiles[i].setEnabled(true);
        }

        // Update the players current scores
        scoreStr = getScoreString();
        scoreLabel.setText(scoreStr);

        // Update the current player
        playerName.setText(model.getCurrentPlayer().getName());
        playerName.setEditable(false);
    }

    /**
     * Displays the game winner at the end of the game.
     */
    public void showEnd() {
        List<Player> players = model.getPlayers();
        Player winner = players.getFirst();

        for (Player p : players) {
            if (p.getScore() > winner.getScore()) {
                winner = p;
            }
        }

        JOptionPane.showMessageDialog(this, "The winner is: " + winner.getName() + "!");
        System.out.println("The winner is: " + winner.getName() + "!");
    }

     /**
     * Shows a help dialog with instructions on how to play the game.
      */
    public void showHelp() {
        JOptionPane.showMessageDialog(this, """
                To place a tile, select it from your hand then select the space on the board in which you'd like to place it.
                You can remove a previously placed tile (in the same turn) by selecting it again from the board.
                For the first move it must be on the coordinates 8 8, it doesn't have to begin there, as long as it passes through.
                A player can skip their turn by clicking the 'Skip Turn' button.
                The game can be restarted with the current players or a brand new game by using the 'Game' menu.""");

    }

    /**
     * Returns the main game frame.
     * @return the main JFrame instance for the game.
     */
    public JFrame getFrame() {
        return this;
    }

    /**
     * Removes a temporary tile from the board and re-enables it in the player's hand.
     * @param x Board x-coordinate.
     * @param y Board y-coordinate.
     * @param handIndex Index of tile in hand.
     */
    public void removeTempTile(int x, int y, int handIndex) {
        boardCells[x][y].setText(" ");
        boardCells[x][y].setEnabled(true);
        handTiles[handIndex].setEnabled(true);
    }

   /**
   * Adds a temporary tile to the board and disables it in the player's hand.
   * @param tile Character representing the tile
    */
    public void addTempTile(char tile, int x, int y, int handIndex) {
        boardCells[x][y].setText(String.valueOf(tile));
        handTiles[handIndex].setEnabled(false);
    }

    /**
     * Resets the game by reinitializing the game model and controller, and prompting the user
     * to input player information again. This method clears the board, resets player hands, and
     * updates the view to reflect the initial game state.
     */
    public void resetGame() {
        model = new ScrabbleModel(this);
        sc.setModel(model);
        this.setPlayers();
    }

    public static void main(String[] args) {
        new ScrabbleView();
    }

}
