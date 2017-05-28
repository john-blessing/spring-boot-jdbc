package com.example.demo.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Created by keifc on 2017/5/26.
 */

@Configuration
public class DataBaseConfig {

    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public HikariDataSource dataSource(){
        return (HikariDataSource) DataSourceBuilder.create()
                .type(HikariDataSource.class).build();
    }
}
