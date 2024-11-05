
import javax.swing.*;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.awt.*;

public class ScrabbleView extends JFrame {
    private final JPanel boardPanel;
    private final JButton[][] boardCells;
    private final JPanel playerHandPanel;
    private final JButton[] handTiles;
    private final JPanel scorePanel;
    private final JLabel scoreLabel;
    private final JPanel controlPanel;
    private final JButton playWordButton;
    private final JMenuItem resetGameItem, resetGameSPItem;


    public ScrabbleView(ScrabbleModel model) {
        // Initialize JFrame
        this.setTitle("Scrabble");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);

        // Initialize boardPanel
        boardPanel = new JPanel(new GridLayout(15, 15));
        boardCells = new JButton[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                JButton boardCell = new JButton();
                boardCell.setPreferredSize(new Dimension(40, 40));
                boardCells[i][j] = boardCell;
                boardPanel.add(boardCell);
            }
        }

        // Initialize playerHandPanel
        playerHandPanel = new JPanel(new FlowLayout());
        handTiles = new JButton[7];
        playerHandPanel.add(new JTextField(model.getCurrentPlayer().getName() + "'s turn:"));
        for (int i = 0; i < 7; i++) {
            handTiles[i] = new JButton(" ");
            handTiles[i].setPreferredSize(new Dimension(40, 40));
            playerHandPanel.add(handTiles[i]);
        }

        // Initialize scorePanel
        scorePanel = new JPanel();
        String scoreStr = "Scores:\n";
        for (Player player : model.getAllPlayers()) {
            scoreStr += player.getName() + ": " + player.getScore() + "\n";
        }
        scoreLabel = new JLabel(scoreStr);
        scorePanel.add(scoreLabel);

        // Initialize controlPanel
        controlPanel = new JPanel();
        playWordButton = new JButton("Play Word");
        controlPanel.add(playWordButton);

        // Add gameMenu and items
        JMenu gameMenu = new JMenu("Game");
        resetGameSPItem = new JMenuItem("Restart game (with same players)");
        resetGameItem = new JMenuItem("New game (with new players)");
        gameMenu.add(resetGameItem);
        gameMenu.add(resetGameSPItem);

        // Add panels to the frame
        add(boardPanel, BorderLayout.CENTER);
        add(playerHandPanel, BorderLayout.SOUTH);
        add(scorePanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateBoard() {


        Iterator var5 = players.iterator();

        PrintStream var10000;
        String var10001;
        while(var5.hasNext()) {
            Player p = (Player)var5.next();
            var10000 = System.out;
            var10001 = p.getName();
            var10000.println(var10001 + ": " + p.getScore());
        }

        System.out.println("                              1  1  1  1  1  1 ");
        System.out.println("   1  2  3  4  5  6  7  8  9  0  1  2  3  4  5 ");
        System.out.println(board.getBoardView());
        var10000 = System.out;
        var10001 = player.getName();
        var10000.println(var10001 + "'s score is " + player.getScore() + "!");
        System.out.println(player.getName() + "'s turn!\n");
        System.out.println(player.getHandView());
    }

    public void showEnd(Board board, List<Player> players) {
        Player winner = players.getFirst();
        System.out.println(board.getBoardView());

        for (Player p : players) {
            System.out.println(p.getName() + ": " + p.getScore());
            if (p.getScore() > winner.getScore()) {
                winner = p;
            }
        }

        System.out.println("The winner is: " + winner.getName() + "!");
    }

    public void showHelp() {
        System.out.println("To move input: x y direction word");
        System.out.println("x y = x y coordinates of word start tile, direction = R or D (right or down), \n word = word you want to play");
        System.out.println("For the first move it must begin on the coordinates 8 8");
        System.out.println("Other commands: quit, reset, pass");
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

    public void highlightHandTile(int tileIndex) {
        for (JButton tile : handTiles) {
            tile.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }

        if (tileIndex >= 0 && tileIndex < handTiles.length) {
            handTiles[tileIndex].setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        }
    }
}
