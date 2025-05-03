package com.reservas.sistematurnos.service.impl;

import com.reservas.sistematurnos.exception.BadRequestException;
import com.reservas.sistematurnos.model.Usuario;
import com.reservas.sistematurnos.repository.usuario.IUsuarioRepository;
import com.reservas.sistematurnos.service.IUsuarioService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService extends BaseServiceImpl<Usuario, Long> implements IUsuarioService {
    private final IUsuarioRepository usuarioRepository;

    public UsuarioService(IUsuarioRepository usuarioRepository) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        validarCorreoUnico(usuario.getCorreo());
        return super.guardar(usuario); // usa el método de la clase base
    }
    public void validarCorreoUnico(String correo){
        Optional<Usuario> usuarioExistente = usuarioRepository.findByCorreo(correo);
        if (usuarioExistente.isPresent()) {
            throw new BadRequestException("El correo electrónico ya está registrado.");
        }
    }

    @Override
    public Usuario modificar(Usuario usuario) {
        validarCorreoUnicoParaModificacion(usuario);
        return super.modificar(usuario);
    }
    private void validarCorreoUnicoParaModificacion(Usuario usuario) {
        Optional<Usuario> existente = usuarioRepository.findByCorreo(usuario.getCorreo());
        if (existente.isPresent() && !existente.get().getId().equals(usuario.getId())) {
            throw new BadRequestException("Ya existe otro usuario con ese correo electrónico.");
        }
    }
}
//aca puedo agregar validaciones especificar si las necesito, por ejemplo, en guardar o modificar