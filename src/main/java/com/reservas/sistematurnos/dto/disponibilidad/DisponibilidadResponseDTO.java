package com.reservas.sistematurnos.dto.disponibilidad;

import com.reservas.sistematurnos.dto.profesional.ProfesionalResponseDTO;
import com.reservas.sistematurnos.model.Disponibilidad;
import com.reservas.sistematurnos.model.EstadoDisponibilidad;

import java.time.LocalDateTime;

public record DisponibilidadResponseDTO(
        Long id,
        LocalDateTime fechaHoraInicio,
        LocalDateTime fechaHoraFin,
        EstadoDisponibilidad estado,
        ProfesionalResponseDTO profesional
) {
    public DisponibilidadResponseDTO(Disponibilidad disponibilidad){
        this(
                disponibilidad.getId(),
                disponibilidad.getFechaHoraInicio(),
                disponibilidad.getFechaHoraFin(),
                disponibilidad.getEstado(),
                new ProfesionalResponseDTO(disponibilidad.getProfesional())
        );
    }
}
