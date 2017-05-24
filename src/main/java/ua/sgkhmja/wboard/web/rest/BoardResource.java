package ua.sgkhmja.wboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.hibernate.Hibernate;
import ua.sgkhmja.wboard.domain.Authority;
import ua.sgkhmja.wboard.domain.Board;

import ua.sgkhmja.wboard.domain.User;
import ua.sgkhmja.wboard.repository.BoardRepository;
import ua.sgkhmja.wboard.repository.UserRepository;
import ua.sgkhmja.wboard.repository.search.BoardSearchRepository;
import ua.sgkhmja.wboard.service.dao.UserDAO;
import ua.sgkhmja.wboard.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Board.
 */
@RestController
@RequestMapping("/api")
public class BoardResource {

    private final Logger log = LoggerFactory.getLogger(BoardResource.class);

    private static final String ENTITY_NAME = "board";

    private final BoardRepository boardRepository;

    private final BoardSearchRepository boardSearchRepository;

    private UserRepository userRepository;

    private UserDAO userDAO;

    public BoardResource(BoardRepository boardRepository, BoardSearchRepository boardSearchRepository,
                          UserRepository userRepository, UserDAO userDAO) {
        this.boardRepository = boardRepository;
        this.boardSearchRepository = boardSearchRepository;
        this.userRepository = userRepository;
        this.userDAO = userDAO;
    }

    /**
     * POST  /boards : Create a new board.
     *
     * @param board the board to create
     * @return the ResponseEntity with status 201 (Created) and with body the new board, or with status 400 (Bad Request) if the board has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/boards")
    @Timed
    public ResponseEntity<Board> createBoard(@Valid @RequestBody Board board) throws URISyntaxException {
        log.debug("REST request to save Board : {}", board);
        if (board.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new board cannot already have an ID")).body(null);
        }

        board.setOwner(userRepository.findOne(userDAO.getUserIdByCurrentLogin()));

        Board result = boardRepository.save(board);
        boardSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/boards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /boards : Updates an existing board.
     *
     * @param board the board to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated board,
     * or with status 400 (Bad Request) if the board is not valid,
     * or with status 500 (Internal Server Error) if the board couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/boards")
    @Timed
    public ResponseEntity<Board> updateBoard(@Valid @RequestBody Board board) throws URISyntaxException {
        log.debug("REST request to update Board : {}", board);
        if (board.getId() == null) {
            return createBoard(board);
        }
        Board result = boardRepository.save(board);
        boardSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, board.getId().toString()))
            .body(result);
    }

    /**
     * GET  /boards : get all the boards.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of boards in body
     */
    @GetMapping("/boards")
    @Timed
    public List<Board> getAllBoards() {
        log.debug("REST request to get all Boards");

        List<Board> boards = isAdmin()?boardRepository.findAll()
            :boardRepository.findByOwnerIsCurrentUserExt();
        return boards;
    }

    /**
     * GET  /boards/:id : get the "id" board.
     *
     * @param id the id of the board to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the board, or with status 404 (Not Found)
     */
    @GetMapping("/boards/{id}")
    @Timed
    public ResponseEntity<Board> getBoard(@PathVariable Long id) {
        log.debug("REST request to get Board : {}", id);
        Board board = isAdmin()?boardRepository.findOne(id):boardRepository.findOneExt(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(board));
    }

    /**
     * DELETE  /boards/:id : delete the "id" board.
     *
     * @param id the id of the board to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/boards/{id}")
    @Timed
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        log.debug("REST request to delete Board : {}", id);
        boardRepository.delete(id);
        boardSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/boards?query=:query : search for the board corresponding
     * to the query.
     *
     * @param query the query of the board search
     * @return the result of the search
     */
    @GetMapping("/_search/boards")
    @Timed
    public List<Board> searchBoards(@RequestParam String query) {
        log.debug("REST request to search Boards for query {}", query);
        return StreamSupport
            .stream(boardSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


    boolean isAdmin(){
        User user = userRepository.findOne(userDAO.getUserIdByCurrentLogin());
        Set<Authority> authorities = user.getAuthorities();

        return false;
    }

}
