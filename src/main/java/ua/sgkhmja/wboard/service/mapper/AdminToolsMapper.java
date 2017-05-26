package ua.sgkhmja.wboard.service.mapper;

import ua.sgkhmja.wboard.domain.*;
import ua.sgkhmja.wboard.service.dto.AdminToolsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AdminTools and its DTO AdminToolsDTO.
 */
@Mapper(componentModel = "spring", uses = {AdminMapper.class, })
public interface AdminToolsMapper extends EntityMapper <AdminToolsDTO, AdminTools> {
    @Mapping(source = "admin.id", target = "adminId")
    AdminToolsDTO toDto(AdminTools adminTools); 
    @Mapping(source = "adminId", target = "admin")
    AdminTools toEntity(AdminToolsDTO adminToolsDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default AdminTools fromId(Long id) {
        if (id == null) {
            return null;
        }
        AdminTools adminTools = new AdminTools();
        adminTools.setId(id);
        return adminTools;
    }
}
