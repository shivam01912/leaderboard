package com.intuit.leaderboard.configuration;

import com.intuit.leaderboard.board.Board;
import com.intuit.leaderboard.board.PQueueBoard;
import com.intuit.leaderboard.board.RedisBoard;
import com.intuit.leaderboard.configuration.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class is used to initialize a leaderboard based on the property in application.
 * Can be extended to accommodate different leaderboard creating mechanisms.
 */
@Configuration
public class BoardConfig {

    @Autowired
    private Config config;

    private static final String REDIS_TYPE = "REDIS";

    /**
     * Board bean creation based in the config mentioned in the application.properties
     * @return bean of type com.intuit.leaderboard.board.Board
     */
    @Bean
    @Scope("singleton")
    public Board board() {
        if(REDIS_TYPE.equals(config.getBoardImpl())) {
            return new RedisBoard();
        }
        else {
            return new PQueueBoard(config.getBoardMaxSize());
        }
    }
}
