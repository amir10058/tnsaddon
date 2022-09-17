package de.ampada.tmsaddon.services;

import de.ampada.tmsaddon.dtos.BoardDTO;

import java.util.List;

public interface BoardService {

    BoardDTO create(BoardDTO boardDTO);

    BoardDTO get(String id);

    List<BoardDTO> getList();

    BoardDTO update(BoardDTO boardDTO);

    void delete(String id);
}
