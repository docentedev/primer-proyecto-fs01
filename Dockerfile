# =============================================================================
# STAGE 1: BUILD
# =============================================================================
# Usamos eclipse-temurin (distribución oficial de OpenJDK por Adoptium) con
# Alpine Linux. Alpine es una distro minimalista (~5 MB base), perfecta para
# contenedores ligeros.
# Necesitamos el JDK completo aquí porque el Gradle wrapper lo requiere para
# compilar y empaquetar el proyecto.
# =============================================================================
FROM eclipse-temurin:25-jdk-alpine AS builder

# Directorio de trabajo para el proceso de compilación
WORKDIR /workspace

# ── Truco de caché en capas ──────────────────────────────────────────────────
# Copiamos primero SOLO los descriptores de Gradle (build.gradle, settings.gradle
# y el wrapper). Docker cachea cada capa; si estos archivos no cambian entre
# builds, la capa de descarga de dependencias se reutiliza y el build es mucho
# más rápido.
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Damos permisos de ejecución al script del wrapper de Gradle
RUN chmod +x gradlew

# Descargamos todas las dependencias Maven/Gradle sin compilar todavía.
# --no-daemon  → no levanta el proceso daemon (más seguro y liviano en CI/Docker)
# Si el build.gradle no cambia, Docker reutiliza esta capa en la próxima vez.
RUN ./gradlew dependencies --no-daemon

# ── Código fuente ────────────────────────────────────────────────────────────
# Recién aquí copiamos el código. Así, si solo cambia src/, Docker solo
# rehace las capas a partir de este punto (no vuelve a bajar dependencias).
COPY src/ src/

# Construimos el "fat JAR" (uber-jar): un único archivo .jar que incluye la app
# y todas sus dependencias, listo para ejecutarse sin nada más.
# -x test → omitimos los tests para acelerar el build de la imagen
RUN ./gradlew bootJar --no-daemon -x test


# =============================================================================
# STAGE 2: RUNTIME
# =============================================================================
# En esta etapa SOLO copiamos el JAR generado arriba. No copiamos ni el JDK
# de build, ni el código fuente, ni Gradle → la imagen final es mucho más
# pequeña que si todo estuviera en una sola etapa.
# =============================================================================
FROM eclipse-temurin:25-jdk-alpine AS runtime

# Metadatos de la imagen (buena práctica)
LABEL maintainer="primer-proyecto"
LABEL description="API REST Spring Boot 4 — Primer Proyecto FS01"
LABEL java.version="25"

# Directorio de trabajo donde correrá la aplicación dentro del contenedor
WORKDIR /app

# Creamos un directorio dedicado para la base de datos SQLite.
# Al montar un volumen Docker en /app/data (ver docker-compose.yml), los datos
# persisten aunque el contenedor se detenga o se elimine.
RUN mkdir -p /app/data

# Copiamos únicamente el JAR compilado desde el stage anterior.
# El alias "builder" referencia al primer FROM; así Docker sabe de dónde traer el archivo.
COPY --from=builder /workspace/build/libs/*.jar app.jar

# Declaramos el puerto interno en el que escucha Spring Boot.
# Nota: EXPOSE es informativo — el mapeo real (3000:8080) se hace en docker-compose.
EXPOSE 8080

# ── Variables de entorno ─────────────────────────────────────────────────────

# URL JDBC de SQLite apuntando al directorio con volumen para persistencia.
# Puede ser sobreescrita desde docker-compose o con -e al hacer `docker run`.
ENV SPRING_DATASOURCE_URL=jdbc:sqlite:/app/data/usuarios.db

# Opciones de JVM optimizadas para correr dentro de un contenedor:
#   -XX:+UseContainerSupport    → la JVM respeta los límites de CPU/RAM del contenedor
#                                  (por defecto ve toda la RAM del host, lo cual es incorrecto)
#   -XX:MaxRAMPercentage=75.0   → usa como máximo el 75 % de la RAM asignada al contenedor,
#                                  dejando margen para el SO y otros procesos
#   -Djava.security.egd=...     → acelera el arranque: usa /dev/urandom en lugar de /dev/random
#                                  (evita bloqueos del generador de entropía en contenedores)
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -Djava.security.egd=file:/dev/./urandom"

# Comando de inicio: lanza el JAR con las opciones de JVM configuradas arriba.
# Usamos `sh -c` para que la variable $JAVA_OPTS se expanda correctamente.
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
