package ua.sgkhmja.wboard.service.mapper;

import ua.sgkhmja.wboard.domain.*;
import ua.sgkhmja.wboard.service.dto.BoardsBodyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BoardsBody and its DTO BoardsBodyDTO.
 */
@Mapper(componentModel = "spring", uses = {BoardMapper.class, })
public interface BoardsBodyMapper extends EntityMapper <BoardsBodyDTO, BoardsBody> {

    @Mapping(source = "board.id", target = "boardId")
    @Mapping(source = "board.name", target = "boardName")
    BoardsBodyDTO toDto(BoardsBody boardsBody); 

    @Mapping(source = "boardId", target = "board")
    BoardsBody toEntity(BoardsBodyDTO boardsBodyDTO); 
    default BoardsBody fromId(Long id) {
        if (id == null) {
            return null;
        }
        BoardsBody boardsBody = new BoardsBody();
        boardsBody.setId(id);
        return boardsBody;
    }
}
