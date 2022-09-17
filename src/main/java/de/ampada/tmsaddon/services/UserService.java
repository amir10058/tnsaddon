package de.ampada.tmsaddon.services;

import de.ampada.tmsaddon.dtos.UserDTO;
import de.ampada.tmsaddon.dtos.UserRegisterDTO;

import java.util.List;

public interface UserService {

    UserDTO getById(String id);

    UserDTO getByUsername(String username);

    List<UserDTO> getList();

    UserDTO getCurrentUserDTO();

    String login(String username, String password);

    UserDTO register(UserRegisterDTO userRegisterDTO);
}
