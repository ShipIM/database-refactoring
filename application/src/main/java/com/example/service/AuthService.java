package com.example.service;

import com.example.model.entity.User;
import com.example.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service class responsible for handling user authentication and registration.
 * It provides methods for registering a new user and authenticating an existing user.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final DetailsService detailsService;
    private final JwtUtils jwtUtils;

    /**
     * Registers a new user by encoding their password and creating the user.
     * Also generates an access token for the newly registered user.
     *
     * @param user the user to register
     * @return a pair containing the registered user and their generated access token
     */
    public Pair<User, String> register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user = detailsService.createUser(user);

        return Pair.of(user, generateAccessToken(user));
    }

    /**
     * Authenticates an existing user by verifying their credentials and generating an access token.
     *
     * @param user the user to authenticate
     * @return a pair containing the authenticated user and their generated access token
     */
    public Pair<User, String> authenticate(User user) {
        user = (User) authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()))
                .getPrincipal();

        return Pair.of(user, generateAccessToken(user));
    }

    /**
     * Generates an access token for the specified user.
     *
     * @param user the user for whom the access token is generated
     * @return the generated access token
     */
    private String generateAccessToken(User user) {
        return generateAccessToken(new HashMap<>(), user);
    }

    /**
     * Generates an access token for the specified user with additional claims.
     *
     * @param extraClaims additional claims to include in the token
     * @param user        the user for whom the access token is generated
     * @return the generated access token
     */
    private String generateAccessToken(Map<String, Object> extraClaims, User user) {
        return jwtUtils.generateAccessToken(extraClaims, user);
    }

}
