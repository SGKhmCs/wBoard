package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.service.dto.OwnerToolsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing OwnerTools.
 */
public interface OwnerToolsService {

    /**
     * Save a ownerTools.
     *
     * @param ownerToolsDTO the entity to save
     * @return the persisted entity
     */
    OwnerToolsDTO save(OwnerToolsDTO ownerToolsDTO);

    /**
     *  Get all the ownerTools.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<OwnerToolsDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" ownerTools.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    OwnerToolsDTO findOne(Long id);

    /**
     *  Delete the "id" ownerTools.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the ownerTools corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<OwnerToolsDTO> search(String query, Pageable pageable);

    OwnerToolsDTO setOwnerByCurrentLogin(OwnerToolsDTO ownerToolsDTO);
}
