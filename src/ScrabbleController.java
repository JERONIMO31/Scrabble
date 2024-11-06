import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * A class representing a tile played by the user in Scrabble, with coordinates on the board and the letter it contains.
 */
class PlayedTile {
    public int x;         // x-coordinate on the board
    public int y;         // y-coordinate on the board
    public char letter;   // character representing the tile letter
    public int handIndex; // index of the tile in the player's hand
}

public class ScrabbleController implements ActionListener {
    private ScrabbleModel model;
    private final ScrabbleView view;
    private List<PlayedTile> playedTiles;
    private PlayedTile selectedTile;

    /**
     * Constructor for the ScrabbleController class.
     * Initializes the controller with the given model and view.
     *
     * @param model The model representing the game logic.
     * @param view  The view displaying the game interface.
     */
    public ScrabbleController(ScrabbleModel model, ScrabbleView view) {
        this.model = model;
        this.view = view;
        this.playedTiles = new ArrayList<>();
        this.selectedTile = null;
    }

    /**
     * Resets model
     *
     * @param model the model representing the game logic.
     */
    public void setModel(ScrabbleModel model){
        this.model = model;
    }

    /**
     * Handles user actions, routing based on the action command.
     * The command format determines whether a board, hand, play, or other button is activated.
     *
     * @param e ActionEvent that triggered the action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String[] position = e.getActionCommand().split(" ");
        String command = position[0];

        switch (command) {
            case "B" -> handleBoardButton(Integer.parseInt(position[1]), Integer.parseInt(position[2]));
            case "H" -> handleHandButton(Integer.parseInt(position[1]));
            case "P" -> handlePlayButton();
            case "HELP" -> view.showHelp();
            case "RGSP" -> {
                this.playedTiles = new ArrayList<>();
                this.selectedTile = null;
                model.resetGame(); // Reset same players
            }
            case "RGNP" -> {
                this.playedTiles = new ArrayList<>();
                this.selectedTile = null;
                view.showEnd();
                view.resetGame(); // Reset new players
            }
            case "S" -> {
                this.playedTiles = new ArrayList<>();
                this.selectedTile = null;
                model.skip();
            }
        }
    }

    /**
     * Retrieves a PlayedTile at a specific x and y coordinate on the board.
     *
     * @param x The x-coordinate on the board.
     * @param y The y-coordinate on the board.
     * @return The PlayedTile at the specified coordinates, or null if none found.
     */
    private PlayedTile getPlayedTileAtXY(int x, int y) {
        for (PlayedTile tile : playedTiles) {
            if (x == tile.x && y == tile.y) {
                return tile;
            }
        }
        return null;
    }

