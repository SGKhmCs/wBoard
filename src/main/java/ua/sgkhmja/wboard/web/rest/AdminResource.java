package ua.sgkhmja.wboard.web.rest;

import com.codahale.metrics.annotation.Timed;
import ua.sgkhmja.wboard.service.AdminService;
import ua.sgkhmja.wboard.web.rest.util.HeaderUtil;
import ua.sgkhmja.wboard.service.dto.AdminDTO;
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
 * REST controller for managing Admin.
 */
@RestController
@RequestMapping("/api")
public class AdminResource {

    private final Logger log = LoggerFactory.getLogger(AdminResource.class);

    private static final String ENTITY_NAME = "admin";
        
    private final AdminService adminService;

    public AdminResource(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * POST  /admins : Create a new admin.
     *
     * @param adminDTO the adminDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new adminDTO, or with status 400 (Bad Request) if the admin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/admins")
    @Timed
    public ResponseEntity<AdminDTO> createAdmin(@RequestBody AdminDTO adminDTO) throws URISyntaxException {
        log.debug("REST request to save Admin : {}", adminDTO);
        if (adminDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new admin cannot already have an ID")).body(null);
        }
        AdminDTO result = adminService.save(adminDTO);
        return ResponseEntity.created(new URI("/api/admins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /admins : Updates an existing admin.
     *
     * @param adminDTO the adminDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated adminDTO,
     * or with status 400 (Bad Request) if the adminDTO is not valid,
     * or with status 500 (Internal Server Error) if the adminDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/admins")
    @Timed
    public ResponseEntity<AdminDTO> updateAdmin(@RequestBody AdminDTO adminDTO) throws URISyntaxException {
        log.debug("REST request to update Admin : {}", adminDTO);
        if (adminDTO.getId() == null) {
            return createAdmin(adminDTO);
        }
        AdminDTO result = adminService.save(adminDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, adminDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /admins : get all the admins.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of admins in body
     */
    @GetMapping("/admins")
    @Timed
    public List<AdminDTO> getAllAdmins() {
        log.debug("REST request to get all Admins");
        return adminService.findAll();
    }

    /**
     * GET  /admins/:id : get the "id" admin.
     *
     * @param id the id of the adminDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the adminDTO, or with status 404 (Not Found)
     */
    @GetMapping("/admins/{id}")
    @Timed
    public ResponseEntity<AdminDTO> getAdmin(@PathVariable Long id) {
        log.debug("REST request to get Admin : {}", id);
        AdminDTO adminDTO = adminService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(adminDTO));
    }

    /**
     * DELETE  /admins/:id : delete the "id" admin.
     *
     * @param id the id of the adminDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/admins/{id}")
    @Timed
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        log.debug("REST request to delete Admin : {}", id);
        adminService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/admins?query=:query : search for the admin corresponding
     * to the query.
     *
     * @param query the query of the admin search 
     * @return the result of the search
     */
    @GetMapping("/_search/admins")
    @Timed
    public List<AdminDTO> searchAdmins(@RequestParam String query) {
        log.debug("REST request to search Admins for query {}", query);
        return adminService.search(query);
    }


}
