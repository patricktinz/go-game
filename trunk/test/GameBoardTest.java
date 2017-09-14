import gogame.GameAnalysis;
import gogame.GameBoard;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;

public class GameBoardTest
{
    private static int dimension = 9;
    private static GameBoard testboard;

    @BeforeClass
    public static void setUp()
    {
        testboard = new GameBoard(dimension);
    }

    @Test
    public void testPlayTurn()
    {
        GameAnalysis gameAnalysis;
        int validAsserts = 0;
        Set<Integer> current;
        Set<Integer> expectedPlayer1 = new HashSet<>();
        Set<Integer> expectedPlayer2 = new HashSet<>();

        fill(expectedPlayer1);
        fill(expectedPlayer2);

        // stones surround stone 21
        expectedPlayer2.remove(12);
        expectedPlayer2.remove(22);
        expectedPlayer2.remove(20);
        expectedPlayer2.remove(30);

        // middle stone
        expectedPlayer2.remove(21);

        testboard.playTurn(1, 12);
        testboard.playTurn(1, 22);
        testboard.playTurn(1, 20);
        current = testboard.playTurn(1, 30).getValidTurns();

        if(current.equals(expectedPlayer2))
            validAsserts++;

        expectedPlayer1.remove(12);
        expectedPlayer1.remove(22);
        expectedPlayer1.remove(20);
        expectedPlayer1.remove(30);

        current = testboard.getValidTurns(1);

        if(current.equals(expectedPlayer1))
            validAsserts++;

        assertEquals("Umliegende Steine wurden nicht korrekt markiert", validAsserts, 2);

        validAsserts = 0;

        // Player2 surround Player1 then killed Player1
        // remove stones of Player1 that Player2 want to set
        expectedPlayer1.remove(3);
        expectedPlayer1.remove(13);
        expectedPlayer1.remove(23);
        expectedPlayer1.remove(31);
        expectedPlayer1.remove(39);
        expectedPlayer1.remove(29);
        expectedPlayer1.remove(19);
        expectedPlayer1.remove(11);

        // remove now invalid turn
        expectedPlayer1.remove(21);

        testboard.playTurn(2, 3);
        testboard.playTurn(2, 13);
        testboard.playTurn(2, 23);
        testboard.playTurn(2, 31);
        testboard.playTurn(2, 39);
        testboard.playTurn(2, 29);
        testboard.playTurn(2, 19);
        testboard.playTurn(2, 11);

        current = testboard.getValidTurns(1);

        if(expectedPlayer1.equals(current))
            validAsserts++;

        expectedPlayer2.remove(3);
        expectedPlayer2.remove(13);
        expectedPlayer2.remove(23);
        expectedPlayer2.remove(31);
        expectedPlayer2.remove(39);
        expectedPlayer2.remove(29);
        expectedPlayer2.remove(19);
        expectedPlayer2.remove(11);

        expectedPlayer2.add(21);

        current = testboard.getValidTurns(2);

        if(expectedPlayer2.equals(current))
            validAsserts++;

        assertEquals(validAsserts, 2);

        validAsserts = 0;

        testboard.playTurn(2, 21);

        expectedPlayer2.add(12);
        expectedPlayer2.add(22);
        expectedPlayer2.add(20);
        expectedPlayer2.add(30);

        expectedPlayer2.remove(21);

        current = testboard.getValidTurns(2);

        if(expectedPlayer2.equals(current))
            validAsserts++;

        current = testboard.getValidTurns(1);

        if(expectedPlayer1.equals(current))
            validAsserts++;

        assertEquals("Toter Stein wurde nicht korrekt gelöscht", validAsserts, 2);
    }

    private static void fill(Set<Integer> expected)
    {
        for(int i = 0; i < dimension*dimension; i++)
            expected.add(i);
    }
}
