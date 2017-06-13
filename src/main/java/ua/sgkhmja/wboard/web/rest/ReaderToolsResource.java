package ua.sgkhmja.wboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.sgkhmja.wboard.service.ReaderToolsService;
import ua.sgkhmja.wboard.web.rest.util.HeaderUtil;
import ua.sgkhmja.wboard.web.rest.util.PaginationUtil;
import ua.sgkhmja.wboard.service.dto.ReaderToolsDTO;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ReaderTools.
 */
@RestController
@RequestMapping("/api")
public class ReaderToolsResource {

    private final Logger log = LoggerFactory.getLogger(ReaderToolsResource.class);

    private static final String ENTITY_NAME = "readerTools";

    private final ReaderToolsService readerToolsService;

    public ReaderToolsResource(ReaderToolsService readerToolsService) {
        this.readerToolsService = readerToolsService;
    }

    /**
     * POST  /reader-tools : Create a new readerTools.
     *
     * @param readerToolsDTO the readerToolsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new readerToolsDTO, or with status 400 (Bad Request) if the readerTools has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reader-tools")
    @Timed
    public ResponseEntity<ReaderToolsDTO> createReaderTools(@Valid @RequestBody ReaderToolsDTO readerToolsDTO) throws URISyntaxException {
        log.debug("REST request to save ReaderTools : {}", readerToolsDTO);
        if (readerToolsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new readerTools cannot already have an ID")).body(null);
        }
        ReaderToolsDTO result = readerToolsService.save(readerToolsDTO);
        return ResponseEntity.created(new URI("/api/reader-tools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reader-tools : Updates an existing readerTools.
     *
     * @param readerToolsDTO the readerToolsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated readerToolsDTO,
     * or with status 400 (Bad Request) if the readerToolsDTO is not valid,
     * or with status 500 (Internal Server Error) if the readerToolsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reader-tools")
    @Timed
    public ResponseEntity<ReaderToolsDTO> updateReaderTools(@Valid @RequestBody ReaderToolsDTO readerToolsDTO) throws URISyntaxException {
        log.debug("REST request to update ReaderTools : {}", readerToolsDTO);
        if (readerToolsDTO.getId() == null) {
            return createReaderTools(readerToolsDTO);
        }
        ReaderToolsDTO result = readerToolsService.save(readerToolsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, readerToolsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reader-tools : get all the readerTools.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of readerTools in body
     */
    @GetMapping("/reader-tools")
    @Timed
    public ResponseEntity<List<ReaderToolsDTO>> getAllReaderTools(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ReaderTools");
        Page<ReaderToolsDTO> page = readerToolsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reader-tools");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reader-tools/:id : get the "id" readerTools.
     *
     * @param id the id of the readerToolsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the readerToolsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reader-tools/{id}")
    @Timed
    public ResponseEntity<ReaderToolsDTO> getReaderTools(@PathVariable Long id) {
        log.debug("REST request to get ReaderTools : {}", id);
        ReaderToolsDTO readerToolsDTO = readerToolsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(readerToolsDTO));
    }

    /**
     * DELETE  /reader-tools/:id : delete the "id" readerTools.
     *
     * @param id the id of the readerToolsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reader-tools/{id}")
    @Timed
    public ResponseEntity<Void> deleteReaderTools(@PathVariable Long id) {
        log.debug("REST request to delete ReaderTools : {}", id);
        readerToolsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reader-tools?query=:query : search for the readerTools corresponding
     * to the query.
     *
     * @param query the query of the readerTools search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/reader-tools")
    @Timed
    public ResponseEntity<List<ReaderToolsDTO>> searchReaderTools(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ReaderTools for query {}", query);
        Page<ReaderToolsDTO> page = readerToolsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/reader-tools");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
