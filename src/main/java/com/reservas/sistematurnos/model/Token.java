package com.reservas.sistematurnos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "usuario_tokens")
public class Token {

    public enum TokenType{
        ACCESS,
    }
    @Id
    @GeneratedValue
    private Long id;
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType type = TokenType.ACCESS;
    public boolean revoked;
    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    public Usuario usuario;
}
