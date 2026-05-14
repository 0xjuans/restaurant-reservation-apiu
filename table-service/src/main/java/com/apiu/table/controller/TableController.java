package com.apiu.table.controller;

import com.apiu.table.dto.request.TableRequest;
import com.apiu.table.dto.request.TableStatusRequest;
import com.apiu.table.dto.response.TableResponse;
import com.apiu.table.service.TableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
@Tag(name = "Mesas", description = "Gestión de mesas del restaurante")
public class TableController {

    private final TableService tableService;

    @GetMapping
    @Operation(summary = "Listar todas las mesas")
    public ResponseEntity<List<TableResponse>> getAll() {
        return ResponseEntity.ok(tableService.getAll());
    }

    @GetMapping("/available")
    @Operation(summary = "Listar mesas disponibles")
    public ResponseEntity<List<TableResponse>> getAvailable(
            @RequestParam(required = false) Integer minCapacity) {
        return ResponseEntity.ok(tableService.getAvailable(minCapacity));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener mesa por ID")
    public ResponseEntity<TableResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Crear una mesa")
    public ResponseEntity<TableResponse> create(@Valid @RequestBody TableRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tableService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de una mesa")
    public ResponseEntity<TableResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody TableRequest request) {
        return ResponseEntity.ok(tableService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Cambiar estado de una mesa")
    public ResponseEntity<TableResponse> updateStatus(@PathVariable Long id,
                                                      @Valid @RequestBody TableStatusRequest request) {
        return ResponseEntity.ok(tableService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una mesa")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tableService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
