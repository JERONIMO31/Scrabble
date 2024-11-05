
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

public class ScrabbleView {
    public ScrabbleView() {
    }

    public void showBoard(Board board, Player player, Bag bag, List<Player> players) {
        Iterator var5 = players.iterator();

        PrintStream var10000;
        String var10001;
        while(var5.hasNext()) {
            Player p = (Player)var5.next();
            var10000 = System.out;
            var10001 = p.getName();
            var10000.println(var10001 + ": " + p.getScore());
        }

        System.out.println("                              1  1  1  1  1  1 ");
        System.out.println("   1  2  3  4  5  6  7  8  9  0  1  2  3  4  5 ");
        System.out.println(board.getBoardView());
        var10000 = System.out;
        var10001 = player.getName();
        var10000.println(var10001 + "'s score is " + player.getScore() + "!");
        System.out.println(player.getName() + "'s turn!\n");
        System.out.println(player.getHandView());
    }

    public void showEnd(Board board, List<Player> players) {
        Player winner = (Player)players.getFirst();
        System.out.println(board.getBoardView());
        Iterator var4 = players.iterator();

        while(var4.hasNext()) {
            Player p = (Player)var4.next();
            PrintStream var10000 = System.out;
            String var10001 = p.getName();
            var10000.println(var10001 + ": " + p.getScore());
            if (p.getScore() > winner.getScore()) {
                winner = p;
            }
        }

        System.out.println("The winner is: " + winner.getName() + "!");
    }

    public void showHelp() {
        System.out.println("To move input: x y direction word");
        System.out.println("x y = x y coordinates of word start tile, direction = R or D (right or down), \n word = word you want to play");
        System.out.println("For the first move it must begin on the coordinates 8 8");
        System.out.println("Other commands: quit, reset, pass");
    }
}
