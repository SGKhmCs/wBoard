package ua.sgkhmja.wboard.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.sgkhmja.wboard.repository.UserRepository;
import ua.sgkhmja.wboard.security.SecurityUtils;
import ua.sgkhmja.wboard.service.dto.BoardDTO;
import ua.sgkhmja.wboard.service.dto.OwnerToolsDTO;

@Service
@Transactional
public class RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleService.class);

    public RoleService(){
    }

}
