package com.reservas.sistematurnos.dto.turno;
import com.reservas.sistematurnos.model.EstadoTurno;

public record TurnoRequestDTO(
        EstadoTurno estado,
        Long usuarioId,
        Long disponibilidadId
) {
}


