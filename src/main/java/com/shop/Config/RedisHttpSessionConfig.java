package com.shop.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by Carpenter on 2017/3/8.
 */
@EnableRedisHttpSession
public class RedisHttpSessionConfig {
    @Bean
    public JedisConnectionFactory connectionFactory(){
        JedisConnectionFactory connection = new JedisConnectionFactory();
        connection.setHostName("localhost");
        connection.setPort(6379);
        connection.setPassword("password");
        return connection;
    }

    @Bean
    public RedisOperationsSessionRepository sessionRepository(){
        return new RedisOperationsSessionRepository(connectionFactory());
    }
}
