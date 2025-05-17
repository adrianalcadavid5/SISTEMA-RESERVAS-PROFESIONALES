package com.reservas.sistematurnos.mapper;

import com.reservas.sistematurnos.dto.usuario.UsuarioRequestDTO;
import com.reservas.sistematurnos.dto.usuario.UsuarioResponseDTO;
import com.reservas.sistematurnos.model.Usuario;

public class UsuarioMapper {
    public static Usuario toEntity(UsuarioRequestDTO dto) {
        return Usuario.builder()
                .nombre(dto.nombre())
                .apellido(dto.apellido())
                .identificacion(dto.identificacion())
                .correo(dto.correo().trim().toLowerCase())
                .password(dto.password())
                .celular(dto.celular())
                .build();
    }
    public static UsuarioResponseDTO toDto(Usuario usuario) {
        return new UsuarioResponseDTO(usuario);
    }
}
