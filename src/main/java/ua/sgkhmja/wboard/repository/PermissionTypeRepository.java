package ua.sgkhmja.wboard.repository;

import ua.sgkhmja.wboard.domain.PermissionType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PermissionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionTypeRepository extends JpaRepository<PermissionType,Long> {

}
