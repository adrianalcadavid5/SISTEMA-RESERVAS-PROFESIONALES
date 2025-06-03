package com.reservas.sistematurnos.service.impl;

import com.reservas.sistematurnos.dto.profesional.ProfesionalModifyDTO;
import com.reservas.sistematurnos.dto.profesional.ProfesionalRequestDTO;
import com.reservas.sistematurnos.dto.profesional.ProfesionalResponseDTO;
import com.reservas.sistematurnos.exception.ResourceNotFoundException;
import com.reservas.sistematurnos.mapper.ProfesionalMapper;
import com.reservas.sistematurnos.model.Profesional;
import com.reservas.sistematurnos.repository.profesional.IProfesionalRepository;
import com.reservas.sistematurnos.service.IProfesionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class ProfesionalService extends BaseServiceImpl<Profesional, Long> implements IProfesionalService {
    private final IProfesionalRepository profesionalRepository;
    private final Logger logger = LoggerFactory.getLogger(ProfesionalService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ProfesionalService(IProfesionalRepository profesionalRepository) {
        super(profesionalRepository);
        this.profesionalRepository = profesionalRepository;
    }

    // ======================== GUARDAR DESDE DTO ========================
    @Transactional
    public ProfesionalResponseDTO guardarDesdeDTO(ProfesionalRequestDTO dto){
        validarCorreoUnico(dto.correo());
        Profesional profesional = ProfesionalMapper.toEntity(dto);
        profesional.setPassword(passwordEncoder.encode(dto.password())); // Encriptar
        Profesional guardado = profesionalRepository.save(profesional);
        return ProfesionalMapper.toDto(guardado);
    }
    // ======================== MODIFICAR DESDE DTO ========================
    @Transactional
    public ProfesionalResponseDTO modificarDesdeDTO(Long id, ProfesionalModifyDTO dto){
        Profesional profesionalExistente = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con id: " + id));

        validarCorreoUnicoParaModificacion(dto.correo(), id);

        if (dto.password() != null && !dto.password().isBlank()) {
            profesionalExistente.setPassword(passwordEncoder.encode(dto.password()));
        }
        // Usar métodos de actualización condicional
        actualizarCampoSiNoEsNulo(dto.nombre(), profesionalExistente::setNombre);
        actualizarCampoSiNoEsNulo(dto.correo(), value ->
                profesionalExistente.setCorreo(normalizarCorreo(value)));

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

    // ======================== CONSULTAS A LA BASE ========================

    @Transactional(readOnly = true)
    public List<ProfesionalResponseDTO> buscarTodosDTO(){
        List<Profesional> profesionales = profesionalRepository.findAll();
        return profesionales.stream()
                .map(this::mapearAProfesionalResponse)
                .toList();

    }
    @Override
    @Transactional(readOnly = true)
    public ProfesionalResponseDTO buscarPorIdDTO(Long id){
        return profesionalRepository.findById(id)
                .map(this::mapearAProfesionalResponse) // Mapeo dentro del Optional
                .orElseThrow(() ->
                        new ResourceNotFoundException(("Profesional no encontrado con el ID: " + id))
                );
    }

    private ProfesionalResponseDTO mapearAProfesionalResponse(Profesional profesional){
        return new ProfesionalResponseDTO(
                profesional.getId(),
                profesional.getNombre(),
                profesional.getApellido(),
                profesional.getCorreo(),
                profesional.getCelular(),
                profesional.getCostoHora()
        );
    }
    private <T> void actualizarCampoSiNoEsNulo(T nuevoValor, Consumer<T> setter) {
        if (nuevoValor != null) {
            setter.accept(nuevoValor);
        }
    }
    // ======================== VALIDACIONES REUTILIZANDO BaseValidationsService ========================

    public void validarCorreoUnico(String correo) {
        BaseValidationsService.ValidationUtils.validarCorreoUnico(
                correo,
                profesionalRepository::findByCorreo
        );
    }
    public void validarCorreoUnicoParaModificacion(String correo, Long id) {
        BaseValidationsService.ValidationUtils.validarCorreoUnicoParaModificacion(
                correo,
                id,
                profesionalRepository::findByCorreo,
                Profesional::getId
        );
    }
    public String normalizarCorreo(String correo) {
        return BaseValidationsService.ValidationUtils.normalizarCorreo(correo); //← Delegación clara
    }

}
