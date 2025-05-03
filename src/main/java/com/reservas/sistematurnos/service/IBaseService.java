package com.reservas.sistematurnos.service;

import java.util.List;
import java.util.Optional;

public interface IBaseService<T, ID> {
    T guardar(T entidad);
    Optional<T> buscarPorId(ID id);
    List<T> buscarTodos();
    T modificar(T entidad);
    void eliminar(ID id);
}
