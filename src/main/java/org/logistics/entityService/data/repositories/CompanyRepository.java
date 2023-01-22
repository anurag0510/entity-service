package org.logistics.entityService.data.repositories;

import org.logistics.entityService.data.entities.CompanyEntity;
import org.logistics.entityService.data.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    @Query(value = "SELECT * FROM company c WHERE c.is_active = ?1 AND c.is_deleted = false",
            nativeQuery = true)
    Iterable<CompanyEntity> findAllCompaniesWhereActiveStateIs(boolean activeState);

    @Query(value = "SELECT * FROM company c WHERE c.cid = ?1 AND c.is_active = true AND c.is_deleted = false",
            nativeQuery = true)
    Iterable<CompanyEntity> findAllCompaniesBasedOnCidAndAreActive(String cid);

    @Query(value = "SELECT * FROM company c WHERE c.cid = ?1 AND c.is_deleted = false",
            nativeQuery = true)
    Iterable<CompanyEntity> findAllByCid(String cid);
}
