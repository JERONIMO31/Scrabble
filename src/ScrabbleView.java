package src;

import java.util.*;

public class ScrabbleView {
    public ScrabbleView(){}

    public void showBoard(Board board, Player player, Bag bag, List<Player> players){
        for (Player p : players){
            System.out.print(p.getName() + ": " + p.getScore());
        }
        System.out.print(board.getBoardView());

    }

}
