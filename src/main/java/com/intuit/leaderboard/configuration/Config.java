package com.intuit.leaderboard.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Class used to store all the config values of the application.
 * Reads the values from application.properties file.
 */
@Configuration
public class Config {

    @Value("${board.impl}")
    private String boardImpl;

    @Value("${board.max.size}")
    private int boardMaxSize;

    @Value("${score.file}")
    private String scoreFile;

    public String getBoardImpl() {
        return boardImpl;
    }

    public int getBoardMaxSize() {
        return boardMaxSize;
    }

    public String getScoreFile() {
        return scoreFile;
    }
}
