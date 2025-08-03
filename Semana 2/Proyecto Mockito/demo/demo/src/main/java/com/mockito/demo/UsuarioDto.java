package com.mockito.demo;

public class UsuarioDto {
    private String nombre;
    private Long id;

    public UsuarioDto(long l, String prueba) {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
