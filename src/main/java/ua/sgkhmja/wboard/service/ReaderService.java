package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.domain.Reader;
import ua.sgkhmja.wboard.repository.ReaderRepository;
import ua.sgkhmja.wboard.repository.search.ReaderSearchRepository;
import ua.sgkhmja.wboard.service.dto.ReaderDTO;
import ua.sgkhmja.wboard.service.mapper.ReaderMapper;
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
 * Service Implementation for managing Reader.
 */
@Service
@Transactional
public class ReaderService {

    private final Logger log = LoggerFactory.getLogger(ReaderService.class);
    
    private final ReaderRepository readerRepository;

    private final ReaderMapper readerMapper;

    private final ReaderSearchRepository readerSearchRepository;

    public ReaderService(ReaderRepository readerRepository, ReaderMapper readerMapper, ReaderSearchRepository readerSearchRepository) {
        this.readerRepository = readerRepository;
        this.readerMapper = readerMapper;
        this.readerSearchRepository = readerSearchRepository;
    }

    /**
     * Save a reader.
     *
     * @param readerDTO the entity to save
     * @return the persisted entity
     */
    public ReaderDTO save(ReaderDTO readerDTO) {
        log.debug("Request to save Reader : {}", readerDTO);
        Reader reader = readerMapper.toEntity(readerDTO);
        reader = readerRepository.save(reader);
        ReaderDTO result = readerMapper.toDto(reader);
        readerSearchRepository.save(reader);
        return result;
    }

    /**
     *  Get all the readers.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ReaderDTO> findAll() {
        log.debug("Request to get all Readers");
        List<ReaderDTO> result = readerRepository.findAll().stream()
            .map(readerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one reader by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ReaderDTO findOne(Long id) {
        log.debug("Request to get Reader : {}", id);
        Reader reader = readerRepository.findOne(id);
        ReaderDTO readerDTO = readerMapper.toDto(reader);
        return readerDTO;
    }

    /**
     *  Delete the  reader by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Reader : {}", id);
        readerRepository.delete(id);
        readerSearchRepository.delete(id);
    }

    /**
     * Search for the reader corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ReaderDTO> search(String query) {
        log.debug("Request to search Readers for query {}", query);
        return StreamSupport
            .stream(readerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(readerMapper::toDto)
            .collect(Collectors.toList());
    }
}
