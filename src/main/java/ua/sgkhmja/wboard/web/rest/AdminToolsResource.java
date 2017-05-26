package ua.sgkhmja.wboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.sgkhmja.wboard.service.AdminToolsService;
import ua.sgkhmja.wboard.web.rest.util.HeaderUtil;
import ua.sgkhmja.wboard.service.dto.AdminToolsDTO;
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
 * REST controller for managing AdminTools.
 */
@RestController
@RequestMapping("/api")
public class AdminToolsResource {

    private final Logger log = LoggerFactory.getLogger(AdminToolsResource.class);

    private static final String ENTITY_NAME = "adminTools";
        
    private final AdminToolsService adminToolsService;

    public AdminToolsResource(AdminToolsService adminToolsService) {
        this.adminToolsService = adminToolsService;
    }

    /**
     * POST  /admin-tools : Create a new adminTools.
     *
     * @param adminToolsDTO the adminToolsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new adminToolsDTO, or with status 400 (Bad Request) if the adminTools has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/admin-tools")
    @Timed
    public ResponseEntity<AdminToolsDTO> createAdminTools(@RequestBody AdminToolsDTO adminToolsDTO) throws URISyntaxException {
        log.debug("REST request to save AdminTools : {}", adminToolsDTO);
        if (adminToolsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new adminTools cannot already have an ID")).body(null);
        }
        AdminToolsDTO result = adminToolsService.save(adminToolsDTO);
        return ResponseEntity.created(new URI("/api/admin-tools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /admin-tools : Updates an existing adminTools.
     *
     * @param adminToolsDTO the adminToolsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated adminToolsDTO,
     * or with status 400 (Bad Request) if the adminToolsDTO is not valid,
     * or with status 500 (Internal Server Error) if the adminToolsDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/admin-tools")
    @Timed
    public ResponseEntity<AdminToolsDTO> updateAdminTools(@RequestBody AdminToolsDTO adminToolsDTO) throws URISyntaxException {
        log.debug("REST request to update AdminTools : {}", adminToolsDTO);
        if (adminToolsDTO.getId() == null) {
            return createAdminTools(adminToolsDTO);
        }
        AdminToolsDTO result = adminToolsService.save(adminToolsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, adminToolsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /admin-tools : get all the adminTools.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of adminTools in body
     */
    @GetMapping("/admin-tools")
    @Timed
    public List<AdminToolsDTO> getAllAdminTools() {
        log.debug("REST request to get all AdminTools");
        return adminToolsService.findAll();
    }

    /**
     * GET  /admin-tools/:id : get the "id" adminTools.
     *
     * @param id the id of the adminToolsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the adminToolsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/admin-tools/{id}")
    @Timed
    public ResponseEntity<AdminToolsDTO> getAdminTools(@PathVariable Long id) {
        log.debug("REST request to get AdminTools : {}", id);
        AdminToolsDTO adminToolsDTO = adminToolsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(adminToolsDTO));
    }

    /**
     * DELETE  /admin-tools/:id : delete the "id" adminTools.
     *
     * @param id the id of the adminToolsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/admin-tools/{id}")
    @Timed
    public ResponseEntity<Void> deleteAdminTools(@PathVariable Long id) {
        log.debug("REST request to delete AdminTools : {}", id);
        adminToolsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/admin-tools?query=:query : search for the adminTools corresponding
     * to the query.
     *
     * @param query the query of the adminTools search 
     * @return the result of the search
     */
    @GetMapping("/_search/admin-tools")
    @Timed
    public List<AdminToolsDTO> searchAdminTools(@RequestParam String query) {
        log.debug("REST request to search AdminTools for query {}", query);
        return adminToolsService.search(query);
    }


}