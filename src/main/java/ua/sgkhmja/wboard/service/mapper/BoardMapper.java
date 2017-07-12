package ua.sgkhmja.wboard.service.mapper;

import ua.sgkhmja.wboard.domain.*;
import ua.sgkhmja.wboard.service.dto.BoardDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Board and its DTO BoardDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BoardMapper extends EntityMapper <BoardDTO, Board> {
    
    
    default Board fromId(Long id) {
        if (id == null) {
            return null;
        }
        Board board = new Board();
        board.setId(id);
        return board;
    }
}
