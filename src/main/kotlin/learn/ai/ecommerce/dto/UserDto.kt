package learn.ai.ecommerce.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

const val PHONE_REGEX = "^\\+?[0-9]{10,15}$"

data class UserRegistrationRequest(
    @field:Email(message = "Invalid email format")
    val email: String? = null,
    
    @field:Pattern(regexp = PHONE_REGEX, message = "Invalid phone number format")
    val phoneNumber: String? = null
) {
    init {
        require(email != null || phoneNumber != null) {
            "At least one of email or phone number must be provided"
        }
    }
}

data class UserResponse(
    val id: String,
    val email: String?,
    val phoneNumber: String?,
    val status: String,
    val createdAt: String,
    val lastLoginAt: String?
)
