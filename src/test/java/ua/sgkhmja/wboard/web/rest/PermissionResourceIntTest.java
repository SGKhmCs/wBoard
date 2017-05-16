package ua.sgkhmja.wboard.web.rest;

import ua.sgkhmja.wboard.WBoardApp;

import ua.sgkhmja.wboard.domain.Permission;
import ua.sgkhmja.wboard.domain.User;
import ua.sgkhmja.wboard.domain.Board;
import ua.sgkhmja.wboard.domain.PermissionType;
import ua.sgkhmja.wboard.repository.PermissionRepository;
import ua.sgkhmja.wboard.repository.search.PermissionSearchRepository;
import ua.sgkhmja.wboard.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PermissionResource REST controller.
 *
 * @see PermissionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WBoardApp.class)
public class PermissionResourceIntTest {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionSearchRepository permissionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPermissionMockMvc;

    private Permission permission;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PermissionResource permissionResource = new PermissionResource(permissionRepository, permissionSearchRepository);
        this.restPermissionMockMvc = MockMvcBuilders.standaloneSetup(permissionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permission createEntity(EntityManager em) {
        Permission permission = new Permission();
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        permission.setUser(user);
        // Add required entity
        Board board = BoardResourceIntTest.createEntity(em);
        em.persist(board);
        em.flush();
        permission.setBoard(board);
        // Add required entity
        PermissionType type = PermissionTypeResourceIntTest.createEntity(em);
        em.persist(type);
        em.flush();
        permission.setType(type);
        return permission;
    }

    @Before
    public void initTest() {
        permissionSearchRepository.deleteAll();
        permission = createEntity(em);
    }

    @Test
    @Transactional
    public void createPermission() throws Exception {
        int databaseSizeBeforeCreate = permissionRepository.findAll().size();

        // Create the Permission
        restPermissionMockMvc.perform(post("/api/permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permission)))
            .andExpect(status().isCreated());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeCreate + 1);
        Permission testPermission = permissionList.get(permissionList.size() - 1);

        // Validate the Permission in Elasticsearch
        Permission permissionEs = permissionSearchRepository.findOne(testPermission.getId());
        assertThat(permissionEs).isEqualToComparingFieldByField(testPermission);
    }

    @Test
    @Transactional
    public void createPermissionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = permissionRepository.findAll().size();

        // Create the Permission with an existing ID
        permission.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermissionMockMvc.perform(post("/api/permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permission)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPermissions() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get all the permissionList
        restPermissionMockMvc.perform(get("/api/permissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permission.getId().intValue())));
    }

    @Test
    @Transactional
    public void getPermission() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);

        // Get the permission
        restPermissionMockMvc.perform(get("/api/permissions/{id}", permission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(permission.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPermission() throws Exception {
        // Get the permission
        restPermissionMockMvc.perform(get("/api/permissions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePermission() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);
        permissionSearchRepository.save(permission);
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();

        // Update the permission
        Permission updatedPermission = permissionRepository.findOne(permission.getId());

        restPermissionMockMvc.perform(put("/api/permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPermission)))
            .andExpect(status().isOk());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate);
        Permission testPermission = permissionList.get(permissionList.size() - 1);

        // Validate the Permission in Elasticsearch
        Permission permissionEs = permissionSearchRepository.findOne(testPermission.getId());
        assertThat(permissionEs).isEqualToComparingFieldByField(testPermission);
    }

    @Test
    @Transactional
    public void updateNonExistingPermission() throws Exception {
        int databaseSizeBeforeUpdate = permissionRepository.findAll().size();

        // Create the Permission

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPermissionMockMvc.perform(put("/api/permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permission)))
            .andExpect(status().isCreated());

        // Validate the Permission in the database
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePermission() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);
        permissionSearchRepository.save(permission);
        int databaseSizeBeforeDelete = permissionRepository.findAll().size();

        // Get the permission
        restPermissionMockMvc.perform(delete("/api/permissions/{id}", permission.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean permissionExistsInEs = permissionSearchRepository.exists(permission.getId());
        assertThat(permissionExistsInEs).isFalse();

        // Validate the database is empty
        List<Permission> permissionList = permissionRepository.findAll();
        assertThat(permissionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPermission() throws Exception {
        // Initialize the database
        permissionRepository.saveAndFlush(permission);
        permissionSearchRepository.save(permission);

        // Search the permission
        restPermissionMockMvc.perform(get("/api/_search/permissions?query=id:" + permission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permission.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Permission.class);
        Permission permission1 = new Permission();
        permission1.setId(1L);
        Permission permission2 = new Permission();
        permission2.setId(permission1.getId());
        assertThat(permission1).isEqualTo(permission2);
        permission2.setId(2L);
        assertThat(permission1).isNotEqualTo(permission2);
        permission1.setId(null);
        assertThat(permission1).isNotEqualTo(permission2);
    }
}
