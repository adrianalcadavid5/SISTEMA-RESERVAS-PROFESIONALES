package com.reservas.sistematurnos.dto.disponibilidad;

import com.reservas.sistematurnos.model.EstadoDisponibilidad;

import java.time.LocalDateTime;

public record DisponibilidadRequestDTO(
        LocalDateTime fechaHoraInicio,
        LocalDateTime fechaHoraFin,
        EstadoDisponibilidad estado,
        Long profesionalId
) {
}


