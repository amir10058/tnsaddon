package de.ampada.tmsaddon.services;

import de.ampada.tmsaddon.dtos.UserDTO;
import de.ampada.tmsaddon.dtos.UserRegisterDTO;

public interface UserService {

    UserDTO getById(String id);

    UserDTO getByUsername(String username);

    UserDTO getCurrentUserDTO();

    String login(String username, String password);

    UserDTO register(UserRegisterDTO userRegisterDTO);
}
