package com.reservas.sistematurnos.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException() {
        super();
    }
}

/*
ResourceNotFoundException:

Esta excepción se lanza cuando intentas acceder a un recurso que no existe en la base de datos. Por ejemplo, si buscas un odontólogo,
paciente o turno por su ID y ese ID no se encuentra, lanzarías esta excepción.
Cuándo usarla:
Al intentar obtener, actualizar o eliminar un recurso que no existe. Por ejemplo, si llamas a un método buscarPorId(id) y no hay
coincidencia, deberías lanzar esta excepción para informar que el recurso solicitado no fue encontrado.

 En conclusion:
 ResourceNotFoundException: Se lanza cuando intentas acceder a un recurso que no existe.
 */