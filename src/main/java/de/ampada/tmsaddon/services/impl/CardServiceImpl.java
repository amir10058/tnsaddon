package de.ampada.tmsaddon.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import de.ampada.tmsaddon.dtos.BoardDTO;
import de.ampada.tmsaddon.dtos.CardDTO;
import de.ampada.tmsaddon.entities.Card;
import de.ampada.tmsaddon.entities.User;
import de.ampada.tmsaddon.exception.CustomException;
import de.ampada.tmsaddon.mappers.BoardMapper;
import de.ampada.tmsaddon.mappers.CardMapper;
import de.ampada.tmsaddon.mappers.UserMapper;
import de.ampada.tmsaddon.repository.CardRepository;
import de.ampada.tmsaddon.services.BoardService;
import de.ampada.tmsaddon.services.CardService;
import de.ampada.tmsaddon.services.UserService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardService.class);

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    BoardService boardService;

    @Autowired
    BoardMapper boardMapper;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CardMapper cardMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public CardDTO create(String boardId, CardDTO cardDTO) {
        try {
            LOGGER.info("create.method init.boardId:{}, cardDTO:{}", objectMapper.writeValueAsString(cardDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (Strings.isNullOrEmpty(boardId) || cardDTO == null || Strings.isNullOrEmpty(cardDTO.getCardTitle())) {
            try {
                LOGGER.error("create.boardId or cardDTO or its cardName is null or empty. boardId:{}, cardDTO:{}",
                        boardId, objectMapper.writeValueAsString(cardDTO));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            throw new CustomException("boardId or cardDTO or its cardName is null or empty");
        }
        Card cardEntityFromDTO = cardMapper.convertDTOToEntity(cardDTO);
        cardEntityFromDTO.setBoard(boardMapper.convertDTOToEntity(boardService.get(boardId)));
        if (!CollectionUtils.isEmpty(cardDTO.getMemberUserIdList())) {
            cardEntityFromDTO.setMemberUserList(getUserEntityByMemberUserIdList(cardDTO.getMemberUserIdList()));
        }

        Card cardEntityAfterCreate = cardRepository.save(cardEntityFromDTO);

        LOGGER.debug("create.card has been created successfully. boardId:{}, cardId:{}, createdOnDate:{}",
                cardEntityAfterCreate.getBoard().getId(), cardEntityAfterCreate.getId(), cardEntityAfterCreate.getCreatedOn());

        return cardMapper.convertEntityToDTO(cardEntityAfterCreate);
    }

    @Override
    public CardDTO get(String id) {
        LOGGER.info("get.method init. id:{}", id);
        if (Strings.isNullOrEmpty(id)) {
            LOGGER.error("get.id is null or empty. id:{}");
            throw new CustomException("id is null or empty");
        }
        Card cardEntityById = getCardEntityById(id);

        return cardMapper.convertEntityToDTO(cardEntityById);
    }

    @Override
    public List<CardDTO> getListByBoardId(String boardId) {
        LOGGER.info("getListByBoardId.method init. boardId:{}", boardId);

        BoardDTO boardDTOById = boardService.get(boardId);
        try {
            LOGGER.debug("getListByBoardId. boardDTOById from db is:{}.", objectMapper.writeValueAsString(boardDTOById));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        List<Card> cardEntityListByBoardId = cardRepository.findAllByBoard_Id(new ObjectId(boardDTOById.getId()));
        if (CollectionUtils.isEmpty(cardEntityListByBoardId)) {
            LOGGER.error("getListByBoardId.no card found in DB for boardId:{}.", boardId);
            throw new CustomException("no card found in DB for boardId:" + boardId);
        }
        LOGGER.debug("getListByBoardId.{} card found from DB for boardId:{}.", cardEntityListByBoardId.size(), boardId);
        return cardMapper.convertEntitiesToDTOs(cardEntityListByBoardId);
    }

    @Override
    public CardDTO update(CardDTO cardDTO) {
        try {
            LOGGER.info("update.method init. cardDTO:{}", objectMapper.writeValueAsString(cardDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (cardDTO == null || Strings.isNullOrEmpty(cardDTO.getId())) {
            try {
                LOGGER.error("update.cardDTO or its id is null or empty. cardDTO:{}",
                        objectMapper.writeValueAsString(cardDTO));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            throw new CustomException("cardDTO or its id is null or empty");
        }

        if (Strings.isNullOrEmpty(cardDTO.getCardTitle()) && CollectionUtils.isEmpty(cardDTO.getMemberUserIdList())) {
            LOGGER.error("update.cardTitle is null or empty or memberUserIdList is empty. nothing to update!");
            throw new CustomException("cardTitle is null or empty or memberUserIdList is empty. nothing to update!");
        }

        Card cardEntityFromDTO = getCardEntityById(cardDTO.getId());

        if (!Strings.isNullOrEmpty(cardDTO.getCardTitle())) {
            cardEntityFromDTO.setCardTitle(cardDTO.getCardTitle());
        }

        if (!CollectionUtils.isEmpty(cardDTO.getMemberUserIdList())) {
            cardEntityFromDTO.setMemberUserList(getUserEntityByMemberUserIdList(cardDTO.getMemberUserIdList()));
        }

        Card cardEntityAfterUpdate = cardRepository.save(cardEntityFromDTO);

        LOGGER.debug("update.card has been updated successfully. cardId:{}, boardId:{}, modifiedOnDate:{}, memberUserIdList:{}",
                cardEntityAfterUpdate.getId(),
                cardEntityAfterUpdate.getBoard().getId(),
                cardEntityAfterUpdate.getModifiedOn(),
                cardEntityAfterUpdate.getMemberUserList());
        return cardMapper.convertEntityToDTO(cardEntityAfterUpdate);
    }

    @Override
    public void delete(String id) {
        LOGGER.info("delete.method init. id:{}", id);

        if (Strings.isNullOrEmpty(id)) {
            LOGGER.error("delete.id is null or empty. id:{}");
            throw new CustomException("id is null or empty");
        }
        Card cardEntityById = getCardEntityById(id);

        cardRepository.deleteById(cardEntityById.getId());
        LOGGER.debug("delete.card has been deleted successfully. cardId:{}");
    }

    private Card getCardEntityById(String id) {
        Optional<Card> optionalCardById = cardRepository.findById(new ObjectId(id));
        if (!optionalCardById.isPresent()) {
            LOGGER.error("get.id is invalid or no card found with id:{}", id);
            throw new CustomException("id is invalid or no card found with id:" + id);
        }
        return optionalCardById.get();
    }

    private List<User> getUserEntityByMemberUserIdList(List<String> memberUserIdList) {
        return memberUserIdList.stream().map(userId -> {
            try {
                return userService.getById(userId);
            } catch (CustomException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).map(userMapper::convertDTOToEntity).collect(Collectors.toList());
    }
}
