package com.reservas.sistematurnos.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profesionales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profesional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nombre;
    @NotNull
    private String apellido;
    @Column(unique = true, nullable = false)
    @NotNull
    @Email
    private String correo;
    @NotNull
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    @Column(unique = true, nullable = false)
    @NotNull
    @Size(min = 10, max = 15)
    private String celular;
    @NotNull
    private Double costoHora;
    @NotNull(message = "La especialidad no puede ser nula")
    @ManyToOne
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;
    @JsonIgnore
    @OneToMany(mappedBy = "profesional")
    private List<Disponibilidad> disponibilidades = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "usuario_id")
    @JsonManagedReference
    private Usuario usuario;
}
