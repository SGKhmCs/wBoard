package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.domain.BoardsBody;
import ua.sgkhmja.wboard.repository.BoardsBodyRepository;
import ua.sgkhmja.wboard.repository.search.BoardsBodySearchRepository;
import ua.sgkhmja.wboard.service.dto.BoardsBodyDTO;
import ua.sgkhmja.wboard.service.mapper.BoardsBodyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing BoardsBody.
 */
@Service
@Transactional
public class BoardsBodyService {

    private final Logger log = LoggerFactory.getLogger(BoardsBodyService.class);

    private final BoardsBodyRepository boardsBodyRepository;

    private final BoardsBodyMapper boardsBodyMapper;

    private final BoardsBodySearchRepository boardsBodySearchRepository;

    public BoardsBodyService(BoardsBodyRepository boardsBodyRepository, BoardsBodyMapper boardsBodyMapper, BoardsBodySearchRepository boardsBodySearchRepository) {
        this.boardsBodyRepository = boardsBodyRepository;
        this.boardsBodyMapper = boardsBodyMapper;
        this.boardsBodySearchRepository = boardsBodySearchRepository;
    }

    /**
     * Save a boardsBody.
     *
     * @param boardsBodyDTO the entity to save
     * @return the persisted entity
     */
    public BoardsBodyDTO save(BoardsBodyDTO boardsBodyDTO) {
        log.debug("Request to save BoardsBody : {}", boardsBodyDTO);
        BoardsBody boardsBody = boardsBodyMapper.toEntity(boardsBodyDTO);
        boardsBody = boardsBodyRepository.save(boardsBody);
        BoardsBodyDTO result = boardsBodyMapper.toDto(boardsBody);
        boardsBodySearchRepository.save(boardsBody);
        return result;
    }

    /**
     *  Get all the boardsBodies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BoardsBodyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BoardsBodies");
        return boardsBodyRepository.findAll(pageable)
            .map(boardsBodyMapper::toDto);
    }

    /**
     *  Get one boardsBody by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public BoardsBodyDTO findOne(Long id) {
        log.debug("Request to get BoardsBody : {}", id);
        BoardsBody boardsBody = boardsBodyRepository.findOne(id);
        return boardsBodyMapper.toDto(boardsBody);
    }

    /**
     *  Delete the  boardsBody by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BoardsBody : {}", id);
        boardsBodyRepository.delete(id);
        boardsBodySearchRepository.delete(id);
    }

    /**
     * Search for the boardsBody corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BoardsBodyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BoardsBodies for query {}", query);
        Page<BoardsBody> result = boardsBodySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(boardsBodyMapper::toDto);
    }
}
