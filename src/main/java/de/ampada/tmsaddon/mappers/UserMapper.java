package de.ampada.tmsaddon.mappers;

import com.google.common.base.Strings;
import de.ampada.tmsaddon.dtos.UserDTO;
import de.ampada.tmsaddon.dtos.UserRegisterDTO;
import de.ampada.tmsaddon.entities.Role;
import de.ampada.tmsaddon.entities.User;
import de.ampada.tmsaddon.entities.UserRole;
import de.ampada.tmsaddon.utils.GlobalUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = GlobalUtils.class)
public interface UserMapper {

    User convertUserRegisterDTOToEntity(UserRegisterDTO userRegisterDTO);

    @Mappings({
            @Mapping(source = "userRoleSet", target = "roleNameSet"),
            @Mapping(target = "password", ignore = true)
    })
    UserDTO convertEntityToDTO(User user);

    @Mappings({
            @Mapping(source = "roleNameSet", target = "userRoleSet"),
            @Mapping(target = "password", ignore = true)
    })
    User convertDTOToEntity(UserDTO userDTO);

    default String userRoleToRoleName(UserRole userRole) {
        return userRole != null ? userRole.getAuthority() : null;
    }

    default UserRole roleNameToUserRole(String roleName) {
        return !Strings.isNullOrEmpty(roleName) ? new UserRole(new Role(roleName)) : null;
    }

}
