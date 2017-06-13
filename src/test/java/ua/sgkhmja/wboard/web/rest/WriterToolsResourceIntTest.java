package ua.sgkhmja.wboard.web.rest;

import ua.sgkhmja.wboard.WBoardApp;

import ua.sgkhmja.wboard.domain.WriterTools;
import ua.sgkhmja.wboard.domain.User;
import ua.sgkhmja.wboard.domain.Board;
import ua.sgkhmja.wboard.repository.WriterToolsRepository;
import ua.sgkhmja.wboard.service.WriterToolsService;
import ua.sgkhmja.wboard.repository.search.WriterToolsSearchRepository;
import ua.sgkhmja.wboard.service.dto.WriterToolsDTO;
import ua.sgkhmja.wboard.service.mapper.WriterToolsMapper;
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
 * Test class for the WriterToolsResource REST controller.
 *
 * @see WriterToolsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WBoardApp.class)
public class WriterToolsResourceIntTest {

    @Autowired
    private WriterToolsRepository writerToolsRepository;

    @Autowired
    private WriterToolsMapper writerToolsMapper;

    @Autowired
    private WriterToolsService writerToolsService;

    @Autowired
    private WriterToolsSearchRepository writerToolsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWriterToolsMockMvc;

    private WriterTools writerTools;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WriterToolsResource writerToolsResource = new WriterToolsResource(writerToolsService);
        this.restWriterToolsMockMvc = MockMvcBuilders.standaloneSetup(writerToolsResource)
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
    public static WriterTools createEntity(EntityManager em) {
        WriterTools writerTools = new WriterTools();
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        writerTools.setUser(user);
        // Add required entity
        Board board = BoardResourceIntTest.createEntity(em);
        em.persist(board);
        em.flush();
        writerTools.setBoard(board);
        return writerTools;
    }

    @Before
    public void initTest() {
        writerToolsSearchRepository.deleteAll();
        writerTools = createEntity(em);
    }

