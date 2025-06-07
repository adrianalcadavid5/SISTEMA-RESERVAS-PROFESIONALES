package com.reservas.sistematurnos.controller;

import com.reservas.sistematurnos.dto.auth.AuthRequestDTO;
import com.reservas.sistematurnos.dto.usuario.UsuarioRequestDTO;
import com.reservas.sistematurnos.service.impl.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UsuarioRequestDTO nuevoUsuario){
        Object data = authService.register(nuevoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO usuario){
        return ResponseEntity.ok(authService.login(usuario));
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws BadRequestException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return ResponseEntity.ok(authService.refreshToken(authHeader));
    }


}
