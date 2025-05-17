package com.reservas.sistematurnos.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "El nombre es obligatorio")
    private String nombre;
    @NotNull(message = "El apellido es obligatorio")
    private String apellido;
    @NotNull(message = "El documento es obligatorio")
    @Size(min = 6)
    private String identificacion;
    @NotNull
    @Email(message = "El correo debe de ser valido")
    @Column(unique = true, nullable = false)
    private String correo;
    @NotNull
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    @NotNull
    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 dgitos")
    private String celular;
    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;
    @OneToMany(mappedBy = "usuario")
    private List<Turno> turnos = new ArrayList<>();
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonBackReference
    private Profesional profesional;
}
