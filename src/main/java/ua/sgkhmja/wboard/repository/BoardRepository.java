package ua.sgkhmja.wboard.repository;

import ua.sgkhmja.wboard.domain.Board;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Board entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {

    @Query("select board from Board board where board.createdBy.login = ?#{principal.username}")
    List<Board> findByCreatedByIsCurrentUser();
    
}
