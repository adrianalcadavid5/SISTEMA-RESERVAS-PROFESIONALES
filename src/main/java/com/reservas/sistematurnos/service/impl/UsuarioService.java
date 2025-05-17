package com.reservas.sistematurnos.service.impl;

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
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService extends BaseServiceImpl<Usuario, Long> implements IUsuarioService {
    private final IUsuarioRepository usuarioRepository;
    private final IRolUsuarioRepository rolUsuarioRepository;
    private final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

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
        Usuario guardado = usuarioRepository.save(usuario);
        return UsuarioMapper.toDto(guardado);
    }
    // ======================== MODIFICAR DESDE DTO ========================
    @Transactional
    public UsuarioResponseDTO modificarDesdeDTO(Long id, UsuarioModifyDTO dto){
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        validarCorreoUnicoParaModificacion(dto.correo(), id);

        //actualizo manualmente los campos permitidos
        usuarioExistente.setNombre(dto.nombre());
        usuarioExistente.setApellido(dto.apellido());
        usuarioExistente.setIdentificacion(dto.identificacion());
        usuarioExistente.setCorreo(dto.correo().trim().toLowerCase());
        usuarioExistente.setPassword(dto.password());
        usuarioExistente.setCelular(dto.celular());

        // Si estoy manejando rol como String, lo puedo buscar en el repositorio de roles
        if (dto.rol() != null) {
            Rol rol = rolUsuarioRepository.findByUsuarioRol(UsuarioRol.valueOf(dto.rol()))
                    .orElseThrow(() -> new BadRequestException("Rol no válido: " + dto.rol()));
            usuarioExistente.setRol(rol);
        }

        Usuario actualizado = usuarioRepository.save(usuarioExistente);
        return UsuarioMapper.toDto(actualizado);
    }
    // ======================== VALIDACIONES REUTILIZANDO BaseValidationsService ========================

    public void validarCorreoUnico(String correo){
       new BaseValidationsService<Usuario>(){}.validarCorreoUnico(
               correo,
               usuarioRepository::findByCorreo
       );
    }
    private void validarCorreoUnicoParaModificacion(String correo, Long id) {
       new BaseValidationsService<Usuario>(){}.validarCorreoUnicoParaModificacion(
               correo,
               id,
               usuarioRepository::findByCorreo,
               Usuario::getId
       );
    }
}
//aca puedo agregar validaciones especificar si las necesito, por ejemplo, en guardar o modificar