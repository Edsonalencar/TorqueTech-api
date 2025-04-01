package br.com.starter.domain.usersGarage;

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
public interface UsersGarageRepository extends JpaRepository<UsersGarage, UUID> {

    @Query("""
                SELECT ug FROM UsersGarage ug
                WHERE ug.user.id = :userId
            """)
    List<UsersGarage> findByUser(@Param("userId") UUID userId);

    @Query("""
                SELECT ug FROM UsersGarage ug
                WHERE ug.user.id = :userId AND ug.garage.id = :garageId
            """)
    Optional<UsersGarage> findByUserAndGarage(@Param("userId") UUID userId, @Param("garageId") UUID garageId);

    @Query("""
                SELECT ug FROM UsersGarage ug
                WHERE ug.garage.id = :garageId
            """)
    Page<UsersGarage> findByGarage(@Param("garageId") UUID garageId, Pageable pageable);

    @Query("""
                SELECT ug FROM UsersGarage ug
                WHERE ug.user.id = :userId AND ug.isPrimary = true
            """)
    Optional<UsersGarage> findPrimaryByUser(@Param("userId") UUID userId);

    @Query("""
                SELECT CASE WHEN COUNT(ug) > 0 THEN true ELSE false END
                FROM UsersGarage ug
                WHERE ug.user.id = :userId AND ug.garage.id = :garageId
            """)
    boolean isUserGarage(@Param("userId") UUID userId, @Param("garageId") UUID garageId);

    @Query("""
                SELECT CASE WHEN COUNT(ug) > 0 THEN true ELSE false END
                FROM UsersGarage ug
                WHERE ug.user.id = :userId AND ug.garage.id = :garageId AND ug.isPrimary = true
            """)
    boolean isUserGaragePrimary(@Param("userId") UUID userId, @Param("garageId") UUID garageId);

    @Query("""
                SELECT ug FROM UsersGarage ug
                WHERE ug.garage.id = :garageId AND ug.isPrimary = true
            """)
    Page<UsersGarage> findPrimaryGarageByGarage(@Param("garageId") UUID garageId, Pageable pageable);
}
