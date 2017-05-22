package ua.sgkhmja.wboard.repository;

import ua.sgkhmja.wboard.domain.PermissionType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PermissionType entity.
 */
@SuppressWarnings("unused")
public interface PermissionTypeRepository extends JpaRepository<PermissionType,Long> {

}
