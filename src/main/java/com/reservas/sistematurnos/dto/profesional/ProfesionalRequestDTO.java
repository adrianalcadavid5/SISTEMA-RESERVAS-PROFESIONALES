package com.reservas.sistematurnos.dto.profesional;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProfesionalRequestDTO(
        @NotBlank String nombre,
        @NotBlank String apellido,
        @NotBlank @Email String correo,
        @NotBlank @Size(min = 6) String password,
        @NotBlank @Pattern(regexp = "\\d{10}") String celular,
        @NotBlank Double costoHora
) {
}




