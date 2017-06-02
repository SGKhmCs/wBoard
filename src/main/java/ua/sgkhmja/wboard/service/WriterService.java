package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.domain.Reader;
import ua.sgkhmja.wboard.domain.Writer;
import ua.sgkhmja.wboard.repository.WriterRepository;
import ua.sgkhmja.wboard.repository.search.WriterSearchRepository;
import ua.sgkhmja.wboard.service.dto.WriterDTO;
import ua.sgkhmja.wboard.service.mapper.WriterMapper;
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
 * Service Implementation for managing Writer.
 */
@Service
@Transactional
public class WriterService {

    private final Logger log = LoggerFactory.getLogger(WriterService.class);

    private final WriterRepository writerRepository;

    private final WriterMapper writerMapper;

    private final WriterSearchRepository writerSearchRepository;

    public WriterService(WriterRepository writerRepository, WriterMapper writerMapper, WriterSearchRepository writerSearchRepository) {
        this.writerRepository = writerRepository;
        this.writerMapper = writerMapper;
        this.writerSearchRepository = writerSearchRepository;
    }

    /**
     * Save a writer.
     *
     * @param writerDTO the entity to save
     * @return the persisted entity
     */
    public WriterDTO save(WriterDTO writerDTO) {
        log.debug("Request to save Writer : {}", writerDTO);
        Writer writer = writerMapper.toEntity(writerDTO);
        writer = writerRepository.save(writer);
        WriterDTO result = writerMapper.toDto(writer);
        writerSearchRepository.save(writer);
        return result;
    }

    /**
     *  Get all the writers.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<WriterDTO> findAll() {
        log.debug("Request to get all Writers");
        List<WriterDTO> result = writerRepository.findAll().stream()
            .map(writerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one writer by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public WriterDTO findOne(Long id) {
        log.debug("Request to get Writer : {}", id);
        Writer writer = writerRepository.findOne(id);
        WriterDTO writerDTO = writerMapper.toDto(writer);
        return writerDTO;
    }

    /**
     *  Delete the  writer by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Writer : {}", id);
        writerRepository.delete(id);
        writerSearchRepository.delete(id);
    }

    /**
     * Search for the writer corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<WriterDTO> search(String query) {
        log.debug("Request to search Writers for query {}", query);
        return StreamSupport
            .stream(writerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(writerMapper::toDto)
            .collect(Collectors.toList());
    }

    public Writer createWriter(Reader reader){
        Writer writer = new Writer();

        writer.setReader(reader);

        return writer;
    }
}
