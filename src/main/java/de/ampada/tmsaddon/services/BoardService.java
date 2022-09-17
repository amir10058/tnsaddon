package de.ampada.tmsaddon.services;

import de.ampada.tmsaddon.dtos.BoardDTO;

import java.util.List;

public interface BoardService {

    BoardDTO create(BoardDTO boardDTO);

    List<BoardDTO> get(String id);

    BoardDTO update(BoardDTO boardDTO);

    void delete(String id);
}
