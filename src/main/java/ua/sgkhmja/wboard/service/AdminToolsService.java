package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.service.dto.AdminToolsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing AdminTools.
 */
public interface AdminToolsService {

    /**
     * Save a adminTools.
     *
     * @param adminToolsDTO the entity to save
     * @return the persisted entity
     */
    AdminToolsDTO save(AdminToolsDTO adminToolsDTO);

    /**
     *  Get all the adminTools.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AdminToolsDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" adminTools.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AdminToolsDTO findOne(Long id);

    /**
     *  Delete the "id" adminTools.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the adminTools corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AdminToolsDTO> search(String query, Pageable pageable);
}
