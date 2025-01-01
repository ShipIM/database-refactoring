package com.example.model.entity;

import com.example.model.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Represents a user with email, password, birth date, registration date, and role-based authorities.
 * Implements {@link UserDetails} for Spring Security integration.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    /**
     * The user's email address, used as the username.
     */
    private String email;

    /**
     * The user's password.
     */
    private String password;

    /**
     * The user's birth date.
     */
    private Date birthDate;

    /**
     * The user's registration date.
     */
    private Date registrationDate;

    /**
     * Returns the email as the username for the user.
     *
     * @return the email address
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Returns the password of the user.
     *
     * @return the password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the authorities granted to the user, which in this case is the {@link Role#USER}.
     *
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(Role.USER);
    }

    /**
     * Indicates whether the user's account is expired.
     *
     * @return true if the account is not expired, false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     *
     * @return true if the account is not locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials are expired.
     *
     * @return true if the credentials are not expired, false otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled.
     *
     * @return true if the user is enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
