package com.intuit.leaderboard.board;

import com.intuit.leaderboard.TestConfig;
import com.intuit.leaderboard.TestUtil;
import com.intuit.leaderboard.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Import(TestConfig.class)
class RedisBoardTest {

    private final String KEY = "Leaderboard";

    @Autowired
    private Board testRedisBoard;

    private StringRedisTemplate redisTemplate;

    /**
     * This method runs before each test case and clears the leaderboard for accurate testing.
     */
    @BeforeEach
    void setup() {
        try (Jedis jedis = new Jedis()) {
          jedis.flushAll();
        }
    }

    /**
     * Tests happy path scenario where a Player is added to the leaderboard.
     */
    @Test
    void testUpdateBoardHappyPath() {
        Player testPlayer = new Player("TestPlayer", 100);
        testRedisBoard.updateBoard(testPlayer);

        try (Jedis jedis = new Jedis()) {
            Set<Tuple> tuples = jedis.zrangeWithScores(KEY, 0, 0);
            for(Tuple t: tuples) {
                assertEquals(testPlayer.getName(), t.getElement());
                assertEquals(testPlayer.getScore(), (int)t.getScore());
            }
        }
    }

    /**
     * Tests if we get a correct size and order of leaderboard.
     */
    @Test
    void testGetTopPlayersHappyPath() {
        TestUtil.addData(testRedisBoard, 10);
        List<Player> topPlayers = testRedisBoard.getTopPlayers(5);

        assertEquals(5, topPlayers.size());
        TestUtil.assertOrder(topPlayers);
    }

    /**
     * Tests for well handled case of a lesser number of players available in the leaderboard than requested for.
     */
    @Test
    void testGetTopPlayersLessThanKAvailable() {
        TestUtil.addData(testRedisBoard, 3);
        List<Player> topPlayers = testRedisBoard.getTopPlayers(5);

        assertEquals(3, topPlayers.size());
        TestUtil.assertOrder(topPlayers);
    }
}