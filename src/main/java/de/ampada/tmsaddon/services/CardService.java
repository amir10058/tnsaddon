package de.ampada.tmsaddon.services;

import de.ampada.tmsaddon.dtos.CardDTO;
import de.ampada.tmsaddon.dtos.CardSearchDTO;

import java.util.List;

public interface CardService {

    CardDTO create(String boardId, CardDTO cardDTO);

    CardDTO get(String id);

    List<CardDTO> getListByBoardId(String boardId);

    List<CardDTO> search(CardSearchDTO cardSearchDTO);

    CardDTO update(CardDTO cardDTO);

    void delete(String id);
}
