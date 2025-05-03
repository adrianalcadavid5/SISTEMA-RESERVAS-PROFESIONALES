package com.reservas.sistematurnos.dto.disponibilidad;

import com.reservas.sistematurnos.dto.profesional.ProfesionalResponseDTO;
import com.reservas.sistematurnos.model.EstadoDisponibilidad;

import java.time.LocalDateTime;

public record DisponibilidadModifyDTO(
        Long id,
        LocalDateTime fechaHoraInicio,
        LocalDateTime fechaHoraFin,
        EstadoDisponibilidad estado,
        Long profesionalId
) {
}
