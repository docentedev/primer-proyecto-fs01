# Primer Proyecto - API REST con Spring Boot

## 📋 Descripción

Este es un proyecto educativo que implementa una **API REST profesional** con Spring Boot.
El objetivo es aprender conceptos fundamentales de backend:
- Arquitectura de capas
- Patrón REST
- Mapeo Objeto-Relacional (ORM)
- Inyección de dependencias

## 🎯 ¿Qué aprenderemos?

- ✅ **Crear endpoints REST** (GET, POST, PUT, DELETE)
- ✅ **Separación de responsabilidades** (Controller → Service → Repository)
- ✅ **Usar DTOs** para transferencia segura de datos
- ✅ **Trabajar con una Base de Datos** (JPA/Hibernate)
- ✅ **Manejar errores** de forma profesional

---

## 🛠️ Requisitos Previos

- **Java 25 (OpenJDK)** instalado
- **Git** (opcional)
- Un IDE como **IntelliJ IDEA** o **VS Code**

---

## 📦 Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|-----------|---------|----------|
| **Spring Boot** | 3.x | Framework web |
| **Spring Data JPA** | - | Acceso a base de datos |
| **Hibernate** | - | ORM (mapeo Java ↔ SQL) |
| **SQLite** | - | Base de datos embebida |
| **Lombok** | - | Genera getters/setters automáticamente |

---

## 🚀 Cómo Ejecutar

### 1️⃣ Clonar o descargar el proyecto
```bash
git clone [url-del-repo]
cd primer-proyecto
```

### 2️⃣ Compilar el proyecto
```bash
./gradlew build
```

### 3️⃣ Ejecutar la aplicación
```bash
./gradlew bootRun
```

O desde tu IDE, ejecuta: `PrimerProyectoApplication.java`

### 4️⃣ Verificar que funciona
La aplicación estará en: **http://localhost:8080**

---

## ⚠️ Warnings Esperados (Java 25)

Si ves estos warnings, **es completamente normal** y no afectan el funcionamiento:

```
WARNING: A restricted method in java.lang.System has been called
WARNING: java.lang.System::load has been called by org.sqlite.SQLiteJDBCLoader
WARNING: Use --enable-native-access=ALL-UNNAMED to avoid a warning for callers in this module
```

**¿Por qué ocurren?**
- Java 25 es más restrictivo con métodos nativos por seguridad
- SQLite JDBC necesita acceso a librerías nativas para funcionar
- Es solo un aviso, no impide que la aplicación funcione

**¿Cómo suprimirlos (opcional)?**
Si quieres eliminar los warnings, ejecuta:
```bash
./gradlew bootRun --jvmargs="--enable-native-access=ALL-UNNAMED"
```

**Importante**: El build termina con `BUILD SUCCESSFUL`, así que todo está bien ✓

---

## 🏗️ Arquitectura del Proyecto

### Flujo de una Petición

```
Cliente (Postman/Browser)
    ↓
[Controller] ← Recibe la petición HTTP
    ↓
[Service] ← Procesa la lógica de negocio
    ↓
[Repository] ← Comunica con la BD
    ↓
[Base de Datos] ← Almacena los datos
    ↓
[Repository] → Retorna datos
    ↓
[Service] → Transforma a DTO
    ↓
[Controller] → Retorna JSON
    ↓
Cliente recibe la respuesta
```

### ¿Por qué esta estructura?

1. **Separación de responsabilidades**: cada capa tiene un propósito específico
2. **Reutilización**: el Service se puede usar desde múltiples Controllers
3. **Testabilidad**: es fácil reemplazar componentes con mocks en tests
4. **Mantenimiento**: cambios en la BD no afectan los endpoints

---

## 📁 Estructura de Carpetas

```
src/main/java/com/primerproyecto/
│
├── PrimerProyectoApplication.java      ← Punto de entrada (main)
│
└── api/
    ├── config/                         ← Configuración de Spring
    │   └── [Beans, Security, etc.]
    │
    ├── controller/                     ← Endpoints HTTP (REST)
    │   └── UserController.java         ← GET, POST para usuarios
    │
    ├── dto/                            ← Objetos de Transferencia
    │   └── UserDTO.java                ← Lo que viaja por HTTP
    │
    ├── exception/                      ← Manejo de errores
    │   └── [Excepciones personalizadas]
    │
    ├── model/                          ← Entidades JPA
    │   └── User.java                   ← Mapeo a tabla 'users'
    │
    ├── repository/                     ← Acceso a Base de Datos
    │   └── UserRepository.java         ← Queries a BD
    │
    └── service/                        ← Lógica de Negocio
        └── UserService.java            ← Procesa requests
```

