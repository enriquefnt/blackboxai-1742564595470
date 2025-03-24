# Instrucciones Finales para macOS (v2)

## UN SOLO COMANDO

```bash
./generar_apk_mac_v2.sh
```

## ¿Qué hace?

El script:
1. Instala automáticamente todas las dependencias necesarias
2. Verifica todos los recursos
3. Genera el APK
4. Muestra mensajes detallados si hay errores

## Requisitos

El script instalará automáticamente:
- Java (Zulu OpenJDK 17)
- Android SDK
- Todas las herramientas necesarias

Solo necesitas tener Homebrew instalado:
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

## Si hay errores

1. Verifica Xcode Command Line Tools:
```bash
xcode-select --install
```

2. Instala Java manualmente:
```bash
brew tap homebrew/cask-versions
brew install --cask zulu17
```

3. Instala Android SDK:
```bash
brew install android-commandlinetools
```

4. Verifica los recursos:
```bash
./verificar_recursos.sh
```

## El APK

El APK se generará en:
```
app/release/CaseApp-v1.1.0.apk
```

## Credenciales
- Usuario: `admin`
- Contraseña: `admin123`

## Notas

- El script instala automáticamente Java si no está presente
- Usa Zulu OpenJDK 17, que es más estable en macOS
- Verifica y configura todo el entorno
- Muestra mensajes detallados en cada paso
- Si hay errores, te dice exactamente qué hacer

## Solución de Problemas

Si ves errores de Java:
```bash
# Desinstalar versiones anteriores
brew uninstall --cask temurin
brew uninstall --cask temurin17

# Instalar la versión correcta
brew install --cask zulu17
```

Si ves errores de Android SDK:
```bash
brew reinstall android-commandlinetools
sdkmanager --licenses
```

¡Un solo comando y listo!