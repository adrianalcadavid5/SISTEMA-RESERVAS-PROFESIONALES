package com.reservas.sistematurnos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> manejarExcepcionesRecursoNoEncontrado(ResourceNotFoundException e, HttpServletRequest request){
        logger.warn("Recurso no encontrado: {} - URL: {}", e.getMessage(), request.getRequestURI());

        ApiError apiError = new ApiError(
                request.getRequestURI(),  //url de donde viene el error
                e.getMessage(),   //mensaje de error
                HttpStatus.NOT_FOUND.value(),  //codigo de error
                ZonedDateTime.now(),  //fehca y hora
                List.of()  //lista vacia por ahora, para spring validation
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarTodasLasExcepciones(Exception e,HttpServletRequest request){
        logger.error("Error inesperado en la URL: {}", request.getRequestURI(), e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException e, HttpServletRequest request){
        logger.warn("Solicitud incorrecta: {} - URL: {}", e.getMessage(), request.getRequestURI());


        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                ZonedDateTime.now(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidaciones (MethodArgumentNotValidException e, HttpServletRequest request){
        List<String> errores = new ArrayList<>();
        for(Object error: e.getBindingResult().getAllErrors()){
            if(error instanceof FieldError){
                FieldError fieldError = (FieldError) error;
                String fieldName = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                errores.add(fieldName+": "+message);
            }
        }
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                "validation field",
                HttpStatus.BAD_REQUEST.value(),
                ZonedDateTime.now(),
                errores
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

}
