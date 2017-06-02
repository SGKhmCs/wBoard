package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.domain.Admin;
import ua.sgkhmja.wboard.domain.Reader;
import ua.sgkhmja.wboard.domain.Writer;
import ua.sgkhmja.wboard.repository.AdminRepository;
import ua.sgkhmja.wboard.repository.search.AdminSearchRepository;
import ua.sgkhmja.wboard.service.dto.AdminDTO;
import ua.sgkhmja.wboard.service.mapper.AdminMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Admin.
 */
@Service
@Transactional
public class AdminService {

    private final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final AdminRepository adminRepository;

    private final AdminMapper adminMapper;

    private final AdminSearchRepository adminSearchRepository;

    public AdminService(AdminRepository adminRepository, AdminMapper adminMapper, AdminSearchRepository adminSearchRepository) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
        this.adminSearchRepository = adminSearchRepository;
    }

    /**
     * Save a admin.
     *
     * @param adminDTO the entity to save
     * @return the persisted entity
     */
    public AdminDTO save(AdminDTO adminDTO) {
        log.debug("Request to save Admin : {}", adminDTO);
        Admin admin = adminMapper.toEntity(adminDTO);
        admin = adminRepository.save(admin);
        AdminDTO result = adminMapper.toDto(admin);
        adminSearchRepository.save(admin);
        return result;
    }

    /**
     *  Get all the admins.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AdminDTO> findAll() {
        log.debug("Request to get all Admins");
        List<AdminDTO> result = adminRepository.findAll().stream()
            .map(adminMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one admin by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AdminDTO findOne(Long id) {
        log.debug("Request to get Admin : {}", id);
        Admin admin = adminRepository.findOne(id);
        AdminDTO adminDTO = adminMapper.toDto(admin);
        return adminDTO;
    }

    /**
     *  Delete the  admin by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Admin : {}", id);
        adminRepository.delete(id);
        adminSearchRepository.delete(id);
    }

    /**
     * Search for the admin corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AdminDTO> search(String query) {
        log.debug("Request to search Admins for query {}", query);
        return StreamSupport
            .stream(adminSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(adminMapper::toDto)
            .collect(Collectors.toList());
    }

    public Admin createAdmin(Writer writer){
        Admin admin = new Admin();

        admin.setWriter(writer);

        return admin;
    }
}
