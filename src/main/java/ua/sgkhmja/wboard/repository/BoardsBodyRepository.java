package ua.sgkhmja.wboard.repository;

import ua.sgkhmja.wboard.domain.BoardsBody;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BoardsBody entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardsBodyRepository extends JpaRepository<BoardsBody,Long> {
    
}
