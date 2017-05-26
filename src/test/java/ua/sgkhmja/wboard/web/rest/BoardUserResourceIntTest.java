package ua.sgkhmja.wboard.web.rest;

import ua.sgkhmja.wboard.WBoardApp;

import ua.sgkhmja.wboard.domain.BoardUser;
import ua.sgkhmja.wboard.domain.User;
import ua.sgkhmja.wboard.domain.Board;
import ua.sgkhmja.wboard.repository.BoardUserRepository;
import ua.sgkhmja.wboard.service.BoardUserService;
import ua.sgkhmja.wboard.repository.search.BoardUserSearchRepository;
import ua.sgkhmja.wboard.service.dto.BoardUserDTO;
import ua.sgkhmja.wboard.service.mapper.BoardUserMapper;
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
 * Test class for the BoardUserResource REST controller.
 *
 * @see BoardUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WBoardApp.class)
public class BoardUserResourceIntTest {

    @Autowired
    private BoardUserRepository boardUserRepository;

    @Autowired
    private BoardUserMapper boardUserMapper;

    @Autowired
    private BoardUserService boardUserService;

    @Autowired
    private BoardUserSearchRepository boardUserSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBoardUserMockMvc;

    private BoardUser boardUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BoardUserResource boardUserResource = new BoardUserResource(boardUserService);
        this.restBoardUserMockMvc = MockMvcBuilders.standaloneSetup(boardUserResource)
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
    public static BoardUser createEntity(EntityManager em) {
        BoardUser boardUser = new BoardUser();
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        boardUser.setUser(user);
        // Add required entity
        Board board = BoardResourceIntTest.createEntity(em);
        em.persist(board);
        em.flush();
        boardUser.setBoard(board);
        return boardUser;
    }

    @Before
    public void initTest() {
        boardUserSearchRepository.deleteAll();
        boardUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createBoardUser() throws Exception {
        int databaseSizeBeforeCreate = boardUserRepository.findAll().size();

        // Create the BoardUser
        BoardUserDTO boardUserDTO = boardUserMapper.toDto(boardUser);
        restBoardUserMockMvc.perform(post("/api/board-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardUserDTO)))
            .andExpect(status().isCreated());

        // Validate the BoardUser in the database
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeCreate + 1);
        BoardUser testBoardUser = boardUserList.get(boardUserList.size() - 1);

        // Validate the BoardUser in Elasticsearch
        BoardUser boardUserEs = boardUserSearchRepository.findOne(testBoardUser.getId());
        assertThat(boardUserEs).isEqualToComparingFieldByField(testBoardUser);
    }

    @Test
    @Transactional
    public void createBoardUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boardUserRepository.findAll().size();

        // Create the BoardUser with an existing ID
        boardUser.setId(1L);
        BoardUserDTO boardUserDTO = boardUserMapper.toDto(boardUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoardUserMockMvc.perform(post("/api/board-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBoardUsers() throws Exception {
        // Initialize the database
        boardUserRepository.saveAndFlush(boardUser);

        // Get all the boardUserList
        restBoardUserMockMvc.perform(get("/api/board-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boardUser.getId().intValue())));
    }

    @Test
    @Transactional
    public void getBoardUser() throws Exception {
        // Initialize the database
        boardUserRepository.saveAndFlush(boardUser);

        // Get the boardUser
        restBoardUserMockMvc.perform(get("/api/board-users/{id}", boardUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(boardUser.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBoardUser() throws Exception {
        // Get the boardUser
        restBoardUserMockMvc.perform(get("/api/board-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoardUser() throws Exception {
        // Initialize the database
        boardUserRepository.saveAndFlush(boardUser);
        boardUserSearchRepository.save(boardUser);
        int databaseSizeBeforeUpdate = boardUserRepository.findAll().size();

        // Update the boardUser
        BoardUser updatedBoardUser = boardUserRepository.findOne(boardUser.getId());
        BoardUserDTO boardUserDTO = boardUserMapper.toDto(updatedBoardUser);

        restBoardUserMockMvc.perform(put("/api/board-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardUserDTO)))
            .andExpect(status().isOk());

        // Validate the BoardUser in the database
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeUpdate);
        BoardUser testBoardUser = boardUserList.get(boardUserList.size() - 1);

        // Validate the BoardUser in Elasticsearch
        BoardUser boardUserEs = boardUserSearchRepository.findOne(testBoardUser.getId());
        assertThat(boardUserEs).isEqualToComparingFieldByField(testBoardUser);
    }

    @Test
    @Transactional
    public void updateNonExistingBoardUser() throws Exception {
        int databaseSizeBeforeUpdate = boardUserRepository.findAll().size();

        // Create the BoardUser
        BoardUserDTO boardUserDTO = boardUserMapper.toDto(boardUser);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBoardUserMockMvc.perform(put("/api/board-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boardUserDTO)))
            .andExpect(status().isCreated());

        // Validate the BoardUser in the database
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBoardUser() throws Exception {
        // Initialize the database
        boardUserRepository.saveAndFlush(boardUser);
        boardUserSearchRepository.save(boardUser);
        int databaseSizeBeforeDelete = boardUserRepository.findAll().size();

        // Get the boardUser
        restBoardUserMockMvc.perform(delete("/api/board-users/{id}", boardUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean boardUserExistsInEs = boardUserSearchRepository.exists(boardUser.getId());
        assertThat(boardUserExistsInEs).isFalse();

        // Validate the database is empty
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBoardUser() throws Exception {
        // Initialize the database
        boardUserRepository.saveAndFlush(boardUser);
        boardUserSearchRepository.save(boardUser);

        // Search the boardUser
        restBoardUserMockMvc.perform(get("/api/_search/board-users?query=id:" + boardUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boardUser.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardUser.class);
        BoardUser boardUser1 = new BoardUser();
        boardUser1.setId(1L);
        BoardUser boardUser2 = new BoardUser();
        boardUser2.setId(boardUser1.getId());
        assertThat(boardUser1).isEqualTo(boardUser2);
        boardUser2.setId(2L);
        assertThat(boardUser1).isNotEqualTo(boardUser2);
        boardUser1.setId(null);
        assertThat(boardUser1).isNotEqualTo(boardUser2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardUserDTO.class);
        BoardUserDTO boardUserDTO1 = new BoardUserDTO();
        boardUserDTO1.setId(1L);
        BoardUserDTO boardUserDTO2 = new BoardUserDTO();
        assertThat(boardUserDTO1).isNotEqualTo(boardUserDTO2);
        boardUserDTO2.setId(boardUserDTO1.getId());
        assertThat(boardUserDTO1).isEqualTo(boardUserDTO2);
        boardUserDTO2.setId(2L);
        assertThat(boardUserDTO1).isNotEqualTo(boardUserDTO2);
        boardUserDTO1.setId(null);
        assertThat(boardUserDTO1).isNotEqualTo(boardUserDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(boardUserMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(boardUserMapper.fromId(null)).isNull();
    }
}
