package com.mockito.demo;

public interface UsuarioRespositorio {
    UsuarioDto crearUsuario(String nombre);

    UsuarioDto obtenerUsuario(Long id);
}

