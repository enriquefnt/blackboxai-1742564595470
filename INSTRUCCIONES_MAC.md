# Instrucciones para macOS

## 1. Instala Java 17

Usando Homebrew:
```bash
# Instalar Homebrew si no lo tienes
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Instalar Java 17
brew tap homebrew/cask-versions
brew install --cask temurin17
```

## 2. Instala Android Command Line Tools

```bash
# Instalar Android Command Line Tools
brew install android-commandlinetools

# Aceptar licencias
sdkmanager --licenses
```

## 3. Ejecuta el Script

```bash
# Dar permisos de ejecución
chmod +x compilar_mac.sh

# Ejecutar el script
./compilar_mac.sh
```

## 4. Listo!

El APK estará en:
```
app/release/CaseApp-v1.1.0.apk
```

## Credenciales
- Usuario: admin
- Contraseña: admin123

## Solución de Problemas

Si ves errores:

1. Verifica Java:
```bash
java -version  # Debe mostrar versión 17
```

2. Verifica Android SDK:
```bash
sdkmanager --list
```

3. Si hay problemas con los permisos:
```bash
chmod +x compilar_mac.sh
chmod +x gradlew
```

4. Si hay problemas con Gradle:
```bash
./gradlew clean
./gradlew --refresh-dependencies
```

## Notas para macOS

- El script está optimizado para macOS
- Usa Homebrew para instalar las dependencias
- Funciona en Apple Silicon y Intel
- Requiere conexión a internet para la primera ejecución