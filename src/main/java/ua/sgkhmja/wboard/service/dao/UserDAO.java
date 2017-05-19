package ua.sgkhmja.wboard.service.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.sgkhmja.wboard.security.SecurityUtils;

/**
 * Created by pavel on 10.05.17.
 */
@Component
public class UserDAO {

    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public long getUserIdByCurrentLogin(){
        String sql = "select id from jhi_user where login ='" + SecurityUtils.getCurrentUserLogin() + "'";
        return this.jdbcTemplate.queryForObject(sql, long.class);
    }

    //TODO Зробити перевырку на адміна
    public boolean isAdmin(){
        //String sql = "select user_id from jhi_user_authority where authority_name = 'ROLE_ADMIN' " +
        //    "and user_id = " + getUserIdByCurrentLogin();
        return false;
    }
}
