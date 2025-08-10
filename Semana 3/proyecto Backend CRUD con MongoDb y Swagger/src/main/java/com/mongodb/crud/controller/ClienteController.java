package com.mongodb.crud.controller;

import com.mongodb.crud.model.Cliente;
import com.mongodb.crud.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:5173"})
@Tag(name = "Cliente", description = "API para gestión de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Crear nuevo cliente", description = "Crea un nuevo cliente en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "409", description = "Cliente ya existe con ese email",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content)
    })
    public ResponseEntity<Cliente> createCliente(
            @Parameter(description = "Datos del cliente a crear", required = true)
            @Valid @RequestBody Cliente cliente) {
        try {
            if (clienteService.existsByEmail(cliente.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            Cliente savedCliente = clienteService.save(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCliente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todos los clientes", description = "Obtiene la lista completa de clientes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content)
    })
    public ResponseEntity<List<Cliente>> getAllClientes() {
        try {
            List<Cliente> clientes = clienteService.findAll();
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID", description = "Obtiene un cliente específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content)
    })
    public ResponseEntity<Cliente> getClienteById(
            @Parameter(description = "ID único del cliente", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String id) {
        try {
            Optional<Cliente> cliente = clienteService.findById(id);
            return cliente.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Obtener cliente por email", description = "Obtiene un cliente específico por su email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content)
    })
    public ResponseEntity<Cliente> getClienteByEmail(
            @Parameter(description = "Email del cliente", required = true, example = "cliente@ejemplo.com")
            @PathVariable String email) {
        try {
            Optional<Cliente> cliente = clienteService.findByEmail(email);
            return cliente.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar clientes por nombre", description = "Busca clientes que contengan el texto especificado en el nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content)
    })
    public ResponseEntity<List<Cliente>> searchClientesByNombre(
            @Parameter(description = "Texto a buscar en el nombre del cliente", required = true, example = "Juan")
            @RequestParam String nombre) {
        try {
            List<Cliente> clientes = clienteService.findByNombre(nombre);
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content)
    })
    public ResponseEntity<Cliente> updateCliente(
            @Parameter(description = "ID único del cliente", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String id,
            @Parameter(description = "Datos actualizados del cliente", required = true)
            @Valid @RequestBody Cliente cliente) {
        try {
            Cliente updatedCliente = clienteService.update(id, cliente);
            return ResponseEntity.ok(updatedCliente);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente por ID", description = "Elimina un cliente del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content)
    })
    public ResponseEntity<Void> deleteCliente(
            @Parameter(description = "ID único del cliente", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String id) {
        try {
            if (clienteService.findById(id).isPresent()) {
                clienteService.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/email/{email}")
    @Operation(summary = "Eliminar cliente por email", description = "Elimina un cliente del sistema por su email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content)
    })
    public ResponseEntity<Void> deleteClienteByEmail(
            @Parameter(description = "Email del cliente", required = true, example = "cliente@ejemplo.com")
            @PathVariable String email) {
        try {
            if (clienteService.existsByEmail(email)) {
                clienteService.deleteByEmail(email);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}