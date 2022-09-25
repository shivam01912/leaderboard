package com.intuit.leaderboard.test.infra;

import com.intuit.leaderboard.board.Board;
import com.intuit.leaderboard.configuration.Config;
import com.intuit.leaderboard.configuration.KafkaTopicConfig;
import com.intuit.leaderboard.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Class used to create all the endpoints to add / update player data.
 */
@RestController
public class PlayerService {

    @Autowired
    private Config config;

    @Autowired
    private Board board;

    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;

    @Autowired
    private KafkaTemplate<String, Player> playerKafkaTemplate;

    public void sendMessage(Player msg) {
        playerKafkaTemplate.send(kafkaTopicConfig.getKafkaTopic(), msg);
    }

    @PostMapping("/addall")
    public String addAll(@RequestBody List<Player> players) {
        for(Player player: players) {
            addOrUpdateScore(player);
        }

        return "Successfully published all players to Kafka";
    }

    @PostMapping("/addorupdate")
    public String addOrUpdateScore(@RequestBody Player player) {

        sendMessage(player);

        return "Successfully published to Kafka";
    }

}
