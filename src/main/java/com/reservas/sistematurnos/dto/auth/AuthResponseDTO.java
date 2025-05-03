package com.reservas.sistematurnos.dto.auth;

public record AuthResponseDTO(
    String correo,
    String nombre,
    String apellido,
    String rol,
    String token
) {

}
