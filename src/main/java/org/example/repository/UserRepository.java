package org.example.repository;


import org.example.ShardManager;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {

    private final ShardManager shardManager;
    private final Map<Integer, JdbcTemplate> jdbcTemplates;

    @Autowired
    public UserRepository(ShardManager shardManager, Map<Integer, JdbcTemplate> jdbcTemplates) {
        this.shardManager = shardManager;
        this.jdbcTemplates = jdbcTemplates;
    }

    public Optional<User> getUser(UUID id) {
        int shardIndex = shardManager.getShardIndex(id);
        JdbcTemplate jdbcTemplate = jdbcTemplates.get(shardIndex);

        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(User.class)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void saveUser(User user) {
        int shardIndex = shardManager.getShardIndex(user.getId());
        JdbcTemplate jdbcTemplate = jdbcTemplates.get(shardIndex);

        String sql = "INSERT INTO users (id, name, email) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getId(), user.getName(), user.getEmail());
    }

    public void updateUser(User user) {
        int shardIndex = shardManager.getShardIndex(user.getId());
        JdbcTemplate jdbcTemplate = jdbcTemplates.get(shardIndex);

        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        jdbcTemplate.update(sql,user.getName(), user.getEmail(),user.getId());
    }
}

