# Guía práctica: Dockerizar un proyecto Spring Boot con Java 25

Esta guía te lleva desde cero hasta tener tu API corriendo en un contenedor Docker, paso a paso.

---

## ¿Qué es Docker y por qué usarlo?

Docker empaqueta tu aplicación junto con **todo lo que necesita para correr** (Java, dependencias, configuración) dentro de un "contenedor". El resultado es que funciona igual en tu máquina, en la de un compañero, o en un servidor.

```
Tu código + JDK + dependencias = imagen Docker → contenedor que corre en cualquier lado
```

---

## Requisitos previos

- [ ] Docker instalado → [docs.docker.com/get-docker](https://docs.docker.com/get-docker/)
- [ ] El proyecto compila localmente (`./gradlew bootJar` sin errores)

Verificá que Docker esté corriendo:

```bash
docker --version
# Docker version 27.x.x, build ...
```

---

## Paso 1 — Entender la estructura del Dockerfile

El `Dockerfile` de este proyecto usa **multi-stage build**: dos etapas dentro del mismo archivo.

```
┌─────────────────────────────────┐     ┌─────────────────────────────────┐
│  STAGE 1: builder               │     │  STAGE 2: runtime               │
│  (imagen grande, solo temporal) │ ──► │  (imagen final, liviana)        │
│                                 │     │                                  │
│  JDK 25 + Gradle                │     │  JDK 25 (solo runtime)          │
│  Descarga dependencias          │     │  Solo el .jar compilado         │
│  Compila → genera app.jar       │     │  No hay código fuente           │
└─────────────────────────────────┘     └─────────────────────────────────┘
```

**¿Por qué dos etapas?**
La etapa de compilación necesita herramientas pesadas (Gradle, etc.). La etapa final solo necesita Java para ejecutar el `.jar`. Separándolas, la imagen que se despliega es mucho más pequeña.

---

## Paso 2 — El Dockerfile explicado línea a línea

```dockerfile
# ── ETAPA 1: Compilación ───────────────────────────────────────────────────
FROM eclipse-temurin:25-jdk-alpine AS builder
```
> Usamos `eclipse-temurin` (distribución oficial de OpenJDK) con Alpine Linux.
> Alpine es una distro minimalista de ~5 MB, ideal para contenedores ligeros.
> El alias `AS builder` le da nombre a esta etapa para referenciarla luego.

```dockerfile
WORKDIR /workspace
```
> Crea y posiciona el directorio de trabajo dentro del contenedor.
> Todo lo que hagamos de aquí en más ocurre dentro de `/workspace`.

```dockerfile
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon
```
> **Truco clave de rendimiento:** copiamos primero SOLO los archivos de configuración de Gradle, descargamos las dependencias, y *luego* copiamos el código fuente.
>
> Docker guarda cada instrucción como una "capa" en caché. Si el código fuente cambia pero `build.gradle` no, Docker reutiliza la capa de dependencias y el build es mucho más rápido en la segunda vez.

```dockerfile
COPY src/ src/
RUN ./gradlew bootJar --no-daemon -x test
```
> Ahora sí copiamos el código y compilamos. `-x test` omite los tests para acelerar el build de la imagen.
> El resultado es un "fat JAR" en `build/libs/`: un único archivo `.jar` con todo lo necesario.

```dockerfile
# ── ETAPA 2: Runtime ───────────────────────────────────────────────────────
FROM eclipse-temurin:25-jdk-alpine AS runtime

WORKDIR /app
RUN mkdir -p /app/data

COPY --from=builder /workspace/build/libs/*.jar app.jar
```
> Imagen limpia, solo copiamos el `.jar` desde la etapa `builder`.
> Sin Gradle, sin código fuente → imagen final liviana.

```dockerfile
EXPOSE 8080
ENV SPRING_DATASOURCE_URL=jdbc:sqlite:/app/data/usuarios.db
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```
> - `EXPOSE 8080`: documenta el puerto interno (Spring Boot escucha aquí).
> - `JAVA_OPTS`: opciones de JVM para que respete los límites de RAM del contenedor.
> - `ENTRYPOINT`: el comando que se ejecuta al iniciar el contenedor.

---

## Paso 3 — Construir la imagen

Desde la raíz del proyecto (donde está el `Dockerfile`):

```bash
docker build -t primer-proyecto .
```

| Parte | Significado |
|---|---|
| `docker build` | Construir una imagen |
| `-t primer-proyecto` | Nombre (tag) de la imagen |
| `.` | Buscar el `Dockerfile` en el directorio actual |

Vas a ver cómo Docker ejecuta cada instrucción del `Dockerfile`. La primera vez tarda más porque descarga dependencias; las siguientes serán más rápidas gracias al caché.

```
[+] Building 45.3s (12/12) FINISHED
 => [builder 1/7] FROM eclipse-temurin:25-jdk-alpine
 => [builder 2/7] WORKDIR /workspace
 => ...
 => [runtime 4/4] COPY --from=builder /workspace/build/libs/*.jar app.jar
```

Verificá que la imagen se creó:

```bash
docker images | grep primer-proyecto
# primer-proyecto   latest   abc123def456   1 minute ago   ~280MB
```

---

## Paso 4 — Levantar el contenedor

```bash
docker run -p 3000:8080 primer-proyecto
```

| Parte | Significado |
|---|---|
| `docker run` | Crear y ejecutar un contenedor |
| `-p 3000:8080` | Mapeo de puertos `HOST:CONTENEDOR` |
| `primer-proyecto` | Nombre de la imagen a usar |

La API estará disponible en **`http://localhost:3000`**.

Para correrlo en **segundo plano** (sin bloquear la terminal):

```bash
docker run -d -p 3000:8080 --name mi-api primer-proyecto
```

> `-d` = detached (segundo plano) | `--name` = nombre del contenedor

---

## Paso 5 — Comandos útiles del día a día

```bash
# Ver contenedores corriendo
docker ps

# Ver logs del contenedor (reemplazá mi-api por tu nombre o ID)
docker logs mi-api

# Seguir los logs en tiempo real
docker logs -f mi-api

# Detener el contenedor
docker stop mi-api

# Eliminar el contenedor (debe estar detenido)
docker rm mi-api

# Eliminar la imagen
docker rmi primer-proyecto
```

---

## Persistencia de la base de datos SQLite

SQLite guarda los datos en un archivo dentro del contenedor. Si el contenedor se elimina, **los datos se pierden**. Para que persistan, montá un volumen:

```bash
docker run -d \
  -p 3000:8080 \
  -v $(pwd)/data:/app/data \
  --name mi-api \
  primer-proyecto
```

Esto crea una carpeta `data/` en tu directorio actual que guarda el archivo `usuarios.db` fuera del contenedor.

---

## Flujo completo de un cambio en el código

```
1. Modificás código en src/
       ↓
2. docker build -t primer-proyecto .
   (rápido: Docker reutiliza caché de dependencias)
       ↓
3. docker stop mi-api && docker rm mi-api
       ↓
4. docker run -d -p 3000:8080 --name mi-api primer-proyecto
       ↓
5. Probás en http://localhost:3000
```

---

## Resumen de comandos

```bash
# 1. Construir la imagen
docker build -t primer-proyecto .

# 2. Correr el contenedor
docker run -p 3000:8080 primer-proyecto

# 3. Correr en segundo plano con persistencia
docker run -d \
  -p 3000:8080 \
  -v $(pwd)/data:/app/data \
  --name mi-api \
  primer-proyecto

# 4. Ver que está corriendo
docker ps

# 5. Ver logs
docker logs -f mi-api

# 6. Detener y limpiar
docker stop mi-api && docker rm mi-api
```
