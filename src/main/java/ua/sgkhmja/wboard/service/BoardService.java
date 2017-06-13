package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.service.dto.BoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Board.
 */
public interface BoardService {

    /**
     * Save a board.
     *
     * @param boardDTO the entity to save
     * @return the persisted entity
     */
    BoardDTO save(BoardDTO boardDTO);

    /**
     *  Get all the boards.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BoardDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" board.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BoardDTO findOne(Long id);

    /**
     *  Delete the "id" board.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the board corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BoardDTO> search(String query, Pageable pageable);
}
