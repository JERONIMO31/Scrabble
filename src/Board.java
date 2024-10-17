package src;

public class Board {
    private static Tile[][] board;

    public Board(){
        board = new Tile[15][15];

    }

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

    public Tile getTile(int x, int y){
        return board[x-1][y-1];
    }

    public boolean isEmpty(int x, int y){
        return board[x-1][y-1] == null;
    }

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
