package ua.sgkhmja.wboard.web.rest;

import ua.sgkhmja.wboard.WBoardApp;

import ua.sgkhmja.wboard.domain.BoardsBody;
import ua.sgkhmja.wboard.repository.BoardsBodyRepository;
import ua.sgkhmja.wboard.service.BoardsBodyService;
import ua.sgkhmja.wboard.repository.search.BoardsBodySearchRepository;
import ua.sgkhmja.wboard.service.dto.BoardsBodyDTO;
import ua.sgkhmja.wboard.service.mapper.BoardsBodyMapper;
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
 * Test class for the BoardsBodyResource REST controller.
 *
 * @see BoardsBodyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WBoardApp.class)
public class BoardsBodyResourceIntTest {

    private static final Integer DEFAULT_BACKGROUND_COLOR = 1;
    private static final Integer UPDATED_BACKGROUND_COLOR = 2;

    @Autowired
    private BoardsBodyRepository boardsBodyRepository;

    @Autowired
    private BoardsBodyMapper boardsBodyMapper;

    @Autowired
    private BoardsBodyService boardsBodyService;

    @Autowired
    private BoardsBodySearchRepository boardsBodySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBoardsBodyMockMvc;

    private BoardsBody boardsBody;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BoardsBodyResource boardsBodyResource = new BoardsBodyResource(boardsBodyService);
        this.restBoardsBodyMockMvc = MockMvcBuilders.standaloneSetup(boardsBodyResource)
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
    public static BoardsBody createEntity(EntityManager em) {
        BoardsBody boardsBody = new BoardsBody()
            .backgroundColor(DEFAULT_BACKGROUND_COLOR);
        return boardsBody;
    }

    @Before
    public void initTest() {
        boardsBodySearchRepository.deleteAll();
        boardsBody = createEntity(em);
    }

