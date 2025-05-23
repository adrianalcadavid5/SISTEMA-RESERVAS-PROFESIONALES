package com.reservas.sistematurnos.repository.usuario;

import com.reservas.sistematurnos.model.Rol;
import com.reservas.sistematurnos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByRol(Rol rol);
}
