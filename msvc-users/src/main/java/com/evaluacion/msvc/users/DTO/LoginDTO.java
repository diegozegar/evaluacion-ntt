package com.evaluacion.msvc.users.DTO;

import lombok.Data;

@Data
public class LoginDTO {
    private String correo;
    private String contraseña;
}