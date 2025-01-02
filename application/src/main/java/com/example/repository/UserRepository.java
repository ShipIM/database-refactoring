package com.example.repository;

import com.example.model.entity.User;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * Provides methods for querying and managing user data.
 */
@RepositoryDefinition(domainClass = User.class, idClass = String.class)
public interface UserRepository {

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user
     * @return an Optional containing the user, if found
     */
    @Query("select _user.login as email, p.password as password from _user " +
            "join password p on _user.login = p.user_login " +
            "where login = :email")
    Optional<User> findByEmail(@Param("email") String email);

    /**
     * Creates a new user with the provided details.
     *
     * @param login            the email address of the user
     * @param birthDate        the user's birth date
     * @param registrationDate the registration date of the user
     * @param password         the user's password
     * @return the newly created user
     */
    @Query("select * from create_user(:login, :birth_date, :registration_date, :password)")
    User createUser(@Param("login") String login,
                    @Param("birth_date") Date birthDate,
                    @Param("registration_date") Date registrationDate,
                    @Param("password") String password);

    /**
     * Checks if a user with the specified login already exists.
     *
     * @param login the email address of the user
     * @return true if the user exists, false otherwise
     */
    @Query("select exists(select * from _user where login = :login)")
    Boolean isUserExists(@Param("login") String login);

}
