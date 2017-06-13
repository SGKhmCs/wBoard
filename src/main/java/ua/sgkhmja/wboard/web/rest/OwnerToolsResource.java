package ua.sgkhmja.wboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.sgkhmja.wboard.service.OwnerToolsService;
import ua.sgkhmja.wboard.web.rest.util.HeaderUtil;
import ua.sgkhmja.wboard.web.rest.util.PaginationUtil;
import ua.sgkhmja.wboard.service.dto.OwnerToolsDTO;
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
 * REST controller for managing OwnerTools.
 */
@RestController
@RequestMapping("/api")
public class OwnerToolsResource {

    private final Logger log = LoggerFactory.getLogger(OwnerToolsResource.class);

    private static final String ENTITY_NAME = "ownerTools";

    private final OwnerToolsService ownerToolsService;

    public OwnerToolsResource(OwnerToolsService ownerToolsService) {
        this.ownerToolsService = ownerToolsService;
    }

    /**
     * POST  /owner-tools : Create a new ownerTools.
     *
     * @param ownerToolsDTO the ownerToolsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ownerToolsDTO, or with status 400 (Bad Request) if the ownerTools has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/owner-tools")
    @Timed
    public ResponseEntity<OwnerToolsDTO> createOwnerTools(@Valid @RequestBody OwnerToolsDTO ownerToolsDTO) throws URISyntaxException {
        log.debug("REST request to save OwnerTools : {}", ownerToolsDTO);
        if (ownerToolsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ownerTools cannot already have an ID")).body(null);
        }

        OwnerToolsDTO wOwner = ownerToolsService.setOwnerByCurrentLogin(ownerToolsDTO);

        OwnerToolsDTO result = ownerToolsService.save(wOwner);
        return ResponseEntity.created(new URI("/api/owner-tools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /owner-tools : Updates an existing ownerTools.
     *
     * @param ownerToolsDTO the ownerToolsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ownerToolsDTO,
     * or with status 400 (Bad Request) if the ownerToolsDTO is not valid,
     * or with status 500 (Internal Server Error) if the ownerToolsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/owner-tools")
    @Timed
    public ResponseEntity<OwnerToolsDTO> updateOwnerTools(@Valid @RequestBody OwnerToolsDTO ownerToolsDTO) throws URISyntaxException {
        log.debug("REST request to update OwnerTools : {}", ownerToolsDTO);
        if (ownerToolsDTO.getId() == null) {
            return createOwnerTools(ownerToolsDTO);
        }
        OwnerToolsDTO result = ownerToolsService.save(ownerToolsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ownerToolsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /owner-tools : get all the ownerTools.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ownerTools in body
     */
    @GetMapping("/owner-tools")
    @Timed
    public ResponseEntity<List<OwnerToolsDTO>> getAllOwnerTools(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of OwnerTools");
        Page<OwnerToolsDTO> page = ownerToolsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/owner-tools");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /owner-tools/:id : get the "id" ownerTools.
     *
     * @param id the id of the ownerToolsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ownerToolsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/owner-tools/{id}")
    @Timed
    public ResponseEntity<OwnerToolsDTO> getOwnerTools(@PathVariable Long id) {
        log.debug("REST request to get OwnerTools : {}", id);
        OwnerToolsDTO ownerToolsDTO = ownerToolsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ownerToolsDTO));
    }

    /**
     * DELETE  /owner-tools/:id : delete the "id" ownerTools.
     *
     * @param id the id of the ownerToolsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/owner-tools/{id}")
    @Timed
    public ResponseEntity<Void> deleteOwnerTools(@PathVariable Long id) {
        log.debug("REST request to delete OwnerTools : {}", id);
        ownerToolsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/owner-tools?query=:query : search for the ownerTools corresponding
     * to the query.
     *
     * @param query the query of the ownerTools search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/owner-tools")
    @Timed
    public ResponseEntity<List<OwnerToolsDTO>> searchOwnerTools(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of OwnerTools for query {}", query);
        Page<OwnerToolsDTO> page = ownerToolsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/owner-tools");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
