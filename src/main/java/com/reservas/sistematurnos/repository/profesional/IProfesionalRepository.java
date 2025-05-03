package com.reservas.sistematurnos.repository.profesional;

import com.reservas.sistematurnos.model.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProfesionalRepository extends JpaRepository<Profesional, Long> {
    Optional<Profesional> findByCorreo(String correo);
}
