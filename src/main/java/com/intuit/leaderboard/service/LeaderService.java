package com.intuit.leaderboard.service;

import com.intuit.leaderboard.board.Board;
import com.intuit.leaderboard.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Class used to create the endpoints related to accessing the leaderboard data.
 */
@RestController
public class LeaderService {

    @Autowired
    private Board board;

    @GetMapping("/leaderboard")
    public List<Player> getLeaders(@RequestParam int top) {
        return board.getTopPlayers(top);
    }

}
