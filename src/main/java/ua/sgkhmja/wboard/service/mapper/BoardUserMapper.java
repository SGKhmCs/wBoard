package ua.sgkhmja.wboard.service.mapper;

import ua.sgkhmja.wboard.domain.*;
import ua.sgkhmja.wboard.service.dto.BoardUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BoardUser and its DTO BoardUserDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, BoardMapper.class, })
public interface BoardUserMapper extends EntityMapper <BoardUserDTO, BoardUser> {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "board.id", target = "boardId")
    @Mapping(source = "board.name", target = "boardName")
    BoardUserDTO toDto(BoardUser boardUser); 
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "boardId", target = "board")
    BoardUser toEntity(BoardUserDTO boardUserDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default BoardUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        BoardUser boardUser = new BoardUser();
        boardUser.setId(id);
        return boardUser;
    }
}
