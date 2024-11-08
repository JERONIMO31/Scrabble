import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UnitTesting {
    private static ScrabbleModel game;
    private List<String> Affectedwords;
    /**
            Tests the scoring system of the game
     */
    @Test
    void ScoreTesting(){
        game = new ScrabbleModel();
        game.addPlayer("A");
        game.addPlayer("B");
        game.getCurrentPlayer().SethandTest();
        game.skip();
        game.getCurrentPlayer().SethandTest();
        game.skip();
        Affectedwords = new ArrayList<>();
        Assertions.assertEquals(8, game.Updatescoretest(Affectedwords,"hello"));
        Affectedwords.add("hello");
        game.skip();
        Assertions.assertEquals(12,game.Updatescoretest(Affectedwords,"be"));
        game.skip();
        Affectedwords = new ArrayList<>();
        Assertions.assertEquals(21,game.Updatescoretest(Affectedwords,"aboriginal"));
        game.skip();
        Assertions.assertEquals(13,game.Updatescoretest(Affectedwords,"a"));
        game.skip();
        Assertions.assertEquals(33,game.Updatescoretest(Affectedwords,"zus"));

    }
         /**
     Tests the word validity system of the game and makes sure words given are within the dictionary given
     */

    @Test
    void Wordvalidity(){
        game = new ScrabbleModel();
        Assertions.assertFalse(game.isWord("assdsd"));
        Assertions.assertTrue(game.isWord("a"));
        Assertions.assertFalse(game.isWord("aaaa"));
        Assertions.assertFalse(game.isWord("outstandings"));
        Assertions.assertTrue(game.isWord("outstanding"));
        Assertions.assertTrue(game.isWord("zoophilia"));
        Assertions.assertFalse(game.isWord("zoophilib"));
        Assertions.assertFalse(game.isWord("zoophilias"));
    }
}