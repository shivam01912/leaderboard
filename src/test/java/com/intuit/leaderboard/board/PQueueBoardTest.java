package com.intuit.leaderboard.board;

import com.intuit.leaderboard.TestUtil;
import com.intuit.leaderboard.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PQueueBoardTest {

    private final PQueueBoard pQueueBoard = new PQueueBoard(10);

    /**
     * This method runs before each test case and clears the leaderboard for accurate testing.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        Field leadersField = PQueueBoard.class.getDeclaredField("leaders");
        leadersField.setAccessible(true);
        ((ConcurrentSkipListSet<Player>)leadersField.get(pQueueBoard)).clear();

        Field leadersMapField = PQueueBoard.class.getDeclaredField("leadersMap");
        leadersMapField.setAccessible(true);
        ((ConcurrentHashMap<String, Player>)leadersMapField.get(pQueueBoard)).clear();
    }

    /**
     * Tests happy path scenario where a Player is added to the leaderboard.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    void testUpdateBoardHappyPath() throws NoSuchFieldException, IllegalAccessException {
        Player testPlayer = new Player("TestPlayer", 100);

        pQueueBoard.updateBoard(testPlayer);

        Field leadersField = PQueueBoard.class.getDeclaredField("leaders");
        leadersField.setAccessible(true);
        assertEquals(1, ((ConcurrentSkipListSet<Player>)leadersField.get(pQueueBoard)).size());
        assertEquals(testPlayer, ((ConcurrentSkipListSet<Player>)leadersField.get(pQueueBoard)).first());

        Field leadersMapField = PQueueBoard.class.getDeclaredField("leadersMap");
        leadersMapField.setAccessible(true);
        assertEquals(1, ((ConcurrentHashMap<String, Player>)leadersMapField.get(pQueueBoard)).size());
        assertEquals(testPlayer, ((ConcurrentHashMap<String, Player>)leadersMapField.get(pQueueBoard)).get(testPlayer.getName()));
    }

    /**
     * Tests if a non qualifying player is (low score) is not added to the leaderboard.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    void testUpdateBoardNotQualifying() throws NoSuchFieldException, IllegalAccessException {
        TestUtil.addData(pQueueBoard, 10);

        Player testPlayer = new Player("TestPlayer", 0);

        pQueueBoard.updateBoard(testPlayer);

        Field leadersField = PQueueBoard.class.getDeclaredField("leaders");
        leadersField.setAccessible(true);
        assertTrue(((ConcurrentSkipListSet<Player>)leadersField.get(pQueueBoard)).stream().noneMatch(p -> p.equals(testPlayer)));
    }

    /**
     * Tests if a player with highest score is added to the top of the leaderboard.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    void testUpdateBoardAddToTop() throws NoSuchFieldException, IllegalAccessException {
        TestUtil.addData(pQueueBoard, 5);

        Player testPlayer = new Player("TestPlayer", 110);

        pQueueBoard.updateBoard(testPlayer);

        Field leadersField = PQueueBoard.class.getDeclaredField("leaders");
        leadersField.setAccessible(true);
        assertEquals(testPlayer, ((ConcurrentSkipListSet<Player>)leadersField.get(pQueueBoard)).first());
    }

    /**
     * Tests if a player with a very low score is added to the bottom of the leaderboard.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    void testUpdateBoardAddToBottom() throws NoSuchFieldException, IllegalAccessException {
        TestUtil.addData(pQueueBoard, 5);

        Player testPlayer = new Player("TestPlayer", 10);

        pQueueBoard.updateBoard(testPlayer);

        Field leadersField = PQueueBoard.class.getDeclaredField("leaders");
        leadersField.setAccessible(true);
        assertEquals(testPlayer, ((ConcurrentSkipListSet<Player>)leadersField.get(pQueueBoard)).last());
    }

    /**
     * Tests if an already existing player's score get updated or not.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    void testUpdateBoardUpdateScenario() throws NoSuchFieldException, IllegalAccessException {
        TestUtil.addData(pQueueBoard, 5);

        Player testPlayer = new Player("TestPlayer", 60);

        pQueueBoard.updateBoard(testPlayer);

        Field leadersField = PQueueBoard.class.getDeclaredField("leaders");
        leadersField.setAccessible(true);
        assertTrue(((ConcurrentSkipListSet<Player>)leadersField.get(pQueueBoard)).stream().anyMatch(p -> p.equals(testPlayer)));

        Player testPlayerNew = new Player("TestPlayer", 80);

        pQueueBoard.updateBoard(testPlayerNew);

        assertTrue(((ConcurrentSkipListSet<Player>)leadersField.get(pQueueBoard)).stream().noneMatch(p -> p.equals(testPlayer)));
        assertTrue(((ConcurrentSkipListSet<Player>)leadersField.get(pQueueBoard)).stream().anyMatch(p -> p.equals(testPlayerNew)));
    }

    /**
     * Tests for qualification of player in case of a non-full board. 
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Test
    void testIsQualifyingTrueWithNonFullBoard() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TestUtil.addData(pQueueBoard, 5);

        Method isQualifyingMethod = PQueueBoard.class.getDeclaredMethod("isQualifying", Player.class);
        isQualifyingMethod.setAccessible(true);
        boolean result = (boolean) isQualifyingMethod.invoke(pQueueBoard, new Player("TestPlayer", 100));

        assertTrue(result);
    }

    /**
     * Tests qualification of a player with high score in case of a full board.
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Test
    void testIsQualifyingTrueWithFullBoard() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TestUtil.addData(pQueueBoard, 10);

        Method isQualifyingMethod = PQueueBoard.class.getDeclaredMethod("isQualifying", Player.class);
        isQualifyingMethod.setAccessible(true);
        boolean result = (boolean) isQualifyingMethod.invoke(pQueueBoard, new Player("TestPlayer", 110));

        assertTrue(result);
    }

    /**
     * Tests non qualification of a player with low score in case of a full board.
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Test
    void testIsQualifyingFalse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TestUtil.addData(pQueueBoard, 10);

        Method isQualifyingMethod = PQueueBoard.class.getDeclaredMethod("isQualifying", Player.class);
        isQualifyingMethod.setAccessible(true);
        boolean result = (boolean) isQualifyingMethod.invoke(pQueueBoard, new Player("TestPlayer", 0));

        assertFalse(result);
    }

    /**
     * Tests if we get a correct size and order of leaderboard.
     */
    @Test
    void testGetTopPlayersHappyPath() {
        TestUtil.addData(pQueueBoard, 10);
        List<Player> topPlayers = pQueueBoard.getTopPlayers(5);

        assertEquals(5, topPlayers.size());
        TestUtil.assertOrder(topPlayers);
    }

    /**
     * Tests for well handled case of a lesser number of players available in the leaderboard than requested for.
     */
    @Test
    void testGetTopPlayersLessThanKAvailable() {
        TestUtil.addData(pQueueBoard, 3);
        List<Player> topPlayers = pQueueBoard.getTopPlayers(5);

        assertEquals(3, topPlayers.size());
        TestUtil.assertOrder(topPlayers);
    }
}