package ua.sgkhmja.wboard.service.mapper;

import ua.sgkhmja.wboard.domain.*;
import ua.sgkhmja.wboard.service.dto.BoardDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Board and its DTO BoardDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface BoardMapper extends EntityMapper <BoardDTO, Board> {
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.login", target = "ownerLogin")
    BoardDTO toDto(Board board);
    @Mapping(source = "ownerId", target = "owner")
    Board toEntity(BoardDTO boardDTO);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */

    default Board fromId(Long id) {
        if (id == null) {
            return null;
        }
        Board board = new Board();
        board.setId(id);
        return board;
    }
}
