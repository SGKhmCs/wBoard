package ua.sgkhmja.wboard.service.mapper;

import ua.sgkhmja.wboard.domain.*;
import ua.sgkhmja.wboard.service.dto.WriterToolsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WriterTools and its DTO WriterToolsDTO.
 */
@Mapper(componentModel = "spring", uses = {WriterMapper.class, })
public interface WriterToolsMapper extends EntityMapper <WriterToolsDTO, WriterTools> {
    @Mapping(source = "writer.id", target = "writerId")
    WriterToolsDTO toDto(WriterTools writerTools); 
    @Mapping(source = "writerId", target = "writer")
    WriterTools toEntity(WriterToolsDTO writerToolsDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default WriterTools fromId(Long id) {
        if (id == null) {
            return null;
        }
        WriterTools writerTools = new WriterTools();
        writerTools.setId(id);
        return writerTools;
    }
}
