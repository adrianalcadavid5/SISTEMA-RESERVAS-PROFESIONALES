package com.reservas.sistematurnos.security.config;

import com.reservas.sistematurnos.model.Token;
import com.reservas.sistematurnos.model.Usuario;
import com.reservas.sistematurnos.repository.usuario.ITokenRepository;
import com.reservas.sistematurnos.repository.usuario.IUsuarioRepository;
import com.reservas.sistematurnos.service.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final ITokenRepository tokenRepository;
    private final IUsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        logger.info(String.format("📌 Nueva petición a: %s", request.getServletPath()));

        String path = request.getServletPath();
        if (path.startsWith("/auth") || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.equals("/swagger-ui.html") || path.equals("/usuarios")) {
            logger.info("🔹 Ruta pública, omitiendo filtro.");
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("❌ No se encontró un token válido en el Header.");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authHeader.substring(7);
        logger.info(String.format("🔹 Token extraído: %s", jwtToken));

        final String userEmail = jwtService.extractUsername(jwtToken);
        logger.info(String.format("🔹 Usuario extraído del token: %s", userEmail));

        if (userEmail == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            logger.warn("❌ Usuario no encontrado o ya autenticado.");
            filterChain.doFilter(request, response);
            return;
        }

        final Token token = tokenRepository.findByToken(jwtToken).orElse(null);
        if (token == null) {
            logger.warn("❌ El token no está registrado en la base de datos.");
            filterChain.doFilter(request, response);
            return;
        }

        if (token.isExpired() || token.isRevoked()) {
            logger.warn("❌ El token está expirado o ha sido revocado.");
            filterChain.doFilter(request, response);
            return;
        }

        final Optional<Usuario> userOptional = usuarioRepository.findByCorreo(userEmail);
        if (userOptional.isEmpty()) {
            logger.warn("❌ El usuario no existe en la base de datos.");
            filterChain.doFilter(request, response);
            return;
        }

        final Usuario user = userOptional.get();

        final boolean isTokenValid = jwtService.isTokenValid(jwtToken, user);
        logger.info(String.format("🔹 ¿Token válido?: %s", isTokenValid));

        if (!isTokenValid) {
            logger.warn("❌ Token inválido.");
            filterChain.doFilter(request, response);
            return;
        }

        // El usuario tiene un único rol, lo empaquetamos en una lista
        List<GrantedAuthority> authorities = List.of(user.getRol());

        final var authToken = new UsernamePasswordAuthenticationToken(
                user, // Se puede poner el propio Usuario como principal
                null,
                authorities
        );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        logger.info(String.format("✅ Usuario autenticado correctamente: %s", userEmail));

        filterChain.doFilter(request, response);
    }
}
