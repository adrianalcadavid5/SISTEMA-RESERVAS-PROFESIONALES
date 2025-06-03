package com.reservas.sistematurnos.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {
    private String path; //indicamos de que ruta fue el error
    private String mensaje;  // mensaje
    private int statusCode; //pone el codigo del error
    private ZonedDateTime zonedDateTime;  // para poner fecha y hora
    private List<String> errores;  //para usar spring validation en los errores
}
//clase creada para brindarnos mas informacion del error, esto nos devuelve un objeto json sin tener que hacerlo a mano
