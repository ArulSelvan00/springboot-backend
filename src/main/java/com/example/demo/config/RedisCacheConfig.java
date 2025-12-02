package com.example.demo.config; // Adjust package name to match your structure

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import java.time.Duration;

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                // Set the default Time-To-Live (TTL) for cached items to 10 minutes
                // This means the cache will expire and force a DB call every 10 mins.
                .entryTtl(Duration.ofMinutes(10))

                // Do not store null values in the cache (good practice to prevent issues)
                .disableCachingNullValues()

                // Configure how objects are stored and retrieved from Redis (Serialization)
                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}