package de.ampada.tmsaddon.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import de.ampada.tmsaddon.dtos.BoardDTO;
import de.ampada.tmsaddon.entities.Board;
import de.ampada.tmsaddon.exception.CustomException;
import de.ampada.tmsaddon.mappers.BoardMapper;
import de.ampada.tmsaddon.repository.BoardRepository;
import de.ampada.tmsaddon.services.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImpl implements BoardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BoardService.class);

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardMapper boardMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public BoardDTO create(BoardDTO boardDTO) {
        try {
            LOGGER.info("create. method init. boardDTO:{}", objectMapper.writeValueAsString(boardDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (boardDTO == null ||
                Strings.isNullOrEmpty(boardDTO.getBoardName()) ||
                Strings.isNullOrEmpty(boardDTO.getCreatorId())) {
            try {
                LOGGER.error("create.boardName or creatorId is null or empty. boardDTO:{}",
                        objectMapper.writeValueAsString(boardDTO));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            throw new CustomException("boardName or creatorId is null or empty");
        }
        Board boardEntityFromDTO = boardMapper.convertDTOToEntity(boardDTO);
        Board boardEntityAfterCreate = boardRepository.save(boardEntityFromDTO);

        LOGGER.debug("create.board has been created successfully. boardId:{}, creatorId:{}, createdOnDate:{}",
                boardEntityAfterCreate.getId(), boardEntityAfterCreate.getCreatorId(), boardEntityAfterCreate.getCreatedOn());

        return boardMapper.convertEntityToDTO(boardEntityAfterCreate);
    }
}
