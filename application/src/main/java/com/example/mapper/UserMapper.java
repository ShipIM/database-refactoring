package com.example.mapper;

import com.example.dto.authentication.AuthenticationRequest;
import com.example.dto.authentication.RegistrationRequest;
import com.example.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User mapToUser(AuthenticationRequest dto);

    User mapToUser(RegistrationRequest dto);

}
