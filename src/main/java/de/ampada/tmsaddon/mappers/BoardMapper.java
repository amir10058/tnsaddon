package de.ampada.tmsaddon.mappers;

import de.ampada.tmsaddon.dtos.BoardDTO;
import de.ampada.tmsaddon.entities.Board;
import de.ampada.tmsaddon.entities.User;
import de.ampada.tmsaddon.utils.GlobalUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = GlobalUtils.class)
public interface BoardMapper {

    @Mappings({
            @Mapping(target = "createdOn", ignore = true),
            @Mapping(target = "modifiedOn", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    Board convertDTOToEntity(BoardDTO boardDTO);


    @Mapping(source = "creatorUser", target = "creatorUserId")
    BoardDTO convertEntityToDTO(Board board);

    List<BoardDTO> convertEntitiesToDTOs(List<Board> boardList);

    default String userEntityToUserId(User user) {
        return user != null ? user.getId().toString() : null;
    }

}
