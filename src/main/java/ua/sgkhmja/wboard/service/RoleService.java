package ua.sgkhmja.wboard.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.sgkhmja.wboard.domain.Board;
import ua.sgkhmja.wboard.domain.User;
import ua.sgkhmja.wboard.service.dao.UserDAO;
import ua.sgkhmja.wboard.service.dto.BoardDTO;
import ua.sgkhmja.wboard.service.dto.OwnerToolsDTO;

@Service
@Transactional
public class RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleService.class);

    private final UserDAO userDAO;

    public RoleService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public OwnerToolsDTO setOwner(OwnerToolsDTO ownerToolsDTO){
        log.debug("set owner by current login: {}", ownerToolsDTO);

        ownerToolsDTO.setOwnerId(userDAO.getUserIdByCurrentLogin());
        ownerToolsDTO.setOwnerLogin(userDAO.getCurrentUserLogin());

        return ownerToolsDTO;
    }

    public boolean isOwner(User user, Board board){
        return false;
    }
}
