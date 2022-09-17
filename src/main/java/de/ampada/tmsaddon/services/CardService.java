package de.ampada.tmsaddon.services;

import de.ampada.tmsaddon.dtos.CardDTO;

import java.util.List;

public interface CardService {

    CardDTO create(String boardId, CardDTO cardDTO);

    CardDTO get(String id);

    List<CardDTO> getListByBoardId(String boardId);

    CardDTO update(CardDTO cardDTO);

    void delete(String id);
}
