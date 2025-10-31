package com.lgl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication(scanBasePackages = "com.rpa")
@MapperScan("com.lgl.mapper")
public class Demo444Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Demo444Application.class, args);
        // 打印所有redis相关配置
        System.out.println("Redis  Host: " + run.getEnvironment().getProperty("spring.data.redis.host"));
        System.out.println("Redis  Port: " + run.getEnvironment().getProperty("spring.data.redis.port"));
//        RedisTemplate redisTemplate = run.getBean("redisTemplate", RedisTemplate.class);
//        redisTemplate.opsForValue().set("ssadfsdfs", "sdfssdfsf");
//        redisTemplate.opsForValue().get("hello11");

    }
    @Bean
    public CommandLineRunner checkRedisConfig(RedisProperties redisProps) {
        return args -> {
            System.out.println(" 最终Redis配置：");
            System.out.println("Host:  " + redisProps.getHost());
            System.out.println("Port:  " + redisProps.getPort());
        };
    }

}
