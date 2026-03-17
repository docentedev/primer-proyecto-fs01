# 📚 Guía Completa: SDKMAN! - Gestor de Versiones Java

SDKMAN! es una herramienta que permite instalar y gestionar múltiples versiones de Java (JDK), Maven, Gradle y otros SDKs relacionados. Es similar a **NVM** (Node Version Manager) pero para el ecosistema Java.

**Ventajas:**
- ✅ Instalar múltiples versiones de Java
- ✅ Cambiar entre versiones fácilmente
- ✅ Establecer versiones por proyecto
- ✅ Compatible con Mac, Linux y Windows (con WSL)
- ✅ Gestionar también Maven, Gradle, Ant, SBT, etc.

---

## 🍎 Instalación en macOS

### Requisitos previos
- Terminal (zsh o bash)
- `curl` instalado (generalmente viene por defecto)

### Pasos

1. **Instalar SDKMAN!:**
```bash
curl -s "https://get.sdkman.io" | bash
```

2. **Activar SDKMAN! en la sesión actual:**
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

3. **Verificar instalación:**
```bash
sdk version
```

Deberías ver algo como: `SDKMAN! 5.20.0`

4. **Abre una nueva terminal** (para que se cargue automáticamente) o agrega esto a tu `~/.zshrc`:
```bash
# Al final del archivo
[[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]] && source "$HOME/.sdkman/bin/sdkman-init.sh"
```

> **Nota:** Si usas bash en lugar de zsh, edita `~/.bash_profile` en lugar de `~/.zshrc`

---

## 🐧 Instalación en Linux (Ubuntu/Debian)

### Requisitos previos
```bash
sudo apt-get update
sudo apt-get install -y curl unzip zip
```

### Pasos

1. **Instalar SDKMAN!:**
```bash
curl -s "https://get.sdkman.io" | bash
```

2. **Activar en la sesión actual:**
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

3. **Agregar a tu perfil de shell** (`.bashrc` o `.zshrc`):
```bash
echo 'export SDKMAN_DIR="$HOME/.sdkman"' >> ~/.bashrc
echo '[[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]] && source "$HOME/.sdkman/bin/sdkman-init.sh"' >> ~/.bashrc
source ~/.bashrc
```

4. **Verificar:**
```bash
sdk version
```

---

## 🪟 Instalación en Windows

### Opción 1: WSL2 (Windows Subsystem for Linux) - **RECOMENDADO**

1. **Instala WSL2** (si no lo tienes):
```powershell
# En PowerShell como Admin
wsl --install
```

2. Sigue los **pasos de Linux** arriba en tu terminal WSL

3. Abre tu terminal WSL y verifica:
```bash
sdk version
```

### Opción 2: SDKMAN! Nativo para Windows (Beta)

Si WSL no funciona, descarga el instalador:
- Visita: https://sdkman.io/install
- Descarga el instalador para Windows
- Sigue las instrucciones del asistente

> **Nota:** La versión nativa de Windows aún está en beta. WSL2 es más confiable.

---

## 🚀 Uso Básico

### Ver versiones disponibles de Java
```bash
sdk list java
```

Verás algo como:
```
Available Java Versions
=======================
     * 25.0.1-open
     * 24-open
     * 21.0.1-open
     * 21-open
     ...
```

El `*` indica la versión actualmente seleccionada.

### Instalar una versión de Java

```bash
# Instalar Java 25
sdk install java 25-open

# Instalar Java 21 (LTS - recomendado para estabilidad)
sdk install java 21-open

# Instalar una versión específica
sdk install java 21.0.1-open
```

Se descargará e instalará automáticamente.

### Ver versiones instaladas
```bash
sdk current java
```

O todas las versiones:
```bash
sdk list java
```

### Cambiar la versión activa (temporal)
```bash
sdk use java 21-open
```

Esto cambia la versión solo para la sesión actual.

### Establecer versión por defecto (permanente)
```bash
sdk default java 25-open
```

Ahora todas las nuevas terminales usarán Java 25.

### Desinstalar una versión
```bash
sdk uninstall java 21-open
```

### Ver versión de Java activa
```bash
java -version
echo $JAVA_HOME
```

---

## 📁 Uso por Proyecto

### Opción 1: Usar `.sdkmanrc` (Automático)

1. En la raíz de tu proyecto, crea un archivo `.sdkmanrc`:
```bash
cd /ruta/de/tu/proyecto
echo "java=25-open" > .sdkmanrc
```

2. Cada vez que entres a ese directorio, SDK cambiará automáticamente:
```bash
cd /ruta/de/tu/proyecto
# Java cambia a 25-open automáticamente
java -version  # Muestra Java 25
```

> **Nota:** Necesitas confirmar la primera vez: `Y` o `yes`

### Opción 2: Script de inicialización

