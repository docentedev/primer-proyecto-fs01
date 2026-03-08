package com.primerproyecto.api.service;

import com.primerproyecto.api.dto.UserDTO;
import com.primerproyecto.api.model.User;
import com.primerproyecto.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service - Capa de lógica de negocio
 * Responsable de:
 * - Implementar la lógica de negocio
 * - Transformaciones de datos (Entity ↔ DTO)
 * - Orquestar operaciones entre repository y controller
 * - Manejar excepciones de negocio
 */
@Service
public class UserService {

    /**
     * Inyección del Repository
     * El repository se encarga de comunicarse con la BD
     * Esto separa la lógica de negocio de la persistencia
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Obtiene todos los usuarios de la BD y los convierte a DTO
     * Nota: Convertimos de User (Entity) a UserDTO antes de retornar
     * Esto garantiza que solo exponemos los datos que queremos
     */
    public List<UserDTO> listarTodos() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Recibe un DTO del cliente, lo convierte a Entity y lo guarda en BD
     * Este es el patrón correcto de flujo de datos:
     * DTO (entrada) → Entity → BD → Entity (salida) → DTO
     */
    public UserDTO guardar(UserDTO userDTO) {
        // Aquí podrías encriptar la password antes de guardar
        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);  // Retornamos DTO, no Entity
    }

    /**
     * Mapeo: Entity → DTO
     * Convertimos la entidad de persistencia a objeto de transferencia
     * Aquí podríamos filtrar campos sensibles o transformar datos
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }

    /**
     * Obtiene un usuario por su ID
     * Retorna Optional porque el usuario podría no existir
     */
    public UserDTO obtenerPorId(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);  // Retorna null si no existe
    }

    /**
     * Actualiza un usuario existente
     * Busca por ID y actualiza sus datos
     */
    public UserDTO actualizar(Long id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(userExistente -> {
                    userExistente.setUsername(userDTO.getUsername());
                    userExistente.setEmail(userDTO.getEmail());
                    User updated = userRepository.save(userExistente);
                    return convertToDTO(updated);
                })
                .orElse(null);  // Retorna null si no existe
    }

    /**
     * Elimina un usuario por su ID
     * Retorna true si lo eliminó, false si no existía
     */
    public boolean eliminar(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Mapeo: DTO → Entity
     * Convertimos el objeto del cliente a entidad para guardar en BD
     * Nota: En producción, pueden usar librerías como MapStruct o ModelMapper
     * para automatizar este mapeo (especialmente en aplicaciones grandes)
     */
    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        return user;
    }
}