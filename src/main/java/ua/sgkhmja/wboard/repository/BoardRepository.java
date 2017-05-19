package ua.sgkhmja.wboard.repository;

import ua.sgkhmja.wboard.domain.Board;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Board entity.
 */
@SuppressWarnings("unused")
public interface BoardRepository extends JpaRepository<Board,Long> {

    @Query("select board from Board board where board.owner.login = ?#{principal.username}")
    List<Board> findByOwnerIsCurrentUser();

    @Query("select board from Board board where board.owner.login = ?#{principal.username} or board.pub = true or " +
        "board.id in (select p.board.id from Permission p where p.user.login = ?#{principal.username})")
    List<Board> findByOwnerIsCurrentUserExt();

    @Query("select board from Board board where board.id = ID and board.owner.login = ?#{principal.username}")
    Board findOneExt(Long ID);

}
