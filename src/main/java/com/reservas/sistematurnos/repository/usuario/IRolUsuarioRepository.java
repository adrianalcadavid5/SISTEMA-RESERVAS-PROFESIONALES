package com.reservas.sistematurnos.repository.usuario;

import com.reservas.sistematurnos.model.Rol;
import com.reservas.sistematurnos.model.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRolUsuarioRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByUsuarioRol(UsuarioRol usuarioRol);
}
