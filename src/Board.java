package src;

public class Board {
    private Tile[][] board;

    public Board(){
        board = new Tile[15][15];

    }

    public void addLetter(int x, int y, Tile tile){
        if (board[x][y].isEmpty()){
            board[x][y] = tile;
        }
        else {
            throw new IllegalArgumentException("Tile at " + x + ", " + y + " is already taken with a " + board[x][y].getTileChar());
        }
    }

    public Tile getTile(int x, int y){
        return board[x][y];
    }

    public boolean isEmtpy(int x, int y){
        return board[x][y].isEmpty();
    }
}
