package de.ampada.tmsaddon.mappers;

import de.ampada.tmsaddon.dtos.BoardDTO;
import de.ampada.tmsaddon.entities.Board;
import de.ampada.tmsaddon.utils.GlobalUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = GlobalUtils.class)
public interface BoardMapper {

    @Mappings({
            @Mapping(target = "createdOn", ignore = true),
            @Mapping(target = "modifiedOn", ignore = true)
    })
    Board convertDTOToEntity(BoardDTO boardDTO);

    BoardDTO convertEntityToDTO(Board board);

    List<BoardDTO> convertEnititiesToDTOs(List<Board> boardList);

}
