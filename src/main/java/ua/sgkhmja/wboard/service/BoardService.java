package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.domain.Board;
import ua.sgkhmja.wboard.repository.BoardRepository;
import ua.sgkhmja.wboard.repository.search.BoardSearchRepository;
import ua.sgkhmja.wboard.service.dto.BoardDTO;
import ua.sgkhmja.wboard.service.mapper.BoardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Board.
 */
@Service
@Transactional
public class BoardService {

    private final Logger log = LoggerFactory.getLogger(BoardService.class);
    
    private final BoardRepository boardRepository;

    private final BoardMapper boardMapper;

    private final BoardSearchRepository boardSearchRepository;

    public BoardService(BoardRepository boardRepository, BoardMapper boardMapper, BoardSearchRepository boardSearchRepository) {
        this.boardRepository = boardRepository;
        this.boardMapper = boardMapper;
        this.boardSearchRepository = boardSearchRepository;
    }

    /**
     * Save a board.
     *
     * @param boardDTO the entity to save
     * @return the persisted entity
     */
    public BoardDTO save(BoardDTO boardDTO) {
        log.debug("Request to save Board : {}", boardDTO);
        Board board = boardMapper.toEntity(boardDTO);
        board = boardRepository.save(board);
        BoardDTO result = boardMapper.toDto(board);
        boardSearchRepository.save(board);
        return result;
    }

    /**
     *  Get all the boards.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BoardDTO> findAll() {
        log.debug("Request to get all Boards");
        List<BoardDTO> result = boardRepository.findAll().stream()
            .map(boardMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one board by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public BoardDTO findOne(Long id) {
        log.debug("Request to get Board : {}", id);
        Board board = boardRepository.findOne(id);
        BoardDTO boardDTO = boardMapper.toDto(board);
        return boardDTO;
    }

    /**
     *  Delete the  board by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Board : {}", id);
        boardRepository.delete(id);
        boardSearchRepository.delete(id);
    }

    /**
     * Search for the board corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BoardDTO> search(String query) {
        log.debug("Request to search Boards for query {}", query);
        return StreamSupport
            .stream(boardSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(boardMapper::toDto)
            .collect(Collectors.toList());
    }
}
