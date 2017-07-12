package ua.sgkhmja.wboard.repository;

import org.springframework.data.domain.Page;
import ua.sgkhmja.wboard.domain.AdminTools;
import ua.sgkhmja.wboard.domain.WriterTools;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the WriterTools entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WriterToolsRepository extends JpaRepository<WriterTools,Long> {

    @Query("select writer_tools from WriterTools writer_tools where writer_tools.user.login = ?#{principal.username}")
    List<WriterTools> findByUserIsCurrentUser();

    List<WriterTools> findAllByBoardId(Long boardId);
}
