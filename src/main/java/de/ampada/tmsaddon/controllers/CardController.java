package de.ampada.tmsaddon.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.ampada.tmsaddon.dtos.CardDTO;
import de.ampada.tmsaddon.dtos.CardSearchDTO;
import de.ampada.tmsaddon.services.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/card")
public class CardController {
    private static Logger LOGGER = LoggerFactory.getLogger(CardController.class);

    @Autowired
    CardService cardService;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping("/search")
    public List<CardDTO> search(@RequestBody CardSearchDTO cardSearchDTO) {
        try {
            LOGGER.info("search.search request received. cardSearchDTO:{}", objectMapper.writeValueAsString(cardSearchDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return cardService.search(cardSearchDTO);
    }
}
