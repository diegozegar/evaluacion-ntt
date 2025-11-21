package com.evaluacion.msvc.users.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evaluacion.msvc.users.DTO.UserDTO;
import com.evaluacion.msvc.users.DTO.UserResponseDTO;
import com.evaluacion.msvc.users.entities.Phone;
import com.evaluacion.msvc.users.entities.User;
import com.evaluacion.msvc.users.exceptions.UserException;
import com.evaluacion.msvc.users.mapper.UserMapper;
import com.evaluacion.msvc.users.repositories.IUserRepository;
import com.evaluacion.msvc.users.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper mapper;

    @Value("${app.password.regex}")
    private String passwordRegex;

    private void validateEmailFormat(String email) {
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new UserException("Formato del correo no válido");
        }
    }

    private User validateUserIdIsPresent(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("Id usuario NO resgistrado"));
    }

    private User validateEmailExist(String email) {
        return userRepository.findByCorreo(email)
                .orElseThrow(() -> new UserException("Email NO resgistrado"));
    }

    private void validatePasswordFormat(String password) {
        if (!password.matches(passwordRegex)) {
            throw new UserException(
                    "La contraseña debe tener al menos un numero, una letra mayuscula o minuscula y al menos 6 caracteres");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllUsers() {
        return userMapper.toResponseAll(userRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findUserById(UUID id) {
        return userMapper.toResponse(validateUserIdIsPresent(id));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findUserByEmail(String email) {
        return userMapper.toResponse(validateEmailExist(email));
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserDTO userDto) {
        validateEmailFormat(userDto.getCorreo());
        validatePasswordFormat(userDto.getContraseña());

        User user = userMapper.toEntity(userDto);
        user.setContraseña(passwordEncoder.encode(userDto.getContraseña()));

        // generar token JWT
        String token = jwtUtil.generateToken(user.getCorreo());
        user.setToken(token);

        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateById(UUID id, UserDTO userDto) {

        User user = validateUserIdIsPresent(id);

        validateEmailFormat(userDto.getCorreo());
        user.setCorreo(userDto.getCorreo());

        user.setNombre(userDto.getNombre());
        user.setContraseña(passwordEncoder.encode(userDto.getContraseña()));

        user.getTelefonos().clear();

        if (userDto.getTelefonos() != null) {
            userDto.getTelefonos().forEach(phoneRecord -> {
                Phone phone = Phone.builder()
                        .numero(phoneRecord.getNumero())
                        .codigoCiudad(phoneRecord.getCodigoCiudad())
                        .codigoPais(phoneRecord.getCodigoPais())
                        .user(user)
                        .build();
                user.getTelefonos().add(phone);
            });
        }

        User userUpdated = userRepository.save(user);
        // Sincroniza con base de datos
        userRepository.flush();
        return userMapper.toResponse(userUpdated);
    }

    @Override
    @Transactional
    public UserResponseDTO updateByEmail(String email, UserDTO userDto) {

        User user = validateEmailExist(email);

        validateEmailFormat(userDto.getCorreo());
        user.setCorreo(userDto.getCorreo());

        user.setNombre(userDto.getNombre());
        user.setContraseña(passwordEncoder.encode(userDto.getContraseña()));

        user.getTelefonos().clear();

        if (userDto.getTelefonos() != null) {
            userDto.getTelefonos().forEach(phoneRecord -> {
                Phone phone = Phone.builder()
                        .id(null)
                        .numero(phoneRecord.getNumero())
                        .codigoCiudad(phoneRecord.getCodigoCiudad())
                        .codigoPais(phoneRecord.getCodigoPais())
                        .user(user)
                        .build();
                user.getTelefonos().add(phone);
            });
        }

        User userUpdated = userRepository.save(user);
        userRepository.flush();
        return userMapper.toResponse(userUpdated);
    }

    @Override
    @Transactional
    public void deleteUserById(UUID id) {

        validateUserIdIsPresent(id);

        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteUserByEmail(String email) {
        System.out.println("EMAIL RECIBIDO POR EL CONTROLLER = " + email);
        validateEmailExist(email);

        userRepository.deleteByCorreo(email);
    }

    @Override
    public UserResponseDTO partialUpdateById(UUID id, Map<String, Object> fields) {
        User user = validateUserIdIsPresent(id);
        User mapUser = mapper.convertValue(fields, User.class);

        // Copia SOLO los campos no nulos
        if (mapUser.getNombre() != null)
            user.setNombre(mapUser.getNombre());
        if (mapUser.getCorreo() != null)
            user.setCorreo(mapUser.getCorreo());
        if (mapUser.getContraseña() != null)
            user.setContraseña(mapUser.getContraseña());

        if (mapUser.getTelefonos() != null) {
            user.getTelefonos().clear();
            mapUser.getTelefonos().forEach(p -> p.setUser(user));
            user.getTelefonos().addAll(mapUser.getTelefonos());
        }

        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponseDTO partialUpdateByEmail(String email, Map<String, Object> fields) {
        User user = validateEmailExist(email);
        User mapUser = mapper.convertValue(fields, User.class);

        // Copia SOLO los campos no nulos
        if (mapUser.getNombre() != null)
            user.setNombre(mapUser.getNombre());
        if (mapUser.getCorreo() != null)
            user.setCorreo(mapUser.getCorreo());
        if (mapUser.getContraseña() != null)
            user.setContraseña(mapUser.getContraseña());

        if (mapUser.getTelefonos() != null) {
            user.getTelefonos().clear();
            mapUser.getTelefonos().forEach(p -> p.setUser(user));
            user.getTelefonos().addAll(mapUser.getTelefonos());
        }

        userRepository.save(user);
        return userMapper.toResponse(user);
    }

}
