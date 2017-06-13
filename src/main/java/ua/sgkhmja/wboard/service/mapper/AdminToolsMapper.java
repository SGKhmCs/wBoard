package ua.sgkhmja.wboard.service.mapper;

import ua.sgkhmja.wboard.domain.*;
import ua.sgkhmja.wboard.service.dto.AdminToolsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AdminTools and its DTO AdminToolsDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, BoardMapper.class, })
public interface AdminToolsMapper extends EntityMapper <AdminToolsDTO, AdminTools> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")

    @Mapping(source = "board.id", target = "boardId")
    @Mapping(source = "board.name", target = "boardName")
    AdminToolsDTO toDto(AdminTools adminTools); 

    @Mapping(source = "userId", target = "user")

    @Mapping(source = "boardId", target = "board")
    AdminTools toEntity(AdminToolsDTO adminToolsDTO); 
    default AdminTools fromId(Long id) {
        if (id == null) {
            return null;
        }
        AdminTools adminTools = new AdminTools();
        adminTools.setId(id);
        return adminTools;
    }
}
