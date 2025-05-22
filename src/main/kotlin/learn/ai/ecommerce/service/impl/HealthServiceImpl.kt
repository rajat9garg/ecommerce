package learn.ai.ecommerce.service.impl

import learn.ai.ecommerce.generated.model.HealthResponse
import learn.ai.ecommerce.service.HealthService
import org.springframework.stereotype.Service

@Service
class HealthServiceImpl : HealthService {
    override fun checkHealth(): HealthResponse {
        return HealthResponse(
            status = HealthResponse.Status.UP,
            details = mapOf("status" to "Service is running")
        )
    }
}
