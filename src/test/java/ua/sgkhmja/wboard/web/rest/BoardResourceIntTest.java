package ua.sgkhmja.wboard.web.rest;

import ua.sgkhmja.wboard.WBoardApp;

import ua.sgkhmja.wboard.domain.Board;
import ua.sgkhmja.wboard.repository.BoardRepository;
import ua.sgkhmja.wboard.service.BoardService;
import ua.sgkhmja.wboard.repository.search.BoardSearchRepository;
import ua.sgkhmja.wboard.service.dto.BoardDTO;
import ua.sgkhmja.wboard.service.mapper.BoardMapper;
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
 * Test class for the BoardResource REST controller.
 *
 * @see BoardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WBoardApp.class)
public class BoardResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PUB = false;
    private static final Boolean UPDATED_PUB = true;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardSearchRepository boardSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBoardMockMvc;

    private Board board;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BoardResource boardResource = new BoardResource(boardService, roleService);
        this.restBoardMockMvc = MockMvcBuilders.standaloneSetup(boardResource)
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
    public static Board createEntity(EntityManager em) {
        Board board = new Board()
            .name(DEFAULT_NAME)
            .pub(DEFAULT_PUB);
        return board;
    }

    @Before
    public void initTest() {
        boardSearchRepository.deleteAll();
        board = createEntity(em);
    }

    @Test
    @Transactional
    public void createBoard() throws Exception {
        int databaseSizeBeforeCreate = boardRepository.findAll().size();

        // Create the Board
        BoardDTO boardDTO = boardMapper.toDto(board);
        restBoardMockMvc.perform(post("/api/boards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardDTO)))
            .andExpect(status().isCreated());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeCreate + 1);
        Board testBoard = boardList.get(boardList.size() - 1);
        assertThat(testBoard.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBoard.isPub()).isEqualTo(DEFAULT_PUB);

        // Validate the Board in Elasticsearch
        Board boardEs = boardSearchRepository.findOne(testBoard.getId());
        assertThat(boardEs).isEqualToComparingFieldByField(testBoard);
    }

    @Test
    @Transactional
    public void createBoardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boardRepository.findAll().size();

        // Create the Board with an existing ID
        board.setId(1L);
        BoardDTO boardDTO = boardMapper.toDto(board);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoardMockMvc.perform(post("/api/boards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = boardRepository.findAll().size();
        // set the field null
        board.setName(null);

        // Create the Board, which fails.
        BoardDTO boardDTO = boardMapper.toDto(board);

        restBoardMockMvc.perform(post("/api/boards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardDTO)))
            .andExpect(status().isBadRequest());

        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBoards() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList
        restBoardMockMvc.perform(get("/api/boards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(board.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].pub").value(hasItem(DEFAULT_PUB.booleanValue())));
    }

    @Test
    @Transactional
    public void getBoard() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get the board
        restBoardMockMvc.perform(get("/api/boards/{id}", board.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(board.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.pub").value(DEFAULT_PUB.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBoard() throws Exception {
        // Get the board
        restBoardMockMvc.perform(get("/api/boards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoard() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);
        boardSearchRepository.save(board);
        int databaseSizeBeforeUpdate = boardRepository.findAll().size();

        // Update the board
        Board updatedBoard = boardRepository.findOne(board.getId());
        updatedBoard
            .name(UPDATED_NAME)
            .pub(UPDATED_PUB);
        BoardDTO boardDTO = boardMapper.toDto(updatedBoard);

        restBoardMockMvc.perform(put("/api/boards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardDTO)))
            .andExpect(status().isOk());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
        Board testBoard = boardList.get(boardList.size() - 1);
        assertThat(testBoard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBoard.isPub()).isEqualTo(UPDATED_PUB);

        // Validate the Board in Elasticsearch
        Board boardEs = boardSearchRepository.findOne(testBoard.getId());
        assertThat(boardEs).isEqualToComparingFieldByField(testBoard);
    }

    @Test
    @Transactional
    public void updateNonExistingBoard() throws Exception {
        int databaseSizeBeforeUpdate = boardRepository.findAll().size();

        // Create the Board
        BoardDTO boardDTO = boardMapper.toDto(board);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBoardMockMvc.perform(put("/api/boards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardDTO)))
            .andExpect(status().isCreated());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBoard() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);
        boardSearchRepository.save(board);
        int databaseSizeBeforeDelete = boardRepository.findAll().size();

        // Get the board
        restBoardMockMvc.perform(delete("/api/boards/{id}", board.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean boardExistsInEs = boardSearchRepository.exists(board.getId());
        assertThat(boardExistsInEs).isFalse();

        // Validate the database is empty
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBoard() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);
        boardSearchRepository.save(board);

        // Search the board
        restBoardMockMvc.perform(get("/api/_search/boards?query=id:" + board.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(board.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].pub").value(hasItem(DEFAULT_PUB.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Board.class);
        Board board1 = new Board();
        board1.setId(1L);
        Board board2 = new Board();
        board2.setId(board1.getId());
        assertThat(board1).isEqualTo(board2);
        board2.setId(2L);
        assertThat(board1).isNotEqualTo(board2);
        board1.setId(null);
        assertThat(board1).isNotEqualTo(board2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardDTO.class);
        BoardDTO boardDTO1 = new BoardDTO();
        boardDTO1.setId(1L);
        BoardDTO boardDTO2 = new BoardDTO();
        assertThat(boardDTO1).isNotEqualTo(boardDTO2);
        boardDTO2.setId(boardDTO1.getId());
        assertThat(boardDTO1).isEqualTo(boardDTO2);
        boardDTO2.setId(2L);
        assertThat(boardDTO1).isNotEqualTo(boardDTO2);
        boardDTO1.setId(null);
        assertThat(boardDTO1).isNotEqualTo(boardDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(boardMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(boardMapper.fromId(null)).isNull();
    }
}
