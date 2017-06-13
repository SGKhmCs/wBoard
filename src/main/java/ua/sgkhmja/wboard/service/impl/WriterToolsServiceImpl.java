package ua.sgkhmja.wboard.service.impl;

import ua.sgkhmja.wboard.service.WriterToolsService;
import ua.sgkhmja.wboard.domain.WriterTools;
import ua.sgkhmja.wboard.repository.WriterToolsRepository;
import ua.sgkhmja.wboard.repository.search.WriterToolsSearchRepository;
import ua.sgkhmja.wboard.service.dto.WriterToolsDTO;
import ua.sgkhmja.wboard.service.mapper.WriterToolsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing WriterTools.
 */
@Service
@Transactional
public class WriterToolsServiceImpl implements WriterToolsService{

    private final Logger log = LoggerFactory.getLogger(WriterToolsServiceImpl.class);

    private final WriterToolsRepository writerToolsRepository;

    private final WriterToolsMapper writerToolsMapper;

    private final WriterToolsSearchRepository writerToolsSearchRepository;

    public WriterToolsServiceImpl(WriterToolsRepository writerToolsRepository, WriterToolsMapper writerToolsMapper, WriterToolsSearchRepository writerToolsSearchRepository) {
        this.writerToolsRepository = writerToolsRepository;
        this.writerToolsMapper = writerToolsMapper;
        this.writerToolsSearchRepository = writerToolsSearchRepository;
    }

    /**
     * Save a writerTools.
     *
     * @param writerToolsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WriterToolsDTO save(WriterToolsDTO writerToolsDTO) {
        log.debug("Request to save WriterTools : {}", writerToolsDTO);
        WriterTools writerTools = writerToolsMapper.toEntity(writerToolsDTO);
        writerTools = writerToolsRepository.save(writerTools);
        WriterToolsDTO result = writerToolsMapper.toDto(writerTools);
        writerToolsSearchRepository.save(writerTools);
        return result;
    }

    /**
     *  Get all the writerTools.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WriterToolsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WriterTools");
        return writerToolsRepository.findAll(pageable)
            .map(writerToolsMapper::toDto);
    }

    /**
     *  Get one writerTools by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WriterToolsDTO findOne(Long id) {
        log.debug("Request to get WriterTools : {}", id);
        WriterTools writerTools = writerToolsRepository.findOne(id);
        return writerToolsMapper.toDto(writerTools);
    }

    /**
     *  Delete the  writerTools by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WriterTools : {}", id);
        writerToolsRepository.delete(id);
        writerToolsSearchRepository.delete(id);
    }

    /**
     * Search for the writerTools corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WriterToolsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WriterTools for query {}", query);
        Page<WriterTools> result = writerToolsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(writerToolsMapper::toDto);
    }
}
