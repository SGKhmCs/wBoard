package ua.sgkhmja.wboard.web.rest;

import ua.sgkhmja.wboard.WBoardApp;

import ua.sgkhmja.wboard.domain.OwnerTools;
import ua.sgkhmja.wboard.domain.Board;
import ua.sgkhmja.wboard.repository.OwnerToolsRepository;
import ua.sgkhmja.wboard.service.BussinesLogicService;
import ua.sgkhmja.wboard.service.OwnerToolsService;
import ua.sgkhmja.wboard.repository.search.OwnerToolsSearchRepository;
import ua.sgkhmja.wboard.service.dto.OwnerToolsDTO;
import ua.sgkhmja.wboard.service.mapper.OwnerToolsMapper;
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
 * Test class for the OwnerToolsResource REST controller.
 *
 * @see OwnerToolsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WBoardApp.class)
public class OwnerToolsResourceIntTest {

    @Autowired
    private OwnerToolsRepository ownerToolsRepository;

    @Autowired
    private OwnerToolsMapper ownerToolsMapper;

    @Autowired
    private OwnerToolsService ownerToolsService;

    @Autowired
    private BussinesLogicService bussinesLogicService;

    @Autowired
    private OwnerToolsSearchRepository ownerToolsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOwnerToolsMockMvc;

    private OwnerTools ownerTools;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OwnerToolsResource ownerToolsResource = new OwnerToolsResource(bussinesLogicService, ownerToolsService);
        this.restOwnerToolsMockMvc = MockMvcBuilders.standaloneSetup(ownerToolsResource)
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
    public static OwnerTools createEntity(EntityManager em) {
        OwnerTools ownerTools = new OwnerTools();
        // Add required entity
        Board board = BoardResourceIntTest.createEntity(em);
        em.persist(board);
        em.flush();
        ownerTools.setBoard(board);
        return ownerTools;
    }

    @Before
    public void initTest() {
        ownerToolsSearchRepository.deleteAll();
        ownerTools = createEntity(em);
    }

