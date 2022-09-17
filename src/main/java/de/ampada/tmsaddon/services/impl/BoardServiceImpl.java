package de.ampada.tmsaddon.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import de.ampada.tmsaddon.dtos.BoardDTO;
import de.ampada.tmsaddon.entities.Board;
import de.ampada.tmsaddon.exception.CustomException;
import de.ampada.tmsaddon.mappers.BoardMapper;
import de.ampada.tmsaddon.mappers.UserMapper;
import de.ampada.tmsaddon.repository.BoardRepository;
import de.ampada.tmsaddon.services.BoardService;
import de.ampada.tmsaddon.services.UserService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    UserService userService;

    @Autowired
    UserMapper userMapper;

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

        Board boardEntityFromDTO = boardMapper.convertDTOToEntity(boardDTO);
        boardEntityFromDTO.setCreatorUser(userMapper.convertDTOToEntity(userService.getCurrentUserDTO()));
        Board boardEntityAfterCreate = boardRepository.save(boardEntityFromDTO);

        LOGGER.debug("create.board has been created successfully. boardId:{}, creatorUserId:{}, createdOnDate:{}",
                boardEntityAfterCreate.getId(), boardEntityAfterCreate.getCreatorUser(), boardEntityAfterCreate.getCreatedOn());

        return boardMapper.convertEntityToDTO(boardEntityAfterCreate);
    }

    @Override
    public BoardDTO get(String id) {
        LOGGER.info("get.method init. id:{}", id);
        if (Strings.isNullOrEmpty(id)) {
            LOGGER.error("get.id is null or empty. id:{}");
            throw new CustomException("id is null or empty");
        }
        Board boardEntityById = getBoardEntityById(id);

        return boardMapper.convertEntityToDTO(boardEntityById);
    }

    @Override
    public List<BoardDTO> getList() {
        LOGGER.info("getList.method init.");
        List<Board> allBoardEntityList = boardRepository.findAll();
        if (CollectionUtils.isEmpty(allBoardEntityList)) {
            LOGGER.error("getList.no board found in DB.");
            throw new CustomException("no board found in DB.");
        }
        LOGGER.debug("getList. {} board found from DB.", allBoardEntityList.size());
        return boardMapper.convertEntitiesToDTOs(allBoardEntityList);
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

//        BoardDTO boardDTOById = this.get(boardDTO.getId());
        Board boardEntityById = getBoardEntityById(boardDTO.getId());
        /*BAGHERI: THE ONLY BOARD ATTRIBUTE WHICH CAN CHANGE IS ITS BOARDNAME REFER TO RFP*/
        boardEntityById.setBoardName(boardDTO.getBoardName());

        Board boardEntityAfterUpdate = boardRepository.save(boardEntityById);

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
        Board boardEntityById = getBoardEntityById(id);

        boardRepository.deleteById(boardEntityById.getId());
        LOGGER.debug("delete.board has been deleted successfully. boardId:{}");
    }

    private Board getBoardEntityById(String id) {
        Optional<Board> optionalBoardById = boardRepository.findById(new ObjectId(id));
        if (!optionalBoardById.isPresent()) {
            LOGGER.error("get.id is invalid or no board found with id:{}", id);
            throw new CustomException("id is invalid or no board found with id:" + id);
        }
        return optionalBoardById.get();
    }
}
