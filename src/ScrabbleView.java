package src;

import java.util.*;

public class ScrabbleView {
    public ScrabbleView(){}

    public void showBoard(Board board, Player player, Bag bag, List<Player> players){
        for (Player p : players){
            System.out.println(p.getName() + ": " + p.getScore());
        }
        System.out.println(board.getBoardView());
        System.out.println(player.getName() + "'s turn!\n");
        System.out.println(player.getHandView());
    }

    public void showEnd(Board board, List<Player> players){
        Player winner = players.getFirst();
        System.out.println(board.getBoardView());
        for (Player p : players){
            System.out.println(p.getName() + ": " + p.getScore());
            if (p.getScore() > winner.getScore()){
                winner = p;
            }
        }
        System.out.println("The winner is: " + winner.getName() + "!");
    }

    public void showHelp(){
        System.out.println("To move input: x y direction word");
        System.out.println("x y = x y coordinates of word start tile, direction = R or D (right or down), \n" +
                " word = word you want to play");
        System.out.println("Other commands: quit, reset, pass");
    }

}
