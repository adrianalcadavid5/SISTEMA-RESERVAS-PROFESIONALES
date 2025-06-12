package com.reservas.sistematurnos.service.impl;

import com.reservas.sistematurnos.dto.auth.AuthRequestDTO;
import com.reservas.sistematurnos.dto.auth.AuthResponseDTO;
import com.reservas.sistematurnos.dto.response.MessageResponseDTO;
import com.reservas.sistematurnos.dto.usuario.UsuarioRequestDTO;
import com.reservas.sistematurnos.exception.UnauthorizedException;
import com.reservas.sistematurnos.model.Rol;
import com.reservas.sistematurnos.model.Token;
import com.reservas.sistematurnos.model.Usuario;
import com.reservas.sistematurnos.model.UsuarioRol;
import com.reservas.sistematurnos.repository.usuario.IRolUsuarioRepository;
import com.reservas.sistematurnos.repository.usuario.ITokenRepository;
import com.reservas.sistematurnos.repository.usuario.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final Map<String, String> tokenBlacklist = new ConcurrentHashMap<>();
    private final ITokenRepository tokenRepository;
    private final JwtService jwtService;
    private final IRolUsuarioRepository rolUsuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public MessageResponseDTO register(UsuarioRequestDTO nuevoUsuario) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findByCorreo(nuevoUsuario.correo());

            if (usuario.isPresent()) {
                logger.error("Error: Usuario ya registrado - {}", nuevoUsuario.correo());
                throw new UnauthorizedException("Usuario ya registrado");
            }
            Rol rol = rolUsuarioRepository.findByUsuarioRol(UsuarioRol.USER)
                    .orElseGet(() -> rolUsuarioRepository.save(new Rol(UsuarioRol.USER)));

            Usuario usuarioEntity = Usuario.builder()
                    .nombre(nuevoUsuario.nombre())
                    .apellido(nuevoUsuario.apellido())
                    .identificacion((nuevoUsuario.identificacion()))
                    .correo(nuevoUsuario.correo())
                    .password(passwordEncoder.encode(nuevoUsuario.password()))
                    .celular(nuevoUsuario.celular())
                    .rol(rol)
                    .build();
            Usuario usuarioGuardado = usuarioRepository.save(usuarioEntity);
            var jwtToken = jwtService.generateToken(usuarioGuardado);
            saveUserToken(usuarioGuardado, jwtToken);
            logger.info("Usuario registrado correctamente -{}", nuevoUsuario.correo());

            return new MessageResponseDTO("Usuario registrado correctamente");
        } catch (Exception e) {
            logger.error("Error al registrar usuario: {}", e.getMessage(), e);
            return new MessageResponseDTO(String.format("Error al registrar usuario: %s", e.getMessage()));
        }
    }

    public AuthResponseDTO login(AuthRequestDTO auth) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            auth.correo(),
                            auth.password()
                    )
            );
        } catch (BadCredentialsException e) {
            logger.error("Error de autenticación: Credenciales incorrectas para {}", auth.correo());
            throw new UnauthorizedException("Credenciales incorrectas");
        }

        Optional<Usuario> usuario = usuarioRepository.findByCorreo(auth.correo());
        if (usuario.isEmpty()){
            logger.error("Error: Usuario no encontrado -{}", auth.correo());
            throw new UnauthorizedException("Usuario no encontrado");
        }
        var jwtToken = jwtService.generateToken(usuario.get());
        revokeAllUserTokens(usuario.get());
        saveUserToken(usuario.get(), jwtToken);
        logger.info("Usuario autenticado correctamente - {}", auth.correo());

        return new AuthResponseDTO(
                usuario.get().getCorreo(),
                usuario.get().getNombre(),
                usuario.get().getApellido(),
                usuario.get().getRol().getUsuarioRol().name(),
                jwtToken
        );
    }

    public MessageResponseDTO refreshToken(final String authHeader) throws BadRequestException {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.error("Token no válido");
            throw new BadRequestException("Token no válido");
        }

        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail == null) {
            logger.error("Token no válido");
            throw new BadRequestException("Token no válido");
        }

        final Optional<Usuario> user = usuarioRepository.findByCorreo(userEmail);

        if (!jwtService.isTokenValid(refreshToken, user.get())) {
            logger.error("Token no válido");
            throw new BadRequestException("Token no válido");
        }

        final String newToken = jwtService.generateToken(user.get());
        revokeAllUserTokens(user.get());
        saveUserToken(user.get(), newToken);
        logger.info("Token refrescado correctamente");
        return new MessageResponseDTO(newToken);
    }

    private void saveUserToken (Usuario usuario, String token){
        Token usuarioToken = Token.builder()
                    .token(token)
                    .usuario(usuario)
                    .build();

        tokenRepository.save(usuarioToken);
    }

    private void revokeAllUserTokens(Usuario usuario) {
        final List<Token> validUserTokens = tokenRepository
                .findAllByUsuario_IdAndRevokedFalseAndExpiredFalse(usuario.getId());

        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
                tokenRepository.save(token);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }
}
