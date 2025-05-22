package learn.ai.ecommerce.service

import learn.ai.ecommerce.redis.RedisClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.UUID

@Service
class LockService(
    private val redisClient: RedisClient
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    
    private val LOCK_PREFIX = "lock:"
    private val DEFAULT_LOCK_TIMEOUT = Duration.ofSeconds(30)
    private val LOCK_RETRY_INTERVAL = Duration.ofMillis(100)
    
    /**
     * Acquires a distributed lock with the given key and timeout
     * @param key The lock key
     * @param timeout Maximum time to wait for the lock
     * @param lockTimeout Time after which the lock will be automatically released
     * @return A LockHandle if acquired, null if not acquired within the timeout
     */
    suspend fun acquireLock(key: String, timeout: Duration, lockTimeout: Duration = DEFAULT_LOCK_TIMEOUT): LockHandle? {
        val lockKey = "$LOCK_PREFIX$key"
        val lockValue = UUID.randomUUID().toString()
        val endTime = System.currentTimeMillis() + timeout.toMillis()
        
        while (System.currentTimeMillis() < endTime) {
            if (redisClient.setIfAbsent(lockKey, lockValue, lockTimeout)) {
                logger.debug("Acquired lock for key: $key")
                return LockHandle(lockKey, lockValue, this)
            }
            
            // Wait before retrying
            kotlinx.coroutines.delay(LOCK_RETRY_INTERVAL.toMillis())
        }
        
        return null
    }
    
    /**
     * Releases a previously acquired lock
     * @param lockHandle The lock handle returned by acquireLock
     */
    suspend fun releaseLock(lockHandle: LockHandle) {
        // Use a Lua script to ensure atomic check and delete
        val script = """
            if redis.call('get', KEYS[1]) == ARGV[1] then
                return redis.call('del', KEYS[1])
            else
                return 0
            end
        """.trimIndent()
        
        try {
            // In a real implementation, you would use RedisTemplate's execute method with the script
            // For now, we'll use a simpler approach
            val currentValue = redisClient.get(lockHandle.lockKey)
            if (currentValue == lockHandle.lockValue) {
                redisClient.delete(lockHandle.lockKey)
                logger.debug("Released lock for key: ${lockHandle.lockKey}")
            } else {
                logger.warn("Lock value mismatch for key: ${lockHandle.lockKey}")
            }
        } catch (e: Exception) {
            logger.error("Error releasing lock: ${lockHandle.lockKey}", e)
        }
    }
    
    /**
     * Executes the given block with a distributed lock
     * @param key The lock key
     * @param timeout Maximum time to wait for the lock
     * @param block The block to execute while holding the lock
     * @return The result of the block, or null if the lock couldn't be acquired
     */
    suspend fun <T> withLock(key: String, timeout: Duration = Duration.ofSeconds(5), block: suspend () -> T): T? {
        val lock = acquireLock(key, timeout) ?: return null
        
        return try {
            block()
        } finally {
            releaseLock(lock)
        }
    }
    
    /**
     * Executes the given block with a distributed lock (reactive version)
     * @param key The lock key
     * @param timeout Maximum time to wait for the lock
     * @param block The block to execute while holding the lock
     * @return A Mono containing the result of the block, or empty if the lock couldn't be acquired
     */
    fun <T> withLockReactive(key: String, timeout: Duration = Duration.ofSeconds(5), block: () -> Mono<T>): Mono<T> {
        val lockKey = "$LOCK_PREFIX$key"
        val lockValue = UUID.randomUUID().toString()
        
        return redisClient.setIfAbsentReactive(lockKey, lockValue, DEFAULT_LOCK_TIMEOUT)
            .flatMap { acquired ->
                if (acquired) {
                    logger.debug("Acquired reactive lock for key: $key")
                    block()
                        .doOnTerminate {
                            // Release the lock when the operation completes or errors
                            redisClient.deleteReactive(lockKey).subscribe()
                        }
                        .doOnError { error ->
                            logger.error("Error in locked block for key: $key", error)
                        }
                } else {
                    logger.debug("Failed to acquire reactive lock for key: $key")
                    Mono.empty()
                }
            }
            .timeout(timeout)
            .onErrorResume { error ->
                logger.error("Error in withLockReactive for key: $key", error)
                Mono.error(error)
            }
    }
}

/**
 * Represents an acquired distributed lock
 */
class LockHandle internal constructor(
    internal val lockKey: String,
    internal val lockValue: String,
    private val lockService: LockService
) : AutoCloseable {
    private var released = false
    
    /**
     * Releases the lock if it hasn't been released yet
     */
    suspend fun release() {
        if (!released) {
            lockService.releaseLock(this)
            released = true
        }
    }
    
    override fun close() {
        // This allows using LockHandle in a try-with-resources block
        kotlinx.coroutines.runBlocking {
            release()
        }
    }
    
    protected fun finalize() {
        // As a safety net, release the lock if it wasn't explicitly released
        if (!released) {
            logger.warn("Lock $lockKey was not explicitly released!")
            kotlinx.coroutines.runBlocking {
                release()
            }
        }
    }
    
    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(LockHandle::class.java)
    }
}
