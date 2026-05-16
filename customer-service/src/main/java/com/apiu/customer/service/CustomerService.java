package com.apiu.customer.service;

import com.apiu.customer.dto.request.CustomerRequest;
import com.apiu.customer.dto.response.CustomerResponse;
import com.apiu.customer.entity.Customer;
import com.apiu.customer.repository.CustomerRepository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    // Crea el perfil de cliente. El userId viene del token JWT,
    // no del body — el cliente no puede elegir a qué usuario pertenece su perfil.
    @Transactional
    public CustomerResponse createProfile(Long userId, CustomerRequest request) {
        if (customerRepository.existsByUserId(userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El usuario ya tiene un perfil de cliente");
        }

        Customer customer = Customer.builder()
                .userId(userId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .build();

        return toResponse(customerRepository.save(customer));
    }

    // Busca el perfil del usuario autenticado
    @Transactional(readOnly = true)
    public CustomerResponse getProfile(Long userId) {
        Customer customer = findByUserId(userId);
        return toResponse(customer);
    }

    // Actualiza solo los campos que el cliente envía
    @Transactional
    public CustomerResponse updateProfile(Long userId, CustomerRequest request) {
        Customer customer = findByUserId(userId);

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhone(request.getPhone());

        return toResponse(customerRepository.save(customer));
    }

    // Lista todos los clientes (solo para administradores)
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAll() {
        return customerRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // Busca por ID de perfil (usado internamente desde otros servicios vía Feign)
    @Transactional(readOnly = true)
    public CustomerResponse getById(Long id) {
        return toResponse(customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente no encontrado: " + id)));
    }

    // Método privado reutilizable para buscar o lanzar 404
    private Customer findByUserId(Long userId) {
        return customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Perfil de cliente no encontrado"));
    }

    // Convierte la entidad al DTO de respuesta (mapeo manual sin dependencias extra)
    private CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .userId(customer.getUserId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phone(customer.getPhone())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
