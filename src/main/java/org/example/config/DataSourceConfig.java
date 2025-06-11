package org.example.config;


import com.zaxxer.hikari.HikariDataSource;
import org.example.ShardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Autowired
    private ShardManager shardManager;

    @Bean
    public Map<Integer, DataSource> shardDataSources() {
        Map<Integer, DataSource> dataSources = new HashMap<>();
        for (int i = 0; i < shardManager.getShardCount(); i++) {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl(shardManager.getJdbcUrl(i));
            dataSource.setUsername("postgres");
            dataSource.setPassword("postgres");
            dataSources.put(i, dataSource);
        }
        return dataSources;
    }

    @Bean
    public Map<Integer, JdbcTemplate> shardJdbcTemplates(@Qualifier("shardDataSources") Map<Integer, DataSource> dataSources) {
        Map<Integer, JdbcTemplate> jdbcTemplates = new HashMap<>();
        dataSources.forEach((index, dataSource) -> jdbcTemplates.put(index, new JdbcTemplate(dataSource)));
        return jdbcTemplates;
    }
}