    @Test
    @Transactional
    public void createBoardsBody() throws Exception {
        int databaseSizeBeforeCreate = boardsBodyRepository.findAll().size();

        // Create the BoardsBody
        BoardsBodyDTO boardsBodyDTO = boardsBodyMapper.toDto(boardsBody);
        restBoardsBodyMockMvc.perform(post("/api/boards-bodies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardsBodyDTO)))
            .andExpect(status().isCreated());

        // Validate the BoardsBody in the database
        List<BoardsBody> boardsBodyList = boardsBodyRepository.findAll();
        assertThat(boardsBodyList).hasSize(databaseSizeBeforeCreate + 1);
        BoardsBody testBoardsBody = boardsBodyList.get(boardsBodyList.size() - 1);
        assertThat(testBoardsBody.getBackgroundColor()).isEqualTo(DEFAULT_BACKGROUND_COLOR);

        // Validate the BoardsBody in Elasticsearch
        BoardsBody boardsBodyEs = boardsBodySearchRepository.findOne(testBoardsBody.getId());
        assertThat(boardsBodyEs).isEqualToComparingFieldByField(testBoardsBody);
    }

    @Test
    @Transactional
    public void createBoardsBodyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boardsBodyRepository.findAll().size();

        // Create the BoardsBody with an existing ID
        boardsBody.setId(1L);
        BoardsBodyDTO boardsBodyDTO = boardsBodyMapper.toDto(boardsBody);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoardsBodyMockMvc.perform(post("/api/boards-bodies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardsBodyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BoardsBody> boardsBodyList = boardsBodyRepository.findAll();
        assertThat(boardsBodyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBoardsBodies() throws Exception {
        // Initialize the database
        boardsBodyRepository.saveAndFlush(boardsBody);

        // Get all the boardsBodyList
        restBoardsBodyMockMvc.perform(get("/api/boards-bodies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boardsBody.getId().intValue())))
            .andExpect(jsonPath("$.[*].backgroundColor").value(hasItem(DEFAULT_BACKGROUND_COLOR)));
    }

    @Test
    @Transactional
    public void getBoardsBody() throws Exception {
        // Initialize the database
        boardsBodyRepository.saveAndFlush(boardsBody);

        // Get the boardsBody
        restBoardsBodyMockMvc.perform(get("/api/boards-bodies/{id}", boardsBody.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(boardsBody.getId().intValue()))
            .andExpect(jsonPath("$.backgroundColor").value(DEFAULT_BACKGROUND_COLOR));
    }

    @Test
    @Transactional
    public void getNonExistingBoardsBody() throws Exception {
        // Get the boardsBody
        restBoardsBodyMockMvc.perform(get("/api/boards-bodies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoardsBody() throws Exception {
        // Initialize the database
        boardsBodyRepository.saveAndFlush(boardsBody);
        boardsBodySearchRepository.save(boardsBody);
        int databaseSizeBeforeUpdate = boardsBodyRepository.findAll().size();

        // Update the boardsBody
        BoardsBody updatedBoardsBody = boardsBodyRepository.findOne(boardsBody.getId());
        updatedBoardsBody
            .backgroundColor(UPDATED_BACKGROUND_COLOR);
        BoardsBodyDTO boardsBodyDTO = boardsBodyMapper.toDto(updatedBoardsBody);

        restBoardsBodyMockMvc.perform(put("/api/boards-bodies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardsBodyDTO)))
            .andExpect(status().isOk());

        // Validate the BoardsBody in the database
        List<BoardsBody> boardsBodyList = boardsBodyRepository.findAll();
        assertThat(boardsBodyList).hasSize(databaseSizeBeforeUpdate);
        BoardsBody testBoardsBody = boardsBodyList.get(boardsBodyList.size() - 1);
        assertThat(testBoardsBody.getBackgroundColor()).isEqualTo(UPDATED_BACKGROUND_COLOR);

        // Validate the BoardsBody in Elasticsearch
        BoardsBody boardsBodyEs = boardsBodySearchRepository.findOne(testBoardsBody.getId());
        assertThat(boardsBodyEs).isEqualToComparingFieldByField(testBoardsBody);
    }

    @Test
    @Transactional
    public void updateNonExistingBoardsBody() throws Exception {
        int databaseSizeBeforeUpdate = boardsBodyRepository.findAll().size();

        // Create the BoardsBody
        BoardsBodyDTO boardsBodyDTO = boardsBodyMapper.toDto(boardsBody);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBoardsBodyMockMvc.perform(put("/api/boards-bodies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardsBodyDTO)))
            .andExpect(status().isCreated());

        // Validate the BoardsBody in the database
        List<BoardsBody> boardsBodyList = boardsBodyRepository.findAll();
        assertThat(boardsBodyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBoardsBody() throws Exception {
        // Initialize the database
        boardsBodyRepository.saveAndFlush(boardsBody);
        boardsBodySearchRepository.save(boardsBody);
        int databaseSizeBeforeDelete = boardsBodyRepository.findAll().size();

        // Get the boardsBody
        restBoardsBodyMockMvc.perform(delete("/api/boards-bodies/{id}", boardsBody.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean boardsBodyExistsInEs = boardsBodySearchRepository.exists(boardsBody.getId());
        assertThat(boardsBodyExistsInEs).isFalse();

        // Validate the database is empty
        List<BoardsBody> boardsBodyList = boardsBodyRepository.findAll();
        assertThat(boardsBodyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBoardsBody() throws Exception {
        // Initialize the database
        boardsBodyRepository.saveAndFlush(boardsBody);
        boardsBodySearchRepository.save(boardsBody);

        // Search the boardsBody
        restBoardsBodyMockMvc.perform(get("/api/_search/boards-bodies?query=id:" + boardsBody.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boardsBody.getId().intValue())))
            .andExpect(jsonPath("$.[*].backgroundColor").value(hasItem(DEFAULT_BACKGROUND_COLOR)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardsBody.class);
        BoardsBody boardsBody1 = new BoardsBody();
        boardsBody1.setId(1L);
        BoardsBody boardsBody2 = new BoardsBody();
        boardsBody2.setId(boardsBody1.getId());
        assertThat(boardsBody1).isEqualTo(boardsBody2);
        boardsBody2.setId(2L);
        assertThat(boardsBody1).isNotEqualTo(boardsBody2);
        boardsBody1.setId(null);
        assertThat(boardsBody1).isNotEqualTo(boardsBody2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardsBodyDTO.class);
        BoardsBodyDTO boardsBodyDTO1 = new BoardsBodyDTO();
        boardsBodyDTO1.setId(1L);
        BoardsBodyDTO boardsBodyDTO2 = new BoardsBodyDTO();
        assertThat(boardsBodyDTO1).isNotEqualTo(boardsBodyDTO2);
        boardsBodyDTO2.setId(boardsBodyDTO1.getId());
        assertThat(boardsBodyDTO1).isEqualTo(boardsBodyDTO2);
        boardsBodyDTO2.setId(2L);
        assertThat(boardsBodyDTO1).isNotEqualTo(boardsBodyDTO2);
        boardsBodyDTO1.setId(null);
        assertThat(boardsBodyDTO1).isNotEqualTo(boardsBodyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(boardsBodyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(boardsBodyMapper.fromId(null)).isNull();
    }
}
