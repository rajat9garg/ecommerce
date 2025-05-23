package com.ecommerce.exception

import org.springframework.http.HttpStatus

open class BusinessException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.message,
    val status: HttpStatus = HttpStatus.BAD_REQUEST,
    cause: Throwable? = null
) : RuntimeException(message, cause)

class UserAlreadyExistsException(
    message: String = ErrorCode.USER_ALREADY_EXISTS.message,
    cause: Throwable? = null
) : BusinessException(ErrorCode.USER_ALREADY_EXISTS, message, HttpStatus.CONFLICT, cause)
