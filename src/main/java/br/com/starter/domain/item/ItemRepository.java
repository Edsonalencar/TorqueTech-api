package br.com.starter.domain.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

    @Query("""
        SELECT i FROM Item i
        WHERE i.garage.id = :garageId
        AND i.id = :id
    """)
    Optional<Item> findByIdAndGarage(
        @Param("id") UUID id,
        @Param("garageId") UUID garageId
    );

    @Query("""
        SELECT i FROM Item i
        WHERE i.garage.id = :garageId
        AND i.status = :status
        AND (
            :query IS NULL
            OR LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%'))
        )
    """)
    List<Item> findAllByGarageAndQuery(
        @Param("garageId") UUID garageId,
        @Param("status") ItemStatus status,
        @Param("query") String query
    );

    @Query("""
        SELECT i FROM Item i
        WHERE i.garage.id = :garageId
        AND i.status = :status
        AND (
            :query IS NULL
            OR LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%'))
        )
    """)
    Page<Item> findPageAllByGarageAndQuery(
        @Param("garageId") UUID garageId,
        @Param("status") ItemStatus status,
        @Param("query") String query,
        Pageable pageable
    );
}
