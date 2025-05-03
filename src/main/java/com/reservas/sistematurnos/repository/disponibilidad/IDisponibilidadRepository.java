package com.reservas.sistematurnos.repository.disponibilidad;

import com.reservas.sistematurnos.model.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {
}
