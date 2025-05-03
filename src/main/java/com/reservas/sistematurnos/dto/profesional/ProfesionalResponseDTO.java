package com.reservas.sistematurnos.dto.profesional;

import com.reservas.sistematurnos.model.Profesional;

public record ProfesionalResponseDTO(
        Long id,
        String nombre,
        String apellido,
        String correo,
        String celular
    ) {
    public ProfesionalResponseDTO(Profesional profesional){
        this(profesional.getId(), profesional.getNombre(), profesional.getApellido(), profesional.getCorreo(), profesional.getCelular());
    }
}
