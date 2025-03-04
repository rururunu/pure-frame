package com.runu.web_server.config.jedis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisConfig {

    //服务器地址
    @Value("${spring.redis.host}")
    private String host;

    //端口
    @Value("${spring.redis.port}")
    private int port;

    //密码
    @Value("${spring.redis.password}")
    private String password;

    //超时时间
    @Value("${spring.redis.timeout}")
    private String timeout;

    //最大连接数
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxTotal;

    //最大连接阻塞等待时间
    @Value("${spring.redis.jedis.pool.max-wait}")
    private String maxWaitMillis;

    //最大空闲连接
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    //最小空闲连接
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //注意值的转变
        jedisPoolConfig.setMaxWaitMillis(Long.parseLong(
                maxWaitMillis.substring(
                        0,
                        maxWaitMillis.length() - 2
                )
        ));
        //注意属性名
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        return new JedisPool(
                jedisPoolConfig,
                host,
                port,
                Integer.parseInt(timeout.substring(0, timeout.length() - 2)),
                password
        );

    }
}
