package de.ampada.tmsaddon.mappers;

import de.ampada.tmsaddon.domains.User;
import de.ampada.tmsaddon.dto.UserRegisterDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User convertUserRegisterDTOToUser(UserRegisterDTO userRegisterDTO);

}
