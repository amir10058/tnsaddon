package de.ampada.tmsaddon.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import de.ampada.tmsaddon.dtos.BoardDTO;
import de.ampada.tmsaddon.dtos.UserDTO;
import de.ampada.tmsaddon.entities.Board;
import de.ampada.tmsaddon.exception.CustomException;
import de.ampada.tmsaddon.mappers.BoardMapper;
import de.ampada.tmsaddon.repository.BoardRepository;
import de.ampada.tmsaddon.services.BoardService;
import de.ampada.tmsaddon.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BoardService.class);

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardMapper boardMapper;

    @Autowired
    BoardService boardService;

    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public BoardDTO create(BoardDTO boardDTO) {
        try {
            LOGGER.info("create.method init. boardDTO:{}", objectMapper.writeValueAsString(boardDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (boardDTO == null || Strings.isNullOrEmpty(boardDTO.getBoardName())) {
            try {
                LOGGER.error("create.boardDTO or its boardName is null or empty. boardDTO:{}",
                        objectMapper.writeValueAsString(boardDTO));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            throw new CustomException("boardDTO or its boardName is null or empty");
        }

        String creatorUserId = null;
        try {
            String currentLoginUsername = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserDTO currentLoginUserDTO = userService.getByUsername(currentLoginUsername);
            creatorUserId = currentLoginUserDTO.getId();
        } catch (Exception e) {
            LOGGER.error("create. abnormal error happened! ex.msg:{}", e.getMessage());
        }
        if (Strings.isNullOrEmpty(creatorUserId)) {
            LOGGER.error("create. could not extract creatorUserId from DB!");
            throw new CustomException("could not extract creatorUserId from DB!");
        }

        boardDTO.setCreatorId(creatorUserId);
        LOGGER.debug("create. current user id is :{} and board name :{} is creating by him/her",
                creatorUserId,
                boardDTO.getBoardName());

        Board boardEntityFromDTO = boardMapper.convertDTOToEntity(boardDTO);
        Board boardEntityAfterCreate = boardRepository.save(boardEntityFromDTO);

        LOGGER.debug("create.board has been created successfully. boardId:{}, creatorId:{}, createdOnDate:{}",
                boardEntityAfterCreate.getId(), boardEntityAfterCreate.getCreatorId(), boardEntityAfterCreate.getCreatedOn());

        return boardMapper.convertEntityToDTO(boardEntityAfterCreate);
    }

    @Override
    public List<BoardDTO> get(String id) {
        LOGGER.info("get.method init. id:{}", id);

        if (Strings.isNullOrEmpty(id)) {
            List<Board> allBoardEntityList = boardRepository.findAll();
            if (CollectionUtils.isEmpty(allBoardEntityList)) {
                LOGGER.error("get.no board found.");
                throw new CustomException("no board found.");
            }
            return boardMapper.convertEnititiesToDTOs(allBoardEntityList);
        }
        Optional<Board> optionalBoardById = boardRepository.findById(id);
        if (!optionalBoardById.isPresent()) {
            LOGGER.error("get.id is invalid. id:{}", id);
            throw new CustomException("id is invalid");
        }
        return Collections.singletonList(boardMapper.convertEntityToDTO(optionalBoardById.get()));
    }

    @Override
    public BoardDTO update(BoardDTO boardDTO) {
        try {
            LOGGER.info("update.method init. boardDTO:{}", objectMapper.writeValueAsString(boardDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (boardDTO == null || Strings.isNullOrEmpty(boardDTO.getId())) {
            try {
                LOGGER.error("update.boardDTO or its id is null or empty. boardDTO:{}",
                        objectMapper.writeValueAsString(boardDTO));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            throw new CustomException("boardDTO or its id is null or empty");
        }
        if (Strings.isNullOrEmpty(boardDTO.getBoardName())) {
            LOGGER.error("update.boardName is null or empty. nothing to update!");
            throw new CustomException("boardName is null or empty. nothing to update!");
        }

        BoardDTO boardDTOById = boardService.get(boardDTO.getId()).get(0);

        /*BAGHERI: THE ONLY BOARD ATTRIBUTE WHICH CAN CHANGE IS ITS BOARDNAME REFER TO RFP*/
        boardDTOById.setBoardName(boardDTO.getBoardName());

        Board boardEntityAfterUpdate = boardRepository.save(boardMapper.convertDTOToEntity(boardDTOById));

        LOGGER.debug("update.board has been updated successfully. boardId:{}, boardName:{}, modifiedOnDate:{}",
                boardEntityAfterUpdate.getId(),
                boardEntityAfterUpdate.getBoardName(),
                boardEntityAfterUpdate.getModifiedOn());
        return boardMapper.convertEntityToDTO(boardEntityAfterUpdate);
    }

    @Override
    public void delete(String id) {
        LOGGER.info("delete.method init. id:{}", id);

        if (Strings.isNullOrEmpty(id)) {
            LOGGER.error("delete.id is null or empty. id:{}");
            throw new CustomException("id is null or empty");
        }
        BoardDTO boardDTOById = boardService.get(id).get(0);

        boardRepository.deleteById(boardDTOById.getId());
        LOGGER.debug("delete.board has been deleted successfully. boardId:{}");
    }
}
