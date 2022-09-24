package com.intuit.leaderboard.test.infra.exception;

/**
 * Custom exception foe wrong score published.
 */
public class LowScoreException extends RuntimeException {

    public LowScoreException(String s) {
        super(s);
    }
}
