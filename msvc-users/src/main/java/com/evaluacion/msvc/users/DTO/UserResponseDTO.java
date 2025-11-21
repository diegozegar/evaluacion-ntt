package com.evaluacion.msvc.users.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserResponseDTO {
    private UUID id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime creado;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modificado;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ultimoLogin;
    private String token;
    private boolean active;
    
}
