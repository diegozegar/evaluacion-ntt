package com.evaluacion.msvc.users.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PhoneDTO {
    @NotBlank
    @Pattern(regexp = "^[0-9]{6,15}$")
    private String numero;

    @NotBlank
    @Pattern(regexp = "^[0-9]{1,5}$")
    private String codigoCiudad;

    @NotBlank
    @Pattern(regexp = "^[0-9]{1,5}$")
    private String codigoPais;
}
