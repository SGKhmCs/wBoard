package ua.sgkhmja.wboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.sgkhmja.wboard.service.BoardService;
import ua.sgkhmja.wboard.web.rest.util.HeaderUtil;
import ua.sgkhmja.wboard.service.dto.BoardDTO;
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
        
    private final BoardService boardService;

    public BoardResource(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * POST  /boards : Create a new board.
     *
     * @param boardDTO the boardDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new boardDTO, or with status 400 (Bad Request) if the board has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/boards")
    @Timed
    public ResponseEntity<BoardDTO> createBoard(@Valid @RequestBody BoardDTO boardDTO) throws URISyntaxException {
        log.debug("REST request to save Board : {}", boardDTO);
        if (boardDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new board cannot already have an ID")).body(null);
        }
        BoardDTO result = boardService.save(boardDTO);
        return ResponseEntity.created(new URI("/api/boards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /boards : Updates an existing board.
     *
     * @param boardDTO the boardDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated boardDTO,
     * or with status 400 (Bad Request) if the boardDTO is not valid,
     * or with status 500 (Internal Server Error) if the boardDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/boards")
    @Timed
    public ResponseEntity<BoardDTO> updateBoard(@Valid @RequestBody BoardDTO boardDTO) throws URISyntaxException {
        log.debug("REST request to update Board : {}", boardDTO);
        if (boardDTO.getId() == null) {
            return createBoard(boardDTO);
        }
        BoardDTO result = boardService.save(boardDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, boardDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /boards : get all the boards.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of boards in body
     */
    @GetMapping("/boards")
    @Timed
    public List<BoardDTO> getAllBoards() {
        log.debug("REST request to get all Boards");
        return boardService.findAll();
    }

    /**
     * GET  /boards/:id : get the "id" board.
     *
     * @param id the id of the boardDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the boardDTO, or with status 404 (Not Found)
     */
    @GetMapping("/boards/{id}")
    @Timed
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Long id) {
        log.debug("REST request to get Board : {}", id);
        BoardDTO boardDTO = boardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(boardDTO));
    }

    /**
     * DELETE  /boards/:id : delete the "id" board.
     *
     * @param id the id of the boardDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/boards/{id}")
    @Timed
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        log.debug("REST request to delete Board : {}", id);
        boardService.delete(id);
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
    public List<BoardDTO> searchBoards(@RequestParam String query) {
        log.debug("REST request to search Boards for query {}", query);
        return boardService.search(query);
    }


}
