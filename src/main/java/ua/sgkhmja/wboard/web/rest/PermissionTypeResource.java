package ua.sgkhmja.wboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.sgkhmja.wboard.domain.PermissionType;

import ua.sgkhmja.wboard.repository.PermissionTypeRepository;
import ua.sgkhmja.wboard.repository.search.PermissionTypeSearchRepository;
import ua.sgkhmja.wboard.web.rest.util.HeaderUtil;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PermissionType.
 */
@RestController
@RequestMapping("/api")
public class PermissionTypeResource {

    private final Logger log = LoggerFactory.getLogger(PermissionTypeResource.class);

    private static final String ENTITY_NAME = "permissionType";
        
    private final PermissionTypeRepository permissionTypeRepository;

    private final PermissionTypeSearchRepository permissionTypeSearchRepository;

    public PermissionTypeResource(PermissionTypeRepository permissionTypeRepository, PermissionTypeSearchRepository permissionTypeSearchRepository) {
        this.permissionTypeRepository = permissionTypeRepository;
        this.permissionTypeSearchRepository = permissionTypeSearchRepository;
    }

    /**
     * POST  /permission-types : Create a new permissionType.
     *
     * @param permissionType the permissionType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new permissionType, or with status 400 (Bad Request) if the permissionType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/permission-types")
    @Timed
    public ResponseEntity<PermissionType> createPermissionType(@Valid @RequestBody PermissionType permissionType) throws URISyntaxException {
        log.debug("REST request to save PermissionType : {}", permissionType);
        if (permissionType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new permissionType cannot already have an ID")).body(null);
        }
        PermissionType result = permissionTypeRepository.save(permissionType);
        permissionTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/permission-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /permission-types : Updates an existing permissionType.
     *
     * @param permissionType the permissionType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated permissionType,
     * or with status 400 (Bad Request) if the permissionType is not valid,
     * or with status 500 (Internal Server Error) if the permissionType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/permission-types")
    @Timed
    public ResponseEntity<PermissionType> updatePermissionType(@Valid @RequestBody PermissionType permissionType) throws URISyntaxException {
        log.debug("REST request to update PermissionType : {}", permissionType);
        if (permissionType.getId() == null) {
            return createPermissionType(permissionType);
        }
        PermissionType result = permissionTypeRepository.save(permissionType);
        permissionTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, permissionType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /permission-types : get all the permissionTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of permissionTypes in body
     */
    @GetMapping("/permission-types")
    @Timed
    public List<PermissionType> getAllPermissionTypes() {
        log.debug("REST request to get all PermissionTypes");
        List<PermissionType> permissionTypes = permissionTypeRepository.findAll();
        return permissionTypes;
    }

    /**
     * GET  /permission-types/:id : get the "id" permissionType.
     *
     * @param id the id of the permissionType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the permissionType, or with status 404 (Not Found)
     */
    @GetMapping("/permission-types/{id}")
    @Timed
    public ResponseEntity<PermissionType> getPermissionType(@PathVariable Long id) {
        log.debug("REST request to get PermissionType : {}", id);
        PermissionType permissionType = permissionTypeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(permissionType));
    }

    /**
     * DELETE  /permission-types/:id : delete the "id" permissionType.
     *
     * @param id the id of the permissionType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/permission-types/{id}")
    @Timed
    public ResponseEntity<Void> deletePermissionType(@PathVariable Long id) {
        log.debug("REST request to delete PermissionType : {}", id);
        permissionTypeRepository.delete(id);
        permissionTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/permission-types?query=:query : search for the permissionType corresponding
     * to the query.
     *
     * @param query the query of the permissionType search 
     * @return the result of the search
     */
    @GetMapping("/_search/permission-types")
    @Timed
    public List<PermissionType> searchPermissionTypes(@RequestParam String query) {
        log.debug("REST request to search PermissionTypes for query {}", query);
        return StreamSupport
            .stream(permissionTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
