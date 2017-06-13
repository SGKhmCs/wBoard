package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.service.dto.WriterToolsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing WriterTools.
 */
public interface WriterToolsService {

    /**
     * Save a writerTools.
     *
     * @param writerToolsDTO the entity to save
     * @return the persisted entity
     */
    WriterToolsDTO save(WriterToolsDTO writerToolsDTO);

    /**
     *  Get all the writerTools.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WriterToolsDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" writerTools.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WriterToolsDTO findOne(Long id);

    /**
     *  Delete the "id" writerTools.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the writerTools corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WriterToolsDTO> search(String query, Pageable pageable);
}
