package de.ampada.tmsaddon.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.ampada.tmsaddon.dtos.BoardDTO;
import de.ampada.tmsaddon.dtos.CardDTO;
import de.ampada.tmsaddon.services.BoardService;
import de.ampada.tmsaddon.services.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api")
public class APIController {

    private static Logger LOGGER = LoggerFactory.getLogger(APIController.class);

    @Autowired
    BoardService boardService;

    @Autowired
    CardService cardService;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping("/board")
    public BoardDTO createBoard(@RequestBody @Valid BoardDTO boardDTO) {
        try {
            LOGGER.info("createBoard. create board request received. boardDTO:{}", objectMapper.writeValueAsString(boardDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return boardService.create(boardDTO);
    }

    @GetMapping("/board/{id}")
    public BoardDTO getBoard(@PathVariable String id) {
        LOGGER.info("getBoard. get board request received. id:{}", id);
        return boardService.get(id);
    }

    @GetMapping("/board")
    public List<BoardDTO> getBoardList() {
        LOGGER.info("getBoard. get board list request received.");
        return boardService.getList();
    }

    @PutMapping("/board")
    public BoardDTO updateBoard(@RequestBody @Valid BoardDTO boardDTO) {
        try {
            LOGGER.info("updateBoard. update board request received. boardDTO:{}", objectMapper.writeValueAsString(boardDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return boardService.update(boardDTO);
    }

    @DeleteMapping("/board/{id}")
    public void deleteBoard(@PathVariable String id) {
        LOGGER.info("deleteBoard. delete board request received. id:{}", id);
        boardService.delete(id);
    }

    @PostMapping("/board/{boardId}/cards")
    public CardDTO createCard(@PathVariable String boardId, @RequestBody @Valid CardDTO cardDTO) {
        try {
            LOGGER.info("createCard. create card request received. boardId:{}, cardDTO:{}",
                    boardId,
                    objectMapper.writeValueAsString(cardDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return cardService.create(boardId, cardDTO);
    }

    @GetMapping("/board/{boardId}/cards/{id}")
    public CardDTO getCard(@PathVariable String boardId, @PathVariable String id) {
        LOGGER.info("getCard. get card request received. boardId:{}, id:{}", boardId, id);
        /*BAGHERI: BOARDID IS NOT NEEDED FOR GETTING CARD BY ID BECAUSE CARD ID IS UNIQUE AND
         WE CAN GET IT WITHOUT ITS BOARD ID. SO WE OMIT BOARDID WHICH RECEIVED BY CONTROLLER AND
         DONT PASS IT TO SERVICE 
        */
        return cardService.get(id);
    }

    @GetMapping("/board/{boardId}/cards")
    public List<CardDTO> getListByBoardId(@PathVariable String boardId) {
        LOGGER.info("getListByBoardId. get cards list by board id request received. boardId:{}", boardId);
        return cardService.getListByBoardId(boardId);
    }

    @PutMapping("/board/{boardId}/cards")
    public CardDTO updateCard(@PathVariable String boardId, @RequestBody CardDTO cardDTO) {
        try {
            LOGGER.info("updateCard. update card request received. boardId:{}, cardDTO:{}",
                    boardId,
                    objectMapper.writeValueAsString(cardDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        /*BAGHERI: BOARDID IS NOT NEEDED DURING UPDATING CARD BY ID BECAUSE CARD ID IS UNIQUE AND
         WE CAN UPDATE IT WITHOUT ITS BOARD ID. SO WE OMIT BOARDID WHICH RECEIVED BY CONTROLLER AND
         DONT PASS IT TO SERVICE
        */
        return cardService.update(cardDTO);
    }

    @DeleteMapping("/board/{boardId}/cards/{id}")
    public void deleteCard(@PathVariable String boardId, @PathVariable String id) {
        LOGGER.info("deleteCard. delete card request received. boardId:{}, id:{}", boardId, id);
        /*BAGHERI: BOARDID IS NOT NEEDED DURING DELETING CARD BY ID BECAUSE CARD ID IS UNIQUE AND
         WE CAN DELETE IT WITHOUT ITS BOARD ID. SO WE OMIT BOARDID WHICH RECEIVED BY CONTROLLER AND
         DONT PASS IT TO SERVICE
        */
        cardService.delete(id);
    }
}
