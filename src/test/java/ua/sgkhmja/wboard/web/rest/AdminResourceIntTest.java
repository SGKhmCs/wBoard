package ua.sgkhmja.wboard.web.rest;

import ua.sgkhmja.wboard.WBoardApp;

import ua.sgkhmja.wboard.domain.Admin;
import ua.sgkhmja.wboard.repository.AdminRepository;
import ua.sgkhmja.wboard.service.AdminService;
import ua.sgkhmja.wboard.repository.search.AdminSearchRepository;
import ua.sgkhmja.wboard.service.dto.AdminDTO;
import ua.sgkhmja.wboard.service.mapper.AdminMapper;
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
 * Test class for the AdminResource REST controller.
 *
 * @see AdminResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WBoardApp.class)
public class AdminResourceIntTest {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminSearchRepository adminSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAdminMockMvc;

    private Admin admin;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdminResource adminResource = new AdminResource(adminService);
        this.restAdminMockMvc = MockMvcBuilders.standaloneSetup(adminResource)
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
    public static Admin createEntity(EntityManager em) {
        Admin admin = new Admin();
        return admin;
    }

    @Before
    public void initTest() {
        adminSearchRepository.deleteAll();
        admin = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdmin() throws Exception {
        int databaseSizeBeforeCreate = adminRepository.findAll().size();

        // Create the Admin
        AdminDTO adminDTO = adminMapper.toDto(admin);
        restAdminMockMvc.perform(post("/api/admins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adminDTO)))
            .andExpect(status().isCreated());

        // Validate the Admin in the database
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeCreate + 1);
        Admin testAdmin = adminList.get(adminList.size() - 1);

        // Validate the Admin in Elasticsearch
        Admin adminEs = adminSearchRepository.findOne(testAdmin.getId());
        assertThat(adminEs).isEqualToComparingFieldByField(testAdmin);
    }

    @Test
    @Transactional
    public void createAdminWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adminRepository.findAll().size();

        // Create the Admin with an existing ID
        admin.setId(1L);
        AdminDTO adminDTO = adminMapper.toDto(admin);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminMockMvc.perform(post("/api/admins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adminDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAdmins() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get all the adminList
        restAdminMockMvc.perform(get("/api/admins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(admin.getId().intValue())));
    }

    @Test
    @Transactional
    public void getAdmin() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get the admin
        restAdminMockMvc.perform(get("/api/admins/{id}", admin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(admin.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAdmin() throws Exception {
        // Get the admin
        restAdminMockMvc.perform(get("/api/admins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdmin() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);
        adminSearchRepository.save(admin);
        int databaseSizeBeforeUpdate = adminRepository.findAll().size();

        // Update the admin
        Admin updatedAdmin = adminRepository.findOne(admin.getId());
        AdminDTO adminDTO = adminMapper.toDto(updatedAdmin);

        restAdminMockMvc.perform(put("/api/admins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adminDTO)))
            .andExpect(status().isOk());

        // Validate the Admin in the database
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeUpdate);
        Admin testAdmin = adminList.get(adminList.size() - 1);

        // Validate the Admin in Elasticsearch
        Admin adminEs = adminSearchRepository.findOne(testAdmin.getId());
        assertThat(adminEs).isEqualToComparingFieldByField(testAdmin);
    }

    @Test
    @Transactional
    public void updateNonExistingAdmin() throws Exception {
        int databaseSizeBeforeUpdate = adminRepository.findAll().size();

        // Create the Admin
        AdminDTO adminDTO = adminMapper.toDto(admin);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAdminMockMvc.perform(put("/api/admins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adminDTO)))
            .andExpect(status().isCreated());

        // Validate the Admin in the database
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAdmin() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);
        adminSearchRepository.save(admin);
        int databaseSizeBeforeDelete = adminRepository.findAll().size();

        // Get the admin
        restAdminMockMvc.perform(delete("/api/admins/{id}", admin.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean adminExistsInEs = adminSearchRepository.exists(admin.getId());
        assertThat(adminExistsInEs).isFalse();

        // Validate the database is empty
        List<Admin> adminList = adminRepository.findAll();
        assertThat(adminList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAdmin() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);
        adminSearchRepository.save(admin);

        // Search the admin
        restAdminMockMvc.perform(get("/api/_search/admins?query=id:" + admin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(admin.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Admin.class);
        Admin admin1 = new Admin();
        admin1.setId(1L);
        Admin admin2 = new Admin();
        admin2.setId(admin1.getId());
        assertThat(admin1).isEqualTo(admin2);
        admin2.setId(2L);
        assertThat(admin1).isNotEqualTo(admin2);
        admin1.setId(null);
        assertThat(admin1).isNotEqualTo(admin2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminDTO.class);
        AdminDTO adminDTO1 = new AdminDTO();
        adminDTO1.setId(1L);
        AdminDTO adminDTO2 = new AdminDTO();
        assertThat(adminDTO1).isNotEqualTo(adminDTO2);
        adminDTO2.setId(adminDTO1.getId());
        assertThat(adminDTO1).isEqualTo(adminDTO2);
        adminDTO2.setId(2L);
        assertThat(adminDTO1).isNotEqualTo(adminDTO2);
        adminDTO1.setId(null);
        assertThat(adminDTO1).isNotEqualTo(adminDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(adminMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(adminMapper.fromId(null)).isNull();
    }
}