package ua.sgkhmja.wboard.service.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.sgkhmja.wboard.domain.Board;
import ua.sgkhmja.wboard.domain.User;
import ua.sgkhmja.wboard.security.SecurityUtils;

/**
 * Created by pavel on 10.05.17.
 */
@Component
public class RoleDAO {

    private final JdbcTemplate jdbcTemplate;

    public RoleDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long isReader(User user, Board board){
        Long usesId = user.getId();
        Long boardId = board.getId();

        String sql = "select id from reader where board_user_id in (select id from board_user where user_id = " +
            usesId + "and board_id = " + boardId +" )";

        return this.jdbcTemplate.queryForObject(sql, long.class);
    }

    public Long isWriter(User user, Board board){

        String sql = "select id from writer where reader_id = " + isReader(user, board);

        return this.jdbcTemplate.queryForObject(sql, long.class);
    }

    public Long isAdmin(User user, Board board){

        String sql = "select id from admin where writer_id = " + isWriter(user, board);

        return this.jdbcTemplate.queryForObject(sql, long.class);
    }
}
