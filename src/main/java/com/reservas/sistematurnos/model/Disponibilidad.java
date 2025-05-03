package com.reservas.sistematurnos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "disponibilidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Disponibilidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDateTime fechaHoraInicio;
    @NotNull
    private LocalDateTime fechaHoraFin;
    @Enumerated(EnumType.STRING)
    private EstadoDisponibilidad estado;
    @ManyToOne
    @JoinColumn(name = "profesional_id")
    private Profesional profesional;
    @OneToMany(mappedBy = "disponibilidad")
    private List<Turno> turnos;
}
