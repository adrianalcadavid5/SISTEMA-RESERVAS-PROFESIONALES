package com.reservas.sistematurnos.dto.profesional;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ProfesionalRequestDTO(
        String nombre,
        String apellido,
        @NotBlank
        @Email
        String correo,
        String password,
        String celular
) {
}




