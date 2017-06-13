package ua.sgkhmja.wboard.service.mapper;

import ua.sgkhmja.wboard.domain.*;
import ua.sgkhmja.wboard.service.dto.OwnerToolsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity OwnerTools and its DTO OwnerToolsDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, BoardMapper.class, })
public interface OwnerToolsMapper extends EntityMapper <OwnerToolsDTO, OwnerTools> {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.login", target = "ownerLogin")

    @Mapping(source = "board.id", target = "boardId")
    @Mapping(source = "board.name", target = "boardName")
    OwnerToolsDTO toDto(OwnerTools ownerTools); 

    @Mapping(source = "ownerId", target = "owner")

    @Mapping(source = "boardId", target = "board")
    OwnerTools toEntity(OwnerToolsDTO ownerToolsDTO); 
    default OwnerTools fromId(Long id) {
        if (id == null) {
            return null;
        }
        OwnerTools ownerTools = new OwnerTools();
        ownerTools.setId(id);
        return ownerTools;
    }
}
