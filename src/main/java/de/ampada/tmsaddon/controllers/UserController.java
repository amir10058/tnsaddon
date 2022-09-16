package de.ampada.tmsaddon.controllers;

import de.ampada.tmsaddon.dto.UserRegisterDTO;
import de.ampada.tmsaddon.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user")
public class UserController {
    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        LOGGER.info("login.login request received. username:{}", username);
        return userService.login(username, password);
    }

    @PostMapping("/register")
    public String register(@RequestBody @NotNull UserRegisterDTO userRegisterDTO) {
        LOGGER.info("register.register request received. userRegisterDTO:{}", userRegisterDTO);
        return userService.register(userRegisterDTO);
    }

}
