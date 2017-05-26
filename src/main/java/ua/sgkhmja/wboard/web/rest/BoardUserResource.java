package ua.sgkhmja.wboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.sgkhmja.wboard.service.BoardUserService;
import ua.sgkhmja.wboard.web.rest.util.HeaderUtil;
import ua.sgkhmja.wboard.service.dto.BoardUserDTO;
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
 * REST controller for managing BoardUser.
 */
@RestController
@RequestMapping("/api")
public class BoardUserResource {

    private final Logger log = LoggerFactory.getLogger(BoardUserResource.class);

    private static final String ENTITY_NAME = "boardUser";
        
    private final BoardUserService boardUserService;

    public BoardUserResource(BoardUserService boardUserService) {
        this.boardUserService = boardUserService;
    }

    /**
     * POST  /board-users : Create a new boardUser.
     *
     * @param boardUserDTO the boardUserDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new boardUserDTO, or with status 400 (Bad Request) if the boardUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/board-users")
    @Timed
    public ResponseEntity<BoardUserDTO> createBoardUser(@Valid @RequestBody BoardUserDTO boardUserDTO) throws URISyntaxException {
        log.debug("REST request to save BoardUser : {}", boardUserDTO);
        if (boardUserDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new boardUser cannot already have an ID")).body(null);
        }
        BoardUserDTO result = boardUserService.save(boardUserDTO);
        return ResponseEntity.created(new URI("/api/board-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /board-users : Updates an existing boardUser.
     *
     * @param boardUserDTO the boardUserDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated boardUserDTO,
     * or with status 400 (Bad Request) if the boardUserDTO is not valid,
     * or with status 500 (Internal Server Error) if the boardUserDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/board-users")
    @Timed
    public ResponseEntity<BoardUserDTO> updateBoardUser(@Valid @RequestBody BoardUserDTO boardUserDTO) throws URISyntaxException {
        log.debug("REST request to update BoardUser : {}", boardUserDTO);
        if (boardUserDTO.getId() == null) {
            return createBoardUser(boardUserDTO);
        }
        BoardUserDTO result = boardUserService.save(boardUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, boardUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /board-users : get all the boardUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of boardUsers in body
     */
    @GetMapping("/board-users")
    @Timed
    public List<BoardUserDTO> getAllBoardUsers() {
        log.debug("REST request to get all BoardUsers");
        return boardUserService.findAll();
    }

    /**
     * GET  /board-users/:id : get the "id" boardUser.
     *
     * @param id the id of the boardUserDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the boardUserDTO, or with status 404 (Not Found)
     */
    @GetMapping("/board-users/{id}")
    @Timed
    public ResponseEntity<BoardUserDTO> getBoardUser(@PathVariable Long id) {
        log.debug("REST request to get BoardUser : {}", id);
        BoardUserDTO boardUserDTO = boardUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(boardUserDTO));
    }

    /**
     * DELETE  /board-users/:id : delete the "id" boardUser.
     *
     * @param id the id of the boardUserDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/board-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteBoardUser(@PathVariable Long id) {
        log.debug("REST request to delete BoardUser : {}", id);
        boardUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/board-users?query=:query : search for the boardUser corresponding
     * to the query.
     *
     * @param query the query of the boardUser search 
     * @return the result of the search
     */
    @GetMapping("/_search/board-users")
    @Timed
    public List<BoardUserDTO> searchBoardUsers(@RequestParam String query) {
        log.debug("REST request to search BoardUsers for query {}", query);
        return boardUserService.search(query);
    }


}
