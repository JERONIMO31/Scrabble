package src;

import java.util.*;

/**
 * The ScrabbleView class handles the display and user interaction components of the Scrabble game.
 * It provides methods to display the game board, player information, and game instructions.
 */
public class ScrabbleView {
    /**
     * Constructs a ScrabbleView object.
     * The view is responsible for displaying the game board and player information during the game.
     */
    public ScrabbleView(){}

    /**
     * Displays the current state of the board, the scores of all players, the current player's hand,
     * and whose turn it is.
     *
     * @param board   The current Scrabble game board.
     * @param player  The current player whose turn it is.
     * @param bag     The tile bag (though it is not currently displayed).
     * @param players The list of all players in the game with their respective scores.
     */
    public void showBoard(Board board, Player player, Bag bag, List<Player> players){
        for (Player p : players){
            System.out.println(p.getName() + ": " + p.getScore());
        }
        System.out.println("                              1  1  1  1  1  1 ");
        System.out.println("   1  2  3  4  5  6  7  8  9  0  1  2  3  4  5 ");
        System.out.println(board.getBoardView());
        System.out.println(player.getName()+"'s score is "+player.getScore()+ "!");
        System.out.println(player.getName() + "'s turn!\n");
        System.out.println(player.getHandView());
    }

    /**
     * Displays the final state of the board and the scores of all players.
     * It determines and announces the winner based on the highest score.
     *
     * @param board   The final Scrabble game board at the end of the game.
     * @param players The list of all players in the game.
     */
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

    /**
     * Displays help instructions for how to input a move and other valid game commands.
     * Used when the user enters the "help" command.
     */
    public void showHelp(){
        System.out.println("To move input: x y direction word");
        System.out.println("x y = x y coordinates of word start tile, direction = R or D (right or down), \n" +
                " word = word you want to play");
        System.out.println("Other commands: quit, reset, pass");
    }

}
