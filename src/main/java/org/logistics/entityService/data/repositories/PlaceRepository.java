package org.logistics.entityService.data.repositories;

import org.logistics.entityService.data.entities.CompanyEntity;
import org.logistics.entityService.data.entities.PlaceEntity;
import org.logistics.entityService.data.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceEntity, Long> {


    @Query(value = "SELECT * FROM places p WHERE p.is_active = true AND p.is_deleted = false AND ST_Contains(p.shape, ST_Point(?1,?2))",
            nativeQuery = true)
    Iterable<PlaceEntity> getPlaceDetailsForCoordinates(Float longitude, Float latitude);

    @Query(value = "SELECT * FROM places p WHERE p.is_active = true AND p.is_deleted = false AND p.google_place_id = ?1",
            nativeQuery = true)
    Iterable<PlaceEntity> findByGooglePlaceId(String googlePlaceId);

    @Query(value = "SELECT * FROM places p WHERE p.is_active = true AND p.is_deleted = false AND p.pid = ?1",
            nativeQuery = true)
    Iterable<PlaceEntity> findAllActivePlacesForPid(String pid);

    @Query(value = "SELECT * FROM places p WHERE p.pid = ?1 AND p.is_active = true AND p.is_deleted = false",
            nativeQuery = true)
    Iterable<PlaceEntity> findAllPlacesBasedOnPidAndAreActive(String pid);

    @Query(value = "SELECT * FROM places p WHERE p.pid = ?1 AND p.is_deleted = false",
            nativeQuery = true)
    Iterable<PlaceEntity> findAllByPid(String pid);

    @Query(value = "SELECT * FROM places p WHERE p.is_active = true AND p.is_deleted = false",
            nativeQuery = true)
    Iterable<PlaceEntity> findAllActivePlaces();

    @Query(value = "SELECT * FROM places p WHERE p.is_deleted = false",
            nativeQuery = true)
    Iterable<PlaceEntity> findAllPlaces();
}
