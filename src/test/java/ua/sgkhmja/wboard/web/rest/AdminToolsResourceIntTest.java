package ua.sgkhmja.wboard.web.rest;

import ua.sgkhmja.wboard.WBoardApp;

import ua.sgkhmja.wboard.domain.AdminTools;
import ua.sgkhmja.wboard.domain.User;
import ua.sgkhmja.wboard.domain.Board;
import ua.sgkhmja.wboard.repository.AdminToolsRepository;
import ua.sgkhmja.wboard.service.AdminToolsService;
import ua.sgkhmja.wboard.repository.search.AdminToolsSearchRepository;
import ua.sgkhmja.wboard.service.dto.AdminToolsDTO;
import ua.sgkhmja.wboard.service.mapper.AdminToolsMapper;
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
 * Test class for the AdminToolsResource REST controller.
 *
 * @see AdminToolsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WBoardApp.class)
public class AdminToolsResourceIntTest {

    @Autowired
    private AdminToolsRepository adminToolsRepository;

    @Autowired
    private AdminToolsMapper adminToolsMapper;

    @Autowired
    private AdminToolsService adminToolsService;

    @Autowired
    private AdminToolsSearchRepository adminToolsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAdminToolsMockMvc;

    private AdminTools adminTools;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdminToolsResource adminToolsResource = new AdminToolsResource(adminToolsService);
        this.restAdminToolsMockMvc = MockMvcBuilders.standaloneSetup(adminToolsResource)
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
    public static AdminTools createEntity(EntityManager em) {
        AdminTools adminTools = new AdminTools();
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        adminTools.setUser(user);
        // Add required entity
        Board board = BoardResourceIntTest.createEntity(em);
        em.persist(board);
        em.flush();
        adminTools.setBoard(board);
        return adminTools;
    }

