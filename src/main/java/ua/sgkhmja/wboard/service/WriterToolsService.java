package ua.sgkhmja.wboard.service;

import org.springframework.data.domain.PageImpl;
import ua.sgkhmja.wboard.domain.AdminTools;
import ua.sgkhmja.wboard.domain.OwnerTools;
import ua.sgkhmja.wboard.domain.WriterTools;
import ua.sgkhmja.wboard.repository.UserRepository;
import ua.sgkhmja.wboard.repository.WriterToolsRepository;
import ua.sgkhmja.wboard.repository.search.WriterToolsSearchRepository;
import ua.sgkhmja.wboard.security.SecurityUtils;
import ua.sgkhmja.wboard.service.dto.AdminToolsDTO;
import ua.sgkhmja.wboard.service.dto.BoardDTO;
import ua.sgkhmja.wboard.service.dto.OwnerToolsDTO;
import ua.sgkhmja.wboard.service.dto.WriterToolsDTO;
import ua.sgkhmja.wboard.service.mapper.WriterToolsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

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

    private final UserRepository userRepository;

    public WriterToolsService(WriterToolsRepository writerToolsRepository,
                              WriterToolsMapper writerToolsMapper,
                              WriterToolsSearchRepository writerToolsSearchRepository,
                              UserRepository userRepository) {
        this.writerToolsRepository = writerToolsRepository;
        this.writerToolsMapper = writerToolsMapper;
        this.writerToolsSearchRepository = writerToolsSearchRepository;
        this.userRepository = userRepository;
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
     *  @param pageable the pagination information
     *  @return the list of entities
     */
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
    @Transactional(readOnly = true)
    public Page<WriterToolsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WriterTools for query {}", query);
        Page<WriterTools> result = writerToolsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(writerToolsMapper::toDto);
    }

    public WriterToolsDTO createWrirerTools(BoardDTO boardDTO) {
        if(boardDTO.getId() == null)
            return null;

        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Long currentUserId = userRepository.findOneByLogin(currentUserLogin).get().getId();

        WriterToolsDTO writerToolsDTO = new WriterToolsDTO();
        writerToolsDTO.setBoardId(boardDTO.getId());
        writerToolsDTO.setBoardName(boardDTO.getName());
        writerToolsDTO.setUserId(currentUserId);
        writerToolsDTO.setUserLogin(currentUserLogin);
        return writerToolsDTO;
    }


    public List<WriterTools> findAllByBoardId(Long boardId){
        return writerToolsRepository.findAllByBoardId(boardId);
    }

    @Transactional(readOnly = true)
    public Page<WriterToolsDTO> getAllByBoardId(Long id, Pageable pageable){
        List<WriterTools> list = findAllByBoardId(id);
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());

        return new PageImpl<>(list.subList(start, end), pageable, list.size()).map(writerToolsMapper::toDto);
    }
}
