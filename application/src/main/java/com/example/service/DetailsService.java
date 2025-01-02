package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.metrics.DatabaseQueriesTotal;
import com.example.metrics.DatabaseQueryDuration;
import com.example.model.entity.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Service class that implements {@link UserDetailsService}.
 * Responsible for managing user details, including loading user by email, checking user existence,
 * and creating new users.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class DetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final DatabaseQueriesTotal dbQueriesTotal;
    private final DatabaseQueryDuration dbQueryDuration;

    /**
     * Loads a user by their email address.
     *
     * @param email the email address of the user
     * @return the user details associated with the given email
     * @throws UsernameNotFoundException if no user with the given email is found
     */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        dbQueriesTotal.increment();

        return dbQueryDuration.record(() -> userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("There is no user with this email address")));
    }

    /**
     * Checks if a user exists by their login (email).
     *
     * @param login the login (email) of the user
     * @return true if the user exists, false otherwise
     */
    public boolean isUserExists(String login) {
        dbQueriesTotal.increment();

        return dbQueryDuration.record(() -> userRepository.isUserExists(login));
    }

    /**
     * Creates a new user with the given details.
     *
     * @param user the user to create
     * @return the created user
     */
    public User createUser(User user) {
        dbQueriesTotal.increment();

        return dbQueryDuration.record(() ->
                userRepository.createUser(user.getEmail(), user.getBirthDate(), new Date(), user.getPassword()));
    }

}
