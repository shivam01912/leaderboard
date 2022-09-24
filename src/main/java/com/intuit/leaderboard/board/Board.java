package com.intuit.leaderboard.board;

import com.intuit.leaderboard.model.Player;
import java.util.List;

/**
 * Leaderboard interface which can be implemented to create different board implementations.
 */
public interface Board {
    void updateBoard(Player player);

    List<Player> getTopPlayers(int k);
}
