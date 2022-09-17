package de.ampada.tmsaddon.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.ampada.tmsaddon.dtos.BoardDTO;
import de.ampada.tmsaddon.services.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class APIController {

    private static Logger LOGGER = LoggerFactory.getLogger(APIController.class);

    @Autowired
    BoardService boardService;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping("/board")
    @PreAuthorize("hasRole('ROLE_USER')")
    public BoardDTO createBoard(@RequestBody @Valid BoardDTO boardDTO) {
        try {
            LOGGER.info("createBoard. create board request received. boardDTO:{}", objectMapper.writeValueAsString(boardDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return boardService.create(boardDTO);
    }

    @GetMapping("/board/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<BoardDTO> getBoard(@PathVariable(required = false) String id) {
        LOGGER.info("getBoard. get board request received. id:{}", id);
        return boardService.get(id);
    }

    @PutMapping("/board")
    @PreAuthorize("hasRole('ROLE_USER')")
    public BoardDTO updateBoard(@RequestBody @Valid BoardDTO boardDTO) {
        try {
            LOGGER.info("updateBoard. update board request received. boardDTO:{}", objectMapper.writeValueAsString(boardDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return boardService.update(boardDTO);
    }

    @DeleteMapping("/board/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteBoard(@PathVariable String id) {
        LOGGER.info("deleteBoard. delete board request received. id:{}", id);
        boardService.delete(id);
    }
}
