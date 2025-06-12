package com.reservas.sistematurnos.service.impl;

import com.reservas.sistematurnos.dto.PageDTO.PageResponseDTO;
import com.reservas.sistematurnos.dto.usuario.UsuarioModifyDTO;
import com.reservas.sistematurnos.dto.usuario.UsuarioRequestDTO;
import com.reservas.sistematurnos.dto.usuario.UsuarioResponseDTO;
import com.reservas.sistematurnos.exception.BadRequestException;
import com.reservas.sistematurnos.exception.ResourceNotFoundException;
import com.reservas.sistematurnos.mapper.UsuarioMapper;
import com.reservas.sistematurnos.model.Rol;
import com.reservas.sistematurnos.model.Usuario;
import com.reservas.sistematurnos.model.UsuarioRol;
import com.reservas.sistematurnos.repository.usuario.IRolUsuarioRepository;
import com.reservas.sistematurnos.repository.usuario.IUsuarioRepository;
import com.reservas.sistematurnos.service.IUsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


@Service
public class UsuarioService extends BaseServiceImpl  <Usuario, Long> implements IUsuarioService {
    private final IUsuarioRepository usuarioRepository;
    private final IRolUsuarioRepository rolUsuarioRepository;
    private final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioService(IUsuarioRepository usuarioRepository, IRolUsuarioRepository rolUsuarioRepository) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
    }

    // ======================== GUARDAR DESDE DTO ========================

    @Transactional
    public UsuarioResponseDTO guardarDesdeDTO(UsuarioRequestDTO dto){
        validarCorreoUnico(dto.correo());
        Usuario usuario = UsuarioMapper.toEntity(dto);
        usuario.setPassword(passwordEncoder.encode(dto.password()));  // ← Encriptar
        Usuario guardado = usuarioRepository.save(usuario);
        return UsuarioMapper.toDto(guardado);
    }
    // ======================== MODIFICAR DESDE DTO ========================
    @Transactional
    public UsuarioResponseDTO modificarDesdeDTO(Long id, UsuarioModifyDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        validarCorreoUnicoParaModificacion(dto.correo(), id);

        actualizarCampoSiNoEsNulo(dto.nombre(), usuarioExistente::setNombre);
        actualizarCampoSiNoEsNulo(dto.apellido(), usuarioExistente::setApellido);
        actualizarCampoSiNoEsNulo(dto.identificacion(), usuarioExistente::setIdentificacion);
        actualizarCampoSiNoEsNulo(dto.correo(), value ->
                usuarioExistente.setCorreo(normalizarCorreo(value)));
        actualizarCampoSiNoEsNulo(dto.celular(), usuarioExistente::setCelular);

        // Solo encriptar si se mandó una contraseña nueva
        if (dto.password() != null && !dto.password().isBlank()) {
            usuarioExistente.setPassword(passwordEncoder.encode(dto.password()));
        }

        if (dto.rol() != null) {
            Rol rol = rolUsuarioRepository.findByUsuarioRol(UsuarioRol.valueOf(dto.rol()))
                    .orElseThrow(() -> new BadRequestException("Rol no válido: " + dto.rol()));
            usuarioExistente.setRol(rol);
        }

        Usuario actualizado = usuarioRepository.save(usuarioExistente);
        return UsuarioMapper.toDto(actualizado);
    }
    // ======================== CONSULTAS A LA BASE ========================//
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> buscarTodosDTO() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());
    }
    public PageResponseDTO<UsuarioResponseDTO> listarPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        Page<UsuarioResponseDTO> dtoPage = usuarios.map(UsuarioResponseDTO::new);
        return new PageResponseDTO<>(dtoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorIdDTO(Long id) {
        return usuarioRepository.findById(id)
                .map(this::mapearAUsuarioResponse) // ← Mapeo dentro del Optional
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado con ID: " + id)
                );
    }

    private UsuarioResponseDTO mapearAUsuarioResponse(Usuario usuario){
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getIdentificacion(),
                usuario.getCorreo(),
                usuario.getCelular(),
                usuario.getRol().getUsuarioRol().name()

        );

    }
    //lo nuevo
    private <T> void actualizarCampoSiNoEsNulo(T nuevoValor, Consumer<T> setter) {
        if (nuevoValor != null) {
            setter.accept(nuevoValor);
        }
    }

    // ======================== VALIDACIONES REUTILIZANDO BaseValidationsService ========================

    public void validarCorreoUnico(String correo) {
        BaseValidationsService.ValidationUtils.validarCorreoUnico(
                correo,
                usuarioRepository::findByCorreo
        );
    }
    public void validarCorreoUnicoParaModificacion(String correo, Long id) {
       BaseValidationsService.ValidationUtils.validarCorreoUnicoParaModificacion(
               correo,
               id,
               usuarioRepository::findByCorreo,
               Usuario::getId
       );
    }
    public String normalizarCorreo(String correo) {
        return BaseValidationsService.ValidationUtils.normalizarCorreo(correo); //← Delegación clara
    }

}
//aca puedo agregar validaciones especificar si las necesito, por ejemplo, en guardar o modificar