package com.primerproyecto.api.repository;

import com.primerproyecto.api.model.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Busca un usuario por su email.
     * 
     * <p>Spring Data JPA genera automáticamente la consulta SQL basada en el nombre del método:
     * SELECT * FROM users WHERE email = ?</p>
     * 
     * @param email el email del usuario a buscar
     * @return un Optional con el usuario si existe, o vacío si no
     */
    Optional<User> findByEmail(String email);
}