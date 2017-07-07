package ua.sgkhmja.wboard.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.sgkhmja.wboard.domain.AdminTools;
import ua.sgkhmja.wboard.domain.Board;
import ua.sgkhmja.wboard.domain.ReaderTools;
import ua.sgkhmja.wboard.repository.UserRepository;
import ua.sgkhmja.wboard.security.SecurityUtils;
import ua.sgkhmja.wboard.service.dto.*;
import ua.sgkhmja.wboard.service.mapper.BoardMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
    private final UserRepository userRepository;

    public BussinesLogicService(OwnerToolsService ownerToolsService,
                                BoardService boardService,
                                AdminToolsService adminToolsService,
                                WriterToolsService writerToolsService,
                                ReaderToolsService readerToolsService,
                                UserRepository userRepository) {
        this.ownerToolsService = ownerToolsService;
        this.boardService = boardService;
        this.adminToolsService = adminToolsService;
        this.writerToolsService = writerToolsService;
        this.readerToolsService = readerToolsService;
        this.userRepository = userRepository;
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

    public void deleteAdminToolsIfNotOwner(Long id){
        AdminToolsDTO adminToolsDTO = adminToolsService.findOne(id);

//        BoardDTO boardDTO = boardService.findOne(adminToolsDTO.getBoardId());
//        boardDTO.
//        ownerToolsService.findOne(9)
//
//        OwnerToolsDTO ownerToolsDTO = ownerToolsService.findOne(adminToolsDTO.getUserId());
//        ownerToolsService.
//        ownerToolsDTO.get


        adminToolsService.delete(id);
    }


    public Page<BoardDTO> findAllBoardsByUser(Pageable pageable) {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Long currentUserId = userRepository.findOneByLogin(currentUserLogin).get().getId();

        List<BoardDTO> boardDTOList = boardService.findAll(pageable).getContent();

        List<BoardDTO> fullDTOList = new ArrayList<>();
        fullDTOList.addAll(boardDTOList
            .stream()
            .filter(boardDTO -> boardDTO.isPub())
            .collect(Collectors.toList()));


        List<OwnerToolsDTO> ownerToolsDTOList = ownerToolsService.findAll(pageable).getContent();
        for (OwnerToolsDTO ownerToolsDTO : ownerToolsDTOList) {
//            System.out.println(ownerToolsDTO);
//            System.out.println(currentUserId);
//            for (BoardDTO boardDTO : boardDTOList) {
//                System.out.println(boardDTO);
//                if(ownerToolsDTO.getBoardId().equals(boardDTO.getId())
//                    && ownerToolsDTO.getOwnerId().equals(currentUserId)){
//                    System.out.println(true);
//                }
//            }
            fullDTOList.addAll(boardDTOList.stream()
                .filter(boardDTO -> ownerToolsDTO.getBoardId().equals(boardDTO.getId())
                    && ownerToolsDTO.getOwnerId().equals(currentUserId))
                .collect(Collectors.toList()));
        }

        List<AdminToolsDTO> adminToolsDTOList = adminToolsService.findAll(pageable).getContent();
        for (AdminToolsDTO adminToolsDTO : adminToolsDTOList) {
            fullDTOList.addAll(boardDTOList.stream()
                .filter(boardDTO -> adminToolsDTO.getBoardId().equals(boardDTO.getId())
                    && adminToolsDTO.getUserId().equals(currentUserId))
                .collect(Collectors.toList()));
        }

        List<WriterToolsDTO> writerToolsDTOList = writerToolsService.findAll(pageable).getContent();
        for (WriterToolsDTO writerToolsDTO : writerToolsDTOList) {
            fullDTOList.addAll(boardDTOList.stream()
                .filter(boardDTO -> writerToolsDTO.getBoardId().equals(boardDTO.getId())
                    && writerToolsDTO.getUserId().equals(currentUserId))
                .collect(Collectors.toList()));
        }

        List<ReaderToolsDTO> readerToolsDTOList = readerToolsService.findAll(pageable).getContent();
        for (ReaderToolsDTO readerToolsDTO : readerToolsDTOList) {
            fullDTOList.addAll(boardDTOList.stream()
                .filter(boardDTO -> readerToolsDTO.getBoardId().equals(boardDTO.getId())
                    && readerToolsDTO.getUserId().equals(currentUserId))
                .collect(Collectors.toList()));
        }

        List<BoardDTO> fullDTOListWithoutDuplicates = fullDTOList.stream()
            .distinct()
            .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
            .collect(Collectors.toList());

        Page<BoardDTO> boardDTOPage = new PageImpl<BoardDTO>(fullDTOListWithoutDuplicates);
        return boardDTOPage;
    }

//    private boolean is
}
