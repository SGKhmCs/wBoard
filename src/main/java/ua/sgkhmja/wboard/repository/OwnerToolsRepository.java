package ua.sgkhmja.wboard.repository;

import ua.sgkhmja.wboard.domain.OwnerTools;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the OwnerTools entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OwnerToolsRepository extends JpaRepository<OwnerTools,Long> {

    @Query("select owner_tools from OwnerTools owner_tools where owner_tools.owner.login = ?#{principal.username}")
    List<OwnerTools> findByOwnerIsCurrentUser();

    @Query("select owner_tools from OwnerTools owner_tools where owner_tools.board = ?#{boardId}")
    OwnerTools findOwnerToolsByBoardId(Long boardId);

}