---

## 🔑 Conceptos Clave

### 1. Controller (Capa de Presentación)
```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    // Recibe HTTP, valida, delega al Service
}
```
**Responsabilidad**: Gestionar requests HTTP y responses

### 2. Service (Capa de Lógica de Negocio)
```java
@Service
public class UserService {
    // Contiene la lógica de negocio
    // Mapea Entity ↔ DTO
    // Valida reglas de negocio
}
```
**Responsabilidad**: Ejecutar la lógica del negocio

### 3. Repository (Capa de Persistencia)
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```
**Responsabilidad**: Comunicar con la BD

### 4. Model/Entity (BD)
```java
@Entity
@Table(name = "users")
public class User {
    // Mapeo directo a tabla SQL
}
```
**Responsabilidad**: Representar datos en la BD

### 5. DTO (Transferencia)
```java
@Data
public class UserDTO {
    // Lo que expone la API
    // NO contiene datos sensibles
}
```
**Responsabilidad**: Transferir datos seguros

---

## 📡 Endpoints Disponibles

### Obtener todos los usuarios
```http
GET /api/v1/users
```
**Respuesta:**
```json
[
  {
    "id": 1,
    "username": "claudio",
    "email": "claudio@example.com"
  }
]
```

### Crear un nuevo usuario
```http
POST /api/v1/users
Content-Type: application/json

{
  "username": "juan",
  "email": "juan@example.com"
}
```
**Respuesta:**
```json
{
  "id": 2,
  "username": "juan",
  "email": "juan@example.com"
}
```

---

## 🧪 Probar con Postman

1. Abre **Postman** o **Insomnia**
2. Crea una nueva petición GET: `http://localhost:8080/api/v1/users`
3. Crea una petición POST con body JSON
4. ¡Verifica las respuestas en la consola!

---

## 📚 Notas Importantes para Estudiantes

### ✅ Lo que SÍ está implementado correctamente
- ✔️ Arquitectura de capas (Controller → Service → Repository)
- ✔️ Uso de DTOs (separación BD ↔ API)
- ✔️ Inyección de dependencias (@Autowired)
- ✔️ JPA/Hibernate ORM

### ⚠️ Lo que FALTA para producción
- ❌ **Security**: Se deshabilitó para simplificar el aprendizaje
- ❌ **Validaciones**: Hacer validación en Controller y Service
- ❌ **Manejo de excepciones**: GlobalExceptionHandler
- ❌ **Tests**: Tests unitarios e integración
- ❌ **Documentación**: Swagger/OpenAPI
- ❌ **Logging**: Registrar eventos

## 🔒 Configuración de Seguridad

Spring Security está **COMPLETAMENTE DESHABILITADO** para facilitar el aprendizaje.

### ¿Cómo se deshabilita?

Se configura en la clase: [SecurityConfig.java](src/main/java/com/primerproyecto/api/config/SecurityConfig.java)

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
           .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
        return http.build();
    }
}
```

**¿Qué significa esto?**
- ✅ Se pueden acceder a TODOS los endpoints sin autenticación
- ✅ No aparecerá contraseña generada en los logs
- ✅ Perfecto para aprender conceptos básicos

### ⚠️ Para pasar a PRODUCCIÓN:

En producción necesitarás:
1. **Eliminar o modificar** `SecurityConfig.java`
2. **Implementar autenticación** (usuarios, contraseñas encriptadas)
3. **Configurar autorización** (roles y permisos)
4. **Usar JWT o sesiones** para mantener el login
5. **Implementar HTTPS**

Aquí hay un ejemplo de configuración más avanzada:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/api/v1/users/login").permitAll()  // Login sin autenticación
            .requestMatchers("/api/v1/users/**").authenticated() // Resto requiere autenticación
        )
        .httpBasic();  // Usa autenticación HTTP básica
    return http.build();
}
```

---

## 📖 Recursos Adicionales

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Lombok Documentation](https://projectlombok.org/)
- [REST Best Practices](https://restfulapi.net/)

---

## 👨‍💼 Autor

Proyecto educativo para aprender Spring Boot

**¡Buena suerte con tu aprendizaje!** 🚀
