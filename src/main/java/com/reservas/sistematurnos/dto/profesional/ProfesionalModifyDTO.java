package com.reservas.sistematurnos.dto.profesional;

public record ProfesionalModifyDTO(
        String nombre,
        String apellido,
        String correo,
        String password,
        String celular,
        Double costoHora
) {
}
