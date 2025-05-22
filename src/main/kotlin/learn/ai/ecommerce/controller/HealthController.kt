package learn.ai.ecommerce.controller

import learn.ai.ecommerce.generated.api.HealthApi
import learn.ai.ecommerce.generated.model.HealthResponse
import learn.ai.ecommerce.service.HealthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class HealthController(
    private val healthService: HealthService
) : HealthApi {
    
    override fun healthCheck(): ResponseEntity<HealthResponse> {
        return ResponseEntity.ok(healthService.checkHealth())
    }
}
