package ua.sgkhmja.wboard.service.impl;

import ua.sgkhmja.wboard.service.ReaderToolsService;
import ua.sgkhmja.wboard.domain.ReaderTools;
import ua.sgkhmja.wboard.repository.ReaderToolsRepository;
import ua.sgkhmja.wboard.repository.search.ReaderToolsSearchRepository;
import ua.sgkhmja.wboard.service.dto.ReaderToolsDTO;
import ua.sgkhmja.wboard.service.mapper.ReaderToolsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ReaderTools.
 */
@Service
@Transactional
public class ReaderToolsServiceImpl implements ReaderToolsService{

    private final Logger log = LoggerFactory.getLogger(ReaderToolsServiceImpl.class);

    private final ReaderToolsRepository readerToolsRepository;

    private final ReaderToolsMapper readerToolsMapper;

    private final ReaderToolsSearchRepository readerToolsSearchRepository;

    public ReaderToolsServiceImpl(ReaderToolsRepository readerToolsRepository, ReaderToolsMapper readerToolsMapper, ReaderToolsSearchRepository readerToolsSearchRepository) {
        this.readerToolsRepository = readerToolsRepository;
        this.readerToolsMapper = readerToolsMapper;
        this.readerToolsSearchRepository = readerToolsSearchRepository;
    }

    /**
     * Save a readerTools.
     *
     * @param readerToolsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ReaderToolsDTO save(ReaderToolsDTO readerToolsDTO) {
        log.debug("Request to save ReaderTools : {}", readerToolsDTO);
        ReaderTools readerTools = readerToolsMapper.toEntity(readerToolsDTO);
        readerTools = readerToolsRepository.save(readerTools);
        ReaderToolsDTO result = readerToolsMapper.toDto(readerTools);
        readerToolsSearchRepository.save(readerTools);
        return result;
    }

    /**
     *  Get all the readerTools.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReaderToolsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReaderTools");
        return readerToolsRepository.findAll(pageable)
            .map(readerToolsMapper::toDto);
    }

    /**
     *  Get one readerTools by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ReaderToolsDTO findOne(Long id) {
        log.debug("Request to get ReaderTools : {}", id);
        ReaderTools readerTools = readerToolsRepository.findOne(id);
        return readerToolsMapper.toDto(readerTools);
    }

    /**
     *  Delete the  readerTools by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReaderTools : {}", id);
        readerToolsRepository.delete(id);
        readerToolsSearchRepository.delete(id);
    }

    /**
     * Search for the readerTools corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReaderToolsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReaderTools for query {}", query);
        Page<ReaderTools> result = readerToolsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(readerToolsMapper::toDto);
    }
}
