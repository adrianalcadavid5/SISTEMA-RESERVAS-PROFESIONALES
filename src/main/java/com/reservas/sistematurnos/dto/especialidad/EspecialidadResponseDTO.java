package com.reservas.sistematurnos.dto.especialidad;

import com.reservas.sistematurnos.dto.profesional.ProfesionalResponseDTO;
import com.reservas.sistematurnos.model.Especialidad;
import java.util.List;

public record EspecialidadResponseDTO(
        Long id,
        String nombre,
        List<ProfesionalResponseDTO> profesionales
) {
    public EspecialidadResponseDTO (Especialidad especialidad){
        this(
                especialidad.getId(),
                especialidad.getNombre(),
                especialidad.getProfesionales()
                        .stream()
                        .map(ProfesionalResponseDTO::new)
                        .toList()
        );

    }
}
