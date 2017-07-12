package ua.sgkhmja.wboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.sgkhmja.wboard.service.WriterToolsService;
import ua.sgkhmja.wboard.service.dto.OwnerToolsDTO;
import ua.sgkhmja.wboard.web.rest.util.HeaderUtil;
import ua.sgkhmja.wboard.web.rest.util.PaginationUtil;
import ua.sgkhmja.wboard.service.dto.WriterToolsDTO;
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
 * REST controller for managing WriterTools.
 */
@RestController
@RequestMapping("/api")
public class WriterToolsResource {

    private final Logger log = LoggerFactory.getLogger(WriterToolsResource.class);

    private static final String ENTITY_NAME = "writerTools";

    private final WriterToolsService writerToolsService;

    public WriterToolsResource(WriterToolsService writerToolsService) {
        this.writerToolsService = writerToolsService;
    }

    /**
     * POST  /writer-tools : Create a new writerTools.
     *
     * @param writerToolsDTO the writerToolsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new writerToolsDTO, or with status 400 (Bad Request) if the writerTools has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/writer-tools")
    @Timed
    public ResponseEntity<WriterToolsDTO> createWriterTools(@Valid @RequestBody WriterToolsDTO writerToolsDTO) throws URISyntaxException {
        log.debug("REST request to save WriterTools : {}", writerToolsDTO);
        if (writerToolsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new writerTools cannot already have an ID")).body(null);
        }
        WriterToolsDTO result = writerToolsService.save(writerToolsDTO);
        return ResponseEntity.created(new URI("/api/writer-tools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /writer-tools : Updates an existing writerTools.
     *
     * @param writerToolsDTO the writerToolsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated writerToolsDTO,
     * or with status 400 (Bad Request) if the writerToolsDTO is not valid,
     * or with status 500 (Internal Server Error) if the writerToolsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/writer-tools")
    @Timed
    public ResponseEntity<WriterToolsDTO> updateWriterTools(@Valid @RequestBody WriterToolsDTO writerToolsDTO) throws URISyntaxException {
        log.debug("REST request to update WriterTools : {}", writerToolsDTO);
        if (writerToolsDTO.getId() == null) {
            return createWriterTools(writerToolsDTO);
        }
        WriterToolsDTO result = writerToolsService.save(writerToolsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, writerToolsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /writer-tools : get all the writerTools.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of writerTools in body
     */
    @GetMapping("/writer-tools")
    @Timed
    public ResponseEntity<List<WriterToolsDTO>> getAllWriterTools(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of WriterTools");
        Page<WriterToolsDTO> page = writerToolsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/writer-tools");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /writer-tools/:id : get the "id" writerTools.
     *
     * @param id the id of the writerToolsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the writerToolsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/writer-tools/{id}")
    @Timed
    public ResponseEntity<WriterToolsDTO> getWriterTools(@PathVariable Long id) {
        log.debug("REST request to get WriterTools : {}", id);
        WriterToolsDTO writerToolsDTO = writerToolsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(writerToolsDTO));
    }

    /**
     * DELETE  /writer-tools/:id : delete the "id" writerTools.
     *
     * @param id the id of the writerToolsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/writer-tools/{id}")
    @Timed
    public ResponseEntity<Void> deleteWriterTools(@PathVariable Long id) {
        log.debug("REST request to delete WriterTools : {}", id);
        writerToolsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/writer-tools?query=:query : search for the writerTools corresponding
     * to the query.
     *
     * @param query the query of the writerTools search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/writer-tools")
    @Timed
    public ResponseEntity<List<WriterToolsDTO>> searchWriterTools(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of WriterTools for query {}", query);
        Page<WriterToolsDTO> page = writerToolsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/writer-tools");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/_by-board/writer-tools")
    @Timed
    public ResponseEntity<List<WriterToolsDTO>> getAllWriterToolsByBoardId (@RequestParam Long boardId, @ApiParam Pageable pageable) {
        Page<WriterToolsDTO> page = writerToolsService.getAllByBoardId(boardId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_by-board/writer-tools");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
