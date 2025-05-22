package learn.ai.ecommerce

import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.transaction.ReactiveTransactionManager

@TestConfiguration
class TestConfig {
    @Bean
    @Primary
    fun transactionManager(connectionFactory: ConnectionFactory): ReactiveTransactionManager {
        return R2dbcTransactionManager(connectionFactory)
    }
}
