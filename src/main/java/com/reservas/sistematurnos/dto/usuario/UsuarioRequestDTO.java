package com.reservas.sistematurnos.dto.usuario;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank String nombre,
        @NotBlank String apellido,
        @NotBlank @Size(min = 6) String identificacion,
        @NotBlank @Email String correo,
        @NotBlank @Size(min = 6) String password,
        @NotBlank @Pattern(regexp = "\\d{10}") String celular
) {
}



