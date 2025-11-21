package com.evaluacion.msvc.users.services;

import com.evaluacion.msvc.users.DTO.UserDTO;
import com.evaluacion.msvc.users.DTO.UserResponseDTO;
import com.evaluacion.msvc.users.entities.User;
import com.evaluacion.msvc.users.mapper.UserMapper;
import com.evaluacion.msvc.users.repositories.IUserRepository;
import com.evaluacion.msvc.users.security.JwtUtil;
import com.evaluacion.msvc.users.services.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Mock private IUserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks private UserServiceImpl userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        // inyectar ObjectMapper en la instancia
        ReflectionTestUtils.setField(userService, "mapper", objectMapper);
        // aceptar cualquier password en validación
        ReflectionTestUtils.setField(userService, "passwordRegex", ".*");
    }

    @Test
    void createUser_validDto_savesUserAndReturnsResponse() {
        UserDTO dto = new UserDTO();
        dto.setNombre("Diego");
        dto.setCorreo("diego@test.com");
        dto.setContraseña("hunter2");

        User userEntity = new User();
        userEntity.setCorreo(dto.getCorreo());
        userEntity.setNombre(dto.getNombre());

        when(userMapper.toEntity(dto)).thenReturn(userEntity);
        when(passwordEncoder.encode(dto.getContraseña())).thenReturn("hunter2");
        when(jwtUtil.generateToken(dto.getCorreo())).thenReturn("tokentest");
        when(userRepository.save(any(User.class))).thenReturn(userEntity);
        when(userMapper.toResponse(userEntity)).thenReturn(new UserResponseDTO());

        UserResponseDTO resp = userService.createUser(dto);

        assertNotNull(resp);
        verify(passwordEncoder).encode(dto.getContraseña());
        verify(jwtUtil).generateToken(dto.getCorreo());
        verify(userRepository).save(userEntity);
        verify(userMapper).toResponse(userEntity);
    }

    @Test
    void partialUpdateByEmail_updatesPhonesAndFields() {
        String email = "diego@test.com";
        User existing = new User();
        existing.setCorreo(email);
        existing.setNombre("Diego");

        Map<String,Object> fields = Map.of(
                "nombre", "Diego Zegarra",
                "telefonos", List.of(Map.of("numero","111","codigoCiudad","1","codigoPais","11"))
        );

        when(userRepository.findByCorreo(email)).thenReturn(Optional.of(existing));
        // ObjectMapper convertValue will map map->User (uses the injected mapper)

        UserResponseDTO outDto = new UserResponseDTO();
        when(userMapper.toResponse(any(User.class))).thenReturn(outDto);

        UserResponseDTO resp = userService.partialUpdateByEmail(email, fields);

        assertNotNull(resp);
        assertEquals("Diego Zegarra", existing.getNombre());
        assertEquals(1, existing.getTelefonos().size());
        verify(userRepository).save(existing);
    }
}
