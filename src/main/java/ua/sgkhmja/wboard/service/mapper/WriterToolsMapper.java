package ua.sgkhmja.wboard.service.mapper;

import ua.sgkhmja.wboard.domain.*;
import ua.sgkhmja.wboard.service.dto.WriterToolsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WriterTools and its DTO WriterToolsDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, BoardMapper.class, })
public interface WriterToolsMapper extends EntityMapper <WriterToolsDTO, WriterTools> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")

    @Mapping(source = "board.id", target = "boardId")
    @Mapping(source = "board.name", target = "boardName")
    WriterToolsDTO toDto(WriterTools writerTools); 

    @Mapping(source = "userId", target = "user")

    @Mapping(source = "boardId", target = "board")
    WriterTools toEntity(WriterToolsDTO writerToolsDTO); 
    default WriterTools fromId(Long id) {
        if (id == null) {
            return null;
        }
        WriterTools writerTools = new WriterTools();
        writerTools.setId(id);
        return writerTools;
    }
}
