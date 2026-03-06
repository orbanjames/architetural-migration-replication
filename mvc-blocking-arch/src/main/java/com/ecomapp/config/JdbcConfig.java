package com.ecomapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import org.springframework.core.env.Environment;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableJdbcRepositories(basePackages = {"com.jamesorban.ecommerceapplicationbackend.dao"})
@PropertySource("classpath:jdbc.properties")
public class JdbcConfig {

    private static final String URL = "jdbc.url";
    private static final String USERNAME = "jdbc.username";
    private static final String PASSWORD = "jdbc.password";
    private static final String DRIVER = "jdbc.driver";

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(
                Objects.requireNonNull(env.getProperty(DRIVER)));

        dataSource.setUrl(
                Objects.requireNonNull(env.getProperty(URL)));

        dataSource.setUsername(
                Objects.requireNonNull(env.getProperty(USERNAME)));

        dataSource.setPassword(
                Objects.requireNonNull(env.getProperty(PASSWORD)));

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

}