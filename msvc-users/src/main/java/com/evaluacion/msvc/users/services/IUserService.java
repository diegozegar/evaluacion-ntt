package com.evaluacion.msvc.users.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.evaluacion.msvc.users.DTO.UserDTO;
import com.evaluacion.msvc.users.DTO.UserResponseDTO;

public interface IUserService {

    List<UserResponseDTO> findAllUsers();

    UserResponseDTO findUserById(UUID id);
    UserResponseDTO findUserByEmail(String email);

    UserResponseDTO createUser(UserDTO userDto);

    UserResponseDTO updateById(UUID id, UserDTO userDto);
    UserResponseDTO updateByEmail(String email, UserDTO userDto);

    void deleteUserById(UUID id);
    void deleteUserByEmail(String email);

    UserResponseDTO partialUpdateById(UUID id, Map<String,Object> fields);
    UserResponseDTO partialUpdateByEmail(String email, Map<String,Object> fields);
}
