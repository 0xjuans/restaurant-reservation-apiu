package com.apiu.reservation.client;

import com.apiu.reservation.client.dto.TableResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

// Llama al table-service registrado en Eureka para verificar la mesa y su capacidad.
// El token se reenvía explícitamente porque table-service también requiere JWT.
@FeignClient(name = "table-service")
public interface TableClient {

    @GetMapping("/api/v1/tables/{id}")
    TableResponse getById(@RequestHeader("Authorization") String token,
                          @PathVariable Long id);
}
