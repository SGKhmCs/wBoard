package ua.sgkhmja.wboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.sgkhmja.wboard.service.BoardsBodyService;
import ua.sgkhmja.wboard.web.rest.util.HeaderUtil;
import ua.sgkhmja.wboard.web.rest.util.PaginationUtil;
import ua.sgkhmja.wboard.service.dto.BoardsBodyDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing BoardsBody.
 */
@RestController
@RequestMapping("/api")
public class BoardsBodyResource {

    private final Logger log = LoggerFactory.getLogger(BoardsBodyResource.class);

    private static final String ENTITY_NAME = "boardsBody";

    private final BoardsBodyService boardsBodyService;

    public BoardsBodyResource(BoardsBodyService boardsBodyService) {
        this.boardsBodyService = boardsBodyService;
    }

    /**
     * POST  /boards-bodies : Create a new boardsBody.
     *
     * @param boardsBodyDTO the boardsBodyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new boardsBodyDTO, or with status 400 (Bad Request) if the boardsBody has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/boards-bodies")
    @Timed
    public ResponseEntity<BoardsBodyDTO> createBoardsBody(@RequestBody BoardsBodyDTO boardsBodyDTO) throws URISyntaxException {
        log.debug("REST request to save BoardsBody : {}", boardsBodyDTO);
        if (boardsBodyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new boardsBody cannot already have an ID")).body(null);
        }
        BoardsBodyDTO result = boardsBodyService.save(boardsBodyDTO);
        return ResponseEntity.created(new URI("/api/boards-bodies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /boards-bodies : Updates an existing boardsBody.
     *
     * @param boardsBodyDTO the boardsBodyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated boardsBodyDTO,
     * or with status 400 (Bad Request) if the boardsBodyDTO is not valid,
     * or with status 500 (Internal Server Error) if the boardsBodyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/boards-bodies")
    @Timed
    public ResponseEntity<BoardsBodyDTO> updateBoardsBody(@RequestBody BoardsBodyDTO boardsBodyDTO) throws URISyntaxException {
        log.debug("REST request to update BoardsBody : {}", boardsBodyDTO);
        if (boardsBodyDTO.getId() == null) {
            return createBoardsBody(boardsBodyDTO);
        }
        BoardsBodyDTO result = boardsBodyService.save(boardsBodyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, boardsBodyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /boards-bodies : get all the boardsBodies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of boardsBodies in body
     */
    @GetMapping("/boards-bodies")
    @Timed
    public ResponseEntity<List<BoardsBodyDTO>> getAllBoardsBodies(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of BoardsBodies");
        Page<BoardsBodyDTO> page = boardsBodyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/boards-bodies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /boards-bodies/:id : get the "id" boardsBody.
     *
     * @param id the id of the boardsBodyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the boardsBodyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/boards-bodies/{id}")
    @Timed
    public ResponseEntity<BoardsBodyDTO> getBoardsBody(@PathVariable Long id) {
        log.debug("REST request to get BoardsBody : {}", id);
        BoardsBodyDTO boardsBodyDTO = boardsBodyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(boardsBodyDTO));
    }

    /**
     * DELETE  /boards-bodies/:id : delete the "id" boardsBody.
     *
     * @param id the id of the boardsBodyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/boards-bodies/{id}")
    @Timed
    public ResponseEntity<Void> deleteBoardsBody(@PathVariable Long id) {
        log.debug("REST request to delete BoardsBody : {}", id);
        boardsBodyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/boards-bodies?query=:query : search for the boardsBody corresponding
     * to the query.
     *
     * @param query the query of the boardsBody search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/boards-bodies")
    @Timed
    public ResponseEntity<List<BoardsBodyDTO>> searchBoardsBodies(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of BoardsBodies for query {}", query);
        Page<BoardsBodyDTO> page = boardsBodyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/boards-bodies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
