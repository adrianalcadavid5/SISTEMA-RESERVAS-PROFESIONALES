package com.reservas.sistematurnos.controller;

import com.reservas.sistematurnos.model.Profesional;
import com.reservas.sistematurnos.service.IProfesionalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profesionales")
public class ProfesionalController {
    private final IProfesionalService profesionalService;

    public ProfesionalController(IProfesionalService profesionalService) {
        this.profesionalService = profesionalService;
    }
    @PostMapping
    public ResponseEntity<Profesional> crear(@RequestBody Profesional profesional){
        Profesional creado = profesionalService.guardar(profesional);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
    @PutMapping
    public ResponseEntity<Profesional> modificar(@RequestBody Profesional profesional){
        Profesional actualizado = profesionalService.modificar(profesional);
        return ResponseEntity.ok(actualizado);
    }
}
