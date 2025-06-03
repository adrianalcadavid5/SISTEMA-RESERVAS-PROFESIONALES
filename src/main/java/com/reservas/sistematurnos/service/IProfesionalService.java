package com.reservas.sistematurnos.service;

import com.reservas.sistematurnos.dto.profesional.ProfesionalResponseDTO;
import com.reservas.sistematurnos.model.Profesional;

import java.util.List;

public interface IProfesionalService extends IBaseService<Profesional, Long> {
    List<ProfesionalResponseDTO> buscarTodosDTO();
    ProfesionalResponseDTO buscarPorIdDTO(Long id);
    //Aqui se agregan métodos especificos de profesionales si es necesario.
}
