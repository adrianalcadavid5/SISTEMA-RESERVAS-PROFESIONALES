package com.reservas.sistematurnos.service;

import com.reservas.sistematurnos.dto.usuario.UsuarioResponseDTO;
import com.reservas.sistematurnos.model.Usuario;

import java.util.List;

public interface IUsuarioService extends IBaseService<Usuario, Long> {
    List<UsuarioResponseDTO> buscarTodosDTO();  // ← Añadir método
    UsuarioResponseDTO buscarPorIdDTO(Long id);    // ← Corregir firma
}
