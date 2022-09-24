package com.intuit.leaderboard;

import com.intuit.leaderboard.board.Board;
import com.intuit.leaderboard.model.Player;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtil {

    /**
     * Method used to quickly add k number of players to the leaderboard.
     * @param k
     */
    public static void addData(Board board, int k) {
        Random random = new Random();
        for(int i = 0; i < k; i++) {
            board.updateBoard(new Player("Player_" + i, 10 + random.nextInt(90)));
        }
    }

    /**
     * Method used to assert correct order of scores in the returned leaderboard.
     * @param topPlayers
     */
    public static void assertOrder(List<Player> topPlayers) {
        int prev = Integer.MAX_VALUE;
        for(Player p: topPlayers) {
            assertTrue(p.getScore() <= prev);
            prev = p.getScore();
        }
    }
}
