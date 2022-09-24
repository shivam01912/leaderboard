package com.intuit.leaderboard;

import com.intuit.leaderboard.board.Board;
import com.intuit.leaderboard.board.RedisBoard;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public Board testRedisBoard() {
        return new RedisBoard();
    }
}
