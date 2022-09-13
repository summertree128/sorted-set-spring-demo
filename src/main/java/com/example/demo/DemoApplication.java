package com.example.demo;

import io.lettuce.core.ScoredValue;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.output.ScoredValueOutput;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    private final RedisTemplate<String, String> redisTemplate;

    public DemoApplication(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Supported commands can be done like this
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("ranking", "user1", 100);
        zSetOperations.add("ranking", "user2", 200);
        Set elementsBefore = zSetOperations.range("ranking", 0L, 1L);

        System.out.println("Ranking before ZPOPMIN : " + elementsBefore);

        // Unsupported commands need be executed by LettuceConnection like this
        LettuceConnection conn = (LettuceConnection) redisTemplate.getRequiredConnectionFactory().getConnection();
        Object resultObject = conn.execute("ZPOPMIN", new ScoredValueOutput<>(ByteArrayCodec.INSTANCE), "ranking".getBytes());
        ScoredValue scoredValue = (ScoredValue) resultObject;
        Object valueObject = scoredValue.getValue();
        String value = new String((byte[]) valueObject);
        double score = scoredValue.getScore();

        System.out.println("Popped element : value = " + value + ", score = " + score);

        // Ensure that 1 element is popped from the Sorted Set
        Set elementsAfter = zSetOperations.range("ranking", 0L, 1L);
        System.out.println("Ranking after ZPOPMIN : " + elementsAfter);

        conn.close();
    }
}
