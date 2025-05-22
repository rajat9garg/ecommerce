package learn.ai.ecommerce.repository

import learn.ai.ecommerce.model.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
interface UserRepository : ReactiveCrudRepository<User, UUID> {
    fun findByEmail(email: String): Mono<User>
    fun findByPhoneNumber(phoneNumber: String): Mono<User>
    fun existsByEmail(email: String): Mono<Boolean>
    fun existsByPhoneNumber(phoneNumber: String): Mono<Boolean>
}
