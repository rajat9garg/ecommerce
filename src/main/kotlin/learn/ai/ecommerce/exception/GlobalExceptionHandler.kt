package learn.ai.ecommerce.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import reactor.core.publisher.Mono

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgument(ex: IllegalArgumentException): Mono<ErrorResponse> {
        return Mono.just(
            ErrorResponse(
                code = "BAD_REQUEST",
                message = ex.message ?: "Invalid request"
            )
        )
    }

    @ExceptionHandler(WebExchangeBindException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationExceptions(ex: WebExchangeBindException): Mono<ErrorResponse> {
        val errorMessage = ex.bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        return Mono.just(
            ErrorResponse(
                code = "VALIDATION_ERROR",
                message = errorMessage.ifEmpty { "Invalid request" }
            )
        )
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): Mono<ResponseEntity<ErrorResponse>> {
        return Mono.just(
            ResponseEntity
                .status(ex.status)
                .body(
                    ErrorResponse(
                        code = ex.errorCode.code,
                        message = ex.message ?: ex.errorCode.message
                    )
                )
        )
    }
}

data class ErrorResponse(
    val code: String,
    val message: String
)
