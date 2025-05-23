package com.ecommerce.service

import com.ecommerce.generated.model.HealthResponse

interface HealthService {
    fun checkHealth(): HealthResponse
}
