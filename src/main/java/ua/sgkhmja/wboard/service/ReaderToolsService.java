package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.domain.ReaderTools;
import ua.sgkhmja.wboard.repository.ReaderToolsRepository;
import ua.sgkhmja.wboard.repository.search.ReaderToolsSearchRepository;
import ua.sgkhmja.wboard.service.dto.ReaderToolsDTO;
import ua.sgkhmja.wboard.service.mapper.ReaderToolsMapper;
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
 * Service Implementation for managing ReaderTools.
 */
@Service
@Transactional
public class ReaderToolsService {

    private final Logger log = LoggerFactory.getLogger(ReaderToolsService.class);
    
    private final ReaderToolsRepository readerToolsRepository;

    private final ReaderToolsMapper readerToolsMapper;

    private final ReaderToolsSearchRepository readerToolsSearchRepository;

    public ReaderToolsService(ReaderToolsRepository readerToolsRepository, ReaderToolsMapper readerToolsMapper, ReaderToolsSearchRepository readerToolsSearchRepository) {
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
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ReaderToolsDTO> findAll() {
        log.debug("Request to get all ReaderTools");
        List<ReaderToolsDTO> result = readerToolsRepository.findAll().stream()
            .map(readerToolsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one readerTools by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ReaderToolsDTO findOne(Long id) {
        log.debug("Request to get ReaderTools : {}", id);
        ReaderTools readerTools = readerToolsRepository.findOne(id);
        ReaderToolsDTO readerToolsDTO = readerToolsMapper.toDto(readerTools);
        return readerToolsDTO;
    }

    /**
     *  Delete the  readerTools by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ReaderTools : {}", id);
        readerToolsRepository.delete(id);
        readerToolsSearchRepository.delete(id);
    }

    /**
     * Search for the readerTools corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ReaderToolsDTO> search(String query) {
        log.debug("Request to search ReaderTools for query {}", query);
        return StreamSupport
            .stream(readerToolsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(readerToolsMapper::toDto)
            .collect(Collectors.toList());
    }
}
