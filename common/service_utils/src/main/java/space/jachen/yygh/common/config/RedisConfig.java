package space.jachen.yygh.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author JaChen
 * @date 2023/1/30 18:13
 */

/**
 * 注意：@ EnableCaching 注解是一个用于开启缓存支持的注解，它是 Spring 框架的注解，
 * 主要用于在 Spring Boot 中启用缓存支持。
 * 使用 @EnableCaching 注解后，
 * 可以在需要缓存的方法上使用 @Cacheable、@CacheEvict、@CachePut 等注解，来控制方法的缓存行为。
 * 开启缓存支持后，可以减少方法的调用次数，提高程序的性能。
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 设置RedisTemplate规则
     * @param factory redis连接工厂对象
     * @return 返回一个RedisTemplate<Object,Object>对象
     */
    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory factory){

        // 1、使用RedisTemplate模板对象 关联一个 Redis 连接
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 2、 JSON格式存储工具 避免在存储和读取数据时不会出现乱码等问题。
        Jackson2JsonRedisSerializer<Object> redisSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);

        // ObjectMapper 类是用于处理 JSON 序列化/反序列化的主要工具类
        ObjectMapper om = new ObjectMapper();

        // 使用 setVisibility() 方法来设置某些私有属性可见.
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        // ObjectMapper.DefaultTyping.NON_FINAL常量表示仅将未声明为final的类型包含在序列化的JSON中，
        // 而其他类型（声明为final）将不会被包含。
        om.activateDefaultTyping(om.getPolymorphicTypeValidator()
                ,ObjectMapper.DefaultTyping.NON_FINAL);

        // 设置k-v属性
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(redisSerializer);
        // afterPropertiesSet方法初始化RedisTemplate的各个属性。
        redisTemplate.afterPropertiesSet();
        return redisTemplate;

    }


    /**
     * 设置CacheManager缓存规则
     * @param factory  redis连接工厂对象
     * @return 返回一个CacheManager对象
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory){

        // StringRedisSerializer是RedisSerializer接口的一种序列化器，
        // 用于将Java字符串对象序列化为Redis存储的字符串。
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();

        // 1、设置Jackson2JsonRedisSerializer实例对象属性 解决查询缓存转换异常的问题

        // JSON格式存储工具 避免在存储和读取数据时不会出现乱码等问题。
        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);

        // 使用 setVisibility() 方法来设置某些私有属性可见.
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(om.getPolymorphicTypeValidator()
                ,ObjectMapper.DefaultTyping.NON_FINAL);
        // 设置属性 将指定的ObjectMapper对象设置为当前Jackson2JsonRedisSerializer实例的成员对象。
        jsonRedisSerializer.setObjectMapper(om);


        // 2、建造者模式：配置序列化（解决乱码的问题）,过期时间600秒
        RedisCacheConfiguration config = RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(600))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonRedisSerializer))
                .disableCachingNullValues();

        // 3、配置CacheManager对象返回
        return RedisCacheManager
                .builder(factory)
                .cacheDefaults(config)
                .build();

    }

}
