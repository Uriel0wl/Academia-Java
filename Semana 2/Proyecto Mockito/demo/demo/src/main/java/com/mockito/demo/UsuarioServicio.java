package com.mockito.demo;

public class UsuarioServicio{
    private final UsuarioRespositorio usuarioRespositorio;

    public UsuarioServicio(final UsuarioRespositorio usuarioRespositorio) {
        this.usuarioRespositorio = usuarioRespositorio;
    }

    public UsuarioDto crearUsuario(final String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }
        return usuarioRespositorio.crearUsuario(nombre);
    }

    public UsuarioDto obtenerUsuario(final long id) {
        return usuarioRespositorio.obtenerUsuario(id);
    }
}