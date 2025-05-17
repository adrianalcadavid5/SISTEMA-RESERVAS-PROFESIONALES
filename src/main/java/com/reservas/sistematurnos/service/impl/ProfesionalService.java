package com.reservas.sistematurnos.service.impl;

import com.reservas.sistematurnos.dto.profesional.ProfesionalModifyDTO;
import com.reservas.sistematurnos.dto.profesional.ProfesionalRequestDTO;
import com.reservas.sistematurnos.dto.profesional.ProfesionalResponseDTO;
import com.reservas.sistematurnos.exception.ResourceNotFoundException;
import com.reservas.sistematurnos.mapper.ProfesionalMapper;
import com.reservas.sistematurnos.model.Profesional;
import com.reservas.sistematurnos.repository.profesional.IProfesionalRepository;
import com.reservas.sistematurnos.service.IProfesionalService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class ProfesionalService extends BaseServiceImpl<Profesional, Long> implements IProfesionalService {
    private final IProfesionalRepository profesionalRepository;
    private final Logger logger = LoggerFactory.getLogger(ProfesionalService.class);

    public ProfesionalService(IProfesionalRepository profesionalRepository) {
        super(profesionalRepository);
        this.profesionalRepository = profesionalRepository;
    }

    // ======================= MAPEAR PARA LOS DTOS ========================

    // ======================== GUARDAR DESDE DTO ========================
    @Transactional
    public ProfesionalResponseDTO guardarDesdeDTO(ProfesionalRequestDTO dto){
        validarCorreoUnico(dto.correo());
        Profesional profesional = ProfesionalMapper.toEntity(dto);
        Profesional guardado = profesionalRepository.save(profesional);
        return ProfesionalMapper.toDto(guardado);
    }
    // ======================== MODIFICAR DESDE DTO ========================
    @Transactional
    public ProfesionalResponseDTO modificarDesdeDTO(Long id, ProfesionalModifyDTO dto){
        Profesional profesionalExistente = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con id: " + id));

        validarCorreoUnicoParaModificacion(dto.correo(), id);

        //actualizo manualmente los campos permitidos
        profesionalExistente.setNombre(dto.nombre());
        profesionalExistente.setApellido(dto.apellido());
        profesionalExistente.setCorreo(dto.correo().trim().toLowerCase());
        profesionalExistente.setPassword(dto.password());
        profesionalExistente.setCelular(dto.celular());
        profesionalExistente.setCostoHora(dto.costoHora());

        Profesional actualizado = profesionalRepository.save(profesionalExistente);
        return ProfesionalMapper.toDto(actualizado);
    }

    // ======================== VALIDACIONES REUTILIZANDO BaseValidationsService ========================

    private void validarCorreoUnico(String correo){
        new BaseValidationsService<Profesional>() {}.validarCorreoUnico(
                correo,
                profesionalRepository::findByCorreo
        );
    }

    private void validarCorreoUnicoParaModificacion(String correo, Long id){
        new BaseValidationsService<Profesional>(){}.validarCorreoUnicoParaModificacion(
                correo,
                id,
                profesionalRepository::findByCorreo,
                Profesional::getId
        );
    }
}
