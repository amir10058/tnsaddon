package de.ampada.tmsaddon.mappers;

import de.ampada.tmsaddon.dtos.UserDTO;
import de.ampada.tmsaddon.dtos.UserRegisterDTO;
import de.ampada.tmsaddon.entities.User;
import de.ampada.tmsaddon.entities.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User convertUserRegisterDTOToEntity(UserRegisterDTO userRegisterDTO);

    @Mappings({
            @Mapping(source = "userRoleSet", target = "roleNameSet"),
            @Mapping(target = "blank.password", ignore = true)
    })
    UserDTO convertEntityToDTO(User user);

    default String userRoleToRoleName(UserRole userRole) {
        return userRole != null ? userRole.getAuthority() : null;
    }

}
