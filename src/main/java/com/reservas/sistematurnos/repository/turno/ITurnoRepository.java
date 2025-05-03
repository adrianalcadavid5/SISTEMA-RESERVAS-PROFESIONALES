package com.reservas.sistematurnos.repository.turno;

import com.reservas.sistematurnos.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITurnoRepository extends JpaRepository<Turno, Long> {
}