    @Before
    public void initTest() {
        adminToolsSearchRepository.deleteAll();
        adminTools = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdminTools() throws Exception {
        int databaseSizeBeforeCreate = adminToolsRepository.findAll().size();

        // Create the AdminTools
        AdminToolsDTO adminToolsDTO = adminToolsMapper.toDto(adminTools);
        restAdminToolsMockMvc.perform(post("/api/admin-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adminToolsDTO)))
            .andExpect(status().isCreated());

        // Validate the AdminTools in the database
        List<AdminTools> adminToolsList = adminToolsRepository.findAll();
        assertThat(adminToolsList).hasSize(databaseSizeBeforeCreate + 1);
        AdminTools testAdminTools = adminToolsList.get(adminToolsList.size() - 1);

        // Validate the AdminTools in Elasticsearch
        AdminTools adminToolsEs = adminToolsSearchRepository.findOne(testAdminTools.getId());
        assertThat(adminToolsEs).isEqualToComparingFieldByField(testAdminTools);
    }

    @Test
    @Transactional
    public void createAdminToolsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adminToolsRepository.findAll().size();

        // Create the AdminTools with an existing ID
        adminTools.setId(1L);
        AdminToolsDTO adminToolsDTO = adminToolsMapper.toDto(adminTools);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminToolsMockMvc.perform(post("/api/admin-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adminToolsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AdminTools> adminToolsList = adminToolsRepository.findAll();
        assertThat(adminToolsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAdminTools() throws Exception {
        // Initialize the database
        adminToolsRepository.saveAndFlush(adminTools);

        // Get all the adminToolsList
        restAdminToolsMockMvc.perform(get("/api/admin-tools?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminTools.getId().intValue())));
    }

    @Test
    @Transactional
    public void getAdminTools() throws Exception {
        // Initialize the database
        adminToolsRepository.saveAndFlush(adminTools);

        // Get the adminTools
        restAdminToolsMockMvc.perform(get("/api/admin-tools/{id}", adminTools.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(adminTools.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAdminTools() throws Exception {
        // Get the adminTools
        restAdminToolsMockMvc.perform(get("/api/admin-tools/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdminTools() throws Exception {
        // Initialize the database
        adminToolsRepository.saveAndFlush(adminTools);
        adminToolsSearchRepository.save(adminTools);
        int databaseSizeBeforeUpdate = adminToolsRepository.findAll().size();

        // Update the adminTools
        AdminTools updatedAdminTools = adminToolsRepository.findOne(adminTools.getId());
        AdminToolsDTO adminToolsDTO = adminToolsMapper.toDto(updatedAdminTools);

        restAdminToolsMockMvc.perform(put("/api/admin-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adminToolsDTO)))
            .andExpect(status().isOk());

        // Validate the AdminTools in the database
        List<AdminTools> adminToolsList = adminToolsRepository.findAll();
        assertThat(adminToolsList).hasSize(databaseSizeBeforeUpdate);
        AdminTools testAdminTools = adminToolsList.get(adminToolsList.size() - 1);

        // Validate the AdminTools in Elasticsearch
        AdminTools adminToolsEs = adminToolsSearchRepository.findOne(testAdminTools.getId());
        assertThat(adminToolsEs).isEqualToComparingFieldByField(testAdminTools);
    }

    @Test
    @Transactional
    public void updateNonExistingAdminTools() throws Exception {
        int databaseSizeBeforeUpdate = adminToolsRepository.findAll().size();

        // Create the AdminTools
        AdminToolsDTO adminToolsDTO = adminToolsMapper.toDto(adminTools);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAdminToolsMockMvc.perform(put("/api/admin-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adminToolsDTO)))
            .andExpect(status().isCreated());

        // Validate the AdminTools in the database
        List<AdminTools> adminToolsList = adminToolsRepository.findAll();
        assertThat(adminToolsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAdminTools() throws Exception {
        // Initialize the database
        adminToolsRepository.saveAndFlush(adminTools);
        adminToolsSearchRepository.save(adminTools);
        int databaseSizeBeforeDelete = adminToolsRepository.findAll().size();

        // Get the adminTools
        restAdminToolsMockMvc.perform(delete("/api/admin-tools/{id}", adminTools.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean adminToolsExistsInEs = adminToolsSearchRepository.exists(adminTools.getId());
        assertThat(adminToolsExistsInEs).isFalse();

        // Validate the database is empty
        List<AdminTools> adminToolsList = adminToolsRepository.findAll();
        assertThat(adminToolsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAdminTools() throws Exception {
        // Initialize the database
        adminToolsRepository.saveAndFlush(adminTools);
        adminToolsSearchRepository.save(adminTools);

        // Search the adminTools
        restAdminToolsMockMvc.perform(get("/api/_search/admin-tools?query=id:" + adminTools.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminTools.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminTools.class);
        AdminTools adminTools1 = new AdminTools();
        adminTools1.setId(1L);
        AdminTools adminTools2 = new AdminTools();
        adminTools2.setId(adminTools1.getId());
        assertThat(adminTools1).isEqualTo(adminTools2);
        adminTools2.setId(2L);
        assertThat(adminTools1).isNotEqualTo(adminTools2);
        adminTools1.setId(null);
        assertThat(adminTools1).isNotEqualTo(adminTools2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminToolsDTO.class);
        AdminToolsDTO adminToolsDTO1 = new AdminToolsDTO();
        adminToolsDTO1.setId(1L);
        AdminToolsDTO adminToolsDTO2 = new AdminToolsDTO();
        assertThat(adminToolsDTO1).isNotEqualTo(adminToolsDTO2);
        adminToolsDTO2.setId(adminToolsDTO1.getId());
        assertThat(adminToolsDTO1).isEqualTo(adminToolsDTO2);
        adminToolsDTO2.setId(2L);
        assertThat(adminToolsDTO1).isNotEqualTo(adminToolsDTO2);
        adminToolsDTO1.setId(null);
        assertThat(adminToolsDTO1).isNotEqualTo(adminToolsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(adminToolsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(adminToolsMapper.fromId(null)).isNull();
    }
}
