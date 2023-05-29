package org.logistics.entityService.data.repositories;

import org.logistics.entityService.data.entities.PlaceEntity;
import org.logistics.entityService.data.entities.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<RouteEntity, Long> {

    @Query(value = "SELECT * FROM routes r WHERE r.is_active = true AND r.is_deleted = false",
            nativeQuery = true)
    Iterable<RouteEntity> findAllActiveRoutes();

    @Query(value = "SELECT * FROM routes r WHERE r.is_deleted = false",
            nativeQuery = true)
    Iterable<RouteEntity> findAllRoutes();

    @Query(value = "SELECT * FROM routes r WHERE r.rid = ?1 AND r.is_active = true AND r.is_deleted = false",
            nativeQuery = true)
    Iterable<RouteEntity> findAllRoutesBasedOnRidAndAreActive(String rid);

    @Query(value = "SELECT * FROM routes r WHERE r.rid = ?1 AND r.is_deleted = false",
            nativeQuery = true)
    Iterable<RouteEntity> findAllByRid(String rid);

    @Query(value = "SELECT * FROM routes r WHERE r.is_active = true AND r.is_deleted = false AND r.rid = ?1",
            nativeQuery = true)
    Iterable<RouteEntity> findAllActiveRoutesForRid(String rid);
}
