package ua.sgkhmja.wboard.web.rest;

import ua.sgkhmja.wboard.WBoardApp;

import ua.sgkhmja.wboard.domain.Writer;
import ua.sgkhmja.wboard.repository.WriterRepository;
import ua.sgkhmja.wboard.service.WriterService;
import ua.sgkhmja.wboard.repository.search.WriterSearchRepository;
import ua.sgkhmja.wboard.service.dto.WriterDTO;
import ua.sgkhmja.wboard.service.mapper.WriterMapper;
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
 * Test class for the WriterResource REST controller.
 *
 * @see WriterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WBoardApp.class)
public class WriterResourceIntTest {

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private WriterMapper writerMapper;

    @Autowired
    private WriterService writerService;

    @Autowired
    private WriterSearchRepository writerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWriterMockMvc;

    private Writer writer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WriterResource writerResource = new WriterResource(writerService);
        this.restWriterMockMvc = MockMvcBuilders.standaloneSetup(writerResource)
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
    public static Writer createEntity(EntityManager em) {
        Writer writer = new Writer();
        return writer;
    }

    @Before
    public void initTest() {
        writerSearchRepository.deleteAll();
        writer = createEntity(em);
    }

    @Test
    @Transactional
    public void createWriter() throws Exception {
        int databaseSizeBeforeCreate = writerRepository.findAll().size();

        // Create the Writer
        WriterDTO writerDTO = writerMapper.toDto(writer);
        restWriterMockMvc.perform(post("/api/writers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(writerDTO)))
            .andExpect(status().isCreated());

        // Validate the Writer in the database
        List<Writer> writerList = writerRepository.findAll();
        assertThat(writerList).hasSize(databaseSizeBeforeCreate + 1);
        Writer testWriter = writerList.get(writerList.size() - 1);

        // Validate the Writer in Elasticsearch
        Writer writerEs = writerSearchRepository.findOne(testWriter.getId());
        assertThat(writerEs).isEqualToComparingFieldByField(testWriter);
    }

    @Test
    @Transactional
    public void createWriterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = writerRepository.findAll().size();

        // Create the Writer with an existing ID
        writer.setId(1L);
        WriterDTO writerDTO = writerMapper.toDto(writer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWriterMockMvc.perform(post("/api/writers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(writerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Writer> writerList = writerRepository.findAll();
        assertThat(writerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWriters() throws Exception {
        // Initialize the database
        writerRepository.saveAndFlush(writer);

        // Get all the writerList
        restWriterMockMvc.perform(get("/api/writers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(writer.getId().intValue())));
    }

    @Test
    @Transactional
    public void getWriter() throws Exception {
        // Initialize the database
        writerRepository.saveAndFlush(writer);

        // Get the writer
        restWriterMockMvc.perform(get("/api/writers/{id}", writer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(writer.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWriter() throws Exception {
        // Get the writer
        restWriterMockMvc.perform(get("/api/writers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWriter() throws Exception {
        // Initialize the database
        writerRepository.saveAndFlush(writer);
        writerSearchRepository.save(writer);
        int databaseSizeBeforeUpdate = writerRepository.findAll().size();

        // Update the writer
        Writer updatedWriter = writerRepository.findOne(writer.getId());
        WriterDTO writerDTO = writerMapper.toDto(updatedWriter);

        restWriterMockMvc.perform(put("/api/writers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(writerDTO)))
            .andExpect(status().isOk());

        // Validate the Writer in the database
        List<Writer> writerList = writerRepository.findAll();
        assertThat(writerList).hasSize(databaseSizeBeforeUpdate);
        Writer testWriter = writerList.get(writerList.size() - 1);

        // Validate the Writer in Elasticsearch
        Writer writerEs = writerSearchRepository.findOne(testWriter.getId());
        assertThat(writerEs).isEqualToComparingFieldByField(testWriter);
    }

    @Test
    @Transactional
    public void updateNonExistingWriter() throws Exception {
        int databaseSizeBeforeUpdate = writerRepository.findAll().size();

        // Create the Writer
        WriterDTO writerDTO = writerMapper.toDto(writer);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWriterMockMvc.perform(put("/api/writers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(writerDTO)))
            .andExpect(status().isCreated());

        // Validate the Writer in the database
        List<Writer> writerList = writerRepository.findAll();
        assertThat(writerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWriter() throws Exception {
        // Initialize the database
        writerRepository.saveAndFlush(writer);
        writerSearchRepository.save(writer);
        int databaseSizeBeforeDelete = writerRepository.findAll().size();

        // Get the writer
        restWriterMockMvc.perform(delete("/api/writers/{id}", writer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean writerExistsInEs = writerSearchRepository.exists(writer.getId());
        assertThat(writerExistsInEs).isFalse();

        // Validate the database is empty
        List<Writer> writerList = writerRepository.findAll();
        assertThat(writerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWriter() throws Exception {
        // Initialize the database
        writerRepository.saveAndFlush(writer);
        writerSearchRepository.save(writer);

        // Search the writer
        restWriterMockMvc.perform(get("/api/_search/writers?query=id:" + writer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(writer.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Writer.class);
        Writer writer1 = new Writer();
        writer1.setId(1L);
        Writer writer2 = new Writer();
        writer2.setId(writer1.getId());
        assertThat(writer1).isEqualTo(writer2);
        writer2.setId(2L);
        assertThat(writer1).isNotEqualTo(writer2);
        writer1.setId(null);
        assertThat(writer1).isNotEqualTo(writer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WriterDTO.class);
        WriterDTO writerDTO1 = new WriterDTO();
        writerDTO1.setId(1L);
        WriterDTO writerDTO2 = new WriterDTO();
        assertThat(writerDTO1).isNotEqualTo(writerDTO2);
        writerDTO2.setId(writerDTO1.getId());
        assertThat(writerDTO1).isEqualTo(writerDTO2);
        writerDTO2.setId(2L);
        assertThat(writerDTO1).isNotEqualTo(writerDTO2);
        writerDTO1.setId(null);
        assertThat(writerDTO1).isNotEqualTo(writerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(writerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(writerMapper.fromId(null)).isNull();
    }
}
