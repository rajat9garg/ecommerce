package learn.ai.ecommerce.repository

import learn.ai.ecommerce.db.DatabaseClient
import learn.ai.ecommerce.db.toMap
import org.slf4j.LoggerFactory
import org.springframework.r2dbc.core.DatabaseClient
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

/**
 * Base repository class that provides common CRUD operations
 * @param T The entity type
 * @param ID The ID type of the entity
 * @param tableName The name of the database table
 * @param idColumn The name of the ID column (defaults to "id")
 */
abstract class BaseRepository<T : Any, ID : Any>(
    private val dbClient: DatabaseClient,
    private val entityClass: KClass<T>,
    private val tableName: String,
    private val idColumn: String = "id"
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    
    /**
     * Maps a row to an entity
     */
    protected abstract fun mapRow(row: Map<String, Any>): T
    
    /**
     * Converts an entity to a map of column names to values
     */
    protected fun toMap(entity: T): Map<String, Any?> {
        return entityClass.memberProperties.associate { prop ->
            @Suppress("UNCHECKED_CAST")
            val value = (prop as KProperty1<T, *>).get(entity)
            prop.name to value
        }.filterValues { it != null }
    }
    
    /**
     * Finds an entity by ID
     */
    suspend fun findById(id: ID): T? {
        val sql = "SELECT * FROM $tableName WHERE $idColumn = :id"
        return dbClient.queryOne(sql, mapOf("id" to id)) { mapRow(it.toMap()) }
    }
    
    /**
     * Finds all entities
     */
    suspend fun findAll(): List<T> {
        val sql = "SELECT * FROM $tableName"
        return dbClient.queryMany(sql) { mapRow(it.toMap()) }
    }
    
    /**
     * Saves an entity (insert or update)
     */
    suspend fun save(entity: T): T {
        val values = toMap(entity)
        
        return if (values[idColumn] == null) {
            // Insert
            val columns = values.keys.joinToString(", ")
            val params = values.keys.joinToString(", ") { ":$it" }
            val sql = "INSERT INTO $tableName ($columns) VALUES ($params) RETURNING *"
            
            dbClient.queryOne(sql, values) { mapRow(it.toMap()) }!!
        } else {
            // Update
            val updates = values.filterKeys { it != idColumn }
                .keys
                .joinToString(", ") { "$it = :$it" }
                
            val sql = """
                UPDATE $tableName 
                SET $updates 
                WHERE $idColumn = :$idColumn 
                RETURNING *
            """.trimIndent()
            
            dbClient.queryOne(sql, values) { mapRow(it.toMap()) }!!
        }
    }
    
    /**
     * Deletes an entity by ID
     */
    suspend fun deleteById(id: ID): Boolean {
        val sql = "DELETE FROM $tableName WHERE $idColumn = :id"
        val rowsAffected = dbClient.execute(sql, mapOf("id" to id))
        return rowsAffected > 0
    }
    
    /**
     * Checks if an entity with the given ID exists
     */
    suspend fun existsById(id: ID): Boolean {
        val sql = "SELECT COUNT(*) > 0 FROM $tableName WHERE $idColumn = :id"
        return dbClient.queryOne(sql, mapOf("id" to id)) { row ->
            row.get(0, Boolean::class.java) ?: false
        } ?: false
    }
    
    /**
     * Counts all entities
     */
    suspend fun count(): Long {
        val sql = "SELECT COUNT(*) FROM $tableName"
        return dbClient.queryOne(sql) { row ->
            row.get(0, Long::class.java) ?: 0L
        } ?: 0L
    }
    
    /**
     * Executes a custom query and maps the results
     */
    protected suspend fun <R> query(
        sql: String,
        params: Map<String, Any> = emptyMap(),
        mapper: (Map<String, Any>) -> R
    ): List<R> {
        return dbClient.queryMany(sql, params) { row ->
            mapper(row.toMap())
        }
    }
    
    /**
     * Executes a custom query and returns a single result
     */
    protected suspend fun <R> queryOne(
        sql: String,
        params: Map<String, Any> = emptyMap(),
        mapper: (Map<String, Any>) -> R
    ): R? {
        return dbClient.queryOne(sql, params) { row ->
            mapper(row.toMap())
        }
    }
}
