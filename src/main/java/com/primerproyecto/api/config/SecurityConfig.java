package com.primerproyecto.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de Spring Security - DESHABILITADA PARA APRENDIZAJE
 * 
 * DESARROLLO (AHORA):
 * - Spring Security está deshabilitado
 * - Todos los endpoints son accesibles SIN autenticación
 * - Perfecto para aprender conceptos sin complicaciones de security
 * 
 * PRODUCCIÓN (DESPUÉS):
 * La estructura de esta clase es CORRECTA para usar en producción.
 * Solo cambiarías el contenido del método filterChain() para:
 * - Requerir login en endpoints específicos
 * - Usar JWT, OAuth2 o sesiones
 * - Habilitar CSRF y HTTPS
 * 
 * ¿Cómo Spring descubre esta clase automáticamente?
 * Spring escanea el package com.primerproyecto.* buscando clases con @Configuration.
 * No necesitas importarla manualmente en otro lado.
 */
@Configuration  // Le dice a Spring: "Esta clase configura algo"
@EnableWebSecurity  // Le dice a Spring: "Usa esta clase para configurar Security"
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitamos CSRF (protección contra ataques web)
            // En producción: deberías habilitarlo
            .csrf(csrf -> csrf.disable())
            
            // Definimos quién puede acceder a qué
            // anyRequest().permitAll() = cualquier request sin login
            // En producción: especificarías qué requiere autenticación
            .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());

        return http.build();
    }
}
