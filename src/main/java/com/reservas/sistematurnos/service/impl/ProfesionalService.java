package com.reservas.sistematurnos.service.impl;

import com.reservas.sistematurnos.exception.BadRequestException;
import com.reservas.sistematurnos.model.Profesional;
import com.reservas.sistematurnos.repository.profesional.IProfesionalRepository;
import com.reservas.sistematurnos.service.IProfesionalService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfesionalService extends BaseServiceImpl<Profesional, Long> implements IProfesionalService {
    private final IProfesionalRepository profesionalRepository;
    private final Logger logger = LoggerFactory.getLogger(ProfesionalService.class);

    public ProfesionalService(IProfesionalRepository profesionalRepository) {
        super(profesionalRepository);
        this.profesionalRepository = profesionalRepository;
    }
    @Transactional
    @Override
    public Profesional guardar(Profesional profesional) {
        validarCorreoUnico(profesional.getCorreo());
        return super.guardar(profesional);
    }
    @Transactional
    @Override
    public Profesional modificar(Profesional profesional) {
        validarCorreoUnicoParaModificacion(profesional);
        return super.modificar(profesional);
    }
    private void validarCorreoUnico(String correo){
        if (correo == null || correo.trim().isEmpty()) {
            throw new BadRequestException("El correo electrónico es obligatorio.");
        }

        Optional<Profesional> profesionalExistente = profesionalRepository.findByCorreo(correo.trim().toLowerCase());
        if (profesionalExistente.isPresent()){
            logger.info("Intento de registrar un correo ya existente: {}", correo);
            throw new BadRequestException("El correo electrónico ya está registrado.");
        }
    }
    private void validarCorreoUnicoParaModificacion(Profesional profesional){
        if (profesional.getCorreo() == null || profesional.getCorreo().trim().isEmpty()) {
            throw new BadRequestException("El correo electrónico no puede estar vacío.");
        }

        Optional<Profesional> existente = profesionalRepository.findByCorreo(profesional.getCorreo().trim().toLowerCase());
        if (existente.isPresent() && !existente.get().getId().equals(profesional.getId())){
            throw new BadRequestException("Ya existe otro profesional con ese correo electrónico.");
        }
    }
}
