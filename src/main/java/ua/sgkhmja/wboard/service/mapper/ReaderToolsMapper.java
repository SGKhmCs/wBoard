package ua.sgkhmja.wboard.service.mapper;

import ua.sgkhmja.wboard.domain.*;
import ua.sgkhmja.wboard.service.dto.ReaderToolsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ReaderTools and its DTO ReaderToolsDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, BoardMapper.class, })
public interface ReaderToolsMapper extends EntityMapper <ReaderToolsDTO, ReaderTools> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")

    @Mapping(source = "board.id", target = "boardId")
    @Mapping(source = "board.name", target = "boardName")
    ReaderToolsDTO toDto(ReaderTools readerTools); 

    @Mapping(source = "userId", target = "user")

    @Mapping(source = "boardId", target = "board")
    ReaderTools toEntity(ReaderToolsDTO readerToolsDTO); 
    default ReaderTools fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReaderTools readerTools = new ReaderTools();
        readerTools.setId(id);
        return readerTools;
    }
}
