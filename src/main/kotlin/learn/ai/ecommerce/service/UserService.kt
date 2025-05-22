package learn.ai.ecommerce.service

import learn.ai.ecommerce.dto.UserRegistrationRequest
import learn.ai.ecommerce.exception.UserAlreadyExistsException
import learn.ai.ecommerce.model.User
import learn.ai.ecommerce.model.UserStatus
import learn.ai.ecommerce.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun registerUser(request: UserRegistrationRequest): Mono<User> {
        return validateRequest(request)
            .then(checkUserExists(request))
            .then(createUser(request))
    }

    private fun validateRequest(request: UserRegistrationRequest): Mono<Void> {
        return Mono.fromCallable {
            if (request.email == null && request.phoneNumber == null) {
                throw IllegalArgumentException("At least one of email or phone number must be provided")
            }
        }.then()
    }

    private fun checkUserExists(request: UserRegistrationRequest): Mono<Void> {
        val checks = mutableListOf<Mono<Boolean>>()
        
        request.email?.let { email ->
            checks.add(userRepository.existsByEmail(email).map { exists ->
                if (exists) throw UserAlreadyExistsException("User with email $email already exists")
                else true
            })
        }
        
        request.phoneNumber?.let { phoneNumber ->
            checks.add(userRepository.existsByPhoneNumber(phoneNumber).map { exists ->
                if (exists) throw UserAlreadyExistsException("User with phone number $phoneNumber already exists")
                else true
            })
        }
        
        return if (checks.isNotEmpty()) {
            Mono.`when`(checks).then()
        } else {
            Mono.empty()
        }
    }

    private fun createUser(request: UserRegistrationRequest): Mono<User> {
        val user = User(
            email = request.email,
            phoneNumber = request.phoneNumber,
            status = UserStatus.ACTIVE,
            createdAt = Instant.now()
        )
        return userRepository.save(user)
    }
}
