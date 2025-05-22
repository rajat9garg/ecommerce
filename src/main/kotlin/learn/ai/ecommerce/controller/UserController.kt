package learn.ai.ecommerce.controller

import learn.ai.ecommerce.dto.UserRegistrationRequest
import learn.ai.ecommerce.dto.UserResponse
import learn.ai.ecommerce.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.net.URI

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(
        @RequestBody request: UserRegistrationRequest
    ): Mono<UserResponse> {
        return userService.registerUser(request)
            .map { user ->
                UserResponse(
                    id = user.id.toString(),
                    email = user.email,
                    phoneNumber = user.phoneNumber,
                    status = user.status.name,
                    createdAt = user.createdAt.toString(),
                    lastLoginAt = user.lastLoginAt?.toString()
                )
            }
    }
}
