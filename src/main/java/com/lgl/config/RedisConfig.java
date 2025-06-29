package com.lgl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author   刘国良
 * @Date  2025/4/14     20:23
 * @Destription
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用String序列化器处理key
        template.setKeySerializer(new StringRedisSerializer());

        // 使用GenericJackson2JsonRedisSerializer处理value
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 对hash结构同样处理
        template.setHashKeySerializer(new  StringRedisSerializer());
        template.setHashValueSerializer(new  GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }


}

