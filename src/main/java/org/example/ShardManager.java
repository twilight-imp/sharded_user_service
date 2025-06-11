package org.example;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ShardManager {

    private final int shardCount = 3;

    public int getShardIndex(UUID userId) {
        return Math.abs(userId.hashCode()) % shardCount;
    }

    public String getJdbcUrl(int shardIndex) {
        return switch (shardIndex) {
            case 0 -> "jdbc:postgresql://localhost:5432/shard1";
            case 1 -> "jdbc:postgresql://localhost:5433/shard2";
            case 2 -> "jdbc:postgresql://localhost:5434/shard3";
            default -> throw new IllegalArgumentException("Invalid shard index");
        };
    }

    public int getShardCount() {
        return shardCount;
    }
}


