package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.domain.Board;
import ua.sgkhmja.wboard.repository.BoardRepository;
import ua.sgkhmja.wboard.repository.UserRepository;
import ua.sgkhmja.wboard.repository.search.BoardSearchRepository;
import ua.sgkhmja.wboard.security.SecurityUtils;
import ua.sgkhmja.wboard.service.dto.BoardDTO;
import ua.sgkhmja.wboard.service.dto.OwnerToolsDTO;
import ua.sgkhmja.wboard.service.mapper.BoardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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


    public BoardService(BoardRepository boardRepository, BoardMapper boardMapper,
                        BoardSearchRepository boardSearchRepository) {
        this.boardRepository = boardRepository;
        this.boardMapper = boardMapper;
        this.boardSearchRepository = boardSearchRepository;
    }


    /**
     *  Get all the boards.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BoardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Boards");
        return boardRepository.findAll(pageable)
            .map(boardMapper::toDto);
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
        return boardMapper.toDto(board);
    }

    /**
     * Search for the board corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BoardDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Boards for query {}", query);
        Page<Board> result = boardSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(boardMapper::toDto);
    }

    /**
     * Save a board.
     *
     * @param boardDTO the entity to save
     * @return the persisted entity
     */
    public BoardDTO createBoard(BoardDTO boardDTO) {
        log.debug("Request to save Board : {}", boardDTO);

        Board board = boardMapper.toEntity(boardDTO);
        board = boardRepository.save(board);
        BoardDTO result = boardMapper.toDto(board);
        boardSearchRepository.save(board);

//        if(boardDTO.getId() == null) {
//            save(createOwnerTools(result));
//        }
        return result;
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
}
