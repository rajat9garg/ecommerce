package com.ecommerce.controller

import com.ecommerce.generated.api.HealthApi
import com.ecommerce.generated.model.HealthResponse
import com.ecommerce.service.HealthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${api.base-path:/api/v1}")
class HealthController(
    private val healthService: HealthService
) : HealthApi {
    
    override fun healthCheck(): ResponseEntity<HealthResponse> {
        return ResponseEntity.ok(healthService.checkHealth())
    }
}
