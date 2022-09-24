package com.intuit.leaderboard.board;

import com.intuit.leaderboard.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * A Java Sorted Set based implementation of the leaderboard.
 * It makes use of the ConcurrentSkipListSet and ConcurrentHashMap to maintain the leaderboard
 */
public class PQueueBoard implements Board {
    private Logger logger = LoggerFactory.getLogger(PQueueBoard.class);

    private final ConcurrentSkipListSet<Player> leaders;

    private final ConcurrentHashMap<String, Player> leadersMap;

    private final int boardMaxSize;

    public PQueueBoard(int boardMaxSize) {
        logger.info("Initialized Priority queue board");
        this.boardMaxSize = boardMaxSize;
        this.leaders = new ConcurrentSkipListSet<>(Comparator.comparingInt(Player::getScore).reversed().thenComparing(p -> p));
        this.leadersMap = new ConcurrentHashMap<>();
    }

    /**
     * this method verifies if the player is eligible to be in the leaderboard
     * @param player - Object containing the player's username and score
     * @return boolean
     */
    private boolean isQualifying(Player player) {
        return this.leaders.size() < this.boardMaxSize || player.getScore() >= this.leaders.last().getScore();
    }

    /**
     * This method is used to update the leaderboard whenever a new score is published
     * @param player - Object containing the player's username and score
     */
    @Override
    public void updateBoard(Player player) {
        if(!isQualifying(player)) {
            logger.info("Player : " + player.getName() + " did not qualify for leaderboard with score - " + player.getScore());
            return;
        }

        if(!this.leadersMap.containsKey(player.getName())) {
            if(this.leaders.size() == this.boardMaxSize) {
                Player last = this.leaders.pollLast();
                this.leadersMap.remove(last.getName());
            }
        } else {
            this.leaders.remove(this.leadersMap.get(player.getName()));
        }

        this.leaders.add(player);
        this.leadersMap.put(player.getName(), player);

        logger.info("Player : "+ player.getName() + "'s score updated");
    }

    /**
     * This is the method used to get the players in leaderboard
     * @return list of top k players (k - defined in the properties file and is equal to the board size)
     */
    @Override
    public List<Player> getTopPlayers(int k) {
        List<Player> response = new ArrayList<>();
        int count = 0;
        for(Player p: this.leaders) {
            response.add(p);
            count++;
            if(count == k) break;
        }
        return response;
    }
}