```bash
#!/bin/bash
# init-project.sh

# Instalar Java 25 si no lo tienes
sdk install java 25-open

# Cambiar a Java 25
sdk use java 25-open

echo "✓ Proyecto configurado con Java 25"
java -version
```

---

## 📋 Comandos útiles

```bash
# Información general
sdk help

# Ver versión de SDKMAN!
sdk version

# Actualizar SDKMAN!
sdk selfupdate

# Listar candidatos (JDKs, Maven, Gradle, etc.)
sdk list

# Instalar un candidato específico
sdk install gradle 8.5

# Cambiar versión de un candidato
sdk use maven 3.9.5

# Ver la versión actual de todos
sdk current

# Desinstalar una versión
sdk uninstall kotlin 1.9.10

# Limpiar archivos innecesarios
sdk flush
```

---

## 🔧 Casos de Uso Comunes

### Caso 1: Tienes código en Java 21 pero tu proyecto nuevo requiere Java 25

```bash
# Instala Java 25
sdk install java 25-open

# Establécelo como default para nuevos proyectos
sdk default java 25-open

# Para un proyecto antiguo que requiere Java 21
cd /ruta/proyecto-antiguo
sdk use java 21-open
./gradlew build  # Usa Java 21
```

### Caso 2: Probar código en diferentes versiones de Java

```bash
# Instala múltiples versiones
sdk install java 17-open
sdk install java 21-open
sdk install java 25-open

# Prueba con cada una
sdk use java 17-open && ./gradlew test
sdk use java 21-open && ./gradlew test
sdk use java 25-open && ./gradlew test
```

### Caso 3: Usar Maven o Gradle con versiones específicas

```bash
# Instalar Maven
sdk install maven 3.9.5

# Instalar Gradle
sdk install gradle 8.5

# Cambiar versiones
sdk use maven 3.8.1
sdk use gradle 7.6

# Verificar
mvn -version
gradle -version
```

---

## 🐛 Solucionar Problemas

### Problema: "sdk: command not found"

**Solución:**
```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

O reinicia tu terminal.

### Problema: SDKMAN! no activa automáticamente en nuevas terminales

**Solución:**
Verifica que tu `~/.zshrc` o `~/.bash_profile` contenga:
```bash
[[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]] && source "$HOME/.sdkman/bin/sdkman-init.sh"
```

Si no está, agrégalo manualmente.

### Problema: Java no funciona después de cambiar versión

**Solución:**
```bash
# Verifica qué Java está activo
sdk current java

# Mira la ruta de JAVA_HOME
echo $JAVA_HOME

# Reinicia tu terminal
```

### Problema: Gradle aún usa la versión vieja de Java

**Solución:**
```bash
# En el directorio del proyecto
sdk default java 25-open

# O crea .sdkmanrc
echo "java=25-open" > .sdkmanrc
```

---

## 📚 Recursos Oficiales

- **Sitio web:** https://sdkman.io
- **Documentación completa:** https://sdkman.io/usage
- **GitHub:** https://github.com/sdkman/sdkman-cli
- **Problemas frecuentes:** https://sdkman.io/faq

---

## 💡 Tips Profesionales

1. **Mantén SDKMAN! actualizado:**
```bash
sdk selfupdate
```

2. **Usa versiones LTS para producción:**
   - Java 21 (actual LTS recomendado)
   - Próximo LTS: Java 23

3. **Documenta la versión de Java en tu proyecto:**
   - Crea `.sdkmanrc` en el root
   - O docúmentalo en `README.md`

4. **Automatiza con CI/CD:**
   ```bash
   # En GitHub Actions, GitLab CI, etc.
   curl -s "https://get.sdkman.io" | bash
   source "$HOME/.sdkman/bin/sdkman-init.sh"
   sdk install java 25-open
   ```

---

## 📝 Quiz/Ejercicios para estudiantes

### Ejercicio 1: Instalación y verificación
1. Instala SDKMAN!
2. Instala Java 21 y Java 25
3. Verifica que ambas estén instaladas
4. Establece Java 25 como default

### Ejercicio 2: Cambiar entre versiones
1. Verifica que Java 25 está activo: `java -version`
2. Cambia a Java 21: `sdk use java 21-open`
3. Verifica que cambió: `java -version`
4. Vuelve a Java 25

### Ejercicio 3: Configurar un proyecto
1. Crea una carpeta para un proyecto: `mkdir mi-proyecto`
2. Crea `.sdkmanrc` con `java=21-open`
3. Entra al directorio y verifica que cambió la versión de Java

### Ejercicio 4: Compilar con diferentes versiones
1. Descarga el proyecto `primer-proyecto-fs01`
2. Instala Java 25: `sdk install java 25-open`
3. Establece Java 25: `sdk use java 25-open`
4. Ejecuta: `./gradlew build`

---

**¡Listo! Ahora tienes un gestor de versiones Java profesional como los grandes desarrolladores. 🚀**
