package ua.sgkhmja.wboard.repository;

import ua.sgkhmja.wboard.domain.AdminTools;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AdminTools entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminToolsRepository extends JpaRepository<AdminTools,Long> {

}
