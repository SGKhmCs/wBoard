package ua.sgkhmja.wboard.service.mapper;

import ua.sgkhmja.wboard.domain.*;
import ua.sgkhmja.wboard.service.dto.BoardDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Board and its DTO BoardDTO.
 */
@Mapper(componentModel = "spring", uses = {BoardsBodyMapper.class, UserMapper.class, })
public interface BoardMapper extends EntityMapper <BoardDTO, Board> {

    @Mapping(source = "body.id", target = "bodyId")

    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    BoardDTO toDto(Board board); 

    @Mapping(source = "bodyId", target = "body")

    @Mapping(source = "createdById", target = "createdBy")
    Board toEntity(BoardDTO boardDTO); 
    default Board fromId(Long id) {
        if (id == null) {
            return null;
        }
        Board board = new Board();
        board.setId(id);
        return board;
    }
}
