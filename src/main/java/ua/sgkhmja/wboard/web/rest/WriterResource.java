package ua.sgkhmja.wboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.sgkhmja.wboard.domain.Writer;

import ua.sgkhmja.wboard.repository.WriterRepository;
import ua.sgkhmja.wboard.repository.search.WriterSearchRepository;
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
 * REST controller for managing Writer.
 */
@RestController
@RequestMapping("/api")
public class WriterResource {

    private final Logger log = LoggerFactory.getLogger(WriterResource.class);

    private static final String ENTITY_NAME = "writer";
        
    private final WriterRepository writerRepository;

    private final WriterSearchRepository writerSearchRepository;

    public WriterResource(WriterRepository writerRepository, WriterSearchRepository writerSearchRepository) {
        this.writerRepository = writerRepository;
        this.writerSearchRepository = writerSearchRepository;
    }

    /**
     * POST  /writers : Create a new writer.
     *
     * @param writer the writer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new writer, or with status 400 (Bad Request) if the writer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/writers")
    @Timed
    public ResponseEntity<Writer> createWriter(@RequestBody Writer writer) throws URISyntaxException {
        log.debug("REST request to save Writer : {}", writer);
        if (writer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new writer cannot already have an ID")).body(null);
        }
        Writer result = writerRepository.save(writer);
        writerSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/writers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /writers : Updates an existing writer.
     *
     * @param writer the writer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated writer,
     * or with status 400 (Bad Request) if the writer is not valid,
     * or with status 500 (Internal Server Error) if the writer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/writers")
    @Timed
    public ResponseEntity<Writer> updateWriter(@RequestBody Writer writer) throws URISyntaxException {
        log.debug("REST request to update Writer : {}", writer);
        if (writer.getId() == null) {
            return createWriter(writer);
        }
        Writer result = writerRepository.save(writer);
        writerSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, writer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /writers : get all the writers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of writers in body
     */
    @GetMapping("/writers")
    @Timed
    public List<Writer> getAllWriters() {
        log.debug("REST request to get all Writers");
        List<Writer> writers = writerRepository.findAll();
        return writers;
    }

    /**
     * GET  /writers/:id : get the "id" writer.
     *
     * @param id the id of the writer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the writer, or with status 404 (Not Found)
     */
    @GetMapping("/writers/{id}")
    @Timed
    public ResponseEntity<Writer> getWriter(@PathVariable Long id) {
        log.debug("REST request to get Writer : {}", id);
        Writer writer = writerRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(writer));
    }

    /**
     * DELETE  /writers/:id : delete the "id" writer.
     *
     * @param id the id of the writer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/writers/{id}")
    @Timed
    public ResponseEntity<Void> deleteWriter(@PathVariable Long id) {
        log.debug("REST request to delete Writer : {}", id);
        writerRepository.delete(id);
        writerSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/writers?query=:query : search for the writer corresponding
     * to the query.
     *
     * @param query the query of the writer search 
     * @return the result of the search
     */
    @GetMapping("/_search/writers")
    @Timed
    public List<Writer> searchWriters(@RequestParam String query) {
        log.debug("REST request to search Writers for query {}", query);
        return StreamSupport
            .stream(writerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
