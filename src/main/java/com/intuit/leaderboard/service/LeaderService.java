package com.intuit.leaderboard.service;

import com.intuit.leaderboard.board.Board;
import com.intuit.leaderboard.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    @ResponseBody
    public List<Player> getLeaders(@RequestParam(defaultValue = "5", required = false) Integer top) {
        top = (top <= 0)? 5 : top;
        return board.getTopPlayers(top);
    }

}