    /**
     * Handles the play action by validating the move, determining the word direction (down or right),
     * building the word, and checking for validity based on model rules.
     * If the move is illegal, calls handleIllegalMove().
     */
    private void handlePlayButton() {

        if (playedTiles.isEmpty()) {
            handleIllegalMove();
            return;
        }

        PlayedTile firstTile = playedTiles.getFirst();

        char direction;

        StringBuilder word = new StringBuilder(String.valueOf(firstTile.letter));

        Board board = model.getBoard();

        int xIndex;
        int yIndex;

        int xStartIndex;
        int yStartIndex;
        int xFinishIndex;
        int yFinishIndex;

        // Determine play direction and validates for single tile play
        if (playedTiles.size() == 1) {
            if (!board.isEmpty(firstTile.x - 1, firstTile.y) || !board.isEmpty(firstTile.x + 1, firstTile.y)) {
                direction = 'R';
            } else if (!board.isEmpty(firstTile.x, firstTile.y - 1) || !board.isEmpty(firstTile.x, firstTile.y + 1)) {
                direction = 'D';
            } else {
                handleIllegalMove();
                return;
            }
        } else {
            // Check if the direction is down or right based on tile placements
            direction = (firstTile.x == playedTiles.get(1).x) ? 'D' : 'R';
        }

        // Construct the word and calculate start/end indexes based on direction
        if (direction == 'D') {
            yIndex = firstTile.y + 1;
            xIndex = firstTile.x;
            while (!board.isEmpty(xIndex, yIndex) || getPlayedTileAtXY(xIndex, yIndex) != null) {
                word.append(!board.isEmpty(xIndex, yIndex) ?
                        board.getTile(xIndex, yIndex).getTileChar() :
                        getPlayedTileAtXY(xIndex, yIndex).letter);
                yIndex++;
            }
            xFinishIndex = xIndex;
            yFinishIndex = yIndex - 1;
            yIndex = firstTile.y - 1;
            while (!board.isEmpty(xIndex, yIndex) || getPlayedTileAtXY(xIndex, yIndex) != null) {
                word.insert(0, !board.isEmpty(xIndex, yIndex) ?
                        board.getTile(xIndex, yIndex).getTileChar() :
                        getPlayedTileAtXY(xIndex, yIndex).letter);
                yIndex--;
            }
            xStartIndex = xIndex;
            yStartIndex = yIndex + 1;
        } else {
            yIndex = firstTile.y;
            xIndex = firstTile.x + 1;
            while (!board.isEmpty(xIndex, yIndex) || getPlayedTileAtXY(xIndex, yIndex) != null) {
                word.append(!board.isEmpty(xIndex, yIndex) ?
                        board.getTile(xIndex, yIndex).getTileChar() :
                        getPlayedTileAtXY(xIndex, yIndex).letter);
                xIndex++;
            }
            xFinishIndex = xIndex - 1;
            yFinishIndex = yIndex;
            xIndex = firstTile.x - 1;
            while (!board.isEmpty(xIndex, yIndex) || getPlayedTileAtXY(xIndex, yIndex) != null) {
                word.insert(0, !board.isEmpty(xIndex, yIndex) ?
                        board.getTile(xIndex, yIndex).getTileChar() :
                        getPlayedTileAtXY(xIndex, yIndex).letter);
                xIndex--;
            }
            xStartIndex = xIndex + 1;
            yStartIndex = yIndex;
        }

        // Ensures all played tiles are part of constructed word
        for (PlayedTile tile : playedTiles) {
            if (direction == 'D' && (tile.x != xStartIndex || tile.y > yFinishIndex || tile.y < yStartIndex) ||
                    direction == 'R' && (tile.y != yStartIndex || tile.x > xFinishIndex || tile.x < xStartIndex)) {
                handleIllegalMove();
                return;
            }
        }

        // Make the move on the model, handle illegal move if unsuccessful
        if (!model.makeMove(xStartIndex, yStartIndex, direction, word.toString())) {
            handleIllegalMove();
        }
        this.playedTiles = new ArrayList<>();
        this.selectedTile = null;
    }

    /**
     * Handles clicking a board button to place or remove tiles.
     * Adds or removes a tile at the specified coordinates, depending on if a tile is selected.
     *
     * @param x The x-coordinate on the board.
     * @param y The y-coordinate on the board.
     */
    private void handleBoardButton(int x, int y) {
        if (!model.getBoard().isEmpty(x, y)) {
            return; // Return if the board space is occupied
        }
        if (selectedTile == null) {
            PlayedTile tile = getPlayedTileAtXY(x, y);
            if (tile != null) {
                view.removeTempTile(x, y, tile.handIndex);
                playedTiles.remove(tile);
            }
        } else {
            PlayedTile tile = getPlayedTileAtXY(x, y);
            if (tile != null) {
                view.removeTempTile(x, y, tile.handIndex);
                playedTiles.remove(tile);
            }
            view.addTempTile(selectedTile.letter, x, y, selectedTile.handIndex);
            selectedTile.x = x;
            selectedTile.y = y;
            playedTiles.add(selectedTile);
            selectedTile = null;
        }
    }

    /**
     * Handles selecting a tile from the player's hand to place on the board.
     * Prevents reselecting already played tiles.
     *
     * @param index The index of the tile in the player's hand.
     */
    private void handleHandButton(int index) {
        for (PlayedTile tile : playedTiles) {
            if (index == tile.handIndex) {
                return;
            }
        }
        selectedTile = new PlayedTile();
        selectedTile.handIndex = index;
        selectedTile.letter = model.getCurrentPlayer().getHand().get(index).getTileChar();
    }

    /**
     * Handles illegal moves by clearing played tiles and notifying the user.
     */
    private void handleIllegalMove() {
        this.playedTiles = new ArrayList<>();
        this.selectedTile = null;
        JOptionPane.showMessageDialog(view.getFrame(), "This move is illegal! \n Try again!");
        view.updateView();
    }
}
