package com.example.model.enumeration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * Enum representing user roles in the application. Implements {@link GrantedAuthority} for Spring Security.
 */
@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    /**
     * Regular user role with basic access.
     */
    USER("ROLE_USER"),

    /**
     * Admin role with elevated privileges.
     */
    ADMIN("ROLE_ADMIN");

    private final String value;

    /**
     * Returns the authority string associated with the role.
     *
     * @return the authority value of the role (e.g., "ROLE_USER", "ROLE_ADMIN")
     */
    @Override
    public String getAuthority() {
        return value;
    }

}
