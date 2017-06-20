package ua.sgkhmja.wboard.service;

import ua.sgkhmja.wboard.domain.Board;
import ua.sgkhmja.wboard.domain.OwnerTools;
import ua.sgkhmja.wboard.repository.BoardRepository;
import ua.sgkhmja.wboard.repository.OwnerToolsRepository;
import ua.sgkhmja.wboard.repository.UserRepository;
import ua.sgkhmja.wboard.repository.search.BoardSearchRepository;
import ua.sgkhmja.wboard.repository.search.OwnerToolsSearchRepository;
import ua.sgkhmja.wboard.security.SecurityUtils;
import ua.sgkhmja.wboard.service.dto.BoardDTO;
import ua.sgkhmja.wboard.service.dto.OwnerToolsDTO;
import ua.sgkhmja.wboard.service.mapper.BoardMapper;
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
public class OwnerToolsService {

    private final Logger log = LoggerFactory.getLogger(OwnerToolsService.class);

    private final OwnerToolsRepository ownerToolsRepository;

    private final OwnerToolsMapper ownerToolsMapper;

    private final OwnerToolsSearchRepository ownerToolsSearchRepository;

    private final BoardRepository boardRepository;

    private final BoardMapper boardMapper;

    private final BoardSearchRepository boardSearchRepository;

    private final UserRepository userRepository;

    public OwnerToolsService(OwnerToolsRepository ownerToolsRepository, OwnerToolsMapper ownerToolsMapper,
                             OwnerToolsSearchRepository ownerToolsSearchRepository, BoardRepository boardRepository,
                             BoardMapper boardMapper, BoardSearchRepository boardSearchRepository, UserRepository userRepository) {
        this.ownerToolsRepository = ownerToolsRepository;
        this.ownerToolsMapper = ownerToolsMapper;
        this.ownerToolsSearchRepository = ownerToolsSearchRepository;
        this.boardRepository = boardRepository;
        this.boardMapper = boardMapper;
        this.boardSearchRepository = boardSearchRepository;
        this.userRepository = userRepository;
    }
    /**
     * Save a ownerTools.
     *
     * @param ownerToolsDTO the entity to save
     * @return the persisted entity
     */
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
    public void delete(Long id) {
        log.debug("Request to delete OwnerTools : {}", id);

        Long boardId = ownerToolsRepository.findOne(id).getBoard().getId();

        ownerToolsRepository.delete(id);
        ownerToolsSearchRepository.delete(id);

        deleteBoard(boardId);
    }

    /**
     * Search for the ownerTools corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OwnerToolsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OwnerTools for query {}", query);
        Page<OwnerTools> result = ownerToolsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(ownerToolsMapper::toDto);
    }

    /**
     *  Delete the  board by id.
     *
     *  @param id the id of the entity
     */
    private void deleteBoard(Long id) {
        log.debug("Request to delete Board : {}", id);
        boardRepository.delete(id);
        boardSearchRepository.delete(id);
    }



    /**
     * set owner by current user login
     *
     */

    public OwnerToolsDTO createOwnerTools(BoardDTO boardDTO) {

        if(boardDTO.getId() == null)
            return null;

        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Long currentUserId = userRepository.findOneByLogin(currentUserLogin).get().getId();

        OwnerToolsDTO ownerToolsDTO = new OwnerToolsDTO();
        ownerToolsDTO.setBoardId(boardDTO.getId());
        ownerToolsDTO.setBoardName(boardDTO.getName());
        ownerToolsDTO.setOwnerId(currentUserId);
        ownerToolsDTO.setOwnerLogin(currentUserLogin);

        return ownerToolsDTO;
    }
}
