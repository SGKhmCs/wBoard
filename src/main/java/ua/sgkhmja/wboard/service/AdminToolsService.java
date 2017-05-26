package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.domain.AdminTools;
import ua.sgkhmja.wboard.repository.AdminToolsRepository;
import ua.sgkhmja.wboard.repository.search.AdminToolsSearchRepository;
import ua.sgkhmja.wboard.service.dto.AdminToolsDTO;
import ua.sgkhmja.wboard.service.mapper.AdminToolsMapper;
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
 * Service Implementation for managing AdminTools.
 */
@Service
@Transactional
public class AdminToolsService {

    private final Logger log = LoggerFactory.getLogger(AdminToolsService.class);
    
    private final AdminToolsRepository adminToolsRepository;

    private final AdminToolsMapper adminToolsMapper;

    private final AdminToolsSearchRepository adminToolsSearchRepository;

    public AdminToolsService(AdminToolsRepository adminToolsRepository, AdminToolsMapper adminToolsMapper, AdminToolsSearchRepository adminToolsSearchRepository) {
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
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AdminToolsDTO> findAll() {
        log.debug("Request to get all AdminTools");
        List<AdminToolsDTO> result = adminToolsRepository.findAll().stream()
            .map(adminToolsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one adminTools by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AdminToolsDTO findOne(Long id) {
        log.debug("Request to get AdminTools : {}", id);
        AdminTools adminTools = adminToolsRepository.findOne(id);
        AdminToolsDTO adminToolsDTO = adminToolsMapper.toDto(adminTools);
        return adminToolsDTO;
    }

    /**
     *  Delete the  adminTools by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AdminTools : {}", id);
        adminToolsRepository.delete(id);
        adminToolsSearchRepository.delete(id);
    }

    /**
     * Search for the adminTools corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AdminToolsDTO> search(String query) {
        log.debug("Request to search AdminTools for query {}", query);
        return StreamSupport
            .stream(adminToolsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(adminToolsMapper::toDto)
            .collect(Collectors.toList());
    }
}
