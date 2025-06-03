package com.reservas.sistematurnos.service.impl;

import com.reservas.sistematurnos.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public abstract class BaseValidationsService<T> {

    public final class ValidationUtils{
        private ValidationUtils() {} // Bloquea instanciación

        public static String normalizarCorreo(String correo) {
            return correo == null ? null : correo.trim().toLowerCase();
        }
        public static <T> void validarCorreoUnico(String correo, Function<String, Optional<T>> buscarPorCorreo) {
            String correoNormalizado = normalizarCorreo(correo);
            if (correoNormalizado == null || correoNormalizado.isEmpty()) {
                throw new BadRequestException("El correo electrónico es obligatorio.");
            }

            if (buscarPorCorreo.apply(correoNormalizado).isPresent()) {
                throw new BadRequestException("El correo electrónico ya está registrado.");
            }
        }
        public static <T> void validarCorreoUnicoParaModificacion(String correo, Long id, Function<String, Optional<T>> buscarPorCorreo, Function<T, Long> obtenerId) {
            String correoNormalizado = normalizarCorreo(correo);
            if (correoNormalizado == null || correoNormalizado.isEmpty()) {
                throw new BadRequestException("El correo electrónico no puede estar vacío.");
            }

            Optional<T> existente = buscarPorCorreo.apply(correoNormalizado);
            if (existente.isPresent() && !obtenerId.apply(existente.get()).equals(id)) {
                throw new BadRequestException("Ya existe otro registro con ese correo electrónico.");
            }
        }

    }

}