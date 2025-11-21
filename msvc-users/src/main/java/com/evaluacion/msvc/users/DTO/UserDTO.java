package com.evaluacion.msvc.users.DTO;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    // @Email
    private String correo;

    @NotBlank
    private String contraseña;

    private List<PhoneDTO> telefonos;
}
