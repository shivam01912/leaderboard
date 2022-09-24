package com.intuit.leaderboard.board;

import com.intuit.leaderboard.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Redis based implementation of the leaderboard
 */
public class RedisBoard implements Board {

    private Logger logger = LoggerFactory.getLogger(RedisBoard.class);

    private final String KEY = "Leaderboard";

    @Autowired
    private StringRedisTemplate redisTemplate;

    public RedisBoard() {
        logger.info("Initialized Redis board");
    }

    /**
     * This method is used to update the leaderboard whenever a new score is published
     * @param player - Object containing the player's username and score
     */
    @Override
    public void updateBoard(Player player) {
        redisTemplate.opsForZSet().add(KEY, player.getName(), player.getScore());
        logger.info("Player : "+ player.getName() + "'s score updated");
    }

    /**
     * This is the method used to get the players in leaderboard
     * @return list of top k players (k - defined in the properties file and is equal to the board size)
     */
    @Override
    public List<Player> getTopPlayers(int k) {
        Set<ZSetOperations.TypedTuple<String>> topk = redisTemplate.opsForZSet().reverseRangeWithScores(KEY, 0, k - 1);
        List<Player> response = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> entry : topk) {
            response.add(new Player(entry.getValue(), entry.getScore().intValue()));
        }
        return response;
    }
}
