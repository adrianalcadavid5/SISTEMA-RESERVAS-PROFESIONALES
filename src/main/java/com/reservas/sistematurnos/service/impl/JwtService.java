package com.reservas.sistematurnos.service.impl;

import com.reservas.sistematurnos.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String extractUsername(final String token) {
        final Claims jwtToken = Jwts.parserBuilder()
                .setSigningKey(getSecretKey()) // método correcto para definir la clave
                .build()
                .parseClaimsJws(token)
                .getBody();

        return jwtToken.getSubject();
    }

    public String generateToken(final Usuario usuario) {
        return generateToken(usuario, expiration);
    }

    private String generateToken(final Usuario usuario, long expiration) {
        return Jwts.builder()
                .setId(usuario.getId().toString())
                .claim("name", usuario.getNombre())
                .setSubject(usuario.getCorreo())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey())
                .compact();
    }

    public boolean isTokenValid(final String token, final Usuario usuario) {
        final String username = extractUsername(token);
        return (username.equals(usuario.getCorreo()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey()) // Usa tu clave secreta
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    private SecretKey getSecretKey() {
        byte[] secretBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }
}
