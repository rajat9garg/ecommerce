package learn.ai.ecommerce.redis

import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class RedisClient(
    private val redisTemplate: ReactiveRedisTemplate<String, String>
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    suspend fun set(key: String, value: String, ttl: Duration? = null): Boolean {
        return try {
            val operation = redisTemplate.opsForValue().set(key, value)
            
            ttl?.let {
                redisTemplate.expire(key, it).block()
            } ?: operation.block()
            
            true
        } catch (e: Exception) {
            logger.error("Error setting key: $key", e)
            false
        }
    }
    
    suspend fun get(key: String): String? {
        return try {
            redisTemplate.opsForValue().get(key).block()
        } catch (e: Exception) {
            logger.error("Error getting key: $key", e)
            null
        }
    }
    
    suspend fun delete(key: String): Boolean {
        return try {
            val deletedCount: Long? = redisTemplate.delete(key).block()
            deletedCount != null && deletedCount > 0
        } catch (e: Exception) {
            logger.error("Error deleting key: '$key'. Details: ${e.message}", e)
            false
        }
    }
    
    suspend fun setIfAbsent(key: String, value: String, ttl: Duration? = null): Boolean {
        return try {
            val set = redisTemplate.opsForValue().setIfAbsent(key, value).block() ?: false
            
            if (set && ttl != null) {
                redisTemplate.expire(key, ttl).block()
            }
            
            set
        } catch (e: Exception) {
            logger.error("Error in setIfAbsent for key: $key", e)
            false
        }
    }
    
    suspend fun exists(key: String): Boolean {
        return try {
            redisTemplate.hasKey(key).block() ?: false
        } catch (e: Exception) {
            logger.error("Error checking if key exists: $key", e)
            false
        }
    }
    
    // Reactive versions of the methods
    fun setReactive(key: String, value: String, ttl: Duration? = null): Mono<Boolean> {
        return redisTemplate.opsForValue().set(key, value)
            .flatMap {
                if (ttl != null) {
                    redisTemplate.expire(key, ttl).thenReturn(it)
                } else {
                    Mono.just(it)
                }
            }
            .onErrorResume { 
                logger.error("Error setting key: $key", it)
                Mono.just(false)
            }
    }
    
    fun getReactive(key: String): Mono<String> {
        return redisTemplate.opsForValue().get(key)
            .onErrorResume {
                logger.error("Error getting key: $key", it)
                Mono.empty()
            }
    }
    
    fun deleteReactive(key: String): Mono<Boolean> {
        return redisTemplate.delete(key) // This returns Mono<Long> (number of keys deleted)
            .map { deletedCount -> // Map the Long result to Boolean
                deletedCount > 0
            }
            .onErrorResume { throwable ->
                // Log the error with full stack trace for better debugging
                logger.error("Error deleting key: '$key'. Details: ${throwable.message}", throwable)
                // If an error occurs, we consider the deletion to have failed, so return false.
                Mono.just(false)
            }
    }
    
    fun setIfAbsentReactive(key: String, value: String, ttl: Duration? = null): Mono<Boolean> {
        return redisTemplate.opsForValue().setIfAbsent(key, value)
            .flatMap { set ->
                if (set && ttl != null) {
                    redisTemplate.expire(key, ttl).thenReturn(set)
                } else {
                    Mono.just(set)
                }
            }
            .onErrorResume {
                logger.error("Error in setIfAbsent for key: $key", it)
                Mono.just(false)
            }
    }
    
    fun existsReactive(key: String): Mono<Boolean> {
        return redisTemplate.hasKey(key)
            .onErrorResume {
                logger.error("Error checking if key exists: $key", it)
                Mono.just(false)
            }
    }
}
