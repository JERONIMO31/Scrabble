import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
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
    private ScrabbleModel model;
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
        else if (command.equals("P")){
            handlePlayButton();
        }
        else if (command.equals("HELP")){
            view.showHelp();
        }
        else if (command.equals("RGSP")){
            this.playedTiles = new ArrayList<>();
            this.selectedTile = null;
            model.resetGame();
        }
        else if (command.equals("RGNP")){
            this.playedTiles = new ArrayList<>();
            this.selectedTile = null;
            view.showEnd();
            model = new ScrabbleModel(view);
        }
        else if (command.equals("S")){
            this.playedTiles = new ArrayList<>();
            this.selectedTile = null;
            model.skip();
        }
    }

    private PlayedTile getPlayedTileAtXY(int x, int y){
        for (PlayedTile tile : playedTiles){
            if (x == tile.x && y == tile.y){
                return tile;
            }
        }
        return null;
    }

    private void handlePlayButton() {

        if (playedTiles.isEmpty()){
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
        int  xFinishIndex;
        int  yFinishIndex;

        if (playedTiles.size() == 1){
            if (!board.isEmpty(firstTile.x - 1, firstTile.y) || !board.isEmpty(firstTile.x + 1, firstTile.y)){
                direction = 'R';
            } else if (!board.isEmpty(firstTile.x, firstTile.y - 1) || !board.isEmpty(firstTile.x, firstTile.y + 1)){
                direction = 'D';
            }
            else {
                handleIllegalMove();
                return;
            }
        }
        else {
            if (firstTile.x == playedTiles.get(1).x){
                direction = 'D';
            }
            else {direction = 'R';}
        }
        if (direction == 'D') {
            yIndex = firstTile.y + 1;
            xIndex = firstTile.x;
            while (!board.isEmpty(xIndex, yIndex) || getPlayedTileAtXY(xIndex, yIndex) != null) {
                if (!board.isEmpty(xIndex, yIndex)) {
                    word.append(board.getTile(xIndex, yIndex).getTileChar());
                }
                else {
                    word.append(getPlayedTileAtXY(xIndex, yIndex).letter);
                }
                yIndex++;
            }
            xFinishIndex = xIndex;
            yFinishIndex = yIndex - 1;
            yIndex = firstTile.y - 1;
            while (!board.isEmpty(xIndex, yIndex) || getPlayedTileAtXY(xIndex, yIndex) != null) {

                if (!board.isEmpty(xIndex, yIndex)) {
                    word.insert(0, board.getTile(xIndex, yIndex).getTileChar());
                }
                else {
                    word.insert(0, word.append(getPlayedTileAtXY(xIndex, yIndex).letter));
                }
                yIndex--;
            }
            xStartIndex = xIndex;
            yStartIndex = yIndex + 1;

        } else {
            yIndex = firstTile.y;
            xIndex = firstTile.x + 1;
            while (!board.isEmpty(xIndex, yIndex) || getPlayedTileAtXY(xIndex, yIndex) != null) {
                if (!board.isEmpty(xIndex, yIndex)) {
                    word.append(board.getTile(xIndex, yIndex).getTileChar());
                }
                else {
                    word.append(getPlayedTileAtXY(xIndex, yIndex).letter);
                }
                xIndex++;
            }
            xFinishIndex = xIndex - 1;
            yFinishIndex = yIndex;
            xIndex = firstTile.x - 1;
            while (!board.isEmpty(xIndex, yIndex) || getPlayedTileAtXY(xIndex, yIndex) != null) {
                if (!board.isEmpty(xIndex, yIndex)) {
                    word.insert(0, board.getTile(xIndex, yIndex).getTileChar());
                }
                else {
                    word.insert(0, word.append(getPlayedTileAtXY(xIndex, yIndex).letter));
                }
                xIndex--;
            }
            xStartIndex = xIndex + 1;
            yStartIndex = yIndex;
        }

        for (PlayedTile tile : playedTiles){
            if (direction == 'D'){
                if (tile.x != xStartIndex){
                    handleIllegalMove();
                    return;
                }
                if (tile.y > yFinishIndex || tile.y < yStartIndex){
                    handleIllegalMove();
                    return;
                }
            }
            else {
                if (tile.y != yStartIndex){
                    handleIllegalMove();
                    return;
                }
                if (tile.x > xFinishIndex || tile.x < xStartIndex){
                    handleIllegalMove();
                    return;
                }
            }
        }

        if (!model.makeMove(xStartIndex, yStartIndex, direction, word.toString())){
            handleIllegalMove();
        }
        this.playedTiles = new ArrayList<>();
        this.selectedTile = null;
    }

    private void handleBoardButton(int x, int y){
        if (!model.getBoard().isEmpty(x, y)){
            return;
        }
        else if (selectedTile == null){
            PlayedTile tile = getPlayedTileAtXY(x, y);
            if (tile != null){
                view.removeTempTile(x, y, tile.handIndex);
                for (int i = 0; i < playedTiles.size(); i++){
                    if (playedTiles.get(i).x == x && playedTiles.get(i).y == y){
                        playedTiles.remove(i);
                        break;
                    }
                }
            }
        }
        else {
            view.addTempTile(selectedTile.letter, x, y, selectedTile.handIndex);
            selectedTile.x = x;
            selectedTile.y = y;
            playedTiles.add(selectedTile);
            selectedTile = null;
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

    private void handleIllegalMove(){
        this.playedTiles = new ArrayList<>();
        this.selectedTile = null;
        JOptionPane.showMessageDialog(view.getFrame(), "This move is illegal! \n Try again!");
        view.updateView();
    }

}
