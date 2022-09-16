package de.ampada.tmsaddon.mappers;

import de.ampada.tmsaddon.dtos.UserDTO;
import de.ampada.tmsaddon.dtos.UserRegisterDTO;
import de.ampada.tmsaddon.entities.User;
import de.ampada.tmsaddon.entities.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User convertUserRegisterDTOToEntity(UserRegisterDTO userRegisterDTO);

    @Mapping(source = "userRoleSet", target = "roleNameSet")
    UserDTO convertEntityToDTO(User user);

    default String userRoleToRoleName(UserRole userRole) {
        return userRole != null ? userRole.getAuthority() : null;
    }

}
