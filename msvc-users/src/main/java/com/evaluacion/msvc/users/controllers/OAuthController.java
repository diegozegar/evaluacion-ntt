package com.evaluacion.msvc.users.controllers;

import com.evaluacion.msvc.users.DTO.LoginDTO;
import com.evaluacion.msvc.users.entities.User;
import com.evaluacion.msvc.users.exceptions.UserException;
import com.evaluacion.msvc.users.repositories.IUserRepository;
import com.evaluacion.msvc.users.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/auth")

public class OAuthController {

    private final IUserRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public OAuthController(IUserRepository repository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginDTO request) {

        User user = repository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new UserException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getContraseña(), user.getContraseña())) {
            throw new UserException("Contraseña incorrecta");
        }

        String token = jwtUtil.generateToken(user.getCorreo());
        user.setToken(token);
        user.setUltimoLogin(LocalDateTime.now().withNano(0));
        repository.save(user);

        return Map.of(
                "id", user.getId(),
                "nombre", user.getNombre(),
                "correo", user.getCorreo(),
                "token", token);

    }
}
