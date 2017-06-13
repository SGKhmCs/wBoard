package ua.sgkhmja.wboard.repository;

import ua.sgkhmja.wboard.domain.ReaderTools;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the ReaderTools entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReaderToolsRepository extends JpaRepository<ReaderTools,Long> {

    @Query("select reader_tools from ReaderTools reader_tools where reader_tools.user.login = ?#{principal.username}")
    List<ReaderTools> findByUserIsCurrentUser();
    
}
