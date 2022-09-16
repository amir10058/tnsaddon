package de.ampada.tmsaddon.services;

import de.ampada.tmsaddon.dtos.BoardDTO;

public interface BoardService {

    BoardDTO create(BoardDTO boardDTO);

    BoardDTO get(String id);
}
