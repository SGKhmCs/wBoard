package ua.sgkhmja.wboard.service.mapper;

import ua.sgkhmja.wboard.domain.*;
import ua.sgkhmja.wboard.service.dto.ReaderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Reader and its DTO ReaderDTO.
 */
@Mapper(componentModel = "spring", uses = {BoardUserMapper.class, })
public interface ReaderMapper extends EntityMapper <ReaderDTO, Reader> {
    @Mapping(source = "boardUser.id", target = "boardUserId")
    ReaderDTO toDto(Reader reader); 
    @Mapping(source = "boardUserId", target = "boardUser")
    Reader toEntity(ReaderDTO readerDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Reader fromId(Long id) {
        if (id == null) {
            return null;
        }
        Reader reader = new Reader();
        reader.setId(id);
        return reader;
    }
}
