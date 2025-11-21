package com.evaluacion.msvc.users.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.evaluacion.msvc.users.DTO.UserDTO;
import com.evaluacion.msvc.users.DTO.UserResponseDTO;
import com.evaluacion.msvc.users.entities.Phone;
import com.evaluacion.msvc.users.entities.User;

@Component
public class UserMapper {
    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setNombre(dto.getNombre());
        user.setCorreo(dto.getCorreo());
        // user.setPassword(dto.getPassword());
        user.setActive(true);

        if (dto.getTelefonos() != null) {
            user.getTelefonos().clear();
            user.setTelefonos(
                    dto.getTelefonos()
                            .stream()
                            .map(phoneDto -> {
                                Phone p = new Phone();
                                p.setNumero(phoneDto.getNumero());
                                p.setCodigoCiudad(phoneDto.getCodigoCiudad());
                                p.setCodigoPais(phoneDto.getCodigoPais());
                                p.setUser(user); // MUY IMPORTANTE
                                return p;
                            })
                            .collect(Collectors.toList()));
        }

        return user;
    }

    public UserResponseDTO toResponse(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setCreado(user.getCreado());
        dto.setModificado(user.getModificado());
        dto.setUltimoLogin(user.getUltimoLogin());
        dto.setActive(user.isActive());
        dto.setToken(user.getToken());
        return dto;
    }

    public List<UserResponseDTO> toResponseAll(List<User> userList) {
        List<UserResponseDTO> userDTOList = new ArrayList<>();
        userList.forEach((user) -> {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(user.getId());
            // dto.setName(user.getName());
            // dto.setEmail(user.getEmail());
            dto.setCreado(user.getCreado());
            dto.setModificado(user.getModificado());
            dto.setUltimoLogin(user.getUltimoLogin());
            dto.setActive(user.isActive());
            dto.setToken(user.getToken());
            userDTOList.add(dto);
        });

        return userDTOList;
    }
}
