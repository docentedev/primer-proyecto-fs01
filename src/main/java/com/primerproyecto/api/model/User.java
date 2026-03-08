package com.primerproyecto.api.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Model/Entity - Representacion de datos en la Base de Datos
 * 
 * Esta clase esta vinculada directamente a la tabla 'users' de la BD.
 * NO se debe exponer directamente en los endpoints HTTP.
 * En su lugar, usamos DTOs para transferir datos.
 * 
 * @Lombock @Data: Genera automaticamente getters, setters, equals(), toString()
 */
@Entity  // Indica que es una entidad JPA (mapeada a tabla de BD)
@Table(name = "users")  // Especifica el nombre de la tabla
@Data  // Lombok: genera getters, setters, equals, hashCode, toString
public class User {
    
    /**
     * ID - Clave primaria con auto-generacion
     * @GeneratedValue(strategy = GenerationType.IDENTITY): 
     * La BD genera automaticamente el ID (auto-increment)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username - Unico y obligatorio
     * @Column(unique = true): No pueden existir dos usuarios con mismo username
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Email - Obligatorio
     * nullable = false: No puede ser nulo en la BD
     */
    @Column(nullable = false)
    private String email;
}