package ua.sgkhmja.wboard.service.mapper;

import ua.sgkhmja.wboard.domain.*;
import ua.sgkhmja.wboard.service.dto.AdminDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Admin and its DTO AdminDTO.
 */
@Mapper(componentModel = "spring", uses = {WriterMapper.class, })
public interface AdminMapper extends EntityMapper <AdminDTO, Admin> {
    @Mapping(source = "writer.id", target = "writerId")
    AdminDTO toDto(Admin admin); 
    @Mapping(source = "writerId", target = "writer")
    Admin toEntity(AdminDTO adminDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Admin fromId(Long id) {
        if (id == null) {
            return null;
        }
        Admin admin = new Admin();
        admin.setId(id);
        return admin;
    }
}
