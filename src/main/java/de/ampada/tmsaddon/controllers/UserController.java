package de.ampada.tmsaddon.controllers;

import de.ampada.tmsaddon.dtos.UserDTO;
import de.ampada.tmsaddon.dtos.UserRegisterDTO;
import de.ampada.tmsaddon.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<UserDTO> getList() {
        LOGGER.info("getList. get user list request received.");
        return userService.getList();
    }

    @GetMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        LOGGER.info("login.login request received. username:{}", username);
        return userService.login(username, password);
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody @NotNull @Valid UserRegisterDTO userRegisterDTO) {
        LOGGER.info("register.register request received. userDTO:{}", userRegisterDTO);
        return userService.register(userRegisterDTO);
    }

}
