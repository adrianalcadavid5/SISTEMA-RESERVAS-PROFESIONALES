package com.reservas.sistematurnos.dto.turno;

import com.reservas.sistematurnos.dto.disponibilidad.DisponibilidadResponseDTO;
import com.reservas.sistematurnos.dto.usuario.UsuarioResponseDTO;
import com.reservas.sistematurnos.model.EstadoTurno;
import com.reservas.sistematurnos.model.Turno;

public record TurnoResponseDTO(
        Long id,
        EstadoTurno estado,
        UsuarioResponseDTO usuario,
        DisponibilidadResponseDTO disponibilidad
) {
    public TurnoResponseDTO(Turno turno) {
        this(
                turno.getId(),
                turno.getEstado(),
                new UsuarioResponseDTO(turno.getUsuario()),
                new DisponibilidadResponseDTO(turno.getDisponibilidad())
        );
    }
}

