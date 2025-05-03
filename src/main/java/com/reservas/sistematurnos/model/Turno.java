package com.reservas.sistematurnos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "turnos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private EstadoTurno estado;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "disponibilidad_id")
    private Disponibilidad disponibilidad;


}

//aun no he creado los controladores ni el servicio.... debo de crear dtos para todas las entidades faltantes como Disponiblidad, Especialdad, Rol ?
