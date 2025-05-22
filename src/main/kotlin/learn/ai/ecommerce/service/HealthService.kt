package learn.ai.ecommerce.service

import learn.ai.ecommerce.generated.model.HealthResponse

interface HealthService {
    fun checkHealth(): HealthResponse
}
