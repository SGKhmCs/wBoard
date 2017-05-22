package ua.sgkhmja.wboard.repository;

import ua.sgkhmja.wboard.domain.Permission;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Permission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {

    @Query("select permission from Permission permission where permission.user.login = ?#{principal.username}")
    List<Permission> findByUserIsCurrentUser();

}
