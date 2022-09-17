package de.ampada.tmsaddon.entities;

import org.springframework.security.core.GrantedAuthority;

public class UserRole implements GrantedAuthority {

    private Role role;

    public UserRole(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.getName();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
