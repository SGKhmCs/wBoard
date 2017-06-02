package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.domain.Board;
import ua.sgkhmja.wboard.domain.BoardUser;
import ua.sgkhmja.wboard.domain.User;
import ua.sgkhmja.wboard.repository.BoardUserRepository;
import ua.sgkhmja.wboard.repository.search.BoardUserSearchRepository;
import ua.sgkhmja.wboard.service.dto.BoardUserDTO;
import ua.sgkhmja.wboard.service.mapper.BoardUserMapper;
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
 * Service Implementation for managing BoardUser.
 */
@Service
@Transactional
public class BoardUserService {

    private final Logger log = LoggerFactory.getLogger(BoardUserService.class);

    private final BoardUserRepository boardUserRepository;

    private final BoardUserMapper boardUserMapper;

    private final BoardUserSearchRepository boardUserSearchRepository;

    public BoardUserService(BoardUserRepository boardUserRepository, BoardUserMapper boardUserMapper, BoardUserSearchRepository boardUserSearchRepository) {
        this.boardUserRepository = boardUserRepository;
        this.boardUserMapper = boardUserMapper;
        this.boardUserSearchRepository = boardUserSearchRepository;
    }

    /**
     * Save a boardUser.
     *
     * @param boardUserDTO the entity to save
     * @return the persisted entity
     */
    public BoardUserDTO save(BoardUserDTO boardUserDTO) {
        log.debug("Request to save BoardUser : {}", boardUserDTO);
        BoardUser boardUser = boardUserMapper.toEntity(boardUserDTO);
        boardUser = boardUserRepository.save(boardUser);
        BoardUserDTO result = boardUserMapper.toDto(boardUser);
        boardUserSearchRepository.save(boardUser);
        return result;
    }

    /**
     *  Get all the boardUsers.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BoardUserDTO> findAll() {
        log.debug("Request to get all BoardUsers");
        List<BoardUserDTO> result = boardUserRepository.findAll().stream()
            .map(boardUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one boardUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public BoardUserDTO findOne(Long id) {
        log.debug("Request to get BoardUser : {}", id);
        BoardUser boardUser = boardUserRepository.findOne(id);
        BoardUserDTO boardUserDTO = boardUserMapper.toDto(boardUser);
        return boardUserDTO;
    }

    /**
     *  Delete the  boardUser by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BoardUser : {}", id);
        boardUserRepository.delete(id);
        boardUserSearchRepository.delete(id);
    }

    /**
     * Search for the boardUser corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BoardUserDTO> search(String query) {
        log.debug("Request to search BoardUsers for query {}", query);
        return StreamSupport
            .stream(boardUserSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(boardUserMapper::toDto)
            .collect(Collectors.toList());
    }

    public BoardUser createBoardUser(User user, Board board){
        BoardUser boardUser = new BoardUser();

        boardUser.setUser(user);
        boardUser.setBoard(board);

        return boardUser;
    }


}
