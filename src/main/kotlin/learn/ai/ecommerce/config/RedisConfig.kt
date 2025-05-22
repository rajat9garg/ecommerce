package learn.ai.ecommerce.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    @Bean
    fun reactiveRedisTemplate(
        factory: ReactiveRedisConnectionFactory
    ): ReactiveRedisTemplate<String, String> {
        val keySerializer: RedisSerializer<String> = StringRedisSerializer.UTF_8
        val valueSerializer: RedisSerializer<String> = StringRedisSerializer.UTF_8
        
        val builder: RedisSerializationContext.RedisSerializationContextBuilder<String, String> = 
            RedisSerializationContext.newSerializationContext(keySerializer)
        
        val context = builder.value(valueSerializer).build()
        
        return ReactiveRedisTemplate(factory, context)
    }
    
    @Bean
    fun reactiveStringRedisTemplate(
        factory: ReactiveRedisConnectionFactory
    ) = ReactiveRedisTemplate(factory, RedisSerializationContext.string())
}