    @Test
    @Transactional
    public void createWriterTools() throws Exception {
        int databaseSizeBeforeCreate = writerToolsRepository.findAll().size();

        // Create the WriterTools
        WriterToolsDTO writerToolsDTO = writerToolsMapper.toDto(writerTools);
        restWriterToolsMockMvc.perform(post("/api/writer-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(writerToolsDTO)))
            .andExpect(status().isCreated());

        // Validate the WriterTools in the database
        List<WriterTools> writerToolsList = writerToolsRepository.findAll();
        assertThat(writerToolsList).hasSize(databaseSizeBeforeCreate + 1);
        WriterTools testWriterTools = writerToolsList.get(writerToolsList.size() - 1);

        // Validate the WriterTools in Elasticsearch
        WriterTools writerToolsEs = writerToolsSearchRepository.findOne(testWriterTools.getId());
        assertThat(writerToolsEs).isEqualToComparingFieldByField(testWriterTools);
    }

    @Test
    @Transactional
    public void createWriterToolsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = writerToolsRepository.findAll().size();

        // Create the WriterTools with an existing ID
        writerTools.setId(1L);
        WriterToolsDTO writerToolsDTO = writerToolsMapper.toDto(writerTools);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWriterToolsMockMvc.perform(post("/api/writer-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(writerToolsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<WriterTools> writerToolsList = writerToolsRepository.findAll();
        assertThat(writerToolsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWriterTools() throws Exception {
        // Initialize the database
        writerToolsRepository.saveAndFlush(writerTools);

        // Get all the writerToolsList
        restWriterToolsMockMvc.perform(get("/api/writer-tools?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(writerTools.getId().intValue())));
    }

    @Test
    @Transactional
    public void getWriterTools() throws Exception {
        // Initialize the database
        writerToolsRepository.saveAndFlush(writerTools);

        // Get the writerTools
        restWriterToolsMockMvc.perform(get("/api/writer-tools/{id}", writerTools.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(writerTools.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWriterTools() throws Exception {
        // Get the writerTools
        restWriterToolsMockMvc.perform(get("/api/writer-tools/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWriterTools() throws Exception {
        // Initialize the database
        writerToolsRepository.saveAndFlush(writerTools);
        writerToolsSearchRepository.save(writerTools);
        int databaseSizeBeforeUpdate = writerToolsRepository.findAll().size();

        // Update the writerTools
        WriterTools updatedWriterTools = writerToolsRepository.findOne(writerTools.getId());
        WriterToolsDTO writerToolsDTO = writerToolsMapper.toDto(updatedWriterTools);

        restWriterToolsMockMvc.perform(put("/api/writer-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(writerToolsDTO)))
            .andExpect(status().isOk());

        // Validate the WriterTools in the database
        List<WriterTools> writerToolsList = writerToolsRepository.findAll();
        assertThat(writerToolsList).hasSize(databaseSizeBeforeUpdate);
        WriterTools testWriterTools = writerToolsList.get(writerToolsList.size() - 1);

        // Validate the WriterTools in Elasticsearch
        WriterTools writerToolsEs = writerToolsSearchRepository.findOne(testWriterTools.getId());
        assertThat(writerToolsEs).isEqualToComparingFieldByField(testWriterTools);
    }

    @Test
    @Transactional
    public void updateNonExistingWriterTools() throws Exception {
        int databaseSizeBeforeUpdate = writerToolsRepository.findAll().size();

        // Create the WriterTools
        WriterToolsDTO writerToolsDTO = writerToolsMapper.toDto(writerTools);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWriterToolsMockMvc.perform(put("/api/writer-tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(writerToolsDTO)))
            .andExpect(status().isCreated());

        // Validate the WriterTools in the database
        List<WriterTools> writerToolsList = writerToolsRepository.findAll();
        assertThat(writerToolsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWriterTools() throws Exception {
        // Initialize the database
        writerToolsRepository.saveAndFlush(writerTools);
        writerToolsSearchRepository.save(writerTools);
        int databaseSizeBeforeDelete = writerToolsRepository.findAll().size();

        // Get the writerTools
        restWriterToolsMockMvc.perform(delete("/api/writer-tools/{id}", writerTools.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean writerToolsExistsInEs = writerToolsSearchRepository.exists(writerTools.getId());
        assertThat(writerToolsExistsInEs).isFalse();

        // Validate the database is empty
        List<WriterTools> writerToolsList = writerToolsRepository.findAll();
        assertThat(writerToolsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWriterTools() throws Exception {
        // Initialize the database
        writerToolsRepository.saveAndFlush(writerTools);
        writerToolsSearchRepository.save(writerTools);

        // Search the writerTools
        restWriterToolsMockMvc.perform(get("/api/_search/writer-tools?query=id:" + writerTools.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(writerTools.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WriterTools.class);
        WriterTools writerTools1 = new WriterTools();
        writerTools1.setId(1L);
        WriterTools writerTools2 = new WriterTools();
        writerTools2.setId(writerTools1.getId());
        assertThat(writerTools1).isEqualTo(writerTools2);
        writerTools2.setId(2L);
        assertThat(writerTools1).isNotEqualTo(writerTools2);
        writerTools1.setId(null);
        assertThat(writerTools1).isNotEqualTo(writerTools2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WriterToolsDTO.class);
        WriterToolsDTO writerToolsDTO1 = new WriterToolsDTO();
        writerToolsDTO1.setId(1L);
        WriterToolsDTO writerToolsDTO2 = new WriterToolsDTO();
        assertThat(writerToolsDTO1).isNotEqualTo(writerToolsDTO2);
        writerToolsDTO2.setId(writerToolsDTO1.getId());
        assertThat(writerToolsDTO1).isEqualTo(writerToolsDTO2);
        writerToolsDTO2.setId(2L);
        assertThat(writerToolsDTO1).isNotEqualTo(writerToolsDTO2);
        writerToolsDTO1.setId(null);
        assertThat(writerToolsDTO1).isNotEqualTo(writerToolsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(writerToolsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(writerToolsMapper.fromId(null)).isNull();
    }
}
