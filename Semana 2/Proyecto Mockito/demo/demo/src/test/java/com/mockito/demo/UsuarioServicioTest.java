package com.mockito.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UsuarioServicioTest {
    @Mock
    private UsuarioRespositorio usuarioRepositorio;

    @InjectMocks
    private UsuarioServicio usuarioServicio;

    @DisplayName("Dado un usuario que queremos crear " +
            "Cuando llamamos a 'crearUsuario' " +
            "Esperamos que el usuario este creado")

    @Test
    public void dadoUsuarioParaCrearEsperamosUnUsuarioCreado(){
        //Se crean(Inicializan) Datos Dummy
        UsuarioDto esperado = new UsuarioDto(1L, "Prueba");
        //Se genera Mock para la respuesta al ejecutar el crearUsuario
        Mockito.when(usuarioRepositorio.crearUsuario("Prueba"))
                .thenReturn(esperado);
        //Se ejecuta el Metodo Crearusuario
        final UsuarioDto resultado = usuarioServicio.crearUsuario("Prueba");
        Assertions.assertEquals(esperado.getId(), resultado.getId());
        Assertions.assertEquals(esperado.getNombre(), resultado.getNombre(), "Los nombres son iguales");
        Assertions.assertEquals(esperado, resultado);
    }

}
