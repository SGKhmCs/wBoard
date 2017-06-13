package ua.sgkhmja.wboard.service.impl;

import ua.sgkhmja.wboard.service.OwnerToolsService;
import ua.sgkhmja.wboard.domain.OwnerTools;
import ua.sgkhmja.wboard.repository.OwnerToolsRepository;
import ua.sgkhmja.wboard.repository.search.OwnerToolsSearchRepository;
import ua.sgkhmja.wboard.service.dao.UserDAO;
import ua.sgkhmja.wboard.service.dto.OwnerToolsDTO;
import ua.sgkhmja.wboard.service.mapper.OwnerToolsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OwnerTools.
 */
@Service
@Transactional
public class OwnerToolsServiceImpl implements OwnerToolsService{

    private final Logger log = LoggerFactory.getLogger(OwnerToolsServiceImpl.class);

    private final OwnerToolsRepository ownerToolsRepository;

    private final OwnerToolsMapper ownerToolsMapper;

    private final OwnerToolsSearchRepository ownerToolsSearchRepository;

    private final UserDAO userDAO;

    public OwnerToolsServiceImpl(OwnerToolsRepository ownerToolsRepository, OwnerToolsMapper ownerToolsMapper,
                                 OwnerToolsSearchRepository ownerToolsSearchRepository, UserDAO userDAO) {
        this.ownerToolsRepository = ownerToolsRepository;
        this.ownerToolsMapper = ownerToolsMapper;
        this.ownerToolsSearchRepository = ownerToolsSearchRepository;
        this.userDAO = userDAO;
    }

    /**
     * Save a ownerTools.
     *
     * @param ownerToolsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OwnerToolsDTO save(OwnerToolsDTO ownerToolsDTO) {
        log.debug("Request to save OwnerTools : {}", ownerToolsDTO);

        OwnerTools ownerTools = ownerToolsMapper.toEntity(ownerToolsDTO);
        ownerTools = ownerToolsRepository.save(ownerTools);
        OwnerToolsDTO result = ownerToolsMapper.toDto(ownerTools);
        ownerToolsSearchRepository.save(ownerTools);
        return result;
    }

    /**
     *  Get all the ownerTools.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OwnerToolsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OwnerTools");
        return ownerToolsRepository.findAll(pageable)
            .map(ownerToolsMapper::toDto);
    }

    /**
     *  Get one ownerTools by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public OwnerToolsDTO findOne(Long id) {
        log.debug("Request to get OwnerTools : {}", id);
        OwnerTools ownerTools = ownerToolsRepository.findOne(id);
        return ownerToolsMapper.toDto(ownerTools);
    }

    /**
     *  Delete the  ownerTools by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OwnerTools : {}", id);
        ownerToolsRepository.delete(id);
        ownerToolsSearchRepository.delete(id);
    }

    /**
     * Search for the ownerTools corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OwnerToolsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OwnerTools for query {}", query);
        Page<OwnerTools> result = ownerToolsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(ownerToolsMapper::toDto);
    }

    @Override
    public OwnerToolsDTO setOwnerByCurrentLogin(OwnerToolsDTO ownerToolsDTO){
        log.debug("set owner by current login: {}", ownerToolsDTO);

        ownerToolsDTO.setOwnerId(userDAO.getUserIdByCurrentLogin());
        ownerToolsDTO.setOwnerLogin(userDAO.getCurrentUserLogin());

        return ownerToolsDTO;
    }
}
