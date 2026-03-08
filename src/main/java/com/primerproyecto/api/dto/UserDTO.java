package com.primerproyecto.api.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) para transferir datos de usuario entre capas.
 * 
 * <p>Un DTO actúa como intermediario entre la API REST y la lógica de negocio,
 * proporcionando control sobre qué datos se exponen al cliente.</p>
 * 
 * <h3>Ventajas del patrón DTO:</h3>
 * <ul>
 *   <li><b>Seguridad:</b> Controla qué datos se exponen (ej: no exponer campos sensibles)</li>
 *   <li><b>Desacoplamiento:</b> Si la BD cambia, la API externa no se afecta</li>
 *   <li><b>Flexibilidad:</b> Modifica la entidad sin romper APIs existentes</li>
 *   <li><b>Validación:</b> Agrega validaciones específicas para entrada/salida</li>
 * </ul>
 * 
 * <p><b>Nota sobre {@code @Data}:</b> La anotación Lombok genera automáticamente
 * getters, setters, equals(), hashCode() y toString().</p>
 * 
 * @see com.primerproyecto.api.model.User la entidad persistida en BD
 * @since 1.0
 */
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
}
