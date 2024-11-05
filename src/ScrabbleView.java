import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.List;
import java.awt.*;

public class ScrabbleView extends JFrame {
    ScrabbleModel model;
    private final JPanel boardPanel;
    private final JButton[][] boardCells;
    private final JPanel playerHandPanel;
    private final JButton[] handTiles;
    private final JTextField playerName;
    private final JPanel scorePanel;
    private final JLabel scoreLabel;
    private final JPanel controlPanel;
    private final JButton playWordButton;
    private final JMenuItem resetGameItem, resetGameSPItem, helpItem;
    private String scoreStr;


    public ScrabbleView(ScrabbleModel model) throws FileNotFoundException {
        // Initialize JFrame
        this.scoreStr = "";
        this.setTitle("Scrabble");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);

        model = new ScrabbleModel(this);
        
        ScrabbleController sc = new ScrabbleController(this.model, this);

        // Initialize boardPanel
        boardPanel = new JPanel(new GridLayout(15, 15));
        boardCells = new JButton[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                JButton boardCell = new JButton();
                boardCell.setPreferredSize(new Dimension(40, 40));
                boardCell.setActionCommand("B " + i + " " + j);
                boardCell.addActionListener(sc);
                boardCells[i][j] = boardCell;
                boardPanel.add(boardCell);
            }
        }

        // Initialize playerHandPanel
        playerHandPanel = new JPanel(new FlowLayout());
        handTiles = new JButton[7];
        playerName = new JTextField(model.getCurrentPlayer().getName() + "'s turn:");
        playerHandPanel.add(playerName);
        for (int i = 0; i < 7; i++) {
            handTiles[i] = new JButton(" ");
            handTiles[i].setPreferredSize(new Dimension(40, 40));
            handTiles[i].setActionCommand("H " + i);
            handTiles[i].addActionListener(sc);
            playerHandPanel.add(handTiles[i]);
        }

        // Initialize scorePanel
        scorePanel = new JPanel();
        scoreStr = getScoreString();
        scoreLabel = new JLabel(scoreStr);
        scorePanel.add(scoreLabel);

        // Initialize controlPanel
        controlPanel = new JPanel();
        playWordButton = new JButton("Play Word");
        playWordButton.setActionCommand("P");
        playWordButton.addActionListener(sc);
        controlPanel.add(playWordButton);

        // Add gameMenu and items
        JMenu gameMenu = new JMenu("Game");
        resetGameSPItem = new JMenuItem("Restart game (with same players)");
        resetGameItem = new JMenuItem("New game (with new players)");
        helpItem = new JMenuItem("Help");

        resetGameSPItem.setActionCommand("HELP");
        resetGameSPItem.addActionListener(sc);

        resetGameSPItem.setActionCommand("RGSP");
        resetGameSPItem.addActionListener(sc);

        resetGameItem.setActionCommand("RGNP");
        resetGameItem.addActionListener(sc);

        gameMenu.add(resetGameItem);
        gameMenu.add(resetGameSPItem);
        gameMenu.add(helpItem);

        // Add panels to the frame
        add(boardPanel, BorderLayout.CENTER);
        add(playerHandPanel, BorderLayout.SOUTH);
        add(scorePanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String getScoreString() {
        for (Player player : this.model.getPlayers()) {
            this.scoreStr = "Scores:\n" + player.getName() + ": " + player.getScore() + "\n";
        }
        return scoreStr;
    }

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

        // Update the current player
        playerName.setText(model.getCurrentPlayer().getName());
    }

    public void showEnd(Board board, List<Player> players) {
        Player winner = players.getFirst();

        for (Player p : players) {
            if (p.getScore() > winner.getScore()) {
                winner = p;
            }
        }

        JOptionPane.showMessageDialog(this, "The winner is: " + winner.getName() + "!");
        System.out.println("The winner is: " + winner.getName() + "!");
    }

    public void showHelp() {
        JOptionPane.showMessageDialog(this, """
                To place a tile, select it from your hand then select the space on the board in which you'd like to place it.
                You can remove a previously placed tile (in the same turn) by selecting it again from the board.
                For the first move it must be on the coordinates 8 8, it doesn't have to begin there, as long as it passes through.
                A player can skip their turn by clicking the 'Skip Turn' button.
                The game can be restarted with the current players or a brand new game by using the 'Game' menu.""");

    }

    public JFrame getFrame() {
        return this;
    }

    public JButton[][] getBoardCells() {
        return boardCells;
    }

    public JButton[] getHandTiles() {
        return handTiles;
    }

    public JButton getPlayWordButton() {
        return playWordButton;
    }

    public JLabel getScoreLabel() {
        return scoreLabel;
    }

    public JMenuItem getResetGameItem() {
        return resetGameItem;
    }

    public JMenuItem getResetGameSPItem() {
        return resetGameSPItem;
    }

    public JMenuItem getHelpItem() {
        return helpItem;
    }

    public void highlightHandTile(int tileIndex) {
        for (JButton tile : handTiles) {
            tile.setBackground(Color.white);
        }
        if (tileIndex >= 0 && tileIndex < handTiles.length) {
            handTiles[tileIndex].setBackground(Color.blue);
        }
    }

    public void removedTempTile(int x, int y, int handIndex) {
        boardCells[x][y].setText(" ");
        boardCells[x][y].setEnabled(true);

    }

    public void addTempTile(char tile, int x, int y, int handIndex) {
        boardCells[x][y].setText(String.valueOf(tile));
        boardCells[x][y].setEnabled(false);
    }
}
