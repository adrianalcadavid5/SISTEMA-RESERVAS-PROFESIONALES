package com.reservas.sistematurnos.dto.rol;

import com.reservas.sistematurnos.model.Rol;
import com.reservas.sistematurnos.model.UsuarioRol;

public record RolResponseDTO(
        Long id,
        UsuarioRol usuarioRol
) {
    public RolResponseDTO(Rol rol) {
        this(rol.getId(), rol.getUsuarioRol());
    }

}
