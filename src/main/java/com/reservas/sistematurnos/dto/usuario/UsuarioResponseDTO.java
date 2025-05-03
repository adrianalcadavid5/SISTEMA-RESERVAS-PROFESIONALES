package com.reservas.sistematurnos.dto.usuario;

import com.reservas.sistematurnos.model.Rol;
import com.reservas.sistematurnos.model.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String nombre,
        String apellido,
        String identificacion,
        String correo,
        String celular,
        String rol
) {
    public UsuarioResponseDTO(Usuario usuario){
        this(usuario.getId(), usuario.getNombre(), usuario.getApellido(), usuario.getIdentificacion(), usuario.getCorreo(), usuario.getCelular(), usuario.getRol() != null ? usuario.getRol().getUsuarioRol().name() : null);
    }
}
