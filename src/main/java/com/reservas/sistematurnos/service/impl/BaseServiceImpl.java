package com.reservas.sistematurnos.service.impl;

import com.reservas.sistematurnos.exception.ResourceNotFoundException;
import com.reservas.sistematurnos.service.IBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T, ID> implements IBaseService<T, ID> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final JpaRepository<T, ID> repository;

    protected BaseServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T guardar(T entidad) {
        T guardado = repository.save(entidad);
        logger.info("Entidad guardada: {}", guardado);
        return guardado;
    }

    @Override
    public Optional<T> buscarPorId(ID id) {
        Optional<T> encontrado = repository.findById(id);
        if (encontrado.isEmpty()) {
            logger.warn("Entidad no encontrada con ID: {}", id);
            throw new ResourceNotFoundException("Entidad no encontrada con ID: " + id);
        }
        logger.info("Entidad encontrada con ID: {}", id);
        return encontrado;
    }

    @Override
    public List<T> buscarTodos() {
        List<T> lista = repository.findAll();
        if (lista.isEmpty()) {
            logger.warn("No se encontraron entidades.");
        } else {
            logger.info("Se encontraron {} entidades.", lista.size());
        }
        return lista;
    }

    @Override
    public T modificar(T entidad) {
        T actualizado = repository.save(entidad);
        logger.info("Entidad modificada: {}", actualizado);
        return actualizado;
    }

    @Override
    public void eliminar(ID id) {
        if (!repository.existsById(id)) {
            logger.error("No se puede eliminar. Entidad no encontrada con ID: {}", id);
            throw new ResourceNotFoundException("Entidad no encontrada con ID: " + id);
        }
        repository.deleteById(id);
        logger.info("Entidad eliminada con ID: {}", id);
    }
}
