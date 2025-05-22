package learn.ai.ecommerce.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("users")
data class User(
    @Id
    val id: UUID = UUID.randomUUID(),
    val email: String? = null,
    val phoneNumber: String? = null,
    var status: UserStatus = UserStatus.ACTIVE,
    val createdAt: Instant = Instant.now(),
    var lastLoginAt: Instant? = null
) {
    init {
        require(email != null || phoneNumber != null) {
            "At least one of email or phone number must be provided"
        }
    }
}

enum class UserStatus {
    ACTIVE, INACTIVE, SUSPENDED
}
