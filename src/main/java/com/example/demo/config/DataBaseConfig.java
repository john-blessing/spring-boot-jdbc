package com.example.demo.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Primary;

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
