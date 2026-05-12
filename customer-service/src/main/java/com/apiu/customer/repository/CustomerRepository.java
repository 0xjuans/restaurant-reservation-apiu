package com.apiu.customer.repository;

import com.apiu.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// @Repository marca esta interfaz como componente de acceso a datos.
// Spring la detecta en el classpath y crea una implementación automáticamente —
// tú nunca escribes el código de las queries básicas.
@Repository
// JpaRepository<Customer, Long> nos da gratis:
//   - save(customer)       → INSERT o UPDATE
//   - findById(id)         → SELECT WHERE id = ?
//   - findAll()            → SELECT *
//   - delete(customer)     → DELETE
//   - count()              → SELECT COUNT(*)
//   - existsById(id)       → SELECT EXISTS(...)
// El primer tipo es la entidad, el segundo es el tipo del ID.
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Spring interpreta el nombre del método y genera el SQL:
    // SELECT * FROM customers WHERE user_id = ?
    // Optional<> significa que puede retornar un resultado o estar vacío
    // (mejor que retornar null, que causa NullPointerExceptions)
    Optional<Customer> findByUserId(Long userId);

    // SELECT EXISTS(SELECT 1 FROM customers WHERE user_id = ?)
    // Útil para verificar si un usuario ya tiene perfil de cliente
    // antes de intentar crear uno nuevo.
    boolean existsByUserId(Long userId);
}