    @Test
    @Transactional
    public void createOwnerTools() throws Exception {
        int databaseSizeBeforeCreate = ownerToolsRepository.findAll().size();

        // Create the OwnerTools
        OwnerToolsDTO ownerToolsDTO = ownerToolsMapper.toDto(ownerTools);
        restOwnerToolsMockMvc.perform(post("/api/owner-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ownerToolsDTO)))
            .andExpect(status().isCreated());

        // Validate the OwnerTools in the database
        List<OwnerTools> ownerToolsList = ownerToolsRepository.findAll();
        assertThat(ownerToolsList).hasSize(databaseSizeBeforeCreate + 1);
        OwnerTools testOwnerTools = ownerToolsList.get(ownerToolsList.size() - 1);

        // Validate the OwnerTools in Elasticsearch
        OwnerTools ownerToolsEs = ownerToolsSearchRepository.findOne(testOwnerTools.getId());
        assertThat(ownerToolsEs).isEqualToComparingFieldByField(testOwnerTools);
    }

    @Test
    @Transactional
    public void createOwnerToolsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ownerToolsRepository.findAll().size();

        // Create the OwnerTools with an existing ID
        ownerTools.setId(1L);
        OwnerToolsDTO ownerToolsDTO = ownerToolsMapper.toDto(ownerTools);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOwnerToolsMockMvc.perform(post("/api/owner-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ownerToolsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<OwnerTools> ownerToolsList = ownerToolsRepository.findAll();
        assertThat(ownerToolsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOwnerTools() throws Exception {
        // Initialize the database
        ownerToolsRepository.saveAndFlush(ownerTools);

        // Get all the ownerToolsList
        restOwnerToolsMockMvc.perform(get("/api/owner-tools?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ownerTools.getId().intValue())));
    }

    @Test
    @Transactional
    public void getOwnerTools() throws Exception {
        // Initialize the database
        ownerToolsRepository.saveAndFlush(ownerTools);

        // Get the ownerTools
        restOwnerToolsMockMvc.perform(get("/api/owner-tools/{id}", ownerTools.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ownerTools.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOwnerTools() throws Exception {
        // Get the ownerTools
        restOwnerToolsMockMvc.perform(get("/api/owner-tools/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOwnerTools() throws Exception {
        // Initialize the database
        ownerToolsRepository.saveAndFlush(ownerTools);
        ownerToolsSearchRepository.save(ownerTools);
        int databaseSizeBeforeUpdate = ownerToolsRepository.findAll().size();

        // Update the ownerTools
        OwnerTools updatedOwnerTools = ownerToolsRepository.findOne(ownerTools.getId());
        OwnerToolsDTO ownerToolsDTO = ownerToolsMapper.toDto(updatedOwnerTools);

        restOwnerToolsMockMvc.perform(put("/api/owner-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ownerToolsDTO)))
            .andExpect(status().isOk());

        // Validate the OwnerTools in the database
        List<OwnerTools> ownerToolsList = ownerToolsRepository.findAll();
        assertThat(ownerToolsList).hasSize(databaseSizeBeforeUpdate);
        OwnerTools testOwnerTools = ownerToolsList.get(ownerToolsList.size() - 1);

        // Validate the OwnerTools in Elasticsearch
        OwnerTools ownerToolsEs = ownerToolsSearchRepository.findOne(testOwnerTools.getId());
        assertThat(ownerToolsEs).isEqualToComparingFieldByField(testOwnerTools);
    }

    @Test
    @Transactional
    public void updateNonExistingOwnerTools() throws Exception {
        int databaseSizeBeforeUpdate = ownerToolsRepository.findAll().size();

        // Create the OwnerTools
        OwnerToolsDTO ownerToolsDTO = ownerToolsMapper.toDto(ownerTools);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOwnerToolsMockMvc.perform(put("/api/owner-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ownerToolsDTO)))
            .andExpect(status().isCreated());

        // Validate the OwnerTools in the database
        List<OwnerTools> ownerToolsList = ownerToolsRepository.findAll();
        assertThat(ownerToolsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOwnerTools() throws Exception {
        // Initialize the database
        ownerToolsRepository.saveAndFlush(ownerTools);
        ownerToolsSearchRepository.save(ownerTools);
        int databaseSizeBeforeDelete = ownerToolsRepository.findAll().size();

        // Get the ownerTools
        restOwnerToolsMockMvc.perform(delete("/api/owner-tools/{id}", ownerTools.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean ownerToolsExistsInEs = ownerToolsSearchRepository.exists(ownerTools.getId());
        assertThat(ownerToolsExistsInEs).isFalse();

        // Validate the database is empty
        List<OwnerTools> ownerToolsList = ownerToolsRepository.findAll();
        assertThat(ownerToolsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOwnerTools() throws Exception {
        // Initialize the database
        ownerToolsRepository.saveAndFlush(ownerTools);
        ownerToolsSearchRepository.save(ownerTools);

        // Search the ownerTools
        restOwnerToolsMockMvc.perform(get("/api/_search/owner-tools?query=id:" + ownerTools.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ownerTools.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OwnerTools.class);
        OwnerTools ownerTools1 = new OwnerTools();
        ownerTools1.setId(1L);
        OwnerTools ownerTools2 = new OwnerTools();
        ownerTools2.setId(ownerTools1.getId());
        assertThat(ownerTools1).isEqualTo(ownerTools2);
        ownerTools2.setId(2L);
        assertThat(ownerTools1).isNotEqualTo(ownerTools2);
        ownerTools1.setId(null);
        assertThat(ownerTools1).isNotEqualTo(ownerTools2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OwnerToolsDTO.class);
        OwnerToolsDTO ownerToolsDTO1 = new OwnerToolsDTO();
        ownerToolsDTO1.setId(1L);
        OwnerToolsDTO ownerToolsDTO2 = new OwnerToolsDTO();
        assertThat(ownerToolsDTO1).isNotEqualTo(ownerToolsDTO2);
        ownerToolsDTO2.setId(ownerToolsDTO1.getId());
        assertThat(ownerToolsDTO1).isEqualTo(ownerToolsDTO2);
        ownerToolsDTO2.setId(2L);
        assertThat(ownerToolsDTO1).isNotEqualTo(ownerToolsDTO2);
        ownerToolsDTO1.setId(null);
        assertThat(ownerToolsDTO1).isNotEqualTo(ownerToolsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(ownerToolsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(ownerToolsMapper.fromId(null)).isNull();
    }
}
