package com.reservas.sistematurnos.dto.turno;

import com.reservas.sistematurnos.model.EstadoTurno;

public record TurnoModifyDTO(
        Long id,
        EstadoTurno estado,
        Long usuarioId,
        Long disponibilidadId
) {
}
