package learn.ai.ecommerce.db

import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.postgresql.api.PostgresqlConnection
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.Row
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactive.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitOneOrNull
import org.springframework.r2dbc.core.flow.FetchSpec
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

/**
 * A reactive database client that provides a more convenient API for database operations
 * with built-in retry logic and transaction management.
 */
@Component
class DatabaseClient(
    private val connectionFactory: ConnectionFactory
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    
    private val client = DatabaseClient.create(connectionFactory)
    
    /**
     * Execute a query and return a single result
     */
    suspend fun <T> queryOne(
        sql: String,
        params: Map<String, Any> = emptyMap(),
        rowMapper: (Row) -> T
    ): T? {
        return withRetry("queryOne: $sql") {
            client.sql(sql)
                .bindParameters(params)
                .map(rowMapper)
                .one()
                .awaitSingleOrNull()
        }
    }
    
    /**
     * Execute a query and return multiple results
     */
    suspend fun <T> queryMany(
        sql: String,
        params: Map<String, Any> = emptyMap(),
        rowMapper: (Row) -> T
    ): List<T> {
        return withRetry("queryMany: $sql") {
            client.sql(sql)
                .bindParameters(params)
                .map(rowMapper)
                .all()
                .collectList()
                .awaitSingle()
        }
    }
    
    /**
     * Execute an update/insert/delete query and return the number of affected rows
     */
    suspend fun execute(
        sql: String,
        params: Map<String, Any> = emptyMap()
    ): Long {
        return withRetry("execute: $sql") {
            client.sql(sql)
                .bindParameters(params)
                .fetch()
                .rowsUpdated()
                .awaitSingle()
        }
    }
    
    /**
     * Execute a query within a transaction
     */
    suspend fun <T> inTransaction(block: suspend (DatabaseClient) -> T): T {
        val connection = (connectionFactory as PostgresqlConnectionFactory).create().awaitSingle()
        
        return try {
            // Begin transaction
            connection.beginTransaction().awaitSingle()
            
            // Create a new client with this connection
            val txClient = DatabaseClient.create(connection)
            
            // Execute the block
            val result = block(txClient)
            
            // Commit transaction
            connection.commitTransaction().awaitSingle()
            
            result
        } catch (e: Exception) {
            // Rollback on error
            connection.rollbackTransaction().awaitFirstOrNull()
            throw e
        } finally {
            // Always close the connection
            connection.close().awaitFirstOrNull()
        }
    }
    
    /**
     * Helper function to bind parameters to a query
     */
    private fun <T : FetchSpec<*>> T.bindParameters(params: Map<String, Any>): T {
        var spec = this
        params.forEach { (key, value) ->
            spec = spec.bind(key, value) as T
        }
        return spec
    }
    
    /**
     * Retry logic for database operations
     */
    private suspend fun <T> withRetry(
        operation: String,
        maxRetries: Int = 3,
        initialDelay: Duration = Duration.ofMillis(100),
        maxDelay: Duration = Duration.ofSeconds(5),
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        var lastError: Throwable? = null
        
        repeat(maxRetries) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                lastError = e
                logger.warn("Attempt ${attempt + 1}/$maxRetries failed for $operation: ${e.message}")
                
                if (attempt < maxRetries - 1) {
                    // Exponential backoff with jitter
                    kotlinx.coroutines.delay(currentDelay.toMillis())
                    currentDelay = (currentDelay * 2).coerceAtMost(maxDelay)
                }
            }
        }
        
        throw lastError ?: IllegalStateException("No error recorded in retry loop")
    }
    
    companion object {
        /**
         * Extension function to map a row to a Map<String, Any?>
         */
        fun Row.toMap(): Map<String, Any?> {
            return metadata.columnMetadatas.associate { it.name to get(it.name) }
        }
    }
}
