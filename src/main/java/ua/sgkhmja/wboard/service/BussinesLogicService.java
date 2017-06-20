package ua.sgkhmja.wboard.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.sgkhmja.wboard.domain.Board;
import ua.sgkhmja.wboard.domain.ReaderTools;
import ua.sgkhmja.wboard.service.dto.*;
import ua.sgkhmja.wboard.service.mapper.BoardMapper;

/**
 * Created by Sasha on 18.06.2017.
 */
@Service
//@Transactional
public class BussinesLogicService {
    private final Logger log = LoggerFactory.getLogger(BoardService.class);

    private final OwnerToolsService ownerToolsService;
    private final BoardService boardService;
    private final AdminToolsService adminToolsService;
    private final WriterToolsService writerToolsService;
    private final ReaderToolsService readerToolsService;



    public BussinesLogicService(OwnerToolsService ownerToolsService,
                                BoardService boardService,
                                AdminToolsService adminToolsService,
                                WriterToolsService writerToolsService,
                                ReaderToolsService readerToolsService) {
        this.ownerToolsService = ownerToolsService;
        this.boardService = boardService;
        this.adminToolsService = adminToolsService;
        this.writerToolsService = writerToolsService;
        this.readerToolsService = readerToolsService;
    }

    /**
     * Save a board.
     *
     * @param boardDTO the entity to save
     * @return the persisted entity
     */
    public BoardDTO createBoardAndOwnerTools(BoardDTO boardDTO) {
        log.debug("Request to save Board and OwnerTools : {}", boardDTO);

        BoardDTO result = boardService.createBoard(boardDTO);

        if(boardDTO.getId() == null) {
            OwnerToolsDTO ownerToolsDTO = ownerToolsService.createOwnerTools(result);
            ownerToolsDTO = ownerToolsService.save(ownerToolsDTO);

            AdminToolsDTO adminToolsDTO = adminToolsService.createAdminTools(result);
            adminToolsDTO = adminToolsService.save(adminToolsDTO);

            WriterToolsDTO writerToolsDTO = writerToolsService.createWrirerTools(result);
            writerToolsDTO = writerToolsService.save(writerToolsDTO);

            ReaderToolsDTO readerToolsDTO = readerToolsService.createReaderTools(result);
            readerToolsDTO = readerToolsService.save(readerToolsDTO);
        }
        return result;
    }


}
