package com.apiu.reservation.client;

import com.apiu.reservation.client.dto.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

// Llama al customer-service registrado en Eureka para verificar que el cliente existe.
// El token se reenvía explícitamente porque customer-service también requiere JWT.
@FeignClient(name = "customer-service")
public interface CustomerClient {

    @GetMapping("/api/v1/customers/{id}")
    CustomerResponse getById(@RequestHeader("Authorization") String token,
                             @PathVariable Long id);
}
