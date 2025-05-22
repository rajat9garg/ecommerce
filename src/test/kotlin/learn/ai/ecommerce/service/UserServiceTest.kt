package learn.ai.ecommerce.service

import learn.ai.ecommerce.dto.UserRegistrationRequest
import learn.ai.ecommerce.exception.UserAlreadyExistsException
import learn.ai.ecommerce.model.User
import learn.ai.ecommerce.model.UserStatus
import learn.ai.ecommerce.repository.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Instant
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    @Test
    fun `registerUser with new email should create user`() {
        // Given
        val request = UserRegistrationRequest(email = "test@example.com")
        val expectedUser = User(email = "test@example.com")
        
        `when`(userRepository.existsByEmail("test@example.com")).thenReturn(Mono.just(false))
        `when`(userRepository.save(any(User::class.java))).thenReturn(Mono.just(expectedUser))

        // When
        val result = userService.registerUser(request)

        // Then
        StepVerifier.create(result)
            .expectNextMatches { user ->
                user.email == "test@example.com" && user.status == UserStatus.ACTIVE
            }
            .verifyComplete()
    }
    
    @Test
    fun `registerUser with existing email should fail`() {
        // Given
        val request = UserRegistrationRequest(email = "existing@example.com")
        
        `when`(userRepository.existsByEmail("existing@example.com")).thenReturn(Mono.just(true))

        // When
        val result = userService.registerUser(request)

        // Then
        StepVerifier.create(result)
            .expectErrorSatisfies { error ->
                assert(error is UserAlreadyExistsException)
                assert(error.message == "User with email existing@example.com already exists")
            }
            .verify()
    }
    
    @Test
    fun `registerUser with phone number should create user`() {
        // Given
        val request = UserRegistrationRequest(phoneNumber = "+1234567890")
        val expectedUser = User(phoneNumber = "+1234567890")
        
        `when`(userRepository.existsByPhoneNumber("+1234567890")).thenReturn(Mono.just(false))
        `when`(userRepository.save(any(User::class.java))).thenReturn(Mono.just(expectedUser))

        // When
        val result = userService.registerUser(request)

        // Then
        StepVerifier.create(result)
            .expectNextMatches { user ->
                user.phoneNumber == "+1234567890" && user.status == UserStatus.ACTIVE
            }
            .verifyComplete()
    }
    
    @Test
    fun `registerUser with no contact info should fail`() {
        // Given
        val request = UserRegistrationRequest()

        // When
        val result = userService.registerUser(request)

        // Then
        StepVerifier.create(result)
            .expectErrorSatisfies { error ->
                assert(error is IllegalArgumentException)
                assert(error.message == "At least one of email or phone number must be provided")
            }
            .verify()
    }
    
    private fun <T> any(type: Class<T>): T = org.mockito.ArgumentMatchers.any(type)
}
