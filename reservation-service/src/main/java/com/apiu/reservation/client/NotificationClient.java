package com.apiu.reservation.client;

import com.apiu.reservation.client.dto.NotificationRequest;
import com.apiu.reservation.client.dto.NotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

// Llama al notification-service para enviar emails tras cambios en reservas
@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/api/v1/notifications/send")
    NotificationResponse send(@RequestHeader("Authorization") String token,
                              @RequestBody NotificationRequest request);
}
