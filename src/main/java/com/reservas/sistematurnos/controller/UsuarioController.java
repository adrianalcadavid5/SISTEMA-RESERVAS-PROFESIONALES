package com.reservas.sistematurnos.controller;

import com.reservas.sistematurnos.dto.PageDTO.PageResponseDTO;
import com.reservas.sistematurnos.dto.usuario.UsuarioModifyDTO;
import com.reservas.sistematurnos.dto.usuario.UsuarioRequestDTO;
import com.reservas.sistematurnos.dto.usuario.UsuarioResponseDTO;
import com.reservas.sistematurnos.service.impl.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/buscarTodos")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos(){
        return ResponseEntity.ok(usuarioService.buscarTodosDTO());
    }
    @GetMapping("/paginado")
    public ResponseEntity<PageResponseDTO<UsuarioResponseDTO>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(usuarioService.listarPaginado(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorIdDTO(id));
    }
}
