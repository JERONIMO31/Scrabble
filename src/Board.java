package src;

/**
 * The Board class represents the Scrabble game board, a 15x15 grid of tiles.
 * It allows adding letters to specific locations on the board, checking the contents
 * of a specific location, and displaying the current state of the board.
 */
public class Board {
    private static Tile[][] board;

    /**
     * Constructs a new Board object, creating a 15x15 grid for the Scrabble game.
     * Each position in the grid is initially empty (null).
     */
    public Board(){
        board = new Tile[15][15];

    }

    /**
     * Adds a tile to the board at the specified x and y coordinates.
     * The valid range is from 1 to 15.
     *
     * @param x    The x-coordinate (column) where the tile should be placed.
     * @param y    The y-coordinate (row) where the tile should be placed.
     * @param tile The Tile to be placed on the board.
     * @throws IllegalArgumentException If the coordinates are out of range (not between 1 and 15),
     *                                     or if the tile at the specified location is already occupied.
     */
    public void addLetter(int x, int y, Tile tile){
        if (x > 15 || x < 1 || y > 15 || y < 0){
            throw new IllegalArgumentException("Index is out of range :(");
        }
        if (board[x-1][y-1] == null){
            board[x-1][y-1] = tile;
        }
        else {
            throw new IllegalArgumentException("Tile at " + x + ", " + y + " is already taken with a " + board[x][y].getTileChar());
        }
    }

    /**
     * Gets the tile at the specified x and y coordinates on the board.
     *
     * @param x The x-coordinate (column) to retrieve the tile from.
     * @param y The y-coordinate (row) to retrieve the tile from.
     * @return The Tile at the specified coordinates, or null if the location is empty.
     */
    public Tile getTile(int x, int y){
        return board[x-1][y-1];
    }

    /**
     * Checks if the specified location on the board is empty.
     *
     * @param x The x-coordinate (column) to check.
     * @param y The y-coordinate (row) to check.
     * @return true if the specified location is empty, false if the location is occupied.
     */
    public boolean isEmpty(int x, int y){
        return board[x-1][y-1] == null;
    }

    /**
     * Returns a string representation of the current state of the board.
     * Empty positions are displayed as "[ ]", and filled positions display the tile's character.
     *
     * @return A string showing the current state of the board.
     */
    public String getBoardView(){
        StringBuilder view = new StringBuilder();
        for (int y = 0; y < 15; y++){
            for (int x = 0; x < 15; x++){
                if (board[x][y] == null){view.append("[ ]");}
                else {view.append("[").append(board[x][y].getTileChar()).append("]");}
            }
            view.append("\n");
        }
        return view.toString();
    }
}
