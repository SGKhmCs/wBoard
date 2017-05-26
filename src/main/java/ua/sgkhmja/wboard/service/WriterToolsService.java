package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.domain.WriterTools;
import ua.sgkhmja.wboard.repository.WriterToolsRepository;
import ua.sgkhmja.wboard.repository.search.WriterToolsSearchRepository;
import ua.sgkhmja.wboard.service.dto.WriterToolsDTO;
import ua.sgkhmja.wboard.service.mapper.WriterToolsMapper;
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
 * Service Implementation for managing WriterTools.
 */
@Service
@Transactional
public class WriterToolsService {

    private final Logger log = LoggerFactory.getLogger(WriterToolsService.class);
    
    private final WriterToolsRepository writerToolsRepository;

    private final WriterToolsMapper writerToolsMapper;

    private final WriterToolsSearchRepository writerToolsSearchRepository;

    public WriterToolsService(WriterToolsRepository writerToolsRepository, WriterToolsMapper writerToolsMapper, WriterToolsSearchRepository writerToolsSearchRepository) {
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
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<WriterToolsDTO> findAll() {
        log.debug("Request to get all WriterTools");
        List<WriterToolsDTO> result = writerToolsRepository.findAll().stream()
            .map(writerToolsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one writerTools by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public WriterToolsDTO findOne(Long id) {
        log.debug("Request to get WriterTools : {}", id);
        WriterTools writerTools = writerToolsRepository.findOne(id);
        WriterToolsDTO writerToolsDTO = writerToolsMapper.toDto(writerTools);
        return writerToolsDTO;
    }

    /**
     *  Delete the  writerTools by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WriterTools : {}", id);
        writerToolsRepository.delete(id);
        writerToolsSearchRepository.delete(id);
    }

    /**
     * Search for the writerTools corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<WriterToolsDTO> search(String query) {
        log.debug("Request to search WriterTools for query {}", query);
        return StreamSupport
            .stream(writerToolsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(writerToolsMapper::toDto)
            .collect(Collectors.toList());
    }
}
