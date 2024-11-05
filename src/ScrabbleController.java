import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;

class PlayedTile{
    public int x;
    public int y;
    public char letter;
    public int handIndex;
}

public class ScrabbleController implements ActionListener {
    private final ScrabbleModel model;
    private final ScrabbleView view;
    private List<PlayedTile> playedTiles;
    private PlayedTile selectedTile;


    public ScrabbleController(ScrabbleModel model, ScrabbleView view) {
        this.model = model;
        this.view = view;
        this.playedTiles = new ArrayList<>();
        this.selectedTile = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] position = e.getActionCommand().split(" ");
        String command = position[0];

        if (command.equals("B")){
            handleBoardButton(Integer.parseInt(position[1]), Integer.parseInt(position[2]));
        }
        else if (command.equals("H")){
            handleHandButton(Integer.parseInt(position[1]));
        }
    }

    private void handleBoardButton(int x, int y){
        if (!model.getBoard().isEmpty(x, y)){
            return;
        }
        else if (selectedTile == null){
            for (PlayedTile tile : playedTiles){
                if (x == tile.x && y == tile.y){
                    view.removeTempTile(x, y, tile.handIndex);
                    return;
                }
            }
        }
        else {
            view.addTempTile(selectedTile.letter, x, y, selectedTile.handIndex);
            selectedTile.x = x;
            selectedTile.y = y;
            playedTiles.add(selectedTile);
        }
    }

    private void handleHandButton(int index){
        for (PlayedTile tile : playedTiles) {
            if (index == tile.handIndex) {
                return;
            }
        }
        selectedTile = new PlayedTile();
        selectedTile.handIndex = index;
        selectedTile.letter = model.getCurrentPlayer().getHand().get(index).getTileChar();
    }
//
//        // Add event listeners to view components
//        // Add listeners for boardPanel buttons
//        JButton[][] boardCells = view.getBoardCells();
//        for (int i = 0; i < boardCells.length; i++) {
//            for (int j = 0; j < boardCells[i].length; j++) {
//                final int row = i;
//                final int col = j;
//                boardCells[i][j].addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        if (model.hasSelectedTile()) {
//                            model.placeTile(row, col, model.getSelectedTile());
//                            view.updateBoard(row, col, model.getSelectedTile());
//                            model.clearSelectedTile();
//                        }
//                    }
//                });
//            }
//        }
//
//        // Add listeners for playerHandPanel buttons
//        JButton[] playerHandTiles = view.getHandTiles();
//        for (int i = 0; i < playerHandTiles.length; i++) {
//            final int tileIndex = i;
//            playerHandTiles[i].addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    Tile selectedTile = model.selectTileFromRack(tileIndex);
//                    view.highlightHandTile(tileIndex);
//                }
//            });
//        }
//
//        // Add listeners for controlPanel
//        view.getPlayWordButton().addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Validate the word, update the score, and reset the board and rack if valid
//                if (model.validateWord()) {
//                    model.updateScore();
//                    view.updateScoreDisplay(model.getPlayerScore());
//                    model.resetBoard();
//                    view.resetBoardDisplay();
//                    view.updateRack(model.getPlayerRack());
//                }
//                else {
//                    JOptionPane.showMessageDialog(view.getFrame(), "Invalid word");
//                }
//            }
//        });
//    }
//
//
//    }
//
//    public void startGame() {
//        System.out.print("How many players? > ");
//        int numberOfPlayers = this.reader.nextInt();
//        this.reader.nextLine();
//        if (numberOfPlayers > 0 && numberOfPlayers <= 4) {
//            for(int i = 0; i < numberOfPlayers; ++i) {
//                System.out.print("Enter player " + (i + 1) + "'s name > ");
//                String name = this.reader.nextLine();
//                this.model.addPlayer(name);
//            }
//
//            this.model.play();
//        } else {
//            throw new IllegalArgumentException("Number of players must be greater than 0 but less then 4");
//        }
//    }

}
