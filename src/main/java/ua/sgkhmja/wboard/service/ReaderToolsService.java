package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.service.dto.ReaderToolsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ReaderTools.
 */
public interface ReaderToolsService {

    /**
     * Save a readerTools.
     *
     * @param readerToolsDTO the entity to save
     * @return the persisted entity
     */
    ReaderToolsDTO save(ReaderToolsDTO readerToolsDTO);

    /**
     *  Get all the readerTools.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ReaderToolsDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" readerTools.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ReaderToolsDTO findOne(Long id);

    /**
     *  Delete the "id" readerTools.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the readerTools corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ReaderToolsDTO> search(String query, Pageable pageable);
}
