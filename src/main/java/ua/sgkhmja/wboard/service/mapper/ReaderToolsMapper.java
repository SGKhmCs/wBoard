package ua.sgkhmja.wboard.service.mapper;

import ua.sgkhmja.wboard.domain.*;
import ua.sgkhmja.wboard.service.dto.ReaderToolsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ReaderTools and its DTO ReaderToolsDTO.
 */
@Mapper(componentModel = "spring", uses = {ReaderMapper.class, })
public interface ReaderToolsMapper extends EntityMapper <ReaderToolsDTO, ReaderTools> {
    @Mapping(source = "reader.id", target = "readerId")
    ReaderToolsDTO toDto(ReaderTools readerTools); 
    @Mapping(source = "readerId", target = "reader")
    ReaderTools toEntity(ReaderToolsDTO readerToolsDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default ReaderTools fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReaderTools readerTools = new ReaderTools();
        readerTools.setId(id);
        return readerTools;
    }
}
