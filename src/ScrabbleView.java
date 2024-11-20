import javax.swing.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

public class ScrabbleView extends JFrame {
    ScrabbleModel model;
    ScrabbleController sc;
    private final JButton[][] boardCells;
    private final JButton[] handTiles;
    private final JLabel playerName;
    private final JLabel scoreLabel;
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

        // Initialize panel to contain the board
        JPanel boardPanel = new JPanel(new GridLayout(15, 15));
        boardPanel.setBackground(null);
        boardCells = new JButton[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                JButton boardCell = new JButton();
                boardCell.setPreferredSize(new Dimension(50, 50));
                boardCell.setActionCommand("B " + i + " " + j);
                boardCell.addActionListener(sc); // Set up board cell actionListener
                boardCells[i][j] = boardCell;
                boardCells[i][j].setBorder(BorderFactory.createLineBorder(Color.black)); // Set black borders

                ImageIcon boardIcon = new ImageIcon(getClass().getResource("/regularBoardTile.png"));
                boardCells[i][j].setIcon(boardIcon); // Set the image as the icon

                mouseListener(boardCells[i][j], null, null, 2); // Set hover border to red
                boardPanel.add(boardCell);
            }
        }
        // Set up imageIcons for special tiles
        this.setSpecialTiles();

        // Initialize panel for the players name, hand, and play/skip buttons
        JPanel playerHandPanel = new JPanel(new FlowLayout());
        handTiles = new JButton[7];
        playerName = new JLabel(model.getCurrentPlayer().getName() + "'s hand:");
        playerName.setSize(60, 50);
        playerHandPanel.add(playerName);
        for (int i = 0; i < 7; i++) {
            handTiles[i] = new JButton(" ");
            handTiles[i].setPreferredSize(new Dimension(50, 50));
            handTiles[i].setActionCommand("H " + i);
            handTiles[i].addActionListener(sc); // Set up hand tile actionListener
            handTiles[i].setBorder(BorderFactory.createLineBorder(Color.black)); // Set black borders
            mouseListener(handTiles[i], null, null, 1); // Set hover border to pink
            playerHandPanel.add(handTiles[i]);
        }
        // Set imageIcons for players hand tiles
        this.setHandTiles();

        // Initialize panel for the control buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        JButton playWordButton = new JButton("Play Word");
        JButton skipButton = new JButton("Skip Turn");

        playWordButton.setActionCommand("P");
        playWordButton.addActionListener(sc); // Play word actionListener
        playWordButton.setBackground(Color.cyan);

        skipButton.setActionCommand("S");
        skipButton.addActionListener(sc); // Skip turn actionListener
        skipButton.setBackground(Color.green);

        controlPanel.add(playWordButton);
        controlPanel.add(skipButton);

        controlPanel.setSize(60, 50);
        playerHandPanel.add(controlPanel); // Add to playerHandPanel

        // Initialize panel for displaying the scores
        JPanel scorePanel = new JPanel();
        scoreStr = getScoreString();
        scoreLabel = new JLabel(scoreStr);
        scorePanel.add(scoreLabel);

        // Add gameMenu and items
        JMenu gameMenu = new JMenu("Game");
        mouseListener(null, gameMenu, null, 2); // Set hover border to pink
        JMenuItem resetGameSPItem = new JMenuItem("Restart game (with same players)");
        mouseListener(null, null, resetGameSPItem, 2); // Set hover border to pink
        JMenuItem resetGameItem = new JMenuItem("New game (with new players)");
        mouseListener(null, null, resetGameItem, 2); // Set hover border to pink
        JMenuItem helpItem = new JMenuItem("Help");
        mouseListener(null, null, helpItem, 2); // Set hover border to pink

        helpItem.setActionCommand("HELP");
        helpItem.addActionListener(sc); // Help action

        resetGameSPItem.setActionCommand("1RGSP");
        resetGameSPItem.addActionListener(sc); // Restart game with same players actionListener

        resetGameItem.setActionCommand("RGNP");
        resetGameItem.addActionListener(sc); // New game with new players actionListener

        JMenuBar menuBar = new JMenuBar();
        gameMenu.add(resetGameItem);
        gameMenu.add(resetGameSPItem);
        gameMenu.add(helpItem);

        menuBar.add(gameMenu);
        this.setJMenuBar(menuBar); // Add menu bar to frame

        // Add panels to the frame
        add(boardPanel, BorderLayout.CENTER);
        add(playerHandPanel, BorderLayout.SOUTH);
        add(scorePanel, BorderLayout.NORTH);

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
                    mouseListener(boardCells[i][j], null, null, 2); // Set border to red
                }
                else {
                    boardCells[i][j].setText(String.valueOf(board.getTile(i, j).getTileChar()));
                    boardCells[i][j].setEnabled(false);
                    mouseListener(boardCells[i][j], null, null, 3); // Set border to black
                }
            }
        }

        // Restore the current players hand
        for (int i = 0; i < model.getCurrentPlayer().getHand().size(); i++) {
            handTiles[i].setText(String.valueOf(model.getCurrentPlayer().getHand().get(i).getTileChar()));
            handTiles[i].setEnabled(true);
            mouseListener(handTiles[i], null, null, 1); // Set border to pink
        }
        this.setHandTiles();

        // Update the players current scores
        scoreStr = getScoreString();
        scoreLabel.setText(scoreStr);

        // Update the current player
        playerName.setText(model.getCurrentPlayer().getName() + "'s hand:");
    }

    /**
     * Adds a MouseListener to a button, menu, or menu item.
     * @param button The JButton to add the listener to.
     * @param menu The JMenu to add the listener to.
     * @param item The JMenuItem to add the listener to.
     * @param type Indicates if hover border should be pink, red or black (1, 2, 3 respectively).
     */
    private void mouseListener(JButton button, JMenu menu, JMenuItem item, int type) {
        // Check for a JButton
        if (button != null) {
            // Depending on type, add different mouse listeners to the button
            if (type == 1) {
                // Add a mouse listener to the button
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // Change the button border to pink when mouse enters
                        button.setBorder(BorderFactory.createLineBorder(Color.pink));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        // Reset the button border to black when mouse exits
                        button.setBorder(BorderFactory.createLineBorder(Color.black));
                    }
                });
            }
            else if (type == 2) {
                // Add a mouse listener to the button
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // Change the button border to red when mouse enters
                        button.setBorder(BorderFactory.createLineBorder(Color.red));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        // Reset the button border to black when mouse exits
                        button.setBorder(BorderFactory.createLineBorder(Color.black));
                    }
                });
            }
            else if (type == 3) {
                // Add a mouse listener to the button
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // No change in button border on mouse enter
                        button.setBorder(BorderFactory.createLineBorder(Color.black));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        // No change in button border on mouse exit
                        button.setBorder(BorderFactory.createLineBorder(Color.black));
                    }
                });
            }
        }
        // Check for JMenu
        if (menu != null) {
            // Add a mouse listener to the menu
            menu.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    // Change the menu border to pink when mouse enters
                    menu.setBorder(BorderFactory.createLineBorder(Color.pink));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Remove the border when mouse exits
                    menu.setBorder(null);
                }
            });
        }
        // Check for JMenuItem
        if (item != null) {
            // Add a mouse listener to the menu item
            item.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    // Change the item border to pink when mouse enters
                    item.setBorder(BorderFactory.createLineBorder(Color.pink));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Remove the border when mouse exits
                    item.setBorder(null);
                }
            });
        }
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
        mouseListener(handTiles[handIndex], null, null, 1); // Set border to pink
    }

   /**
   * Adds a temporary tile to the board and disables it in the player's hand.
   * @param tile Character representing the tile
    */
    public void addTempTile(char tile, int x, int y, int handIndex) {
        boardCells[x][y].setText(String.valueOf(tile));
        handTiles[handIndex].setEnabled(false);
        mouseListener(handTiles[handIndex], null, null, 3); // Set border to black
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
        this.updateView();
    }

    public void setSpecialTiles() {
        // Set up imageIcons for special tiles
        ImageIcon centerIcon = new ImageIcon(getClass().getResource("/centerTile.png"));
        boardCells[7][7].setIcon(centerIcon); // Highlight center tile for first move requirement

        ImageIcon dLSIcon = new ImageIcon(getClass().getResource("/doubleLetterScore.png"));
        boardCells[3][0].setIcon(dLSIcon);
        boardCells[11][0].setIcon(dLSIcon);
        boardCells[6][2].setIcon(dLSIcon);
        boardCells[8][2].setIcon(dLSIcon);
        boardCells[0][3].setIcon(dLSIcon);
        boardCells[7][3].setIcon(dLSIcon);
        boardCells[14][3].setIcon(dLSIcon);
        boardCells[2][6].setIcon(dLSIcon);
        boardCells[6][6].setIcon(dLSIcon);
        boardCells[8][6].setIcon(dLSIcon);
        boardCells[12][6].setIcon(dLSIcon);
        boardCells[3][7].setIcon(dLSIcon);
        boardCells[11][7].setIcon(dLSIcon);
        boardCells[2][8].setIcon(dLSIcon);
        boardCells[6][8].setIcon(dLSIcon);
        boardCells[8][8].setIcon(dLSIcon);
        boardCells[12][8].setIcon(dLSIcon);
        boardCells[0][11].setIcon(dLSIcon);
        boardCells[7][11].setIcon(dLSIcon);
        boardCells[14][11].setIcon(dLSIcon);
        boardCells[6][12].setIcon(dLSIcon);
        boardCells[8][12].setIcon(dLSIcon);
        boardCells[3][14].setIcon(dLSIcon);
        boardCells[11][14].setIcon(dLSIcon);

        ImageIcon tLSIcon = new ImageIcon(getClass().getResource("/tripleLetterScore.png"));
        boardCells[5][1].setIcon(tLSIcon);
        boardCells[9][1].setIcon(tLSIcon);
        boardCells[1][5].setIcon(tLSIcon);
        boardCells[5][5].setIcon(tLSIcon);
        boardCells[9][5].setIcon(tLSIcon);
        boardCells[13][5].setIcon(tLSIcon);
        boardCells[1][9].setIcon(tLSIcon);
        boardCells[5][9].setIcon(tLSIcon);
        boardCells[9][9].setIcon(tLSIcon);
        boardCells[13][9].setIcon(tLSIcon);
        boardCells[5][13].setIcon(tLSIcon);
        boardCells[9][13].setIcon(tLSIcon);

        ImageIcon dWSIcon = new ImageIcon(getClass().getResource("/doubleWordScore.png"));
        boardCells[1][1].setIcon(dWSIcon);
        boardCells[13][1].setIcon(dWSIcon);
        boardCells[2][2].setIcon(dWSIcon);
        boardCells[12][2].setIcon(dWSIcon);
        boardCells[3][3].setIcon(dWSIcon);
        boardCells[11][3].setIcon(dWSIcon);
        boardCells[4][4].setIcon(dWSIcon);
        boardCells[10][4].setIcon(dWSIcon);
        boardCells[4][10].setIcon(dWSIcon);
        boardCells[10][10].setIcon(dWSIcon);
        boardCells[3][11].setIcon(dWSIcon);
        boardCells[11][11].setIcon(dWSIcon);
        boardCells[2][12].setIcon(dWSIcon);
        boardCells[12][12].setIcon(dWSIcon);
        boardCells[1][13].setIcon(dWSIcon);
        boardCells[13][13].setIcon(dWSIcon);

        ImageIcon tWSIcon = new ImageIcon(getClass().getResource("/tripleWordScore.png"));
        boardCells[0][0].setIcon(tWSIcon);
        boardCells[7][0].setIcon(tWSIcon);
        boardCells[14][0].setIcon(tWSIcon);
        boardCells[0][7].setIcon(tWSIcon);
        boardCells[14][7].setIcon(tWSIcon);
        boardCells[0][14].setIcon(tWSIcon);
        boardCells[7][14].setIcon(tWSIcon);
        boardCells[14][14].setIcon(tWSIcon);
    }

    public void setHandTiles() {
        List<Tile> hand = model.getCurrentPlayer().getHand();
        for (int i = 0; i < model.getCurrentPlayer().getHand().size(); i++) {
            char letter = hand.get(i).getTileChar();
            String path = "src/tile" + Character.toUpperCase(letter) + ".png";
            ImageIcon Icon = new ImageIcon(path);
            handTiles[i].setIcon(Icon);
        }

    }

    public static void main(String[] args) {
        new ScrabbleView();
    }

}
