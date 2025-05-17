package com.reservas.sistematurnos.controller;

import com.reservas.sistematurnos.dto.profesional.ProfesionalModifyDTO;
import com.reservas.sistematurnos.dto.profesional.ProfesionalRequestDTO;
import com.reservas.sistematurnos.dto.profesional.ProfesionalResponseDTO;
import com.reservas.sistematurnos.service.impl.ProfesionalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profesionales")
public class ProfesionalController {

    private final ProfesionalService profesionalService;

    public ProfesionalController(ProfesionalService profesionalService) {
        this.profesionalService = profesionalService;
    }

    @PostMapping
    public ResponseEntity<ProfesionalResponseDTO> crearProfesional(@RequestBody @Valid ProfesionalRequestDTO profesionalRequestDTO){
        ProfesionalResponseDTO response = profesionalService.guardarDesdeDTO(profesionalRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProfesionalResponseDTO> actualizarProfesional(
            @PathVariable Long id,
            @RequestBody @Valid ProfesionalModifyDTO profesionalModifyDTO){
        ProfesionalResponseDTO actualizado = profesionalService.modificarDesdeDTO(id, profesionalModifyDTO);
        return ResponseEntity.ok(actualizado);
    }
}
