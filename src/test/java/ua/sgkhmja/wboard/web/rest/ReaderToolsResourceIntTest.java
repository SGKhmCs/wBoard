package ua.sgkhmja.wboard.web.rest;

import ua.sgkhmja.wboard.WBoardApp;

import ua.sgkhmja.wboard.domain.ReaderTools;
import ua.sgkhmja.wboard.repository.ReaderToolsRepository;
import ua.sgkhmja.wboard.service.ReaderToolsService;
import ua.sgkhmja.wboard.repository.search.ReaderToolsSearchRepository;
import ua.sgkhmja.wboard.service.dto.ReaderToolsDTO;
import ua.sgkhmja.wboard.service.mapper.ReaderToolsMapper;
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
 * Test class for the ReaderToolsResource REST controller.
 *
 * @see ReaderToolsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WBoardApp.class)
public class ReaderToolsResourceIntTest {

    @Autowired
    private ReaderToolsRepository readerToolsRepository;

    @Autowired
    private ReaderToolsMapper readerToolsMapper;

    @Autowired
    private ReaderToolsService readerToolsService;

    @Autowired
    private ReaderToolsSearchRepository readerToolsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReaderToolsMockMvc;

    private ReaderTools readerTools;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReaderToolsResource readerToolsResource = new ReaderToolsResource(readerToolsService);
        this.restReaderToolsMockMvc = MockMvcBuilders.standaloneSetup(readerToolsResource)
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
    public static ReaderTools createEntity(EntityManager em) {
        ReaderTools readerTools = new ReaderTools();
        return readerTools;
    }

    @Before
    public void initTest() {
        readerToolsSearchRepository.deleteAll();
        readerTools = createEntity(em);
    }

