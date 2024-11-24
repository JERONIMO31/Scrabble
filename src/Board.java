import javax.swing.*;
import java.util.ArrayList;

public class Board {
    private Tile[][] board;
    private static String[][] multipliers;

    /**
     * Constructor for the Board class.
     * Initializes a 15x15 board with all positions set to null (empty).
     */
    public Board() {
        multipliers = new String[15][15]; 
        board = new Tile[15][15];  // Initialize an empty 15x15 board
        setMultiplier();
    }
    public Tile[][] getBoard(){return board;}
    /**
     * Adds a tile to the board at the specified coordinates.
     *
     * @param x the x-coordinate of the tile to be placed
     * @param y the y-coordinate of the tile to be placed
     * @param tile the Tile object to place on the board
     * @throws IllegalArgumentException if the position is out of bounds or already occupied
     */
    public void addLetter(int x, int y, Tile tile) {
        // Check if the coordinates are within the valid range (0-14 for x, 0-14 for y)
        if (x <= 14 && x >= 0 && y <= 14 && y >= 0) {
            // Check if the specified board position is empty
            if (board[x][y] == null) {
                board[x][y] = tile;  // Place the tile on the board
            } else {
                // Throw an exception if the tile position is already occupied
                throw new IllegalArgumentException(
                        "Tile at " + x + ", " + y + " is already taken with a " + board[x][y].getTileChar());
            }
        } else {
            // Throw an exception if the coordinates are out of range
            throw new IllegalArgumentException("Index is out of range :(");
        }
    }

    /**
     * Retrieves the tile at the specified coordinates on the board.
     *
     * @param x the x-coordinate of the tile to retrieve
     * @param y the y-coordinate of the tile to retrieve
     * @return the Tile object at the specified position, or null if the position is empty
     */
    public Tile getTile(int x, int y) {
        if (x <= 14 && x >= 0 && y <= 14 && y >= 0) {
            return board[x][y];  // Return the tile at the given position
        }
        else {return null;}

    }

    /**
     * Checks if the position at the specified coordinates is empty.
     *
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @return true if the position is empty (null), false if occupied by a tile
     */
    public boolean isEmpty(int x, int y) {
        if (x <= 14 && x >= 0 && y <= 14 && y >= 0) {
            return board[x][y] == null;  // Return true if the position is empty
        }
        else {return true;}
    }

    private void setMultiplier(){

        multipliers[3][0] = "DL";
        multipliers[11][0] = "DL";
        multipliers[6][2] = "DL";
        multipliers[8][2] = "DL";
        multipliers[0][3] = "DL";
        multipliers[7][3] = "DL";
        multipliers[14][3] = "DL";
        multipliers[2][6] = "DL";
        multipliers[6][6] = "DL";
        multipliers[8][6] = "DL";
        multipliers[12][6] = "DL";
        multipliers[3][7] = "DL";
        multipliers[11][7] = "DL";
        multipliers[2][8] = "DL";
        multipliers[6][8] = "DL";
        multipliers[8][8] = "DL";
        multipliers[12][8] = "DL";
        multipliers[0][11] = "DL";
        multipliers[7][11] = "DL";
        multipliers[14][11] = "DL";
        multipliers[6][12] = "DL";
        multipliers[8][12] = "DL";
        multipliers[3][14] = "DL";
        multipliers[11][14] = "DL";
        
        multipliers[5][1] = "TL";
        multipliers[9][1] = "TL";
        multipliers[1][5] = "TL";
        multipliers[5][5] = "TL";
        multipliers[9][5] = "TL";
        multipliers[13][5] = "TL";
        multipliers[1][9] = "TL";
        multipliers[5][9] = "TL";
        multipliers[9][9] = "TL";
        multipliers[13][9] = "TL";
        multipliers[5][13] = "TL";
        multipliers[9][13] = "TL";

        multipliers[1][1] = "DW";
        multipliers[13][1] = "DW";
        multipliers[2][2] = "DW";
        multipliers[12][2] = "DW";
        multipliers[3][3] = "DW";
        multipliers[11][3] = "DW";
        multipliers[4][4] = "DW";
        multipliers[10][4] = "DW";
        multipliers[4][10] = "DW";
        multipliers[10][10] = "DW";
        multipliers[3][11] = "DW";
        multipliers[11][11] = "DW";
        multipliers[2][12] = "DW";
        multipliers[12][12] = "DW";
        multipliers[1][13] = "DW";
        multipliers[13][13] = "DW";

        multipliers[0][0] = "TW";
        multipliers[7][0] = "TW";
        multipliers[14][0] = "TW";
        multipliers[0][7] = "TW";
        multipliers[14][7] = "TW";
        multipliers[0][14] = "TW";
        multipliers[7][14] = "TW";
        multipliers[14][14] = "TW";
    }

    public static String getMultiplier(int x, int y){
        if (x <= 14 && x >= 0 && y <= 14 && y >= 0) {
            String multiplier = multipliers[x][y];
            if (multiplier == null) {
                return "normal";
            }
            return multiplier;
        }
        else {return "normal";}
    }
}
