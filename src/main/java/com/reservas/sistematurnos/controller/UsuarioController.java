package com.reservas.sistematurnos.controller;

import com.reservas.sistematurnos.dto.PageDTO.PageResponseDTO;
import com.reservas.sistematurnos.dto.usuario.UsuarioModifyDTO;
import com.reservas.sistematurnos.dto.usuario.UsuarioRequestDTO;
import com.reservas.sistematurnos.dto.usuario.UsuarioResponseDTO;
import com.reservas.sistematurnos.service.impl.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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

    @Operation(summary = "Crear un usuario nuevo",
    description = "Crea un usuario nuevo desde el panel del administrador")
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@RequestBody @Valid UsuarioRequestDTO usuarioRequestDTO) {
        UsuarioResponseDTO response = usuarioService.guardarDesdeDTO(usuarioRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar los datos de un usuario",
            description = "Actualiza los datos de un usuario desde el panel de administrador" +
                    "Requiere del rol ADMIN y del id del usuario a modificar.")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioModifyDTO usuarioModifyDTO) {
        UsuarioResponseDTO actualizado = usuarioService.modificarDesdeDTO(id, usuarioModifyDTO);
        return ResponseEntity.ok(actualizado);
    }
    @Operation(summary = "Listar todos los usuarios registrados")
    @GetMapping("/buscarTodos")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos(){
        return ResponseEntity.ok(usuarioService.buscarTodosDTO());
    }
    @Operation(summary = "Listar todos los usuarios registrados con paginación")
    @GetMapping("/paginado")
    public ResponseEntity<PageResponseDTO<UsuarioResponseDTO>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(usuarioService.listarPaginado(page, size));
    }
    @Operation(summary = "Buscar un usuario por su id",
            description = "Busca un usuario registrado, " +
                    "Requiere del rol ADMIN y del id del usuario a buscar.")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorIdDTO(id));
    }
}
