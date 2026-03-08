package com.primerproyecto.api.controller;

import com.primerproyecto.api.dto.UserDTO;
import com.primerproyecto.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller - Capa de presentación
 * Responsable de:
 * - Recibir peticiones HTTP del cliente
 * - Validar la entrada
 * - Delegar la lógica al service
 * - Retornar respuestas HTTP
 */
@RestController
@RequestMapping("/api/v1/users") // Versionalizar tu API es buena práctica
public class UserController {

    /**
     * @Autowired - Inyección de dependencias de Spring
     * 
     * Spring automáticamente busca una implementación de UserService
     * y la inyecta aquí. Beneficios:
     * 1. DESACOPLAMIENTO: No creamos 'new UserService()' manualmente
     * 2. TESTABILIDAD: Podemos reemplazar con un mock en tests
     * 3. REUTILIZACIÓN: La misma instancia se comparte entre componentes
     * 4. CICLO DE VIDA: Spring gestiona la creación y destrucción
     */
    @Autowired
    private UserService userService;

    /**
     * GET /api/v1/users
     * Retorna lista de DTOs (NO entidades)
     * Permite a Spring convertir automáticamente a JSON
     */
    @GetMapping
    public List<UserDTO> getAll() {
        return userService.listarTodos();
    }

    /**
     * POST /api/v1/users
     * @RequestBody: Spring deserializa el JSON del cliente a UserDTO
     * ResponseEntity: Envolvemos la respuesta para controlar headers, status, etc.
     */
    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.guardar(userDTO));
    }

    /**
     * GET /api/v1/users/{id}
     * Obtiene un usuario específico por su ID
     * @PathVariable: toma el ID de la URL (ej: /api/v1/users/1)
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        UserDTO user = userService.obtenerPorId(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();  // 404 si no existe
    }

    /**
     * PUT /api/v1/users/{id}
     * Actualiza un usuario completo
     * Requiere todos los campos en el body
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updated = userService.actualizar(id, userDTO);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();  // 404 si no existe
    }

    /**
     * DELETE /api/v1/users/{id}
     * Elimina un usuario por su ID
     * Retorna 204 No Content si fue exitoso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (userService.eliminar(id)) {
            return ResponseEntity.noContent().build();  // 204
        }
        return ResponseEntity.notFound().build();  // 404 si no existía
    }
}