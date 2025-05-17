package com.reservas.sistematurnos.mapper;

import com.reservas.sistematurnos.dto.profesional.ProfesionalRequestDTO;
import com.reservas.sistematurnos.dto.profesional.ProfesionalResponseDTO;
import com.reservas.sistematurnos.model.Profesional;

public class ProfesionalMapper {
    public static Profesional toEntity(ProfesionalRequestDTO dto){
        return Profesional.builder()
                .nombre(dto.nombre())
                .apellido(dto.apellido())
                .correo(dto.correo().trim().toLowerCase())
                .password(dto.password())
                .celular(dto.celular())
                .costoHora(dto.costoHora())
                .build();
    }
    public static ProfesionalResponseDTO toDto(Profesional profesional){
        return new ProfesionalResponseDTO(profesional);
    }
}
