public class Board {
    private Tile[][] board;

    /**
     * Constructor for the Board class.
     * Initializes a 15x15 board with all positions set to null (empty).
     */
    public Board() {
        board = new Tile[15][15];  // Initialize an empty 15x15 board
    }

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
        return board[x][y];  // Return the tile at the given position
    }

    /**
     * Checks if the position at the specified coordinates is empty.
     *
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @return true if the position is empty (null), false if occupied by a tile
     */
    public boolean isEmpty(int x, int y) {
        return board[x][y] == null;  // Return true if the position is empty
    }
}
