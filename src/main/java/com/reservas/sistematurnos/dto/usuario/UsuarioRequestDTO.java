package com.reservas.sistematurnos.dto.usuario;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRequestDTO(
        String nombre,
        String apellido,
        String identificacion,
        @NotBlank
        @Email
        String correo,
        String password,
        String celular
) {
}