    @Test
    @Transactional
    public void createReaderTools() throws Exception {
        int databaseSizeBeforeCreate = readerToolsRepository.findAll().size();

        // Create the ReaderTools
        ReaderToolsDTO readerToolsDTO = readerToolsMapper.toDto(readerTools);
        restReaderToolsMockMvc.perform(post("/api/reader-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(readerToolsDTO)))
            .andExpect(status().isCreated());

        // Validate the ReaderTools in the database
        List<ReaderTools> readerToolsList = readerToolsRepository.findAll();
        assertThat(readerToolsList).hasSize(databaseSizeBeforeCreate + 1);
        ReaderTools testReaderTools = readerToolsList.get(readerToolsList.size() - 1);

        // Validate the ReaderTools in Elasticsearch
        ReaderTools readerToolsEs = readerToolsSearchRepository.findOne(testReaderTools.getId());
        assertThat(readerToolsEs).isEqualToComparingFieldByField(testReaderTools);
    }

    @Test
    @Transactional
    public void createReaderToolsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = readerToolsRepository.findAll().size();

        // Create the ReaderTools with an existing ID
        readerTools.setId(1L);
        ReaderToolsDTO readerToolsDTO = readerToolsMapper.toDto(readerTools);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReaderToolsMockMvc.perform(post("/api/reader-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(readerToolsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ReaderTools> readerToolsList = readerToolsRepository.findAll();
        assertThat(readerToolsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReaderTools() throws Exception {
        // Initialize the database
        readerToolsRepository.saveAndFlush(readerTools);

        // Get all the readerToolsList
        restReaderToolsMockMvc.perform(get("/api/reader-tools?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(readerTools.getId().intValue())));
    }

    @Test
    @Transactional
    public void getReaderTools() throws Exception {
        // Initialize the database
        readerToolsRepository.saveAndFlush(readerTools);

        // Get the readerTools
        restReaderToolsMockMvc.perform(get("/api/reader-tools/{id}", readerTools.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(readerTools.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingReaderTools() throws Exception {
        // Get the readerTools
        restReaderToolsMockMvc.perform(get("/api/reader-tools/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReaderTools() throws Exception {
        // Initialize the database
        readerToolsRepository.saveAndFlush(readerTools);
        readerToolsSearchRepository.save(readerTools);
        int databaseSizeBeforeUpdate = readerToolsRepository.findAll().size();

        // Update the readerTools
        ReaderTools updatedReaderTools = readerToolsRepository.findOne(readerTools.getId());
        ReaderToolsDTO readerToolsDTO = readerToolsMapper.toDto(updatedReaderTools);

        restReaderToolsMockMvc.perform(put("/api/reader-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(readerToolsDTO)))
            .andExpect(status().isOk());

        // Validate the ReaderTools in the database
        List<ReaderTools> readerToolsList = readerToolsRepository.findAll();
        assertThat(readerToolsList).hasSize(databaseSizeBeforeUpdate);
        ReaderTools testReaderTools = readerToolsList.get(readerToolsList.size() - 1);

        // Validate the ReaderTools in Elasticsearch
        ReaderTools readerToolsEs = readerToolsSearchRepository.findOne(testReaderTools.getId());
        assertThat(readerToolsEs).isEqualToComparingFieldByField(testReaderTools);
    }

    @Test
    @Transactional
    public void updateNonExistingReaderTools() throws Exception {
        int databaseSizeBeforeUpdate = readerToolsRepository.findAll().size();

        // Create the ReaderTools
        ReaderToolsDTO readerToolsDTO = readerToolsMapper.toDto(readerTools);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReaderToolsMockMvc.perform(put("/api/reader-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(readerToolsDTO)))
            .andExpect(status().isCreated());

        // Validate the ReaderTools in the database
        List<ReaderTools> readerToolsList = readerToolsRepository.findAll();
        assertThat(readerToolsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReaderTools() throws Exception {
        // Initialize the database
        readerToolsRepository.saveAndFlush(readerTools);
        readerToolsSearchRepository.save(readerTools);
        int databaseSizeBeforeDelete = readerToolsRepository.findAll().size();

        // Get the readerTools
        restReaderToolsMockMvc.perform(delete("/api/reader-tools/{id}", readerTools.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean readerToolsExistsInEs = readerToolsSearchRepository.exists(readerTools.getId());
        assertThat(readerToolsExistsInEs).isFalse();

        // Validate the database is empty
        List<ReaderTools> readerToolsList = readerToolsRepository.findAll();
        assertThat(readerToolsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReaderTools() throws Exception {
        // Initialize the database
        readerToolsRepository.saveAndFlush(readerTools);
        readerToolsSearchRepository.save(readerTools);

        // Search the readerTools
        restReaderToolsMockMvc.perform(get("/api/_search/reader-tools?query=id:" + readerTools.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(readerTools.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReaderTools.class);
        ReaderTools readerTools1 = new ReaderTools();
        readerTools1.setId(1L);
        ReaderTools readerTools2 = new ReaderTools();
        readerTools2.setId(readerTools1.getId());
        assertThat(readerTools1).isEqualTo(readerTools2);
        readerTools2.setId(2L);
        assertThat(readerTools1).isNotEqualTo(readerTools2);
        readerTools1.setId(null);
        assertThat(readerTools1).isNotEqualTo(readerTools2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReaderToolsDTO.class);
        ReaderToolsDTO readerToolsDTO1 = new ReaderToolsDTO();
        readerToolsDTO1.setId(1L);
        ReaderToolsDTO readerToolsDTO2 = new ReaderToolsDTO();
        assertThat(readerToolsDTO1).isNotEqualTo(readerToolsDTO2);
        readerToolsDTO2.setId(readerToolsDTO1.getId());
        assertThat(readerToolsDTO1).isEqualTo(readerToolsDTO2);
        readerToolsDTO2.setId(2L);
        assertThat(readerToolsDTO1).isNotEqualTo(readerToolsDTO2);
        readerToolsDTO1.setId(null);
        assertThat(readerToolsDTO1).isNotEqualTo(readerToolsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(readerToolsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(readerToolsMapper.fromId(null)).isNull();
    }
}
