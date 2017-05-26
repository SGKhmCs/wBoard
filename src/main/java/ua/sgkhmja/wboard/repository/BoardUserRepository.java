package ua.sgkhmja.wboard.repository;

import ua.sgkhmja.wboard.domain.BoardUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the BoardUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardUserRepository extends JpaRepository<BoardUser,Long> {

    @Query("select board_user from BoardUser board_user where board_user.user.login = ?#{principal.username}")
    List<BoardUser> findByUserIsCurrentUser();

}
