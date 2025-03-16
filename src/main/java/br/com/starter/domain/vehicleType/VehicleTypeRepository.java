package br.com.starter.domain.vehicleType;

import br.com.starter.domain.local.Local;
import br.com.starter.domain.local.LocalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, UUID> {
    Page<VehicleType> findAll(Pageable pageable);

    Page<VehicleType> findByBrandContainingIgnoreCaseAndModelContainingIgnoreCase(String brand, String model, Pageable pageable);

    Optional<VehicleType> findByModel(String model);

    @Query("""
        SELECT v FROM VehicleType v
        WHERE (
            :query IS NULL
            OR LOWER(v.model) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(v.brand) LIKE LOWER(CONCAT('%', :query, '%'))
        )
    """)
    Page<VehicleType> findPageByQuery(
        @Param("query") String query,
        Pageable pageable
    );
}
