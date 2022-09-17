package de.ampada.tmsaddon.mappers;

import com.google.common.base.Strings;
import de.ampada.tmsaddon.dtos.BoardDTO;
import de.ampada.tmsaddon.entities.Board;
import de.ampada.tmsaddon.entities.User;
import de.ampada.tmsaddon.utils.GlobalUtils;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = GlobalUtils.class)
public interface BoardMapper {

    @Mapping(source = "creatorUserId", target = "creatorUser")
    Board convertDTOToEntity(BoardDTO boardDTO);

    @Mapping(source = "creatorUser", target = "creatorUserId")
    BoardDTO convertEntityToDTO(Board board);

    List<BoardDTO> convertEntitiesToDTOs(List<Board> boardList);

    default String userEntityToUserId(User user) {
        return user != null ? user.getId().toString() : null;
    }

    default User userIdToUserEntity(String userId) {
        if (!Strings.isNullOrEmpty(userId)) {
            User user = new User();
            user.setId(new ObjectId(userId));
            return user;
        }
        return null;
    }

}
