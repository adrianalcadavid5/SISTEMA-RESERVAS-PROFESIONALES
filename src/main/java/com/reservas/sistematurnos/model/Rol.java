package com.reservas.sistematurnos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios_roles")
public class Rol implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private UsuarioRol usuarioRol;

    public UsuarioRol getUsuarioRol() {
        return usuarioRol;
    }

    public Rol(UsuarioRol usuarioRol) {
        this.usuarioRol = usuarioRol;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + usuarioRol.name();
    }
}
