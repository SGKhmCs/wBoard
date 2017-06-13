package ua.sgkhmja.wboard.service.impl;

import ua.sgkhmja.wboard.service.AdminToolsService;
import ua.sgkhmja.wboard.domain.AdminTools;
import ua.sgkhmja.wboard.repository.AdminToolsRepository;
import ua.sgkhmja.wboard.repository.search.AdminToolsSearchRepository;
import ua.sgkhmja.wboard.service.dto.AdminToolsDTO;
import ua.sgkhmja.wboard.service.mapper.AdminToolsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AdminTools.
 */
@Service
@Transactional
public class AdminToolsServiceImpl implements AdminToolsService{

    private final Logger log = LoggerFactory.getLogger(AdminToolsServiceImpl.class);

    private final AdminToolsRepository adminToolsRepository;

    private final AdminToolsMapper adminToolsMapper;

    private final AdminToolsSearchRepository adminToolsSearchRepository;

    public AdminToolsServiceImpl(AdminToolsRepository adminToolsRepository, AdminToolsMapper adminToolsMapper, AdminToolsSearchRepository adminToolsSearchRepository) {
        this.adminToolsRepository = adminToolsRepository;
        this.adminToolsMapper = adminToolsMapper;
        this.adminToolsSearchRepository = adminToolsSearchRepository;
    }

    /**
     * Save a adminTools.
     *
     * @param adminToolsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AdminToolsDTO save(AdminToolsDTO adminToolsDTO) {
        log.debug("Request to save AdminTools : {}", adminToolsDTO);
        AdminTools adminTools = adminToolsMapper.toEntity(adminToolsDTO);
        adminTools = adminToolsRepository.save(adminTools);
        AdminToolsDTO result = adminToolsMapper.toDto(adminTools);
        adminToolsSearchRepository.save(adminTools);
        return result;
    }

    /**
     *  Get all the adminTools.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminToolsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AdminTools");
        return adminToolsRepository.findAll(pageable)
            .map(adminToolsMapper::toDto);
    }

    /**
     *  Get one adminTools by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AdminToolsDTO findOne(Long id) {
        log.debug("Request to get AdminTools : {}", id);
        AdminTools adminTools = adminToolsRepository.findOne(id);
        return adminToolsMapper.toDto(adminTools);
    }

    /**
     *  Delete the  adminTools by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AdminTools : {}", id);
        adminToolsRepository.delete(id);
        adminToolsSearchRepository.delete(id);
    }

    /**
     * Search for the adminTools corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminToolsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AdminTools for query {}", query);
        Page<AdminTools> result = adminToolsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(adminToolsMapper::toDto);
    }
}
