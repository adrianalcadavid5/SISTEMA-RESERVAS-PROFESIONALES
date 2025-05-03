package com.reservas.sistematurnos.dto.usuario;

public record UsuarioModifyDTO(
        String nombre,
        String apellido,
        String identificacion,
        String correo,
        String password,
        String celular,
        String rol

) {
}
