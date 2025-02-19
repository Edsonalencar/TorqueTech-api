package br.com.starter.domain.garage;

import br.com.starter.domain.user.User;
import br.com.starter.domain.user.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GarageRepository extends JpaRepository<Garage, UUID> {

    @Query("""
        SELECT g FROM Garage g
        WHERE g.owner.id = :ownerId
    """)
    Optional<Garage> findByOwner(
        @Param("ownerId") UUID ownerId
    );

    @Query("""
        SELECT g FROM Garage g
        WHERE g.owner.status = :status
        AND (
           :query IS NULL
           OR LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(g.owner.profile.name) LIKE LOWER(CONCAT('%', :query, '%'))
        )
    """)
    Page<Garage> findPageByStatusAndNames(
        @Param("query") String query,
        @Param("status") UserStatus status,
        Pageable pageable
    );
}
