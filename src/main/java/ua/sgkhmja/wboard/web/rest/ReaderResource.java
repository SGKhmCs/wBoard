package ua.sgkhmja.wboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.sgkhmja.wboard.domain.Reader;

import ua.sgkhmja.wboard.repository.ReaderRepository;
import ua.sgkhmja.wboard.repository.search.ReaderSearchRepository;
import ua.sgkhmja.wboard.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
        
    private final ReaderRepository readerRepository;

    private final ReaderSearchRepository readerSearchRepository;

    public ReaderResource(ReaderRepository readerRepository, ReaderSearchRepository readerSearchRepository) {
        this.readerRepository = readerRepository;
        this.readerSearchRepository = readerSearchRepository;
    }

    /**
     * POST  /readers : Create a new reader.
     *
     * @param reader the reader to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reader, or with status 400 (Bad Request) if the reader has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/readers")
    @Timed
    public ResponseEntity<Reader> createReader(@RequestBody Reader reader) throws URISyntaxException {
        log.debug("REST request to save Reader : {}", reader);
        if (reader.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new reader cannot already have an ID")).body(null);
        }
        Reader result = readerRepository.save(reader);
        readerSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/readers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /readers : Updates an existing reader.
     *
     * @param reader the reader to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reader,
     * or with status 400 (Bad Request) if the reader is not valid,
     * or with status 500 (Internal Server Error) if the reader couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/readers")
    @Timed
    public ResponseEntity<Reader> updateReader(@RequestBody Reader reader) throws URISyntaxException {
        log.debug("REST request to update Reader : {}", reader);
        if (reader.getId() == null) {
            return createReader(reader);
        }
        Reader result = readerRepository.save(reader);
        readerSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reader.getId().toString()))
            .body(result);
    }

    /**
     * GET  /readers : get all the readers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of readers in body
     */
    @GetMapping("/readers")
    @Timed
    public List<Reader> getAllReaders() {
        log.debug("REST request to get all Readers");
        List<Reader> readers = readerRepository.findAll();
        return readers;
    }

    /**
     * GET  /readers/:id : get the "id" reader.
     *
     * @param id the id of the reader to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reader, or with status 404 (Not Found)
     */
    @GetMapping("/readers/{id}")
    @Timed
    public ResponseEntity<Reader> getReader(@PathVariable Long id) {
        log.debug("REST request to get Reader : {}", id);
        Reader reader = readerRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reader));
    }

    /**
     * DELETE  /readers/:id : delete the "id" reader.
     *
     * @param id the id of the reader to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/readers/{id}")
    @Timed
    public ResponseEntity<Void> deleteReader(@PathVariable Long id) {
        log.debug("REST request to delete Reader : {}", id);
        readerRepository.delete(id);
        readerSearchRepository.delete(id);
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
    public List<Reader> searchReaders(@RequestParam String query) {
        log.debug("REST request to search Readers for query {}", query);
        return StreamSupport
            .stream(readerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
