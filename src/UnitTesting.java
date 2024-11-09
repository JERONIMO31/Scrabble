import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class UnitTesting {
    private static ScrabbleModel game;
    private List<String> Affectedwords;
    private ScrabbleModel model;

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

    @Before
    public void setUp() {
        // Initialize the ScrabbleModel without GUI
        model = new ScrabbleModel();
        model.addPlayer("Player1"); // create players
        model.addPlayer("Player2");
    }

    // Helper method to set tiles in hand for testing
    private void setPlayerTiles(char... tiles) {
        Player player = model.getCurrentPlayer();
        player.getHand().clear();
        for (char tileChar : tiles) {
            player.getHand().add(new Tile(tileChar));
        }
    }

    @org.junit.Test
    public void testIsValid_ValidPlacement() {
        // Give Tiles
        setPlayerTiles('H', 'E', 'L', 'L', 'O');

        int x = 7;
        int y = 7;
        char direction = 'R';
        String word = "HELLO";

        boolean result = model.isValid(x, y, direction, word);
        assertTrue("The word placement should be valid", result);
    }

    @org.junit.Test
    public void testIsValid_InvalidPlacement() {
        // Give Tiles
        setPlayerTiles('I', 'N', 'V', 'A', 'L', 'I', 'D', 'W', 'O', 'R', 'D');

        int x = 0;
        int y = 0;
        char direction = 'D';
        String word = "INVALIDWORD";

        boolean result = model.isValid(x, y, direction, word);
        assertFalse("The word placement should be invalid", result);
    }

    @org.junit.Test
    public void testIsPossible_ValidPosition() {
        //Give Tiles
        setPlayerTiles('T', 'E', 'S', 'T');

        int x = 7;
        int y = 7;
        char direction = 'R';
        String word = "TEST";

        boolean result = model.isPossible(x, y, direction, word);
        assertTrue("The position should be possible for placement", result);
    }

    @org.junit.Test
    public void testIsPossible_InvlaidStartPosition() {
        // give tiles to player
        setPlayerTiles('T', 'E', 'S', 'T');

        int x = 8;
        int y = 8;
        char direction = 'R';
        String word = "TEST";

        boolean result = model.isPossible(x, y, direction, word);
        assertFalse("The position should be not possible for placement", result);
    }

    @org.junit.Test
    public void testIsPossible_InvalidPosition() {
        // Give the tiles to player
        setPlayerTiles('L', 'O', 'N', 'G', 'W', 'O', 'R', 'D');

        int x = 14;
        int y = 14;
        char direction = 'R';
        String word = "LONGWORD";

        boolean result = model.isPossible(x, y, direction, word);
        assertFalse("The position should be impossible for placement", result);

    }

    @org.junit.Test
    public void testMakeMove_FirstMoveValid() {
        // Set up the player tiles
        setPlayerTiles('h' ,'e', 'l', 'l', 'o');

        int x = 7;
        int y = 7;
        char direction = 'R';
        String word = "hello";

        boolean result = model.makeMove(x, y, direction, word);
        assertTrue("The move should be successful", result);

        Tile tileAt77 = model.getBoard().getTile(7, 7);
        assertNotNull("Tile at (7, 7) should not be null after move.", tileAt77);
        assertEquals("Tile at (7, 7) should be 'H'", 'H', tileAt77.getTileChar());

        Tile tileAt87 = model.getBoard().getTile(8, 7);
        assertNotNull("Tile at (7, 8) should not be null after move.", tileAt87);
        assertEquals("Tile at (7, 8) should be 'E'", 'E', tileAt87.getTileChar());

        Tile tileAt97 = model.getBoard().getTile(9, 7);
        assertNotNull("Tile at (9,7) should not be null after move.", tileAt97);
        assertEquals("Tile at (9,7) should be 'L'", 'L', tileAt97.getTileChar());

        Tile tileAt107 = model.getBoard().getTile(10, 7);
        assertNotNull("Tile at (10,7) should not be null after move.", tileAt107);
        assertEquals("Tile at (10,7) should be 'L'", 'L', tileAt107.getTileChar());

        Tile tileAt117 = model.getBoard().getTile(11, 7);
        assertNotNull("Tile at (11,7) should not be null after move.", tileAt117);
        assertEquals("Tile at (11,7) should be 'O'", 'O', tileAt117.getTileChar());

        //Test placement for downawrd direction
        setPlayerTiles('e', 'l', 'l', 'o');

        direction = 'D';

        result = model.makeMove(x, y, direction, word);
        assertTrue("The move should be successful", result);

        Tile tileAt78 = model.getBoard().getTile(7, 8);
        assertNotNull("Tile at (7, 8) should not be null after move.", tileAt78);
        assertEquals("Tile at (7, 8) should be 'E'", 'E', tileAt78.getTileChar());


        Tile tileAt79 = model.getBoard().getTile(7, 9);
        assertNotNull("Tile at (9,7) should not be null after move.", tileAt79);
        assertEquals("Tile at (9,7) should be 'L'", 'L', tileAt79.getTileChar());

        Tile tileAt710 = model.getBoard().getTile(7, 10);
        assertNotNull("Tile at (10,7) should not be null after move.", tileAt710);
        assertEquals("Tile at (10,7) should be 'L'", 'L', tileAt710.getTileChar());

        Tile tileAt711 = model.getBoard().getTile(7, 11);
        assertNotNull("Tile at (11,7) should not be null after move.", tileAt711);
        assertEquals("Tile at (11,7) should be 'O'", 'O', tileAt711.getTileChar());

        //make sure the other word is still there
        tileAt117 = model.getBoard().getTile(11, 7);
        assertNotNull("Tile at (11,7) should not be null after move.", tileAt117);
        assertEquals("Tile at (11,7) should be 'O'", 'O', tileAt117.getTileChar());

        //add word not connecting
        result = model.makeMove(1, 1, direction, word);
        assertFalse("The move should be not successful", result);

        direction = 'R'; //add connecting word that dosent work
        result = model.makeMove(6, 6, direction, word);
        assertFalse("The move should be not successful", result);

        direction = 'D'; //add connecting word that dosent work Vertical
        result = model.makeMove(6, 6, direction, word);
        assertFalse("The move should be not successful", result);

        setPlayerTiles('L', 'W'); //add connecting word downward
        word = "low";
        result = model.makeMove(11, 6, direction, word);
        assertTrue("The move should be successful", result);

        Tile tileAt116 = model.getBoard().getTile(11, 6);
        assertNotNull("Tile at (11,7) should not be null after move.", tileAt116);
        assertEquals("Tile at (11,7) should be 'L'", 'L', tileAt116.getTileChar());

        //make sure the other word is still there
        Tile tileAt118 = model.getBoard().getTile(11, 8);
        assertNotNull("Tile at (11,7) should not be null after move.", tileAt118);
        assertEquals("Tile at (11,7) should be 'W'", 'W', tileAt118.getTileChar());

        setPlayerTiles('L', 'W'); //add connecting word horizontal this time
        word = "low";
        direction = 'R';
        result = model.makeMove(6, 11, direction, word);
        assertTrue("The move should be successful", result);

        Tile tileAt611 = model.getBoard().getTile(6, 11);
        assertNotNull("Tile at (11,7) should not be null after move.", tileAt611);
        assertEquals("Tile at (11,7) should be 'L'", 'L', tileAt611.getTileChar());

        Tile tileAt811 = model.getBoard().getTile(8, 11);
        assertNotNull("Tile at (11,7) should not be null after move.", tileAt811);
        assertEquals("Tile at (11,7) should be 'W'", 'W', tileAt811.getTileChar());

    }



}