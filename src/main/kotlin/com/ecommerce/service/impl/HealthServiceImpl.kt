package com.ecommerce.service.impl

import com.ecommerce.generated.model.HealthResponse
import com.ecommerce.service.HealthService
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
