package ua.sgkhmja.wboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.sgkhmja.wboard.service.WriterService;
import ua.sgkhmja.wboard.web.rest.util.HeaderUtil;
import ua.sgkhmja.wboard.service.dto.WriterDTO;
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
 * REST controller for managing Writer.
 */
@RestController
@RequestMapping("/api")
public class WriterResource {

    private final Logger log = LoggerFactory.getLogger(WriterResource.class);

    private static final String ENTITY_NAME = "writer";
        
    private final WriterService writerService;

    public WriterResource(WriterService writerService) {
        this.writerService = writerService;
    }

    /**
     * POST  /writers : Create a new writer.
     *
     * @param writerDTO the writerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new writerDTO, or with status 400 (Bad Request) if the writer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/writers")
    @Timed
    public ResponseEntity<WriterDTO> createWriter(@RequestBody WriterDTO writerDTO) throws URISyntaxException {
        log.debug("REST request to save Writer : {}", writerDTO);
        if (writerDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new writer cannot already have an ID")).body(null);
        }
        WriterDTO result = writerService.save(writerDTO);
        return ResponseEntity.created(new URI("/api/writers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /writers : Updates an existing writer.
     *
     * @param writerDTO the writerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated writerDTO,
     * or with status 400 (Bad Request) if the writerDTO is not valid,
     * or with status 500 (Internal Server Error) if the writerDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/writers")
    @Timed
    public ResponseEntity<WriterDTO> updateWriter(@RequestBody WriterDTO writerDTO) throws URISyntaxException {
        log.debug("REST request to update Writer : {}", writerDTO);
        if (writerDTO.getId() == null) {
            return createWriter(writerDTO);
        }
        WriterDTO result = writerService.save(writerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, writerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /writers : get all the writers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of writers in body
     */
    @GetMapping("/writers")
    @Timed
    public List<WriterDTO> getAllWriters() {
        log.debug("REST request to get all Writers");
        return writerService.findAll();
    }

    /**
     * GET  /writers/:id : get the "id" writer.
     *
     * @param id the id of the writerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the writerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/writers/{id}")
    @Timed
    public ResponseEntity<WriterDTO> getWriter(@PathVariable Long id) {
        log.debug("REST request to get Writer : {}", id);
        WriterDTO writerDTO = writerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(writerDTO));
    }

    /**
     * DELETE  /writers/:id : delete the "id" writer.
     *
     * @param id the id of the writerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/writers/{id}")
    @Timed
    public ResponseEntity<Void> deleteWriter(@PathVariable Long id) {
        log.debug("REST request to delete Writer : {}", id);
        writerService.delete(id);
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
    public List<WriterDTO> searchWriters(@RequestParam String query) {
        log.debug("REST request to search Writers for query {}", query);
        return writerService.search(query);
    }


}
