package ua.sgkhmja.wboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.sgkhmja.wboard.service.ReaderService;
import ua.sgkhmja.wboard.web.rest.util.HeaderUtil;
import ua.sgkhmja.wboard.service.dto.ReaderDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Reader.
 */
@RestController
@RequestMapping("/api")
public class ReaderResource {

    private final Logger log = LoggerFactory.getLogger(ReaderResource.class);

    private static final String ENTITY_NAME = "reader";
        
    private final ReaderService readerService;

    public ReaderResource(ReaderService readerService) {
        this.readerService = readerService;
    }

    /**
     * POST  /readers : Create a new reader.
     *
     * @param readerDTO the readerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new readerDTO, or with status 400 (Bad Request) if the reader has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/readers")
    @Timed
    public ResponseEntity<ReaderDTO> createReader(@RequestBody ReaderDTO readerDTO) throws URISyntaxException {
        log.debug("REST request to save Reader : {}", readerDTO);
        if (readerDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new reader cannot already have an ID")).body(null);
        }
        ReaderDTO result = readerService.save(readerDTO);
        return ResponseEntity.created(new URI("/api/readers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /readers : Updates an existing reader.
     *
     * @param readerDTO the readerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated readerDTO,
     * or with status 400 (Bad Request) if the readerDTO is not valid,
     * or with status 500 (Internal Server Error) if the readerDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/readers")
    @Timed
    public ResponseEntity<ReaderDTO> updateReader(@RequestBody ReaderDTO readerDTO) throws URISyntaxException {
        log.debug("REST request to update Reader : {}", readerDTO);
        if (readerDTO.getId() == null) {
            return createReader(readerDTO);
        }
        ReaderDTO result = readerService.save(readerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, readerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /readers : get all the readers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of readers in body
     */
    @GetMapping("/readers")
    @Timed
    public List<ReaderDTO> getAllReaders() {
        log.debug("REST request to get all Readers");
        return readerService.findAll();
    }

    /**
     * GET  /readers/:id : get the "id" reader.
     *
     * @param id the id of the readerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the readerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/readers/{id}")
    @Timed
    public ResponseEntity<ReaderDTO> getReader(@PathVariable Long id) {
        log.debug("REST request to get Reader : {}", id);
        ReaderDTO readerDTO = readerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(readerDTO));
    }

    /**
     * DELETE  /readers/:id : delete the "id" reader.
     *
     * @param id the id of the readerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/readers/{id}")
    @Timed
    public ResponseEntity<Void> deleteReader(@PathVariable Long id) {
        log.debug("REST request to delete Reader : {}", id);
        readerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/readers?query=:query : search for the reader corresponding
     * to the query.
     *
     * @param query the query of the reader search 
     * @return the result of the search
     */
    @GetMapping("/_search/readers")
    @Timed
    public List<ReaderDTO> searchReaders(@RequestParam String query) {
        log.debug("REST request to search Readers for query {}", query);
        return readerService.search(query);
    }


}
