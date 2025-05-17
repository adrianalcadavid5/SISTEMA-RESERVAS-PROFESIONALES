package com.reservas.sistematurnos.controller;

import com.reservas.sistematurnos.dto.usuario.UsuarioModifyDTO;
import com.reservas.sistematurnos.dto.usuario.UsuarioRequestDTO;
import com.reservas.sistematurnos.dto.usuario.UsuarioResponseDTO;
import com.reservas.sistematurnos.service.impl.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@RequestBody @Valid UsuarioRequestDTO usuarioRequestDTO) {
        UsuarioResponseDTO response = usuarioService.guardarDesdeDTO(usuarioRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioModifyDTO usuarioModifyDTO) {
        UsuarioResponseDTO actualizado = usuarioService.modificarDesdeDTO(id, usuarioModifyDTO);
        return ResponseEntity.ok(actualizado);
    }
}
