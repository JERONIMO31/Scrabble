public class Board {
    private static Tile[][] board;

    public Board() {
        board = new Tile[15][15];
    }

    public void addLetter(int x, int y, Tile tile) {
        if (x <= 15 && x >= 1 && y <= 15 && y >= 0) {
            if (board[x - 1][y - 1] == null) {
                board[x - 1][y - 1] = tile;
            } else {
                throw new IllegalArgumentException("Tile at " + x + ", " + y + " is already taken with a " + board[x][y].getTileChar());
            }
        } else {
            throw new IllegalArgumentException("Index is out of range :(");
        }
    }

    public Tile getTile(int x, int y) {
        return board[x - 1][y - 1];
    }

    public boolean isEmpty(int x, int y) {
        return board[x - 1][y - 1] == null;
    }

}
