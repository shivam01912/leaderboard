package com.intuit.leaderboard.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model for a player.
 * Contains player's username and score.
 */
public class Player implements Comparable<Player> {
    private final String name;

    private final int score;

    public Player(@JsonProperty("name") String name, @JsonProperty("score") int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Player player) {
        return this.getName().compareTo(player.getName());
    }
}
