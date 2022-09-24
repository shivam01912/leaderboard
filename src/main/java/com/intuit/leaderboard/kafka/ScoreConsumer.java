package com.intuit.leaderboard.kafka;

import com.intuit.leaderboard.board.Board;
import com.intuit.leaderboard.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

/**
 * Subscriber reading the messages from Kafka topic "leaderboard".
 */
@Component
public class ScoreConsumer {

    private Logger logger = LoggerFactory.getLogger(ScoreConsumer.class);

    @Autowired
    private Board board;

    @KafkaListener(topics = "leaderboard", containerFactory = "playerKafkaListenerContainerFactory")
    public void playerConsumer(Player player) {
        logger.info("Received Message with Player : " + player.getName());
        if(player.getScore() < 0) {
            logger.error("Negative player score received : "+ player.getName() + " : " + player.getScore());
        }

        //Assign the task updating the leaderboard to a separate thread.
        Executors.newSingleThreadExecutor().submit(() -> board.updateBoard(player));
    }
}
