package de.ampada.tmsaddon.mappers;

import de.ampada.tmsaddon.dtos.CardDTO;
import de.ampada.tmsaddon.entities.Board;
import de.ampada.tmsaddon.entities.Card;
import de.ampada.tmsaddon.entities.User;
import de.ampada.tmsaddon.utils.GlobalUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = GlobalUtils.class)
public interface CardMapper {

    Card convertDTOToEntity(CardDTO cardDTO);

    @Mappings({
            @Mapping(source = "board", target = "boardId"),
            @Mapping(source = "memberUserList", target = "memberUserIdList")
    })
    CardDTO convertEntityToDTO(Card card);

    List<CardDTO> convertEntitiesToDTOs(List<Card> cardEntityListByBoardId);

    default String boardEntityToBoardId(Board board) {
        return board != null && board.getId() != null ? board.getId().toString() : null;
    }

    default String userEntityToUserId(User user) {
        return user != null && user.getId() != null ? user.getId().toString() : null;
    }
}
