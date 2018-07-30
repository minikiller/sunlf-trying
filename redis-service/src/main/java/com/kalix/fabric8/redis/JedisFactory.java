package com.kalix.fabric8.redis;

/**
 * Created by Administrator on 2018-05-14.
 */
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisFactory {
    private static JedisPool jedisPool;
    private static JedisFactory instance;
    public JedisFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(
                poolConfig,
                RedisDBConfig.HOST,
                RedisDBConfig.PORT,
                RedisDBConfig.TIMEOUT,
                RedisDBConfig.PASSWORD
        );
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public static JedisFactory getInstance() {
        if (instance == null) {
            instance = new JedisFactory();
        }
        return instance;
    }
}
