package ua.sgkhmja.wboard.repository;

import ua.sgkhmja.wboard.domain.AdminTools;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import ua.sgkhmja.wboard.domain.User;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data JPA repository for the AdminTools entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminToolsRepository extends JpaRepository<AdminTools,Long> {

    @Query("select admin_tools from AdminTools admin_tools where admin_tools.user.login = ?#{principal.username}")
    List<AdminTools> findByUserIsCurrentUser();

    List<AdminTools> findAllByBoardId(Long boardId);

}
