package com.reservas.sistematurnos.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotNull(message = "El nombre es obligatorio")
    private String nombre;
    @NotNull(message = "El apellido es obligatorio")
    private String apellido;
    @Column(unique = true, nullable = false)
    @NotNull
    @Email(message = "El correo debe de ser valido")
    private String correo;
    @NotNull
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    @Column(unique = true, nullable = false)
    @NotNull
    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 dgitos")
    private String celular;
    @NotNull(message = "El valor de la hora debe de asignarse")
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
