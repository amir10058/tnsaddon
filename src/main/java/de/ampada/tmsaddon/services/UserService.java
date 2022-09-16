package de.ampada.tmsaddon.services;

import de.ampada.tmsaddon.dto.UserRegisterDTO;

public interface UserService {

    String login(String username, String password);

    String register(UserRegisterDTO userRegisterDTO);
}
