package com.reservas.sistematurnos.repository.usuario;

import com.reservas.sistematurnos.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface ITokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllByUsuario_IdAndRevokedFalseAndExpiredFalse(Long id);

    Optional<Token> findByToken(String jwtToken);
}
