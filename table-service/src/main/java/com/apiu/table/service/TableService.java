package com.apiu.table.service;

import com.apiu.table.dto.request.TableRequest;
import com.apiu.table.dto.request.TableStatusRequest;
import com.apiu.table.dto.response.TableResponse;
import com.apiu.table.entity.RestaurantTable;
import com.apiu.table.entity.TableStatus;
import com.apiu.table.entity.Zone;
import com.apiu.table.exception.ApiException;
import com.apiu.table.repository.TableRepository;
import com.apiu.table.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Lógica de negocio para mesas. El PATCH de estado lo usará el reservation-service
// cuando confirme o cancele una reserva (vía Feign o evento).
@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;
    private final ZoneRepository zoneRepository;

    public List<TableResponse> getAll() {
        return tableRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<TableResponse> getAvailable(Integer minCapacity) {
        List<RestaurantTable> tables = minCapacity != null
                ? tableRepository.findByStatusAndCapacityGreaterThanEqual(TableStatus.AVAILABLE, minCapacity)
                : tableRepository.findByStatus(TableStatus.AVAILABLE);

        return tables.stream().map(this::toResponse).toList();
    }

    public TableResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public TableResponse create(TableRequest request) {
        Zone zone = findZoneOrThrow(request.zoneId());

        if (tableRepository.existsByZoneIdAndNumber(request.zoneId(), request.number())) {
            throw new ApiException(
                    "Ya existe una mesa número " + request.number() + " en esa zona", HttpStatus.CONFLICT);
        }

        var table = RestaurantTable.builder()
                .zone(zone)
                .number(request.number())
                .capacity(request.capacity())
                .build();

        return toResponse(tableRepository.save(table));
    }

    @Transactional
    public TableResponse update(Long id, TableRequest request) {
        var table = findOrThrow(id);
        Zone zone = findZoneOrThrow(request.zoneId());

        boolean zoneChanged   = !table.getZone().getId().equals(request.zoneId());
        boolean numberChanged = !table.getNumber().equals(request.number());

        if ((zoneChanged || numberChanged)
                && tableRepository.existsByZoneIdAndNumber(request.zoneId(), request.number())) {
            throw new ApiException(
                    "Ya existe una mesa número " + request.number() + " en esa zona", HttpStatus.CONFLICT);
        }

        table.setZone(zone);
        table.setNumber(request.number());
        table.setCapacity(request.capacity());

        return toResponse(tableRepository.save(table));
    }

    @Transactional
    public TableResponse updateStatus(Long id, TableStatusRequest request) {
        var table = findOrThrow(id);
        table.setStatus(request.status());
        return toResponse(tableRepository.save(table));
    }

    @Transactional
    public void delete(Long id) {
        findOrThrow(id);
        tableRepository.deleteById(id);
    }

    private RestaurantTable findOrThrow(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new ApiException("Mesa no encontrada", HttpStatus.NOT_FOUND));
    }

    private Zone findZoneOrThrow(Long zoneId) {
        return zoneRepository.findById(zoneId)
                .orElseThrow(() -> new ApiException("Zona no encontrada", HttpStatus.NOT_FOUND));
    }

    private TableResponse toResponse(RestaurantTable t) {
        return new TableResponse(
                t.getId(), t.getZone().getId(), t.getZone().getName(),
                t.getNumber(), t.getCapacity(), t.getStatus(),
                t.getCreatedAt(), t.getUpdatedAt());
    }
}
