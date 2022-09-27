package com.intuit.leaderboard.board;

import com.intuit.leaderboard.model.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RedisBoardTest {

    @TestConfiguration
    static class TestConfig {

        @Bean
        public Board testRedisBoard() {
            return new RedisBoard();
        }
    }
    private final String KEY = "Leaderboard";

    @Autowired
    private Board testRedisBoard;

    @MockBean
    private StringRedisTemplate redisTemplate;

    /**
     * Tests happy path scenario where a Player is added to the leaderboard.
     */
    @Test
    void testUpdateBoardHappyPath() {
        ZSetOperations mockOps = Mockito.mock(ZSetOperations.class);
        Mockito.when(redisTemplate.opsForZSet()).thenReturn(mockOps);
        Mockito.when(mockOps.add(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble()))
                .thenReturn(true);

        Player testPlayer = new Player("TestPlayer", 100);
        testRedisBoard.updateBoard(testPlayer);


        Mockito.verify(redisTemplate, Mockito.times(1)).opsForZSet();
        Mockito.verify(mockOps, Mockito.times(1)).add(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble());
    }
}