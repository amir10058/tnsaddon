package de.ampada.tmsaddon.services;

import de.ampada.tmsaddon.dtos.UserDTO;
import de.ampada.tmsaddon.dtos.UserRegisterDTO;

public interface UserService {

    String login(String username, String password);

    UserDTO register(UserRegisterDTO userRegisterDTO);
}
