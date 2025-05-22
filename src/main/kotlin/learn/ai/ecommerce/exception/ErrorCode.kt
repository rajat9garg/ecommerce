package learn.ai.ecommerce.exception

enum class ErrorCode(val code: String, val message: String) {
    // User related errors (1000-1099)
    USER_ALREADY_EXISTS("USER_1000", "User with this email or phone number already exists"),
    INVALID_EMAIL_FORMAT("USER_1001", "Invalid email format"),
    INVALID_PHONE_FORMAT("USER_1002", "Invalid phone number format"),
    MISSING_CONTACT_INFO("USER_1003", "At least one of email or phone number must be provided")
}
