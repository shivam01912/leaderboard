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
    private KafkaTemplate<String, Player> kafkaTemplate;

    public void sendMessage(Player msg) {
        kafkaTemplate.send(kafkaTopicConfig.getKafkaTopic(), msg);
    }

    /*@GetMapping("/populate")
    public void readFromFile() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:"+config.getScoreFile());
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            for(String line; (line = br.readLine()) != null; ) {
                String[] playerData = line.split(" ");
                board.updateBoard(new Player(playerData[0], Integer.parseInt(playerData[1])));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    @PostMapping("/addall")
    public String addAll(@RequestBody List<Player> players) {
        for(Player player: players) {
            updateScore(player);
        }

        return "Successfully published all players to Kafka";
    }

    @PostMapping("/updatescore")
    public String updateScore(@RequestBody Player player) {

        sendMessage(player);

        return "Successfully published to Kafka";
    }

}
