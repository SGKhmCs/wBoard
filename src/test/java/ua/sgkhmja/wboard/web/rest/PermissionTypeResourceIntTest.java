package ua.sgkhmja.wboard.web.rest;

import ua.sgkhmja.wboard.WBoardApp;

import ua.sgkhmja.wboard.domain.PermissionType;
import ua.sgkhmja.wboard.repository.PermissionTypeRepository;
import ua.sgkhmja.wboard.repository.search.PermissionTypeSearchRepository;
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
 * Test class for the PermissionTypeResource REST controller.
 *
 * @see PermissionTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WBoardApp.class)
public class PermissionTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PermissionTypeRepository permissionTypeRepository;

    @Autowired
    private PermissionTypeSearchRepository permissionTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPermissionTypeMockMvc;

    private PermissionType permissionType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PermissionTypeResource permissionTypeResource = new PermissionTypeResource(permissionTypeRepository, permissionTypeSearchRepository);
        this.restPermissionTypeMockMvc = MockMvcBuilders.standaloneSetup(permissionTypeResource)
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
    public static PermissionType createEntity(EntityManager em) {
        PermissionType permissionType = new PermissionType()
            .name(DEFAULT_NAME);
        return permissionType;
    }

    @Before
    public void initTest() {
        permissionTypeSearchRepository.deleteAll();
        permissionType = createEntity(em);
    }

    @Test
    @Transactional
    public void createPermissionType() throws Exception {
        int databaseSizeBeforeCreate = permissionTypeRepository.findAll().size();

        // Create the PermissionType
        restPermissionTypeMockMvc.perform(post("/api/permission-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permissionType)))
            .andExpect(status().isCreated());

        // Validate the PermissionType in the database
        List<PermissionType> permissionTypeList = permissionTypeRepository.findAll();
        assertThat(permissionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PermissionType testPermissionType = permissionTypeList.get(permissionTypeList.size() - 1);
        assertThat(testPermissionType.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the PermissionType in Elasticsearch
        PermissionType permissionTypeEs = permissionTypeSearchRepository.findOne(testPermissionType.getId());
        assertThat(permissionTypeEs).isEqualToComparingFieldByField(testPermissionType);
    }

    @Test
    @Transactional
    public void createPermissionTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = permissionTypeRepository.findAll().size();

        // Create the PermissionType with an existing ID
        permissionType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermissionTypeMockMvc.perform(post("/api/permission-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permissionType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PermissionType> permissionTypeList = permissionTypeRepository.findAll();
        assertThat(permissionTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPermissionTypes() throws Exception {
        // Initialize the database
        permissionTypeRepository.saveAndFlush(permissionType);

        // Get all the permissionTypeList
        restPermissionTypeMockMvc.perform(get("/api/permission-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getPermissionType() throws Exception {
        // Initialize the database
        permissionTypeRepository.saveAndFlush(permissionType);

        // Get the permissionType
        restPermissionTypeMockMvc.perform(get("/api/permission-types/{id}", permissionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(permissionType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPermissionType() throws Exception {
        // Get the permissionType
        restPermissionTypeMockMvc.perform(get("/api/permission-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePermissionType() throws Exception {
        // Initialize the database
        permissionTypeRepository.saveAndFlush(permissionType);
        permissionTypeSearchRepository.save(permissionType);
        int databaseSizeBeforeUpdate = permissionTypeRepository.findAll().size();

        // Update the permissionType
        PermissionType updatedPermissionType = permissionTypeRepository.findOne(permissionType.getId());
        updatedPermissionType
            .name(UPDATED_NAME);

        restPermissionTypeMockMvc.perform(put("/api/permission-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPermissionType)))
            .andExpect(status().isOk());

        // Validate the PermissionType in the database
        List<PermissionType> permissionTypeList = permissionTypeRepository.findAll();
        assertThat(permissionTypeList).hasSize(databaseSizeBeforeUpdate);
        PermissionType testPermissionType = permissionTypeList.get(permissionTypeList.size() - 1);
        assertThat(testPermissionType.getName()).isEqualTo(UPDATED_NAME);

        // Validate the PermissionType in Elasticsearch
        PermissionType permissionTypeEs = permissionTypeSearchRepository.findOne(testPermissionType.getId());
        assertThat(permissionTypeEs).isEqualToComparingFieldByField(testPermissionType);
    }

    @Test
    @Transactional
    public void updateNonExistingPermissionType() throws Exception {
        int databaseSizeBeforeUpdate = permissionTypeRepository.findAll().size();

        // Create the PermissionType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPermissionTypeMockMvc.perform(put("/api/permission-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permissionType)))
            .andExpect(status().isCreated());

        // Validate the PermissionType in the database
        List<PermissionType> permissionTypeList = permissionTypeRepository.findAll();
        assertThat(permissionTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePermissionType() throws Exception {
        // Initialize the database
        permissionTypeRepository.saveAndFlush(permissionType);
        permissionTypeSearchRepository.save(permissionType);
        int databaseSizeBeforeDelete = permissionTypeRepository.findAll().size();

        // Get the permissionType
        restPermissionTypeMockMvc.perform(delete("/api/permission-types/{id}", permissionType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean permissionTypeExistsInEs = permissionTypeSearchRepository.exists(permissionType.getId());
        assertThat(permissionTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<PermissionType> permissionTypeList = permissionTypeRepository.findAll();
        assertThat(permissionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPermissionType() throws Exception {
        // Initialize the database
        permissionTypeRepository.saveAndFlush(permissionType);
        permissionTypeSearchRepository.save(permissionType);

        // Search the permissionType
        restPermissionTypeMockMvc.perform(get("/api/_search/permission-types?query=id:" + permissionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PermissionType.class);
    }
}
