package ua.sgkhmja.wboard.repository;

import org.springframework.data.domain.Page;
import ua.sgkhmja.wboard.domain.OwnerTools;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import ua.sgkhmja.wboard.domain.WriterTools;

import java.util.List;

/**
 * Spring Data JPA repository for the OwnerTools entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OwnerToolsRepository extends JpaRepository<OwnerTools,Long> {

    @Query("select owner_tools from OwnerTools owner_tools where owner_tools.owner.login = ?#{principal.username}")
    List<OwnerTools> findByOwnerIsCurrentUser();

    List<OwnerTools> findAllByBoardId(Long boardId);

}
